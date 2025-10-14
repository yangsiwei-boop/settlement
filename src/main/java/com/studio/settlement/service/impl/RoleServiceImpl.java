package com.studio.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studio.settlement.bean.dto.MenuGrantDTO;
import com.studio.settlement.bean.dto.RoleDTO;
import com.studio.settlement.bean.po.DepToRoles;
import com.studio.settlement.bean.po.Role;
import com.studio.settlement.bean.po.RoleMenu;
import com.studio.settlement.bean.po.UserToRoles;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.RoleVO;
import com.studio.settlement.common.util.AuthAdminUtl;
import com.studio.settlement.common.util.AuthUtil;
import com.studio.settlement.mapper.DepMapper;
import com.studio.settlement.mapper.RoleMapper;
import com.studio.settlement.service.DepToRolesService;
import com.studio.settlement.service.RoleMenuService;
import com.studio.settlement.service.RoleService;
import com.studio.settlement.service.UserToRolesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    UserToRolesService userToRolesService;
    @Autowired
    DepToRolesService depToRolesService;

    @Autowired
    @Lazy
    AuthAdminUtl authAdminUtl;

    @Autowired
    DepMapper depMapper;

    @Autowired
    RoleMenuService roleMenuService;

    public Page<RoleVO> queryRoleList(RoleDTO roleDTO){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        // 所属单位 公司账号查所有，分属单位查自己
        if (null != roleDTO.getDepId() && 1L != roleDTO.getDepId()){
            wrapper.eq(Role::getDepId, roleDTO.getDepId());
        }
        // 角色名称模糊查询
        wrapper.like(StringUtils.isNotBlank(roleDTO.getRoleName()), Role::getRoleName, roleDTO.getRoleName());
        if (null == roleDTO.getPageNo()){
            roleDTO.setPageNo(1);
            roleDTO.setPageSize(10);
        }

        // 角色编码模糊查询
        wrapper.like(StringUtils.isNotBlank(roleDTO.getRoleCode()), Role::getRoleCode, roleDTO.getRoleCode());
        wrapper.orderByDesc(Role::getId);
        Page<Role> page = new Page<>(roleDTO.getPageNo(), roleDTO.getPageSize());
        page = this.page(page, wrapper);

        List<Role> roles = page.getRecords();
        List<RoleVO> roleVOS = new ArrayList<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            roleVO.setId(role.getId());
            roleVO.setRoleName(role.getRoleName());
            roleVO.setRoleComments(role.getRoleComments());
            roleVO.setDepId(role.getDepId());
            if(null != role.getDepId()) {
                roleVO.setDepName(depMapper.selectById(role.getDepId()).getDepName());
            }
            roleVO.setRoleCode(role.getRoleCode());
            long count = userToRolesService.count(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getRoleId, role.getId()));
            roleVO.setRelateCount((int) count);
            roleVOS.add(roleVO);
        }
        Page<RoleVO> roleVOPage = new Page<>();
        roleVOPage.setRecords(roleVOS);
        roleVOPage.setTotal(page.getTotal());
        roleVOPage.setSize(page.getSize());
        roleVOPage.setCurrent(page.getCurrent());
        roleVOPage.setPages(page.getPages());
        log.info("查询结果：{}", roleVOPage.getRecords().size());
        log.info("查询结果：{}", JSONObject.toJSONString(roleVOPage));
        return roleVOPage;
    }

    public Page<RoleVO> queryNoRateRoleList(RoleDTO roleDTO){
        // 获取已绑定角色id列表
        List<DepToRoles> depToRolesList = depToRolesService.list(new LambdaQueryWrapper<DepToRoles>().eq(DepToRoles::getDepId, roleDTO.getDepId()));
        List<Long> roleIds = new ArrayList<>();
        for (DepToRoles depToRoles : depToRolesList) {
            roleIds.add(depToRoles.getRoleId());
        }

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if(roleIds.size() > 0){
            wrapper.notIn(Role::getId, roleIds);
        }
        // 角色名称模糊查询
        wrapper.like(StringUtils.isNotBlank(roleDTO.getRoleName()), Role::getRoleName, roleDTO.getRoleName());
        wrapper.eq(null != roleDTO.getDepId() && 1L != roleDTO.getDepId(), Role::getDepId, roleDTO.getDepId());
        if (null == roleDTO.getPageNo()){
            roleDTO.setPageNo(1);
            roleDTO.setPageSize(10);
        }
        // 角色编码模糊查询
        wrapper.like(StringUtils.isNotBlank(roleDTO.getRoleCode()), Role::getRoleCode, roleDTO.getRoleCode());
        wrapper.orderByDesc(Role::getId);
        Page<Role> page = new Page<>(roleDTO.getPageNo(), roleDTO.getPageSize());
        page = this.page(page, wrapper);

        List<Role> roles = page.getRecords();
        List<RoleVO> roleVOS = new ArrayList<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            roleVO.setId(role.getId());
            roleVO.setRoleName(role.getRoleName());
            roleVO.setRoleComments(role.getRoleComments());
            roleVO.setRoleCode(role.getRoleCode());
            long count = userToRolesService.count(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getRoleId, role.getId()));
            roleVO.setRelateCount((int) count);
            roleVOS.add(roleVO);
        }
        Page<RoleVO> roleVOPage = new Page<>();
        roleVOPage.setRecords(roleVOS);
        roleVOPage.setTotal(page.getTotal());
        roleVOPage.setSize(page.getSize());
        roleVOPage.setCurrent(page.getCurrent());
        roleVOPage.setPages(page.getPages());
        log.info("查询结果：{}", roleVOPage.getRecords().size());
        log.info("查询结果：{}", JSONObject.toJSONString(roleVOPage));
        return roleVOPage;
    }

    public Boolean delRole(Role role){
        log.info("删除角色：{}", JSONObject.toJSONString(role));
        // 删除角色时，需要将该角色的用户关系也删除
        userToRolesService.remove(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getRoleId, role.getId()));
        // 删除组织角色关联
        depToRolesService.remove(new LambdaQueryWrapper<DepToRoles>().eq(DepToRoles::getRoleId, role.getId()));

        // 删除角色
        boolean b = this.removeById(role.getId());

        log.info("删除角色结果：{}", b);

        return b;
    }

    public ApiResult grant(MenuGrantDTO menuGrantDTO) {
        // 防止越权配置超管角色
        long administratorCount = baseMapper.selectCount(Wrappers.<Role>query().lambda()
                .eq(Role::getRoleCode, AuthUtil.SYSADMIN).in(Role::getId, menuGrantDTO.getRoleIds()));
        if (!authAdminUtl.isAdministrator() && administratorCount > 0) {
            return ApiResult.error("无权配置超管角色");
        }

        // 防止越权配置管理员角色
        long adminCount = baseMapper.selectCount(Wrappers.<Role>query().lambda()
                .eq(Role::getRoleCode, AuthUtil.ADMIN).in(Role::getId, menuGrantDTO.getRoleIds()));
        if (!authAdminUtl.isAdmin() && adminCount > 0) {
            return ApiResult.error("无权配置管理员角色");
        }

        // 删除角色配置的菜单集合
        roleMenuService.remove(Wrappers.<RoleMenu>update().lambda().in(RoleMenu::getRoleId, menuGrantDTO.getRoleIds()));

        // 组装配置
        List<RoleMenu> roleMenus = new ArrayList<>();
        menuGrantDTO.getMenuIds().forEach(menuId -> {
            for (Long roleId : menuGrantDTO.getRoleIds()) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
        });

        // 新增配置
        roleMenuService.saveBatch(roleMenus);

        // 半勾选的存库，前端展示用
        if (CollectionUtils.isNotEmpty(menuGrantDTO.getRoleIds()) && CollectionUtils.isNotEmpty(menuGrantDTO.getHalfMenuIds())) {
            LambdaUpdateWrapper<Role> updateWrapper = Wrappers.lambdaUpdate(Role.class)
                    .in(Role::getId, menuGrantDTO.getRoleIds())
                    .set(Role::getHalfMenuIds, JSONObject.toJSONString(menuGrantDTO.getHalfMenuIds()));
            this.update(updateWrapper);
        }

        return ApiResult.ok();
    }

}
