package com.bootdo.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.system.dao.UserRoleDao;
import com.bootdo.system.domain.UserRoleDO;
import com.bootdo.system.service.PersonUserRoleService;


@Service
@CacheConfig(cacheNames = "user-cache")
public class PersonUserRoleServiceImpl implements PersonUserRoleService{
	
	@Autowired
	UserRoleDao userRoleMapper;

	@Override
	public UserRoleDO getUserRoleId(Long id) {
		// TODO Auto-generated method stub
		return userRoleMapper.getUserRoleId(id);
	}

	@Override
	public List<Integer> getUserId(Long roleId) {
		// TODO Auto-generated method stub
		return userRoleMapper.getUserId(roleId);
	}
	
	@CacheEvict(value = "user-cache", allEntries = true)
	@Override
	public int removeByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return userRoleMapper.removeByRoleId(roleId);
	}
	
	@CacheEvict(value = "user-cache", allEntries = true)
	@Override
	public int save(UserRoleDO userRoleDO) {
		// TODO Auto-generated method stub
		return userRoleMapper.save(userRoleDO);
	}
	@CacheEvict(value = "user-cache", allEntries = true)
	@Override
	public int removeByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userRoleMapper.removeByUserId(userId);
	}

	@Override
	public int count(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userRoleMapper.count(map);
	}
	
	@Cacheable
	@Override
	public List<UserRoleDO> list(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userRoleMapper.list(map);
	}
	
	
}
