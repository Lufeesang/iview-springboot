package com.bootdo.testDemo;

import java.util.Set;

import org.junit.Test;

import com.bootdo.common.redis.shiro.RedisManager;
import com.bootdo.common.redis.shiro.SerializeUtils;

public class RedisTest {
	@Test
	public void test01() {
		RedisManager redisManager = new RedisManager();
		redisManager.init();
		Set<byte[]> keys = redisManager.keys("*");
		for (byte[] bs : keys) {  
		      System.out.println(SerializeUtils.deserialize(bs));  
		}  
	}
}
