package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.R;
import com.bootdo.system.domain.UserRoleDO;
import com.bootdo.system.service.PersonUserRoleService;

@RestController
@RequestMapping("/personUserRole")
@CrossOrigin("http://localhost:8080")
public class PersonUserRoleController {
	@Autowired
	PersonUserRoleService personUserRoleService;
	
//	@GetMapping("/getUserRoleList")
//	List<Integer> getUserRoleList(Long roleId){
//    	return personUserRoleService.getUserId(roleId);
//    }
	
	@Log("操作-查询-获取菜单与角色的关系列表")
    @PostMapping("/getUserRoleList")
	@ResponseBody()
	List<Integer> getUserRoleList(@RequestBody JSONObject userRole){
    	Long roleId = userRole.getLong("roleId");
    	return personUserRoleService.getUserId(roleId);
	}
	
	@Log("操作-修改-菜单与角色的关系")
    @PostMapping("/updateRoleUser")
    @ResponseBody()
    R updateRoleUser(@RequestBody JSONObject data) {
    	// 判断成功与否的标志 （1:成功，0:失败）
    	int status = 1;
//    	JSONObject updateData = new JSONObject();
    	Long roleId = data.getLong("roleId");
    	List<Object> jsonArray = data.getJSONArray("userIds");
    	System.out.println("roleId:" + roleId);
    	System.out.println("List:" + jsonArray);
    	Long userIds[] = new Long[jsonArray.size()];
    	for(int i=0; i<jsonArray.size(); i++) {
			userIds[i] = Long.parseLong(new Integer((int)jsonArray.get(i)).toString());
		}
    	personUserRoleService.removeByRoleId(roleId);
    	for(int i=0; i<userIds.length; i++) {
    		UserRoleDO userRoleDO = new UserRoleDO();
    		userRoleDO.setRoleId(roleId);
    		userRoleDO.setUserId(userIds[i]);
    		int temp = personUserRoleService.save(userRoleDO);
    		if(temp == 0) {
    			status = 0;
    		}
    	}
    	if(status==0) {
    		return R.error();
    	}
//    	updateData.put("msg", userIds);
    	return R.ok();
    }
    
	@Log("操作-添加-角色与user关系")
    @PostMapping("addRoleUser")
    @ResponseBody()
    int addRoleUser(@RequestBody JSONObject data) {
    	Long roleId = data.getLong("roleId");
    	Long userId = data.getLong("userId");
    	UserRoleDO userRoleDO = new UserRoleDO();
    	userRoleDO.setRoleId(roleId);
    	userRoleDO.setUserId(userId);
    	if(personUserRoleService.save(userRoleDO) > 0) {
    		return 1;
    	}
 		return 0;
    }
    
	@Log("操作-修改-角色与user关系")
    @PostMapping("updateUserRole")
	@ResponseBody
	int updateUserRole(@RequestBody JSONObject userRoleData) {
		List<Object> userRoleModelList = userRoleData.getJSONArray("RoleModel");
		Long userId = userRoleData.getLong("userId");
		UserRoleDO userRoleDO = new UserRoleDO();
		int count = 0;
		
		personUserRoleService.removeByUserId(userId);
		
		for(int i=0; i<userRoleModelList.size(); i++) {
			Long roleId = Long.parseLong(new Integer((int)userRoleModelList.get(i)).toString());
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("roleId", roleId);
			if (personUserRoleService.count(map) == 0) {
				userRoleDO.setUserId(userId);
				userRoleDO.setRoleId(roleId);
				personUserRoleService.save(userRoleDO);
				count ++;
			}
		}
		return count;
	}
    
	@Log("操作-查询-获得用户角色")
    @PostMapping("getRoleByUserId")
    @ResponseBody
    List<UserRoleDO> getRoleByUserId(@RequestBody JSONObject userIdData) {
    	Long userId = userIdData.getLong("userId");
    	Map<String, Object> map = new HashMap<>();
    	map.put("userId", userId);
    	return personUserRoleService.list(map);
    }
}
