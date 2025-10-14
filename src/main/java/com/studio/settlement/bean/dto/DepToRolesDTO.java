package com.studio.settlement.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "组织角色关联查询修改类")
public class DepToRolesDTO extends BaseDTO {

    @Schema(description = "主键ID")
    private Long ID;

    @Schema(description = "组织ID")
    private Long depId;

    @Schema(description = "角色ID 查询时不传")
    private Long roleId;

    @Schema(description = "1 删除 0 关联 查询时不传")
    private int isDelete;

    @Schema(description = "角色名称")
    private String roleName;
}

