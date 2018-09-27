package com.bootdo.testDemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.service.OrgService;
import com.bootdo.system.service.RoleService;
import com.bootdo.system.service.UserService;

@RestController()
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
	@Autowired
	OrgService orgService;
	@Autowired
	UserService userService;

	// test userService.list()
	@Test
	public void listTest() {
		Map<String, Object> map = new HashMap<>();
		map.put("deptId", 1);
		List<UserDO> sysUserList = userService.list(map);
		System.out.println(sysUserList);
	}

}
