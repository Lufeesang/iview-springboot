package com.bootdo.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bootdo.system.dao.UserDao;
import com.bootdo.system.domain.TransferUser;
import com.bootdo.system.domain.UserSearchLimit;
import com.bootdo.system.service.GetUserService;

@Service
@CacheConfig(cacheNames = "user-cache")
public class GetUserServiceImpl implements GetUserService {
	
	@Autowired
	UserDao userDao;
	@Override
	@Cacheable
	public  List<TransferUser> listUser(UserSearchLimit userSearchLimit){
		System.out.println(userDao.listUser(userSearchLimit));
		return userDao.listUser(userSearchLimit);
	}
}
