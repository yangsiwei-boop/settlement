package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "账户变动记录VO")
public class AccountTransactionsVO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "交易类型 (1:充值 2:消费)")
    private Integer transactionType;

    @Schema(description = "关联订单号")
    private String relatedOrderCode;

    @Schema(description = "变动金额")
    private BigDecimal amount;

    @Schema(description = "变动后余额")
    private BigDecimal balanceAfter;

    @Schema(description = "操作人员")
    private String operator;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "创建时间")
    private Date createTime;
}
