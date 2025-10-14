package com.studio.settlement.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色信息DTO")
public class RoleDTO extends BaseDTO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色描述")
    private String roleComments;

    @Schema(description = "部门id")
    private Long depId;

    @Schema(description = "部门名称")
    private String depName;

    @Schema(description = "角色编码")
    private String roleCode;
}
