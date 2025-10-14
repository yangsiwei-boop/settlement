package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema; // 替换为 SpringDoc 的 Schema 注解
import lombok.Data;

@Data
@Schema(description = "用户表实体类") // 替换 @ApiModel
@TableName("LF_USER")
public class User {

    @Schema(description = "主键ID") // 替换 @ApiModelProperty
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名称")
    @TableField("USERNAME")
    private String username;

    @Schema(description = "账号密码")
    @TableField("PASSWORD")
    private String password;

    @Schema(description = "姓名")
    @TableField("REALNAME")
    private String realName;

    @Schema(description = "工号")
    @TableField("JOB_NUMBER")
    private Long jobNumber;

    @Schema(description = "手机号")
    @TableField("PHONE")
    private String phone;

    @Schema(description = "所属部门ID")
    @TableField("DEP_ID")
    private Long depId;

    @Schema(description = "用户状态 0 正常 1 停用")
    @TableField("IS_DELETE")
    private Integer isDelete;

    @Schema(description = "是否需要更改密码 0不需要 1需要")
    @TableField("IS_UPDATE_PWD")
    private Integer isUpdatePwd;
}
