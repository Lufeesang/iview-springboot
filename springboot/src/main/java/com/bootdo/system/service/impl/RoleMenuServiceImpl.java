package com.bootdo.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.system.dao.RoleMenuDao;
import com.bootdo.system.domain.RoleMenuDO;
import com.bootdo.system.service.RoleMenuService;

@Service
@CacheConfig(cacheNames = "role-menu-cache")
public class RoleMenuServiceImpl implements RoleMenuService{
	
	@Autowired
	RoleMenuDao roleMenuDao;
	@Cacheable
	public List<Long> listMenuIdByRoleId(long roleId){
		
		System.out.println(roleMenuDao.listMenuIdByRoleId(roleId));
		return roleMenuDao.listMenuIdByRoleId(roleId);
	}
	
	@CacheEvict(value = "role-menu-cache", allEntries = true)
	@Transactional(rollbackFor = {RuntimeException.class,Exception.class}
 	, propagation= Propagation.REQUIRED)
	public int updateRoleMenu(Long roleId, List<RoleMenuDO> roleMenuDOs, List<Long> RoleMenuList) {
		int result = -1;
		try {
//			int size = RoleMenuList.size();
//			//转化为 long[]
//			System.out.println("size"+size);
//			Long[] batchRemove = (Long []) RoleMenuList.toArray(new Long[size]);
//			System.out.println("long size"+batchRemove.length);
//			for(int i = 0; i<size ; i++) {
//				System.out.print(batchRemove[i]+" ");
//			}
			System.out.println(" ");
			System.out.println(roleId);
			System.out.println("删除结果"+roleMenuDao.batchRemoveByRoleIdMenuId(roleId, RoleMenuList));
			int saveCount = 0;
			for(RoleMenuDO roleMenuDO : roleMenuDOs) {
				result = roleMenuDao.save(roleMenuDO);
				saveCount = saveCount + result;
			}
			System.out.println("saveCount"+saveCount);
		}catch(Exception e) {
			System.out.println("rolemenuService 出错"+e);
		}
		return result;
	}
}
