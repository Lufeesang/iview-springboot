package com.bootdo.common.utils;

import java.util.Base64;

import com.bootdo.common.redis.shiro.SerializeUtils;

public class Base64Utils {
	public static String encode(Object object) {
		byte[] objectBytes = SerializeUtils.serialize(object);
		String base64encodedString = Base64.getEncoder().encodeToString(objectBytes);
		return base64encodedString;
	}
	
	public static Object decode(String base64encodedString) {
		byte[] objectBytes = Base64.getDecoder().decode(base64encodedString);
		Object obj = SerializeUtils.deserialize(objectBytes);
		return obj;
	}
}
