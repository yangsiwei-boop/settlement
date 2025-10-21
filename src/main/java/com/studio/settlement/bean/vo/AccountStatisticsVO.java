package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "账户统计VO")
public class AccountStatisticsVO {

    @Schema(description = "总账户数")
    private Integer totalAccounts;

    @Schema(description = "总余额")
    private BigDecimal totalBalance;

    @Schema(description = "平均余额")
    private BigDecimal averageBalance;

    @Schema(description = "正常账户数")
    private Integer activeAccounts;

    @Schema(description = "冻结账户数")
    private Integer frozenAccounts;

    @Schema(description = "注销账户数")
    private Integer closedAccounts;
}
