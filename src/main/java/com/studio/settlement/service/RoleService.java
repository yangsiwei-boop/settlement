package com.studio.settlement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studio.settlement.bean.dto.MenuGrantDTO;
import com.studio.settlement.bean.dto.RoleDTO;
import com.studio.settlement.bean.po.Role;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.RoleVO;

public interface RoleService extends IService<Role>{

    Page<RoleVO> queryRoleList(RoleDTO roleDTO);

    Page<RoleVO> queryNoRateRoleList(RoleDTO roleDTO);

    Boolean delRole(Role role);

    ApiResult grant(MenuGrantDTO menuGrantDTO);

}
