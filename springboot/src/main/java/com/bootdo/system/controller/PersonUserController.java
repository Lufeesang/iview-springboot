package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.chrono.IsoEra;

import org.activiti.engine.identity.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.utils.MD5Utils;
import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.RoleDO;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.service.MenuService;
import com.bootdo.system.service.RoleService;
import com.bootdo.system.service.UserService;
import com.bootdo.system.util.PoiUtils;


@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/personuser")
public class PersonUserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	RoleService roleService;
	
	int searchUserNum = 0;
	int userListByDeptIdNum = 0;
	
	// 根据deptId获取用户列表的
	@PostMapping("/getUserList")
	List<UserDO> list(@RequestBody JSONObject params){
		Integer deptId = new Integer(params.getIntValue("id"));
		System.out.println(deptId);
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("deptId", deptId);
		int limit = 10;
		map.put("offset", params.getIntValue("offset"));
		map.put("limit", limit);
		map.put("deptId", deptId);
//		System.out.println(map);
		List<UserDO> sysUserList = userService.mylist(map);
//		System.out.println(sysUserList.size());
//		System.out.print(sysUserList);
		userListByDeptIdNum = userService.count(map1);
    	return sysUserList;
    }
	
	@PostMapping("/getUserListByDeptIdNum")
	int getUserListByDeptIdNum() {
		return userListByDeptIdNum;
	}
	
	// 分页获取用户列表
	@PostMapping("/getUserListByPage")
	List<UserDO> getUserListByPage(@RequestBody JSONObject params){
		Integer offset = new Integer(params.getIntValue("offset"));
		int limit = 10;
		Map<String, Object> map = new HashMap<>();
		map.put("offset", offset);
		map.put("limit", limit);
		return userService.list(map);
	}
	
	@PostMapping("getAllUserList")
	List<UserDO> getAllUserList(){
		return userService.list(new HashMap<>());
	}
	
	// 查询用户
	@PostMapping("/searchUser")
	@ResponseBody
	List<UserDO> searchUser(@RequestBody JSONObject user){
		Map<String, Object> map = new HashMap<>();
//		map.put("userId", user.getInteger("userId"));
//		map.put("username", user.getString("username"));
//		map.put("name", user.getString("name"));
		map.put("userId", user.getInteger("userId"));
		map.put("start", user.getInteger("start"));
		map.put("end", user.getInteger("end"));
		map.put("username", user.getString("username"));
		map.put("name", user.getString("name"));
		map.put("deptId", user.getInteger("deptId"));
		searchUserNum = userService.count(map);
		map.put("offset", user.getInteger("offset"));
		map.put("limit", 10);
		System.out.println(map);
		List<UserDO> userlist = userService.mylist(map);
		return userlist;
	}
	
	// 查询用户-可以查询组织节点及其所有子节点的人员版本
		@PostMapping("/searchUsers")
		@ResponseBody
		List<UserDO> searchUsers(@RequestBody JSONObject user){
			Map<String, Object> map = new HashMap<>();
			map.put("userId", user.getInteger("userId"));
			map.put("start", user.getInteger("start"));
			map.put("end", user.getInteger("end"));
			map.put("username", user.getString("username"));
			map.put("name", user.getString("name"));
			map.put("deptId", user.getInteger("deptId"));
			System.out.println(map);
			List<UserDO> userlist = userService.list(map);
			return userlist;
		}
	
	// 查询用户数量
	@PostMapping("/searchUserNum")
	@ResponseBody
	int searchUserNum() {
		return searchUserNum;
	}
	
//	@RequiresPermissions("unk")
	@GetMapping("/getCount")
	int count(Map<String, Object> map) {
		return userService.count(null);
	}
	
	@GetMapping("/newCount")
	@ResponseBody
	int newCount(Integer deptId) {
		Map<String, Object> map = new HashMap<>();
		map.put("deptId", deptId);
		return userService.count(map);
	}
	
//	@PostMapping("/testResponse")
//	@CrossOrigin("http://localhost:8080")
//	@ResponseBody()
//	List<UserDO> mylist(@RequestBody JSONObject test){
//		Map<String, Object> map = new HashMap<>();
//		map.put("deptId", test.getInteger("deptId"));
//		return userService.list(map);
//	}
	
	@PostMapping("/saveUser")
	@CrossOrigin("http://localhost:8080")
	@ResponseBody()
	String save(@RequestBody JSONObject userData) {
		UserDO user = new UserDO();
		user.setUsername(userData.getString("username"));
		user.setName(userData.getString("name"));
		user.setPassword(userData.getString("password"));
		user.setDeptId(userData.getLong("deptId"));
		user.setEmail(userData.getString("email"));
		user.setMobile(userData.getString("mobile"));
		user.setProvince(userData.getString("privince"));
		user.setCity(userData.getString("city"));
		user.setDistrict(userData.getString("district"));
		if(userData.getString("sex").equals("男")) {
			user.setSex(new Long(1));
		}else if(userData.getString("sex").equals("女")) {
			user.setSex(new Long(0));
		}
		user.setGmtCreate(new Date());
		if (userService.userSave(user) > 0) {
			user.setPassword(MD5Utils.encrypt(user.getUserId().toString(), user.getPassword()));
			System.out.println("创建测试密码：" + user.getPassword());
			userService.userUpdate(user);
			return "OK";
		}
		return "Fail";
	}
	
	// 删除用户
	@PostMapping("/removeUser")
	@ResponseBody
	String removeUser(@RequestBody JSONObject user) {
		Long userId = user.getLong("userId");
		if (userService.remove(userId) > 0) {
			return "OK";
		}
		return "Fail";
	}
	
	// 批量删除用户
	@PostMapping("batchRemoveUser")
	@ResponseBody
	int batchRemoveUser(@RequestBody JSONObject user) {
		List<Object> userIdList = user.getJSONArray("userIdList");
		int res=0;
		System.out.println("List:" + userIdList);
		for(int i=0; i<userIdList.size(); i++) {
			System.out.println(userIdList.get(i));
			Long userId = Long.parseLong(new Integer((int)userIdList.get(i)).toString());
			System.out.println(userId);
			userService.remove(userId);
			res = i+1;
		}
		return res;
	} 

	// 更新用户信息的操作
	@PostMapping("updateUser")
	@ResponseBody
	String updateUser(@RequestBody JSONObject userData) {
		UserDO user = new UserDO();
		user.setUserId(userData.getLong("userId"));
		user.setUsername(userData.getString("username"));
		user.setName(userData.getString("name"));
		user.setDeptId(userData.getLong("deptId"));
		user.setEmail(userData.getString("email"));
		user.setGmtModified(new Date());
		if(userData.getString("sex").equals("男")) {
			user.setSex(new Long(1));
		}else if(userData.getString("sex").equals("女")) {
			user.setSex(new Long(0));
		}
		if(userService.userUpdate(user) > 0) {
			return "OK";
		}
		return "Fail";
	}
	
	// 批量更新用户信息的操作
		@RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
		@ResponseBody
		public void updateUsers(@RequestBody List<UserDO> users) {
			System.out.println(users);
			userService.userUpdates(users);
		}
	
	// 更新用户密码的操作
	@PostMapping("updateUserPassword")
	@ResponseBody
	String updateUserPassword(@RequestBody JSONObject userData) {
		UserDO user = new UserDO();
		user.setUserId(userData.getLong("userId"));
		user.setUsername(userData.getString("username"));
		user.setPassword(userData.getString("oldPassword"));
		user.setPassword(MD5Utils.encrypt(user.getUserId().toString(), user.getPassword()));
		user.setGmtModified(new Date());
		System.out.println("user1:"+user);
		// 查询数据库中的用户
		UserDO dataUser = new UserDO();
		dataUser = userService.get(userData.getLong("userId"));
		System.out.println("user2:"+dataUser.getPassword());
		if(dataUser.getPassword().equals(user.getPassword())) {
			user.setPassword(userData.getString("newPassword"));
			user.setPassword(MD5Utils.encrypt(user.getUserId().toString(), user.getPassword()));
			System.out.println("密码一致");
			if(userService.userUpdate(user) > 0) {
				return "OK";
			}
		}
		return "Fail";
	}
	
	// 导出用户数据
//	@RequestMapping(value = "/exportUser", method = RequestMethod.GET)
	@PostMapping("exportUser")
    public ResponseEntity<byte[]> exportEmp() {
        return PoiUtils.exportEmp2Excel(userService.getUserlist());
    }
    
    // 导入用户数据
    @PostMapping("importUser")
    @ResponseBody
    public int importUser(@RequestBody JSONObject userData) {
    	JSONArray jsonArray = userData.getJSONArray("userlist");
    	int count = 0;
    	for (Object jsonObject : jsonArray) {
    		JSONObject userInfo = JSONObject.parseObject(jsonObject.toString(), JSONObject.class);
    		UserDO userDO = new UserDO();
    		userDO.setName(userInfo.getString("name"));
    		userDO.setPassword("100134");
    		userDO.setUsername(userInfo.getString("username"));
    		userDO.setDeptId(userInfo.getLong("deptId"));
    		userDO.setEmail(userInfo.getString("email"));
    		userDO.setMobile(userInfo.getString("mobile"));
    		userDO.setGmtCreate(new Date());
//    		userService.userSave(userDO);
    		if (userService.userSave(userDO) > 0) {
    			userDO.setPassword(MD5Utils.encrypt(userDO.getUserId().toString(), userDO.getPassword()));
    			System.out.println("创建测试密码：" + userDO.getPassword());
    			userService.userUpdate(userDO);
    		}
    		count++;
    	}
        return count;
    }
    
    // 获取菜单列表
    @PostMapping("getMenuList")
    @ResponseBody
    List<MenuDomain> getMenuList(){
    	return menuService.list(new HashMap<>());
    }
    
    // 获取角色列表
    @PostMapping("getRoleList")
    @ResponseBody
    List<RoleDO> getRoleList(){
    	return roleService.list(new HashMap<>());
    }
    
    // 更新用户的状态
    @PostMapping("updateUserStatus")
    @ResponseBody
    String updateUserStatus(@RequestBody JSONObject userData) {
    	UserDO user = new UserDO();
		user.setUserId(userData.getLong("userId"));
		user.setStatus(userData.getInteger("status"));
		if (userService.userUpdate(user) > 0) {
			return "成功";
		}
    	return "失败";
    }
}
