package com.bootdo.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bootdo.system.domain.RoleDO;
import com.bootdo.system.domain.UserRoleDO;

@Service
public interface RoleService {

	RoleDO get(Long id);

	List<RoleDO> list(HashMap<String, Object> params);

	int save(RoleDO role);

	int update(RoleDO role);

	int remove(Long id);

	List<RoleDO> list(Long roleId);

	int batchremove(Long[] ids);
	
	List<RoleDO> parentsList(List<UserRoleDO> userRoleDOs);
	
	int batchSave(List<RoleDO> list);
}
