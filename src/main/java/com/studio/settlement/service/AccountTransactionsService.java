package com.studio.settlement.service;

import com.studio.settlement.bean.po.AccountTransactionsPo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 账户变动记录表 服务类
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
public interface AccountTransactionsService extends IService<AccountTransactionsPo> {

    /**
     * 记录账户变动
     * @param customerName 客户姓名
     * @param transactionType 交易类型 (1:充值 2:消费)
     * @param amount 变动金额
     * @param balanceAfter 变动后余额
     * @param orderCode 关联订单号
     * @param operator 操作人员
     * @param notes 备注
     */
    void recordTransaction(String customerName, Byte transactionType, BigDecimal amount,
                           BigDecimal balanceAfter, String orderCode, String operator, String notes);
}
