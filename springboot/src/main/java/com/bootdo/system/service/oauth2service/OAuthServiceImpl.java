package com.bootdo.system.service.oauth2service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cglib.proxy.CallbackHelper;
import org.springframework.stereotype.Service;

import com.bootdo.system.service.ClientService;


@Service
public class OAuthServiceImpl implements OAuthService {
	
	//用于记录历史，已经登陆过的code,
	private Cache historyCache;
	private Cache cache;

    @Autowired
    private ClientService clientService;

    @Autowired
    public OAuthServiceImpl(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("code-cache");
        this.historyCache = cacheManager.getCache("history-code-cache");
    }

    @Override
    public void addAuthCode(String authCode, String username) {
    	System.out.println("缓存了数据");
        cache.put(authCode, username);
        System.out.println("code-cache总体"+cache.get(username));
        System.out.println("code-cache总体"+cache.get(authCode).get());
    }

    @Override
    public void addAccessToken(String accessToken, String username) {
    	
        cache.put(accessToken, username);
      
    }

    @Override
    public String getUsernameByAuthCode(String authCode) {
    	System.out.println("根据Code获取Username"+cache.get(authCode).get());
        return (String)cache.get(authCode).get();
    }

    @Override
    public String getUsernameByAccessToken(String accessToken) {
    	System.out.println("用户名字是"+cache.get(accessToken).get());
        return (String)cache.get(accessToken).get();
    }

    @Override
    public boolean checkAuthCode(String authCode) {
    	
        return cache.get(authCode) != null;
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
    	System.out.println(accessToken + cache.get(accessToken) != null);
        return cache.get(accessToken) != null;
    }

    @Override
    public boolean checkClientId(String clientId) {
        return clientService.findByClientId(clientId) != null;
    }

    @Override
    public boolean checkClientSecret(String clientId,String clientSecret) {
        return clientService.findByClientSecret(clientId,clientSecret);
    }

    @Override
    public long getExpireIn() {
        return 3600L;
    }
}
