package com.studio.settlement.common.exception;

import java.math.BigDecimal;

/**
 * 余额不足异常
 */
public class InsufficientBalanceException extends BusinessException {
    private BigDecimal currentBalance;
    private BigDecimal requiredAmount;

    public InsufficientBalanceException(String message, BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(400, message);
        this.currentBalance = currentBalance;
        this.requiredAmount = requiredAmount;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public BigDecimal getRequiredAmount() {
        return requiredAmount;
    }
}
