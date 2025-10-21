package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "客户账户VO")
public class CustomerAccountsVO {

    @Schema(description = "账户ID")
    private Long id;

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "历史总充值金额")
    private BigDecimal totalDeposit;

    @Schema(description = "当前账户余额")
    private BigDecimal currentBalance;

    @Schema(description = "历史总消费金额")
    private BigDecimal totalConsumed;

    @Schema(description = "账户状态 (1:正常 2:冻结 3:注销)")
    private Byte accountStatus;

    @Schema(description = "最后消费时间")
    private Date lastConsumedTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
