package com.studio.settlement.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studio.settlement.bean.dto.*;
import com.studio.settlement.bean.po.*;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.DepVO;
import com.studio.settlement.bean.vo.RoleVO;
import com.studio.settlement.bean.vo.UserVO;
import com.studio.settlement.mapper.DepMapper;
import com.studio.settlement.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepServiceImpl extends ServiceImpl<DepMapper, Dep> implements DepService {
    @Autowired
    UserService userService;
    @Autowired
    DepToRolesService depToRolesService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserToRolesService userToRolesService;
    @Autowired
    DepToUsersService depToUsersService;

    /**
     * 获取所有分厂的部门名称-部门ID的map集合
     * @return
     */
    public Map<String, Long> getDepNameToIdMap(){
        /*List<Dep> depList = this.list();
        log.info("查询到的部门数量：{}", depList.size());
        if (CollectionUtils.isEmpty(depList)){
            return new HashMap<>();
        }

        Map<String, Long> depMap = depList.stream().collect(Collectors.toMap(Dep::getDepName, Dep::getId));
        log.info("部门名称-部门ID的map集合：{}", JSONObject.toJSON(depMap));*/

        Map<String, Long> depMap = new HashMap<>();
        depMap.put("星沙", 2L);
        depMap.put("城西", 3L);
        depMap.put("城北", 4L);
        depMap.put("城南", 5L);
        depMap.put("城东", 6L);

        return depMap;
    }

    /**
     * 获取所有分厂的部门ID-部门名称的map集合
     * @return
     */
    public Map<Long, String> getDepIdToNameMap(){
        List<Dep> depList = this.list();
        log.info("查询到的部门数量：{}", depList.size());
        if (CollectionUtils.isEmpty(depList)){
            return new HashMap<>();
        }

        Map<Long, String> depMap = depList.stream().collect(Collectors.toMap(Dep::getId, Dep::getDepName));
        log.info("部门ID-部门名称的map集合：{}", JSONObject.toJSON(depMap));

        return depMap;
    }


    public DepVO getDeps(){
        Object loginId = StpUtil.getLoginIdByToken(StpUtil.getTokenValue());
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, loginId));

        // 查询一级部门
        Dep dep = this.getOne(new LambdaQueryWrapper<Dep>().eq(Dep::getId, user.getDepId()));
        // 递归查询下级部门
        DepVO depVO = new DepVO();
        BeanUtils.copyProperties(dep, depVO);
        depVO.setDepVOS(this.getChildren(dep));

        return depVO;
    }

    public ApiResult<Boolean> addDep(Dep dep){
        log.info("新增组织：{}", JSONObject.toJSONString(dep));
        long countCode = this.count(new LambdaQueryWrapper<Dep>().eq(Dep::getDepCode, dep.getDepCode()));
        if(countCode > 0){
            log.info("组织编码已存在");
            return ApiResult.error("组织编码已存在");
        }

        if(!this.save(dep)){
           return ApiResult.error("新增组织失败");
        }

        return ApiResult.ok("新增组织成功");
    }

    public Boolean updateDep(Dep dep){
        log.info("修改组织：{}", JSONObject.toJSONString(dep));

        boolean b = this.updateById(dep);

        log.info("修改组织结果：{}", b);

        return b;
    }

    public Boolean delDep(Dep dep){
        log.info("删除组织：{}", JSONObject.toJSONString(dep));

        // 删除组织角色关联
        depToRolesService.remove(new LambdaQueryWrapper<DepToRoles>().eq(DepToRoles::getDepId, dep.getId()));
        // 删除组织用户关联
        depToUsersService.remove(new LambdaQueryWrapper<DepToUsers>().eq(DepToUsers::getDepId, dep.getId()));
        // 删除组织
        boolean b = this.removeById(dep.getId());

        log.info("删除组织结果：{}", b);

        return b;
    }

    public Boolean updateDepToRole(DepToRolesDTO depToRolesDTO){
        boolean b = false;
        if(0 == depToRolesDTO.getIsDelete()){
            log.info("关联组织角色：{}", JSONObject.toJSONString(depToRolesDTO));

            DepToRoles depToRoles = new DepToRoles();
            depToRoles.setDepId(depToRolesDTO.getDepId());
            depToRoles.setRoleId(depToRolesDTO.getRoleId());

            // 保存关联关系
            b = depToRolesService.save(depToRoles);

            log.info("关联组织角色结果：{}", b);
        }else {
            log.info("删除组织角色关联：{}", JSONObject.toJSONString(depToRolesDTO));

            b = depToRolesService.remove(new LambdaQueryWrapper<DepToRoles>().eq(DepToRoles::getRoleId, depToRolesDTO.getRoleId()).eq(DepToRoles::getDepId, depToRolesDTO.getDepId()));

            log.info("删除组织角色关联结果：{}", b);
        }
        return b;
    }

    public Page<RoleVO> queryDepRoles(DepToRolesDTO depToRolesDTO){
        List<Long> roleIdByNames = new ArrayList<>();
        // 查询角色id列表
        if(null != depToRolesDTO.getRoleName()){
            roleIdByNames = roleService.list(new LambdaQueryWrapper<Role>().like(Role::getRoleName, depToRolesDTO.getRoleName())).stream().map(Role::getId).collect(Collectors.toList());
        }
        // 查询所有关联角色Id
        List<DepToRoles> depToRoles = depToRolesService.list(new LambdaQueryWrapper<DepToRoles>().eq(DepToRoles::getDepId, depToRolesDTO.getDepId()).in(roleIdByNames.size() > 0, DepToRoles::getRoleId, roleIdByNames));
        List<Long> roleIds = new ArrayList<>();
        for (DepToRoles depToRole : depToRoles) {
            roleIds.add(depToRole.getRoleId());
        }

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        // 角色名称模糊查询
        // 增加roleIds空判断
        if(roleIds.size() == 0){
            roleIds.add(0L);
        }
        wrapper.in(Role::getId, roleIds);
        if (null == depToRolesDTO.getPageNo()){
            depToRolesDTO.setPageNo(1);
            depToRolesDTO.setPageSize(10);
        }

        wrapper.orderByDesc(Role::getId);
        Page<Role> page = new Page<>(depToRolesDTO.getPageNo(), depToRolesDTO.getPageSize());
        page = roleService.page(page, wrapper);

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
        // 查询厂级部门
        Dep dep = this.getById(roleDTO.getDepId());
        if(1 != dep.getIsComp()){
            dep = this.getCompDep(dep);
        }
        wrapper.eq(Role::getDepId, dep.getId());
        // 角色名称模糊查询
        wrapper.like(StringUtils.isNotBlank(roleDTO.getRoleName()), Role::getRoleName, roleDTO.getRoleName());
        if (null == roleDTO.getPageNo()){
            roleDTO.setPageNo(1);
            roleDTO.setPageSize(1000);
        }
        // 角色编码模糊查询
        wrapper.like(StringUtils.isNotBlank(roleDTO.getRoleCode()), Role::getRoleCode, roleDTO.getRoleCode());
        wrapper.orderByDesc(Role::getId);
        Page<Role> page = new Page<>(roleDTO.getPageNo(), roleDTO.getPageSize());
        page = roleService.page(page, wrapper);

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

    public Boolean updateDepToUser(DepToUsersDTO depToUsersDTO){
        boolean b = false;
        if(0 == depToUsersDTO.getIsDelete()){
            log.info("关联组织角色：{}", JSONObject.toJSONString(depToUsersDTO));

            DepToUsers depToUsers = new DepToUsers();
            depToUsers.setDepId(depToUsersDTO.getDepId());
            depToUsers.setUserId(depToUsersDTO.getUserId());

            // 保存关联关系
            b = depToUsersService.save(depToUsers);

            // 查询厂级部门
            Dep dep = this.getById(depToUsersDTO.getDepId());
            if(1 != dep.getIsComp()){
                dep = this.getCompDep(dep);
            }
            // 更新角色部门id
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getId,depToUsersDTO.getUserId())
                    .set(User::getDepId,dep.getId());
            b = userService.update(null, updateWrapper);

            log.info("关联组织角色结果：{}", b);
        }else {
            log.info("删除组织角色关联：{}", JSONObject.toJSONString(depToUsersDTO));

            b = depToUsersService.remove(new LambdaQueryWrapper<DepToUsers>().eq(DepToUsers::getUserId, depToUsersDTO.getUserId()).eq(DepToUsers::getDepId, depToUsersDTO.getDepId()));

            log.info("删除组织角色关联结果：{}", b);
        }
        return b;
    }

    public Page<UserVO> queryDepUsers(DepToUsersDTO depToUsersDTO){
        // 查询所有关联角色Id
        List<DepToUsers> depToUsersList = depToUsersService.list(new LambdaQueryWrapper<DepToUsers>().eq(DepToUsers::getDepId, depToUsersDTO.getDepId()));
        List<Long> userIds = new ArrayList<>();
        for (DepToUsers depToUsers : depToUsersList) {
            userIds.add(depToUsers.getUserId());
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getId, userIds);

        if (null == depToUsersDTO.getPageNo()){
            depToUsersDTO.setPageNo(1);
            depToUsersDTO.setPageSize(10);
        }

        wrapper.orderByAsc(User::getId);
        Page<User> page = new Page<>(depToUsersDTO.getPageNo(), depToUsersDTO.getPageSize());
        page = userService.page(page, wrapper);

        // 查询结果处理  增加用户角色相关
        List<User> users = page.getRecords();
        List<UserVO> userVOS = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            // 查询用户角色名列表
            List<UserToRoles> userToRoles = userToRolesService.list(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getUserId, user.getId()));
            List<Long> roleIds = userToRoles.stream().map(UserToRoles::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleService.listByIds(roleIds);

            userVO.setRoleIds(roleIds);
            userVO.setRoleNames(CollectionUtil.isEmpty(roles) ? new ArrayList<>() :
                    roles.stream().map(Role::getRoleName).collect(Collectors.toList()));
            userVOS.add(userVO);
        }
        Page<UserVO> userVOPage = new Page<>();
        userVOPage.setRecords(userVOS);
        userVOPage.setTotal(page.getTotal());
        userVOPage.setSize(page.getSize());
        userVOPage.setCurrent(page.getCurrent());
        userVOPage.setPages(page.getPages());
        log.info("查询结果：{}", userVOPage.getRecords().size());
        return userVOPage;
    }

    public Page<UserVO> queryNoRoteUserList(UserDto userDto){
        // 查询所有关联角色Id
        List<DepToUsers> depToUsersList = depToUsersService.list(new LambdaQueryWrapper<DepToUsers>().eq(DepToUsers::getDepId, userDto.getDepId()));
        List<Long> userIds = new ArrayList<>();
        for (DepToUsers depToUsers : depToUsersList) {
            userIds.add(depToUsers.getUserId());
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if(userIds.size() == 0){
            userIds.add(0L);
        }

        // 用户状态
        wrapper.eq(User::getIsDelete, 0);

        if (null == userDto.getPageNo()){
            userDto.setPageNo(1);
            userDto.setPageSize(1000);
        }
        // 查询厂级部门
        Dep dep = this.getById(userDto.getDepId());
        if(1 != dep.getIsComp()){
            dep = this.getCompDep(dep);
        }
        wrapper.eq(User::getDepId, dep.getId());

        wrapper.orderByAsc(User::getId);
        Page<User> page = new Page<>(userDto.getPageNo(), userDto.getPageSize());
        page = userService.page(page, wrapper);

        // 查询结果处理  增加用户角色相关
        List<User> users = page.getRecords();
        List<UserVO> userVOS = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            // 查询用户角色名列表
            List<UserToRoles> userToRoles = userToRolesService.list(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getUserId, user.getId()));
            List<Long> roleIds = new ArrayList<>();
            List<String> roleNames = new ArrayList<>();
            for (UserToRoles userToRole : userToRoles) {
                roleIds.add(userToRole.getRoleId());
                roleNames.add(roleService.getById(userToRole.getRoleId()).getRoleName());
            }
            userVO.setRoleIds(roleIds);
            userVO.setRoleNames(roleNames);
            userVOS.add(userVO);
        }
        Page<UserVO> userVOPage = new Page<>();
        userVOPage.setRecords(userVOS);
        userVOPage.setTotal(page.getTotal());
        userVOPage.setSize(page.getSize());
        userVOPage.setCurrent(page.getCurrent());
        userVOPage.setPages(page.getPages());
        log.info("查询结果：{}", userVOPage.getRecords().size());
        return userVOPage;
    }

    public Boolean moveDepToUser(DepUserMoveDTO depUserMoveDTO){
        boolean b = false;
        depToUsersService.remove(new LambdaQueryWrapper<DepToUsers>().eq(DepToUsers::getDepId, depUserMoveDTO.getOldDepId()).eq(DepToUsers::getUserId, depUserMoveDTO.getUserId()));
        DepToUsers depToUsers = new DepToUsers();
        depToUsers.setDepId(depUserMoveDTO.getNewDepId());
        depToUsers.setUserId(depUserMoveDTO.getUserId());
        // 保存关联关系
        b = depToUsersService.save(depToUsers);
        // 查询厂级部门
        Dep dep = this.getById(depUserMoveDTO.getNewDepId());
        if(1 != dep.getIsComp()){
            dep = this.getCompDep(dep);
        }
        // 更新角色部门id
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,depUserMoveDTO.getUserId())
                .set(User::getDepId,dep.getId());
        b = userService.update(null, updateWrapper);

        return b;
    }

    /**
     * 递归查询下级部门
     * @param dep
     * @return
     */
    private List<DepVO> getChildren(Dep dep){
        List<Dep> children = this.list(new LambdaQueryWrapper<Dep>().eq(Dep::getParentCode, dep.getDepCode()).orderByAsc(Dep::getSorts));
        List<DepVO> depVOS = null;
        if (CollectionUtils.isEmpty(children)){
            return depVOS;
        }
        depVOS = new ArrayList<>();
        for (Dep child : children) {
            DepVO depVO = new DepVO();
            BeanUtils.copyProperties(child, depVO);
            depVO.setParentName(dep.getDepName());
            depVO.setDepVOS(this.getChildren(child));
            depVOS.add(depVO);
        }
        return depVOS;
    }

    /**
     * 递归查询厂级部门
     * @param dep
     * @return
     */
    private Dep getCompDep(Dep dep){
        Dep compDep = this.getOne(new LambdaQueryWrapper<Dep>().eq(Dep::getDepCode, dep.getParentCode()));
        if (1 == compDep.getIsComp()){
            return compDep;
        }else {
            compDep = this.getCompDep(compDep);
        }
        return compDep;
    }
}

