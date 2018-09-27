package com.bootdo.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bootdo.common.domain.InterSystemConnectionDO;
import com.bootdo.common.redis.shiro.RedisManager;
import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;

@Service
public interface SSOService {
	String createToken(UserSubsystemSsuserDO subsystemSsuser);
		
	List<UserSubsystemSsuserDO> getUserSubsystemSsuserList(Map<String, Object> map);
	
	void saveConnection(RedisManager redisManager, InterSystemConnectionDO interSystemConnectionDO);

	@Deprecated
	boolean verify(String subsystemUserId, String sysIdentification, String token);
	
	List<SubsystemDO> getSubsystemList(Map<String, Object> map);
	
	public InterSystemConnectionDO getConnection(String subsystemUserId, String sysIdentification, String token);
	
	public void deleteConnection(RedisManager redisManager, String token);
	
	public SubsystemDO getByMenuId (Long menuId);
}
