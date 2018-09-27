package com.bootdo.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.system.dao.UserMenuDao;
import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.TransferUser;
import com.bootdo.system.domain.UserMenuDO;
import com.bootdo.system.service.UserMenuService;

@CacheConfig(cacheNames = "user-menu-cache")
@Service
public class UserMenuServiceImpl implements UserMenuService{
	
	@Autowired
	UserMenuDao userMenuMapper;
	
	@Cacheable
	@Override
	public List<UserMenuDO> list(Map<String, Object> map){
		return userMenuMapper.list(map);
	}
	@CacheEvict(value = "user-menu-cache", allEntries = true)
	@Override
	public int save(UserMenuDO userRole) {
		// TODO Auto-generated method stub
		return userMenuMapper.save(userRole);
	}
	@CacheEvict(value = "user-menu-cache", allEntries = true)
	@Override
	public int update(UserMenuDO userRole) {
		// TODO Auto-generated method stub
		return userMenuMapper.update(userRole);
	}

	@Override
	public int count(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userMenuMapper.count(map);
	}
	
	
	@CacheEvict(value = "user-menu-cache", allEntries = true)
	@Override
	public int remove(Long id) {
		// TODO Auto-generated method stub
		return userMenuMapper.remove(id);
	}

	@CacheEvict(value = "user-menu-cache", allEntries = true)
	@Override
	public int removeByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userMenuMapper.removeByUserId(userId);
	}
	
	@Autowired
	UserMenuDao  userMenuDao;
	/**
	 * 传入 menuId， 以及条件搜索筛选出来的 transferUser List
	 * 然后遍历 transferUser，
	 * 通过查User——Menu 表， 具有权限的user ID取出来存入数组中，
	 * 返回Transfer User List , 其中具有权限的 hasAuthor 属性设置为 true，
	 * @param menuId
	 * @param transferUsers
	 * @return
	 */
	@Cacheable
	public List<TransferUser> hasUserMenuPerms(long menuId, List<TransferUser> transferUsers){
		List<Long> userIds = new ArrayList<>();
		if(transferUsers != null) {
			int count = 0;
			for(TransferUser  transferUser : transferUsers) {
				transferUser.setHasAuthority(false);
				userIds.add(transferUser.getUserId());
				count++;
			}
			System.out.println(userIds.toString() + transferUsers.toString());
			try {
				System.out.println("daer");
				System.out.println(userMenuDao.hasUserMenuPerms(menuId,userIds).toString()); 
				Long[] hasPerms = userMenuDao.hasUserMenuPerms(menuId,userIds);
				for(int i = 0; i < hasPerms.length; i++) {
					System.out.println("long"+hasPerms[i] +"  "+hasPerms.length +"  "+i);
					for(int j = 0 ; j<transferUsers.size();j++) {
						System.out.println("循环了"+j + transferUsers.get(j).toString());
						if(transferUsers.get(j).getUserId().equals(hasPerms[i])) {
							System.out.println("bian");
							transferUsers.get(j).setHasAuthority(true);
						}
					}
//					for(TransferUser  transferUser : transferUsers) {
//						System.out.println("ididdidi"+transferUser.getUserId());
//						if(hasPerms[i] .equals(transferUser.getUserId())) {
//							System.out.println("更改了");
//							transferUser.setHasAuthority(true);
//						}
//					}
				}
			}catch(Exception e) {
				System.out.println(e);
			}
		}
		System.out.println(transferUsers);
		return transferUsers;
	}
	
	@Autowired
	MenuServiceImpl menuServiceImpl;
	@CacheEvict(value = "user-menu-cache", allEntries = true)
	//指定出现runtimeException 时候，进行回滚
	//指定存在事务的时候，加入已存在的事务，事务不存在，则创建事务（默认值）
	@Transactional(rollbackFor = {RuntimeException.class,Exception.class},propagation=Propagation.REQUIRED)
	public int updateUserMenuPerms(List<Long> allUserIds, List<Long> withoutPermsUserIds, Long menuId) {
		
		int result = 0;

			result = userMenuDao.batchRemoveBymenuIdAnduserIds(menuId, allUserIds);
			System.out.println(allUserIds);
			System.out.println(withoutPermsUserIds);
			allUserIds.removeAll(withoutPermsUserIds);
			System.out.println(allUserIds);
			List<UserMenuDO> userMenuDOs = new ArrayList<>();
			for(Long userId : allUserIds) {
				UserMenuDO userMenuDo = new UserMenuDO();
				userMenuDo.setMenuId(menuId);
				userMenuDo.setUserId(userId);
				userMenuDOs.add(userMenuDo);
			}
			System.out.println(userMenuDOs);
//			if(result > 0) {
//				throw new RuntimeException("事务测试1");
//			}
			userMenuDao.batchSave(userMenuDOs);
			MenuDomain menuDomain = new MenuDomain();
			menuDomain.setName("回滚测试");
			menuDomain.setParentId(50L);
			menuDomain.setMenuId(1507L);
			menuServiceImpl.updateMenu(menuDomain);
		
		
		return result;
	}


}
