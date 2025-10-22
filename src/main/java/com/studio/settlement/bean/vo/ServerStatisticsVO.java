package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 打手统计信息VO
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@Data
@Schema(description = "打手统计信息VO")
public class ServerStatisticsVO {

    @Schema(description = "总打手数")
    private Integer totalServers;

    @Schema(description = "正常打手数")
    private Integer activeServers;

    @Schema(description = "冻结打手数")
    private Integer frozenServers;

    @Schema(description = "在线打手数")
    private Integer onlineServers;

    @Schema(description = "离线打手数")
    private Integer offlineServers;
}
