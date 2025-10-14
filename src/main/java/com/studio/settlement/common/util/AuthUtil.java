package com.studio.settlement.common.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.studio.settlement.bean.po.Role;
import com.studio.settlement.bean.po.User;
import com.studio.settlement.bean.po.UserToRoles;
import com.studio.settlement.service.RoleService;
import com.studio.settlement.service.UserService;
import com.studio.settlement.service.UserToRolesService;
import io.swagger.v3.oas.annotations.media.Schema; // 替换为 SpringDoc 的 Schema 注解
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthUtil {
    // 角色代码常量
    public static final String ADMIN = "admin";
    public static final String SYSADMIN = "sysadmin";
    public static final String EQUIPMENT_MANAGER = "equipmentManager";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserToRolesService userToRolesService;

    @Schema(description = "获取当前登录用户的ID")
    public Long getUserId() {
        Object loginId = StpUtil.getLoginId();
        // 增加更健壮的类型检查和转换
        if (loginId instanceof Long) {
            return (Long) loginId;
        } else if (loginId instanceof Integer) {
            return ((Integer) loginId).longValue();
        } else if (loginId instanceof String) {
            try {
                return Long.parseLong((String) loginId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Schema(description = "根据姓名获取用户ID列表")
    public List<Long> getUserIdByName(String realName) {
        if (StringUtils.isBlank(realName)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getRealName, realName);
        List<User> userList = userService.list(queryWrapper);
        if (CollectionUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }

        return userList.stream().map(User::getId).collect(Collectors.toList());
    }

    @Schema(description = "获取当前登录用户信息")
    public User getUser() {
        Long userId = getUserId();
        return userId != null ? userService.getById(userId) : null;
    }

    @Schema(description = "获取当前用户的角色编码列表")
    public List<String> getRoleCodes() {
        Long userId = getUserId();
        if (userId == null) {
            return new ArrayList<>();
        }

        List<UserToRoles> userToRolesList = userToRolesService.list(
                Wrappers.lambdaQuery(UserToRoles.class).eq(UserToRoles::getUserId, userId)
        );

        List<Long> roleIdList = userToRolesList.stream()
                .map(UserToRoles::getRoleId)
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        List<Role> roles = roleService.listByIds(roleIdList);
        return roles.stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());
    }

    @Schema(description = "获取当前用户的角色名称列表")
    public List<String> getRoleNames() {
        Long userId = getUserId();
        if (userId == null) {
            return new ArrayList<>();
        }

        List<UserToRoles> userToRolesList = userToRolesService.list(
                Wrappers.lambdaQuery(UserToRoles.class).eq(UserToRoles::getUserId, userId)
        );

        List<Long> roleIdList = userToRolesList.stream()
                .map(UserToRoles::getRoleId)
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        List<Role> roles = roleService.listByIds(roleIdList);
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }

//    @Schema(description = "判断当前用户是否是admin管理员")
//    public boolean isAdmin() {
//        List<String> roleCodes = getRoleCodes();
//        return !CollectionUtil.isEmpty(roleCodes) && roleCodes.contains(ADMIN);
//    }
//
//    @Schema(description = "判断当前用户是否是超级管理员")
//    public boolean isAdministrator() {
//        List<String> roleCodes = getRoleCodes();
//        return !CollectionUtil.isEmpty(roleCodes) && roleCodes.contains(SYSADMIN);
//    }

    @Schema(description = "判断当前用户是否是设备管理员")
    public boolean isEquipAdmin() {
        List<String> roleNames = getRoleNames();
        return !CollectionUtil.isEmpty(roleNames) && roleNames.contains("设备负责人");
    }

    @Schema(description = "获取当前用户的账号名")
    public String getUsername() {
        User user = getUser();
        return user != null ? user.getUsername() : "";
    }

    @Schema(description = "获取当前用户的真实姓名")
    public String getUserRealName() {
        User user = getUser();
        return user != null ? user.getRealName() : "";
    }

    @Schema(description = "获取当前用户的部门ID")
    public Long getDepId() {
        User user = getUser();
        return user != null ? user.getDepId() : null;
    }
}
