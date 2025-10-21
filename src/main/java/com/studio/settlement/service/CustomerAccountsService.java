package com.studio.settlement.service;

import com.studio.settlement.bean.po.CustomerAccountsPo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 客户账户表 服务类
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
public interface CustomerAccountsService extends IService<CustomerAccountsPo> {

    /**
     * 客户充值
     * @param customerName 客户姓名
     * @param amount 充值金额
     * @param orderCode 关联订单号
     * @param operator 操作人员
     * @return 是否充值成功
     */
    boolean deposit(String customerName, BigDecimal amount, String orderCode, String operator);

    /**
     * 客户消费扣款
     * @param customerName 客户姓名
     * @param amount 消费金额
     * @param orderCode 关联订单号
     * @param operator 操作人员
     * @return 是否扣款成功
     */
    boolean consume(String customerName, BigDecimal amount, String orderCode, String operator);

    /**
     * 获取客户账户信息
     * @param customerName 客户姓名
     * @return 客户账户信息
     */
    CustomerAccountsPo getByCustomerName(String customerName);

    /**
     * 检查客户余额是否足够
     * @param customerName 客户姓名
     * @param requiredAmount 需要金额
     * @return 是否足够
     */
    boolean isBalanceSufficient(String customerName, BigDecimal requiredAmount);

    /**
     * 创建或初始化客户账户
     * @param customerName 客户姓名
     * @return 客户账户信息
     */
    CustomerAccountsPo createOrInitAccount(String customerName);
}
