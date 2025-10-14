package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "组织角色关联表")
@TableName("LF_DEP_ROLE")
public class DepToRoles {

    @TableId(value = "ID", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long ID;

    @TableField("DEP_ID")
    @Schema(description = "组织ID")
    private Long depId;

    @TableField("ROLE_ID")
    @Schema(description = "角色ID")
    private Long roleId;

}
