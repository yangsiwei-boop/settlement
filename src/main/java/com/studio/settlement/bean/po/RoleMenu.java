package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实体类
 *
 * @author Chill
 */
@Data
@TableName("LB_ROLE_MENU")
@Schema(description = "角色-菜单关联表")
public class RoleMenu {

    @TableId(value = "ID", type = IdType.AUTO)
    @Schema(description = "主键")
    private Long id;

    @TableField("MENU_ID")
    @Schema(description = "菜单id")
    private Long menuId;

    @TableField("ROLE_ID")
    @Schema(description = "角色id")
    private Long roleId;

}
