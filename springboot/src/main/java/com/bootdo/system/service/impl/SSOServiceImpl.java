package com.bootdo.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootdo.common.domain.InterSystemConnectionDO;
import com.bootdo.common.redis.shiro.RedisManager;
import com.bootdo.common.redis.shiro.SerializeUtils;
import com.bootdo.common.utils.Base64Utils;
import com.bootdo.common.utils.MD5Utils;
import com.bootdo.system.dao.SubsystemDAO;
import com.bootdo.system.dao.SubsystemMenuDAO;
import com.bootdo.system.dao.UserSubsystemSsuserDAO;
import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;
import com.bootdo.system.service.SSOService;

@Service
public class SSOServiceImpl implements SSOService{
	@Autowired
	UserSubsystemSsuserDAO userSubsystemSsuserDAO;
	@Autowired
	SubsystemDAO subsystemDAO;
	@Autowired
	SubsystemMenuDAO subsystemMenuDAO;
	
	@Override
	public String createToken(UserSubsystemSsuserDO subsystemSsuser) {
		StringBuffer sb = new StringBuffer();
		sb.append(Base64Utils.encode(subsystemSsuser)).append(
				Base64Utils.encode(System.currentTimeMillis()));
		return MD5Utils.encrypt(sb.toString());
	}

	@Deprecated
	@Override
	public  boolean verify(String subsystemUserId, String sysIdentification, String token) {
		Boolean res = false;
		String key = null;
		byte[] byteKey = null;
		InterSystemConnectionDO connection = null;
		//根据subsystemUserId，sysIdentification在缓存中找到token，并与提供的token作比较
//	    key = "connect-" + sysIdentification + "-" + subsystemUserId;
		key = "connect-" + token;
		byteKey = SerializeUtils.serialize(key);
		RedisManager redisManager = new RedisManager();
		redisManager.init();
		connection = (InterSystemConnectionDO)SerializeUtils.deserialize(redisManager.get(byteKey));
		if(connection != null) {
//			res = connection.getToken().equals(token);
//			if(res) {
//				connection.setVerify(true);
//				saveConnection(redisManager, connection);
//			}
			
		} else {
			System.out.println("Service:no connection in redis!");
		}
		return res;
	}

	@Override
	public List<UserSubsystemSsuserDO> getUserSubsystemSsuserList(Map<String, Object> map) {
		return userSubsystemSsuserDAO.list(map);
	}

	@Override
	public void saveConnection(RedisManager redisManager, InterSystemConnectionDO interSystemConnection) {
		String key = null;
		byte[] byteKey = null;
//		UserSubsystemSsuserDO userSubsystemSsuser = interSystemConnection.getUserSubsystemSsuser();
//		key = "connect-" + interSystemConnection.getSubsystem().getSysIdentification() +
//				"-" + userSubsystemSsuser.getSsuserId();
		key = "connect-" + interSystemConnection.getToken();
		byteKey = SerializeUtils.serialize(key);
		if(redisManager.get(byteKey) != null) {
			redisManager.del(byteKey);
		}
		redisManager.set(byteKey, SerializeUtils.serialize(interSystemConnection));
	}
	
	@Override
	public void deleteConnection(RedisManager redisManager, String token) {
		String key = null;
		byte[] byteKey = null;
//		UserSubsystemSsuserDO userSubsystemSsuser = interSystemConnection.getUserSubsystemSsuser();
//		key = "connect-" + interSystemConnection.getSubsystem().getSysIdentification() +
//				"-" + userSubsystemSsuser.getSsuserId();
		key = "connect-" + token;
		byteKey = SerializeUtils.serialize(key);
		if(redisManager.get(byteKey) != null) {
			redisManager.del(byteKey);
		}
		redisManager.del(byteKey);
	}

	@Override
	public List<SubsystemDO> getSubsystemList(Map<String, Object> map) {
		return subsystemDAO.list(map);
	}
	
	@Override
	public InterSystemConnectionDO getConnection(String subsystemUserId, String sysIdentification, String token) {
		String key = null;
		byte[] byteKey = null;
//		key = "connect-" + sysIdentification + "-" + subsystemUserId;
		key = "connect-" + token;
		byteKey = SerializeUtils.serialize(key);
		RedisManager redisManager = new RedisManager();
		redisManager.init();
		return (InterSystemConnectionDO)SerializeUtils.deserialize
				(redisManager.get(byteKey));
	}

	@Override
	public SubsystemDO getByMenuId(Long menuId) {
		return subsystemDAO.getByMenuId(menuId);
	}
}
