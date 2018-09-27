package com.bootdo.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bootdo.system.domain.TransferUser;
import com.bootdo.system.domain.UserSearchLimit;

@Service 
public interface GetUserService {
	
	public List<TransferUser> listUser(UserSearchLimit userSearchLimit);
}
