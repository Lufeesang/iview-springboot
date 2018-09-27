package com.bootdo.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.bootdo.system.domain.UserMenuDO;

@Service
public interface UserMenuService {
	List<UserMenuDO> list(Map<String, Object> map);
	
	int save(UserMenuDO userRole);

	int update(UserMenuDO userRole);
	
	int count(Map<String, Object> map);
	
	int remove(Long id);
	
	int removeByUserId(Long userId);
	
}
