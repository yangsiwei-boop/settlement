package com.studio.settlement.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "组织员工移动类")
public class DepUserMoveDTO {

    @Schema(description = "原组织ID")
    private Long oldDepId;

    @Schema(description = "员工ID 查询时不传")
    private Long userId;

    @Schema(description = "新组织Id")
    private Long newDepId;
}
