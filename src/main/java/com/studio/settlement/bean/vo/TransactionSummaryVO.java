package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "交易统计VO")
public class TransactionSummaryVO {

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "总充值金额")
    private BigDecimal totalDeposit = BigDecimal.ZERO;

    @Schema(description = "总消费金额")
    private BigDecimal totalConsumption = BigDecimal.ZERO;

    @Schema(description = "净金额")
    private BigDecimal netAmount = BigDecimal.ZERO;
}
