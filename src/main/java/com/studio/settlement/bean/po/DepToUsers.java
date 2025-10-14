package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "组织员工关联表")
@TableName("LF_DEP_USER")
public class DepToUsers {

    @TableId(value = "ID", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long ID;

    @TableField("DEP_ID")
    @Schema(description = "组织ID")
    private Long depId;

    @TableField("USER_ID")
    @Schema(description = "员工ID")
    private Long userId;

}
