package com.bootdo.system.service;

import java.util.List;

import com.bootdo.system.domain.RoleMenuDO;


public interface RoleMenuService {
	
	List<Long> listMenuIdByRoleId(long roleId);
	
	int updateRoleMenu(Long roleId , List<RoleMenuDO> roleMenuDOs, List<Long> RoleMenuList);
}
