package com.studio.settlement.service;

import com.studio.settlement.bean.po.ActualOrdersPo;
import com.studio.settlement.bean.response.ApiResult;

import java.math.BigDecimal;

/**
 * 订单结算服务接口
 */
public interface OrderSettlementService {

    /**
     * 完成订单并扣除客户余额
     * @param orderId 订单ID
     * @param customerName 客户姓名
     * @param consumedAmount 消费金额
     * @return 操作结果
     */
    ApiResult<Void> completeOrderAndDeductBalance(Long orderId, String customerName, BigDecimal consumedAmount);

    /**
     * 客户充值
     * @param customerName 客户姓名
     * @param amount 充值金额
     * @param orderCode 关联订单号
     * @param operator 操作人员
     * @return 操作结果
     */
    ApiResult<Void> rechargeCustomerAccount(String customerName, BigDecimal amount, String orderCode, String operator);

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    ApiResult<ActualOrdersPo> getOrderDetail(Long orderId);

    /**
     * 取消订单并退款
     * @param orderId 订单ID
     * @return 操作结果
     */
    ApiResult<Void> cancelOrderAndRefund(Long orderId);
}
