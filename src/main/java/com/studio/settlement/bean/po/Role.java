package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema; // 改为 SpringDoc 的 Schema 注解
import lombok.Data;

@Data
@Schema(description = "角色表实体类") // 替换 @ApiModel
@TableName("LF_ROLE")
public class Role {

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色名称")
    @TableField("ROLE_NAME")
    private String roleName;

    @Schema(description = "角色描述")
    @TableField("ROLE_COMMENTS")
    private String roleComments;

    @Schema(description = "部门id")
    @TableField("DEP_ID")
    private Long depId;

    @Schema(description = "部门名称")
    @TableField("DEP_NAME")
    private String depName;

    @Schema(description = "角色编码")
    @TableField("ROLE_CODE")
    private String roleCode;

    @Schema(description = "半勾选的菜单id")
    @TableField("HALF_MENU_IDS")
    private String halfMenuIds;
}
