package com.studio.settlement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studio.settlement.bean.dto.UserDto;
import com.studio.settlement.bean.po.User;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.UserVO;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService extends IService<User>{

    ApiResult<UserVO> doLogin(UserDto userDto, HttpServletResponse request);

    ApiResult updatePwd(UserDto userDto);

    Page<UserVO> queryUserList(UserDto userDto);

    ApiResult addUser(UserDto userDto);

    ApiResult updateUser(UserDto userDto);

    Boolean delUser(User user);

    void logout();
}
