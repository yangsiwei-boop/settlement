package com.studio.settlement.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studio.settlement.bean.dto.UserDto;
import com.studio.settlement.bean.po.DepToUsers;
import com.studio.settlement.bean.po.Role;
import com.studio.settlement.bean.po.User;
import com.studio.settlement.bean.po.UserToRoles;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.UserVO;
import com.studio.settlement.mapper.RoleMapper;
import com.studio.settlement.mapper.UserMapper;
import com.studio.settlement.service.DepToUsersService;
import com.studio.settlement.service.UserService;
import com.studio.settlement.service.UserToRolesService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserToRolesService userToRolesService;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    DepToUsersService depToUsersService;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public ApiResult<UserVO> doLogin(UserDto userDto, HttpServletResponse response) {
        if (StringUtils.isBlank(userDto.getUsername()) || StringUtils.isBlank(userDto.getPassword())){
            return ApiResult.error("账号密码不能为空");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDto.getUsername());
        queryWrapper.eq(User::getIsDelete, 0);
        User user = this.getOne(queryWrapper);
        log.info("查询用户结果：{}", JSONObject.toJSON(user));

        if (null == user){
            LambdaQueryWrapper<User> queryDelWrapper = new LambdaQueryWrapper<>();
            queryDelWrapper.eq(User::getUsername, userDto.getUsername());
            queryDelWrapper.eq(User::getIsDelete, 1);
            User userDel = this.getOne(queryDelWrapper);
            if (null != userDel){
                return ApiResult.error(" 该账号已禁用，请联系管理员");
            }
            return ApiResult.error("用户不存在");
        }
        if (!StringUtils.equals(userDto.getPassword(), user.getPassword())){
            return ApiResult.error("密码不正确");
        }

        StpUtil.login(user.getId());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setTokenInfo(tokenInfo);

        Map<String,SaTokenInfo> tokenMap = redisTemplate.opsForHash().entries("saTokenMap");
        if(null == tokenMap){
            tokenMap = new HashMap<>();
        }
        tokenMap.put(user.getId().toString(), tokenInfo);
        redisTemplate.opsForHash().putAll("saTokenMap", tokenMap);

        // 返回角色信息
        LambdaQueryWrapper<UserToRoles> userToRolesWrapper = Wrappers.lambdaQuery(UserToRoles.class)
                .eq(UserToRoles::getUserId, user.getId());
        List<UserToRoles> userToRolesList = userToRolesService.list(userToRolesWrapper);
        List<Long> roleIdList = userToRolesList.stream().map(UserToRoles::getRoleId).collect(Collectors.toList());
        userVO.setRoleIds(roleIdList);

        if (CollectionUtils.isNotEmpty(roleIdList)) {
            List<Role> roles = roleMapper.selectBatchIds(roleIdList);
            List<String> roleNameList = roles.stream().map(Role::getRoleName).collect(Collectors.toList());
            userVO.setRoleNames(roleNameList);
        }

        // 将账号密码设置到cookie
        Cookie username = new Cookie("username", user.getUsername());
        username.setMaxAge(60 * 60 * 24 * 30);
        Cookie password = new Cookie("password", user.getPassword());
        password.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(username);
        response.addCookie(password);

        return ApiResult.data(userVO);
    }

    @Override
    public ApiResult updatePwd(UserDto userDto) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDto.getUsername()));

        if (null == user){
            return ApiResult.error("当前用户不存在");
        }
        user.setPassword(userDto.getPassword());

        boolean save = this.saveOrUpdate(user);
        return save ? ApiResult.ok("修改成功") : ApiResult.error("修改失败");
    }

    public Page<UserVO> queryUserList(UserDto userDto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 所属单位 公司账号查所有，分属单位查自己
        if (null != userDto.getDepId() && 1L != userDto.getDepId()){
            wrapper.eq(User::getDepId, userDto.getDepId());
        }
        // 用户名称模糊查询
        wrapper.like(StringUtils.isNotBlank(userDto.getRealName()), User::getRealName, userDto.getRealName());
        wrapper.like(StringUtils.isNotBlank(userDto.getUsername()), User::getUsername, userDto.getUsername());
        wrapper.like(StringUtils.isNotBlank(userDto.getPhone()), User::getPhone, userDto.getPhone());
        // 用户状态
        if (null != userDto.getIsDelete()){
            wrapper.eq(User::getIsDelete, userDto.getIsDelete());
        }
        if (null == userDto.getPageNo()){
            userDto.setPageNo(1);
            userDto.setPageSize(10);
        }

        wrapper.orderByAsc(User::getId);
        Page<User> page = new Page<>(userDto.getPageNo(), userDto.getPageSize());
        page = this.page(page, wrapper);

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
                roleNames.add(roleMapper.selectById(userToRole.getRoleId()).getRoleName());
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


    public ApiResult addUser(UserDto userDto){
        log.info("新增用户：{}", JSONObject.toJSONString(userDto));

        // 用户名称、手机号校验
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, userDto.getUsername())
                .or().eq(User::getPhone, userDto.getPhone());
        List<User> userList = this.list(queryWrapper);

        if (CollectionUtils.isNotEmpty(userList)) {
            return ApiResult.error("账号或手机号已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        // 设置默认密码
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword("123456");
        }

        if(!this.save(user)){
            return ApiResult.error("新增失败");
        }

        // 新增用户角色关联
        if (CollectionUtils.isNotEmpty(userDto.getRoleIds())) {
            List<Long> roleIds = userDto.getRoleIds();
            List<UserToRoles> userToRolesList = new ArrayList<>();
            for (Long roleId : roleIds) {
                UserToRoles userToRole = new UserToRoles();
                userToRole.setUserId(user.getId());
                userToRole.setRoleId(roleId);
                userToRolesList.add(userToRole);
            }

            userToRolesService.saveBatch(userToRolesList);
        }

        return ApiResult.ok();
    }

    public ApiResult updateUser(UserDto userDto){
        log.info("修改用户：{}", JSONObject.toJSONString(userDto));

        // 用户名称、手机号校验
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .ne(User::getId, userDto.getId())
                .and(temp -> temp.eq(User::getUsername, userDto.getUsername()).or().eq(User::getPhone, userDto.getPhone()));
        List<User> userList = this.list(queryWrapper);

        if (CollectionUtils.isNotEmpty(userList)) {
            return ApiResult.error("账号或手机号已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        if (CollectionUtils.isNotEmpty(userDto.getRoleIds())) {
            // 查询用户角色名列表
            List<Long> roleIds = userDto.getRoleIds();

            // 删除用户角色关联
            userToRolesService.remove(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getUserId, user.getId()));
            // 新增用户角色关联
            for (Long roleId : roleIds) {
                UserToRoles userToRole = new UserToRoles();
                userToRole.setUserId(userDto.getId());
                userToRole.setRoleId(roleId);
                userToRolesService.save(userToRole);
            }
        }

        return ApiResult.data(this.updateById(user));
    }

    public Boolean delUser(User user){
        log.info("删除/激活用户：{}", JSONObject.toJSONString(user));

        // 删除用户角色关联
        userToRolesService.remove(new LambdaQueryWrapper<UserToRoles>().eq(UserToRoles::getUserId, user.getId()));
        // 删除组织员工关联
        depToUsersService.remove(new LambdaQueryWrapper<DepToUsers>().eq(DepToUsers::getUserId, user.getId()));

        // 删除用户
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,user.getId())
                .set(User::getIsDelete,user.getIsDelete());
        boolean b = this.update(null, updateWrapper);

        log.info("删除/激活结果：{}", b);

        return b;
    }

    @Override
    public void logout(){
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        redisTemplate.opsForHash().delete("saTokenMap", userId.toString());
        StpUtil.logout();
    }
}
