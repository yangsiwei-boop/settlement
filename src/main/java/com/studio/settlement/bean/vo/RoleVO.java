package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色信息VO")
public class RoleVO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色描述")
    private String roleComments;

    @Schema(description = "关联角色数")
    private int relateCount;

    @Schema(description = "部门id")
    private Long depId;

    @Schema(description = "部门名称")
    private String depName;

    @Schema(description = "角色编码")
    private String roleCode;
}
