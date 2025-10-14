package com.studio.settlement.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "组织员工关联查询修改类")
public class DepToUsersDTO extends BaseDTO {

    @Schema(description = "主键ID")
    private Long ID;

    @Schema(description = "组织ID")
    private Long depId;

    @Schema(description = "员工ID 查询时不传")
    private Long userId;

    @Schema(description = "1 删除 0 关联 查询时不传")
    private int isDelete;
}
