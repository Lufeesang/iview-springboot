package com.bootdo.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bootdo.system.domain.UserRoleDO;

@Service
public interface PersonUserRoleService {
	
	UserRoleDO getUserRoleId(Long id);
	
	List<Integer> getUserId(Long RoleId);
	
	int removeByRoleId(Long RoleId);
	
	int save(UserRoleDO userRoleDO);
	
	int removeByUserId(Long userId);
	
	int count(Map<String, Object> map);
	
	List<UserRoleDO> list(Map<String, Object> map);
}
