package com.bootdo.testDemo;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.utils.Base64Utils;
import com.bootdo.common.utils.MD5Utils;
import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;
import com.bootdo.system.service.impl.SSOServiceImpl;

public class Base64Test {
//	@Test
	public void test01() {
		SubsystemDO subsystem = new SubsystemDO(new Long(10000), "sopppp", "wwww111", "asasa", null);
		System.out.println(subsystem.toString());
		String base64 = Base64Utils.encode(subsystem);
		System.out.println("encode:" + base64);
		System.out.println("decode:" + ((SubsystemDO)Base64Utils.decode(base64)).toString());
		System.out.println(MD5Utils.encrypt(new SSOServiceImpl().createToken(new UserSubsystemSsuserDO())));
	}
	
	@Test
	public void test02() {
		JSONObject params = new JSONObject();
//		JSONObject params = null;
//		JSONArray jsonArray = params.getJSONArray("ids");
//		Long[] ids = new Long[jsonArray.toArray().length];
		params.put("roleId", "123");
		System.out.println(params.keySet().contains("roleId"));
	}
}
