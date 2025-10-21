package com.studio.settlement.common.handler;

import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.common.exception.BusinessException;
import com.studio.settlement.common.exception.InsufficientBalanceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ApiResult<Void> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return ApiResult.error(e.getCode(), e.getMessage())
                .data(new BalanceInfo(e.getCurrentBalance(), e.getRequiredAmount()));
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult<Void> handleBusinessException(BusinessException e) {
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleException(Exception e) {
        return ApiResult.error("系统错误: " + e.getMessage());
    }

    // 余额信息内部类
    private static class BalanceInfo {
        private final String currentBalance;
        private final String requiredAmount;

        public BalanceInfo(String currentBalance, String requiredAmount) {
            this.currentBalance = currentBalance;
            this.requiredAmount = requiredAmount;
        }

        // 使用BigDecimal参数的构造函数
        public BalanceInfo(java.math.BigDecimal currentBalance, java.math.BigDecimal requiredAmount) {
            this.currentBalance = currentBalance != null ? currentBalance.toPlainString() : "0";
            this.requiredAmount = requiredAmount != null ? requiredAmount.toPlainString() : "0";
        }

        public String getCurrentBalance() {
            return currentBalance;
        }

        public String getRequiredAmount() {
            return requiredAmount;
        }
    }
}
