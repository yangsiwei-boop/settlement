package com.studio.settlement.service.impl;

import com.studio.settlement.bean.po.AccountTransactionsPo;
import com.studio.settlement.mapper.AccountTransactionsMapper;
import com.studio.settlement.service.AccountTransactionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 账户变动记录表 服务实现类
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
@Service
public class AccountTransactionsServiceImpl extends ServiceImpl<AccountTransactionsMapper, AccountTransactionsPo>
        implements AccountTransactionsService {

    @Override
    public void recordTransaction(String customerName, Integer transactionType, BigDecimal amount,
                                  BigDecimal balanceAfter, String orderCode, String operator, String notes) {
        AccountTransactionsPo transaction = new AccountTransactionsPo();
        transaction.setCustomerName(customerName);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setRelatedOrderCode(orderCode);
        transaction.setOperator(operator);
        transaction.setNotes(notes);
        transaction.setCreateTime(new Date());

        baseMapper.insert(transaction);
    }
}
