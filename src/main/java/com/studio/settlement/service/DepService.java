package com.studio.settlement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studio.settlement.bean.dto.*;
import com.studio.settlement.bean.po.Dep;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.DepVO;
import com.studio.settlement.bean.vo.RoleVO;
import com.studio.settlement.bean.vo.UserVO;

import java.util.Map;

public interface DepService extends IService<Dep>{

    Map<String, Long> getDepNameToIdMap();

    Map<Long, String> getDepIdToNameMap();

    DepVO getDeps();

    ApiResult<Boolean> addDep(Dep dep);

    Boolean updateDep(Dep dep);

    Boolean delDep(Dep dep);

    Boolean updateDepToRole(DepToRolesDTO depToRolesDTO);

    Page<RoleVO> queryDepRoles(DepToRolesDTO depToRolesDTO);

    Page<RoleVO> queryNoRateRoleList(RoleDTO roleDTO);

    Boolean updateDepToUser(DepToUsersDTO depToUsersDTO);

    Page<UserVO> queryDepUsers(DepToUsersDTO depToUsersDTO);

    Page<UserVO> queryNoRoteUserList(UserDto userDto);

    Boolean moveDepToUser(DepUserMoveDTO depUserMoveDTO);
}
