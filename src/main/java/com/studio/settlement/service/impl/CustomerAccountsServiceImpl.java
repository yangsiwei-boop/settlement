package com.studio.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.studio.settlement.bean.po.CustomerAccountsPo;
import com.studio.settlement.mapper.CustomerAccountsMapper;
import com.studio.settlement.service.CustomerAccountsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 客户账户表 服务实现类
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
@Service
public class CustomerAccountsServiceImpl extends ServiceImpl<CustomerAccountsMapper, CustomerAccountsPo>
        implements CustomerAccountsService {

    @Override
    public CustomerAccountsPo getByCustomerName(String customerName) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<CustomerAccountsPo>()
                        .eq(CustomerAccountsPo::getCustomerName, customerName)
        );
    }

    @Override
    public CustomerAccountsPo createOrInitAccount(String customerName) {
        CustomerAccountsPo account = getByCustomerName(customerName);
        if (account == null) {
            account = new CustomerAccountsPo();
            account.setCustomerName(customerName);
            account.setTotalDeposit(BigDecimal.ZERO);
            account.setCurrentBalance(BigDecimal.ZERO);
            baseMapper.insert(account);
        }
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deposit(String customerName, BigDecimal amount, String orderCode, String operator) {
        // 1. 查找或创建账户
        CustomerAccountsPo account = createOrInitAccount(customerName);

        // 2. 更新账户
        int updateCount = baseMapper.update(null,
                new LambdaUpdateWrapper<CustomerAccountsPo>()
                        .setSql("total_deposit = total_deposit + " + amount)
                        .setSql("current_balance = current_balance + " + amount)
                        .eq(CustomerAccountsPo::getId, account.getId())
        );

        return updateCount > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean consume(String customerName, BigDecimal amount, String orderCode, String operator) {
        // 1. 检查账户是否存在
        CustomerAccountsPo account = getByCustomerName(customerName);
        if (account == null) {
            throw new RuntimeException("客户账户不存在");
        }

        // 2. 检查余额是否足够
        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new RuntimeException("客户余额不足");
        }

        // 3. 更新账户
        int updateCount = baseMapper.update(null,
                new LambdaUpdateWrapper<CustomerAccountsPo>()
                        .setSql("current_balance = current_balance - " + amount)
                        .set(CustomerAccountsPo::getLastConsumedTime, new Date())
                        .eq(CustomerAccountsPo::getId, account.getId())
                        .ge(CustomerAccountsPo::getCurrentBalance, amount) // 乐观锁确保余额足够
        );

        return updateCount > 0;
    }

    @Override
    public boolean isBalanceSufficient(String customerName, BigDecimal requiredAmount) {
        CustomerAccountsPo account = getByCustomerName(customerName);
        if (account == null) {
            return false;
        }
        return account.getCurrentBalance().compareTo(requiredAmount) >= 0;
    }
}
