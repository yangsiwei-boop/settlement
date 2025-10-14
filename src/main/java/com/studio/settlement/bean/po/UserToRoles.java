package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户角色关联表")
@TableName("LF_USER_ROLE")
public class UserToRoles {

    @TableId(value = "ID", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long ID;

    @TableField("USER_ID")
    @Schema(description = "用户ID")
    private Long userId;

    @TableField("ROLE_ID")
    @Schema(description = "角色ID")
    private Long roleId;

}
