package com.studio.settlement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.dto.*;
import com.studio.settlement.bean.po.Dep;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.DepVO;
import com.studio.settlement.bean.vo.RoleVO;
import com.studio.settlement.bean.vo.UserVO;
import com.studio.settlement.service.DepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 部门信息接口
 */
@Slf4j
@RestController
@RequestMapping("/dep")
@Tag(name = "部门信息接口", description = "部门管理相关接口")
public class DepController {

	@Autowired
	DepService depService;

	@PostMapping("/getDepIdToName")
	@Operation(summary = "获取部门ID-名称集合", description = "获取所有部门ID与名称的映射关系")
	public ApiResult<Map<Long, String>> getDepIdToName() {
		return ApiResult.data(depService.getDepIdToNameMap());
	}

	@PostMapping("/getDepNameToId")
	@Operation(summary = "获取部门名称-ID集合", description = "获取所有部门名称与ID的映射关系")
	public ApiResult<Map<String, Long>> getDepNameToId() {
		return ApiResult.data(depService.getDepNameToIdMap());
	}

	@PostMapping("/getDeps")
	@Operation(summary = "查询当前用户公司下所有组织", description = "查询当前用户所属公司下的所有组织信息")
	public ApiResult<DepVO> getDeps() {
		return ApiResult.data(depService.getDeps());
	}

	@SaCheckLogin
	@PostMapping("/addDep")
	@Operation(summary = "新增组织", description = "新增组织信息")
	public ApiResult<Boolean> addDep(@RequestBody Dep dep) {

		return depService.addDep(dep);
	}


	@SaCheckLogin
	@PostMapping("/updateDep")
	@Operation(summary = "修改组织", description = "修改组织信息")
	public ApiResult<Boolean> updateDep(@RequestBody Dep dep) {

		return ApiResult.data(depService.updateDep(dep));
	}

	@SaCheckLogin
	@PostMapping("/delDep")
	@Operation(summary = "删除组织", description = "删除组织信息")
	public ApiResult<Boolean> delDep(@RequestBody Dep dep) {

		return ApiResult.data(depService.delDep(dep));
	}

	@SaCheckLogin
	@PostMapping("/updateDepToRole")
	@Operation(summary = "关联/删除角色关联关系", description = "建立或删除部门与角色的关联关系")
	public ApiResult<Boolean> updateDepToRole(@RequestBody DepToRolesDTO depToRolesDTO) {

		return ApiResult.data(depService.updateDepToRole(depToRolesDTO));
	}

	@SaCheckLogin
	@PostMapping("/queryDepRoles")
	@Operation(summary = "查询所有关联角色", description = "查询与指定部门关联的所有角色信息")
	public ApiResult<Page<RoleVO>> queryDepRoles(@RequestBody DepToRolesDTO depToRolesDTO) {

		return ApiResult.data(depService.queryDepRoles(depToRolesDTO));
	}

	@SaCheckLogin
	@PostMapping("/queryNoRateRoleList")
	@Operation(summary = "查询未绑定部门角色列表", description = "查询未与指定部门绑定的角色列表")
	public ApiResult<UserVO> queryNoRateRoleList(@RequestBody RoleDTO roleDTO) {
		log.info("查询未绑定部门角色列表：{}", JSONObject.toJSON(roleDTO));
		return ApiResult.data(depService.queryNoRateRoleList(roleDTO));
	}

	@SaCheckLogin
	@PostMapping("/updateDepToUser")
	@Operation(summary = "关联/删除组织员工关联关系", description = "建立或删除部门与员工的关联关系")
	public ApiResult<Boolean> updateDepToUser(@RequestBody DepToUsersDTO depToUsersDTO) {

		return ApiResult.data(depService.updateDepToUser(depToUsersDTO));
	}

	@SaCheckLogin
	@PostMapping("/queryDepUsers")
	@Operation(summary = "查询所有关联员工", description = "查询与指定部门关联的所有员工信息")
	public ApiResult<Page<UserVO>> queryDepUsers(@RequestBody DepToUsersDTO depToUsersDTO) {

		return ApiResult.data(depService.queryDepUsers(depToUsersDTO));
	}

	@SaCheckLogin
	@PostMapping("/queryNoRoteUserList")
	@Operation(summary = "查询为绑定企业用户列表", description = "查询未与企业绑定的用户列表")
	public ApiResult<UserVO> queryNoRoteUserList(@RequestBody UserDto userDto) {
		log.info("查询用户列表：{}", JSONObject.toJSON(userDto));
		return ApiResult.data(depService.queryNoRoteUserList(userDto));
	}

	@SaCheckLogin
	@PostMapping("/moveDepToUser")
	@Operation(summary = "移动员工", description = "将员工从一个部门移动到另一个部门")
	public ApiResult<Boolean> moveDepToUser(@RequestBody DepUserMoveDTO depUserMoveDTO) {

		return ApiResult.data(depService.moveDepToUser(depUserMoveDTO));
	}
}
