package com.studio.settlement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSONObject;
import com.studio.settlement.bean.dto.RoleDTO;
import com.studio.settlement.bean.po.Role;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.UserVO;
import com.studio.settlement.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理接口
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理接口", description = "角色管理相关接口")
public class RoleController {

	@Autowired
	RoleService roleService;

	@SaCheckLogin
	@PostMapping("/queryRoleList")
	@Operation(summary = "查询角色列表", description = "根据条件查询角色列表信息")
	public ApiResult<UserVO> queryRoleList(@RequestBody RoleDTO roleDTO) {
		log.info("查询角色列表：{}", JSONObject.toJSON(roleDTO));
		return ApiResult.data(roleService.queryRoleList(roleDTO));
	}

	@SaCheckLogin
	@PostMapping("/queryNoRateRoleList")
	@Operation(summary = "查询未绑定部门角色列表", description = "查询未与部门绑定的角色列表信息")
	public ApiResult<UserVO> queryNoRateRoleList(@RequestBody RoleDTO roleDTO) {
		log.info("查询未绑定部门角色列表：{}", JSONObject.toJSON(roleDTO));
		return ApiResult.data(roleService.queryNoRateRoleList(roleDTO));
	}

	@SaCheckLogin
	@PostMapping("/addRole")
	@Operation(summary = "新增角色", description = "新增角色信息")
	public ApiResult<Role> addRole(@RequestBody Role role) {
		log.info("新增角色：{}", JSONObject.toJSONString(role));
		roleService.save(role);

		return ApiResult.data(role);
	}


	@SaCheckLogin
	@PostMapping("/updateRole")
	@Operation(summary = "修改角色", description = "修改角色信息")
	public ApiResult<Role> updateRole(@RequestBody Role role) {
		log.info("修改角色：{}", JSONObject.toJSONString(role));
		if (null == roleService.getById(role.getId())) {
			return ApiResult.error("数据异常，角色信息不存在");
		}

		roleService.updateById(role);

		return ApiResult.data(role);
	}

	@SaCheckLogin
	@PostMapping("/delRole")
	@Operation(summary = "删除角色", description = "删除角色信息")
	public ApiResult<Boolean> delRole(@RequestBody Role role) {

		return ApiResult.data(roleService.delRole(role));
	}

}
