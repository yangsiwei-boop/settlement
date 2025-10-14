package com.studio.settlement.common.util;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthAdminUtl {

    public static final String ADMIN = "admin";
    public static final String SYSADMIN = "sysadmin";
    public static final String EQUIPMENT_MANAGER = "equipmentManager";

    @Autowired
    AuthUtil authUtil;

    @Schema(description = "判断当前用户是否是admin管理员")
    public boolean isAdmin() {
        List<String> roleCodes = authUtil.getRoleCodes();
        return !CollectionUtil.isEmpty(roleCodes) && roleCodes.contains(ADMIN);
    }

    @Schema(description = "判断当前用户是否是超级管理员")
    public boolean isAdministrator() {
        List<String> roleCodes = authUtil.getRoleCodes();
        return !CollectionUtil.isEmpty(roleCodes) && roleCodes.contains(SYSADMIN);
    }
}
