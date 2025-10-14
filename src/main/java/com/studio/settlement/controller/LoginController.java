package com.studio.settlement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSONObject;
import com.studio.settlement.bean.dto.UserDto;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.UserVO;
import com.studio.settlement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录
 */
@Slf4j
@RestController
@RequestMapping("/settlement/login")
@Tag(name = "登录接口", description = "用户登录相关接口")
public class LoginController {

	@Autowired
	UserService userService;

	@PostMapping("/login")
	@Operation(summary = "登录", description = "用户登录接口")
	public ApiResult<UserVO> doLogin(@RequestBody UserDto userDto, HttpServletResponse response) {
		log.info("用户登录：{}", JSONObject.toJSON(userDto));

		return userService.doLogin(userDto, response);
	}

	@PostMapping("/updatePwd")
	@Operation(summary = "修改密码", description = "修改用户密码接口")
	@SaCheckLogin
	public ApiResult updatePwd(@RequestBody UserDto userDto) {
		log.info("用户修改密码:{}", JSONObject.toJSON(userDto));

		return userService.updatePwd(userDto);
	}

	@PostMapping("/isLogin")
	@Operation(summary = "校验是否登录", description = "校验用户是否已登录")
	public ApiResult isLogin(@RequestBody UserDto userDto) {
		Object loginId = StpUtil.getLoginIdByToken(userDto.getToken());
		if (null == loginId){
			return ApiResult.error("账号未登录");
		}

		return ApiResult.ok();
	}

	@PostMapping("/tokenInfo")
	@Operation(summary = "获取token信息", description = "获取当前用户的token信息")
	public SaResult tokenInfo() {
		return SaResult.data(StpUtil.getTokenInfo());
	}

	@PostMapping("/logout")
	@Operation(summary = "退出", description = "用户退出登录")
	public SaResult logout() {
		userService.logout();
		return SaResult.ok();
	}

}
