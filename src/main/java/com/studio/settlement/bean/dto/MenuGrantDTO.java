package com.studio.settlement.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "菜单授权DTO")
public class MenuGrantDTO {

    @Schema(description = "roleId集合")
    private List<Long> roleIds;

    @Schema(description = "menuId集合")
    private List<Long> menuIds;

    @Schema(description = "半勾选的menuId集合")
    private List<Long> halfMenuIds;
}
