package com.studio.settlement.bean.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import io.swagger.v3.oas.annotations.media.Schema; // 改为 SpringDoc 的 Schema 注解
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户信息返回对象") // 可选的类级别描述
public class UserVO {

    @Schema(description = "token对象")
    private SaTokenInfo tokenInfo;

    private Long id;

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "所属部门ID")
    private Long depId;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "工号")
    private Long jobNumber;

    @Schema(description = "用户状态 0 正常 1 停用")
    private Integer isDelete;

    @Schema(description = "角色id列表")
    private List<Long> roleIds;

    @Schema(description = "角色名称列表") // 修正了描述，原描述"用户名"可能不准确
    private List<String> roleNames;

    @Schema(description = "是否需要更改密码 0不需要 1需要")
    private Integer isUpdatePwd;
}
