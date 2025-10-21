package com.studio.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.studio.settlement.bean.po.AccountTransactionsPo;
import com.studio.settlement.bean.po.ActualOrdersPo;
import com.studio.settlement.bean.po.CustomerAccountsPo;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.common.exception.InsufficientBalanceException;
import com.studio.settlement.mapper.ActualOrdersMapper;
import com.studio.settlement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单结算服务实现类
 */
@Service
public class OrderSettlementServiceImpl implements OrderSettlementService {

    @Autowired
    private ActualOrdersService actualOrdersService;

    @Autowired
    private CustomerAccountsService customerAccountsService;

    @Autowired
    private AccountTransactionsService accountTransactionsService;

    @Autowired
    private ActualOrdersMapper actualOrdersMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Void> completeOrderAndDeductBalance(Long orderId, String customerName, BigDecimal consumedAmount) {
        try {
            // 1. 获取订单信息
            ActualOrdersPo order = actualOrdersService.getById(orderId);
            if (order == null) {
                return ApiResult.error("订单不存在");
            }

            // 2. 检查订单状态
            if (order.getOrderStatus() == 3) {
                return ApiResult.error("订单已完成，无需重复操作");
            }

            if (order.getOrderStatus() == 4) {
                return ApiResult.error("订单已取消，无法完成");
            }

            // 3. 检查客户余额是否足够
            if (!customerAccountsService.isBalanceSufficient(customerName, consumedAmount)) {
                CustomerAccountsPo account = customerAccountsService.getByCustomerName(customerName);
                throw new InsufficientBalanceException(
                        "客户余额不足",
                        account != null ? account.getCurrentBalance() : BigDecimal.ZERO,
                        consumedAmount
                );
            }

            // 4. 更新订单状态为已完成
            order.setOrderStatus((byte) 3);
            order.setServiceEndTime(new Date());
            order.setCustomerName(customerName);
            actualOrdersService.updateById(order);

            // 5. 扣除客户余额
            boolean deductSuccess = customerAccountsService.consume(
                    customerName, consumedAmount,
                    "ORDER_" + orderId, "系统"
            );

            if (!deductSuccess) {
                throw new RuntimeException("扣款失败");
            }

            // 6. 获取当前账户余额
            CustomerAccountsPo account = customerAccountsService.getByCustomerName(customerName);

            // 7. 记录交易流水
            accountTransactionsService.recordTransaction(
                    customerName,
                    (byte) 2, // 消费
                    consumedAmount.negate(), // 负值表示支出
                    account.getCurrentBalance(),
                    "ORDER_" + orderId,
                    "系统",
                    "订单消费扣款: " + order.getServiceType()
            );

            return ApiResult.ok("订单完成并扣款成功");

        } catch (InsufficientBalanceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("订单完成失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Void> rechargeCustomerAccount(String customerName, BigDecimal amount, String orderCode, String operator) {
        try {
            // 1. 充值
            boolean rechargeSuccess = customerAccountsService.deposit(
                    customerName, amount, orderCode, operator
            );

            if (!rechargeSuccess) {
                return ApiResult.error("充值失败");
            }

            // 2. 获取当前账户余额
            CustomerAccountsPo account = customerAccountsService.getByCustomerName(customerName);

            // 3. 记录交易流水
            accountTransactionsService.recordTransaction(
                    customerName,
                    (byte) 1, // 充值
                    amount,
                    account.getCurrentBalance(),
                    orderCode,
                    operator,
                    "客户充值"
            );

            return ApiResult.ok("充值成功");

        } catch (Exception e) {
            throw new RuntimeException("充值失败: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResult<ActualOrdersPo> getOrderDetail(Long orderId) {
        try {
            ActualOrdersPo order = actualOrdersService.getById(orderId);
            if (order == null) {
                return ApiResult.error("订单不存在");
            }
            return ApiResult.ok("查询成功").data(order);
        } catch (Exception e) {
            throw new RuntimeException("获取订单详情失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Void> cancelOrderAndRefund(Long orderId) {
        try {
            // 1. 获取订单信息
            ActualOrdersPo order = actualOrdersService.getById(orderId);
            if (order == null) {
                return ApiResult.error("订单不存在");
            }

            // 2. 检查订单状态
            if (order.getOrderStatus() == 4) {
                return ApiResult.error("订单已取消，无需重复操作");
            }

            if (order.getOrderStatus() == 3) {
                return ApiResult.error("订单已完成，无法取消");
            }

            // 3. 如果订单已支付，需要退款
            if (order.getOrderStatus() == 2 && order.getCustomerName() != null) {
                // 获取消费金额（这里需要根据实际业务逻辑计算）
                BigDecimal refundAmount = order.getConsumedAmount() != null ?
                        order.getConsumedAmount() : BigDecimal.ZERO;

                if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // 退款到客户账户
                    boolean refundSuccess = customerAccountsService.deposit(
                            order.getCustomerName(), refundAmount,
                            "REFUND_" + orderId, "系统"
                    );

                    if (!refundSuccess) {
                        return ApiResult.error("退款失败");
                    }

                    // 记录退款流水
                    CustomerAccountsPo account = customerAccountsService.getByCustomerName(order.getCustomerName());
                    accountTransactionsService.recordTransaction(
                            order.getCustomerName(),
                            (byte) 1, // 充值（退款）
                            refundAmount,
                            account.getCurrentBalance(),
                            "REFUND_" + orderId,
                            "系统",
                            "订单取消退款: " + order.getServiceType()
                    );
                }
            }

            // 4. 更新订单状态为已取消
            order.setOrderStatus((byte) 4);
            actualOrdersService.updateById(order);

            return ApiResult.ok("订单取消成功");

        } catch (Exception e) {
            throw new RuntimeException("订单取消失败: " + e.getMessage(), e);
        }
    }
}
