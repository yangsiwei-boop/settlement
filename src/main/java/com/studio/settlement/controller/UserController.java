package com.studio.settlement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.studio.settlement.bean.dto.UserDto;
import com.studio.settlement.bean.po.User;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.UserVO;
import com.studio.settlement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户管理接口
 */
@Slf4j
@RestController
@RequestMapping("/settlement/user")
@Tag(name = "用户管理接口", description = "用户管理相关接口")
public class UserController {

	@Autowired
	UserService userService;

	@SaCheckLogin
	@PostMapping("/queryUserList")
	@Operation(summary = "查询用户列表", description = "根据条件查询用户列表信息")
	public ApiResult<UserVO> queryUserList(@RequestBody UserDto userDto) {
		log.info("查询用户列表：{}", JSONObject.toJSON(userDto));
		return ApiResult.data(userService.queryUserList(userDto));
	}

	@SaCheckLogin
	@PostMapping("/addUser")
	@Operation(summary = "新增用户", description = "新增用户信息")
	public ApiResult<Boolean> addUser(@RequestBody UserDto userDto) {

		return userService.addUser(userDto);
	}


	@SaCheckLogin
	@PostMapping("/updateUser")
	@Operation(summary = "修改用户", description = "修改用户信息")
	public ApiResult<Boolean> updateUser(@RequestBody UserDto userDto) {

		return userService.updateUser(userDto);
	}

	@SaCheckLogin
	@PostMapping("/delUser")
	@Operation(summary = "删除/激活用户", description = "删除或激活用户状态")
	public ApiResult<Boolean> delUser(@RequestBody User user) {

		return ApiResult.data(userService.delUser(user));
	}
}
