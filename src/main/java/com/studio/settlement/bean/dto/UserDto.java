package com.studio.settlement.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户信息DTO")
public class UserDto extends BaseDTO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "账号密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "所属部门ID")
    private Long depId;

    @Schema(description = "token")
    private String token;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "工号")
    private Long jobNumber;

    @Schema(description = "用户状态 0 正常 1 停用")
    private Integer isDelete;

    @Schema(description = "角色id列表")
    private List<Long> roleIds;

    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    @Schema(description = "是否需要更改密码 0不需要 1需要")
    private Integer isUpdatePwd;
}
