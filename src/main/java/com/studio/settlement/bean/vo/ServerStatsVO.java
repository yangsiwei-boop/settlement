package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 打手业绩统计VO
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@Data
@Schema(description = "打手业绩统计VO")
public class ServerStatsVO {

    @Schema(description = "打手ID")
    private Long serverId;

    @Schema(description = "总接单数")
    private Integer totalOrders;

    @Schema(description = "完成订单数")
    private Integer completedOrders;

    @Schema(description = "总收入")
    private BigDecimal totalIncome;

    @Schema(description = "本月收入")
    private BigDecimal monthlyIncome;
}
