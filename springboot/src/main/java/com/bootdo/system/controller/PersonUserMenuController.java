package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.system.dao.UserMenuDao;
import com.bootdo.system.domain.UserMenuDO;
import com.bootdo.system.service.UserMenuService;
import com.bootdo.system.service.UserService;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/personUserMenu")
public class PersonUserMenuController {
	
	@Autowired
	UserMenuService userMenuService;
	
	@Log("操作-查询-获取所有用户-菜单关系")
	@GetMapping("getData")
	List<UserMenuDO> getData(){
		return  userMenuService.list(null);
	}
	
	@Log("操作-修改-菜单与用户的关系")
	@PostMapping("updateUserMenu")
	@ResponseBody
	int updateUserMenu(@RequestBody JSONObject userMenuData) {
		List<Object> userMenuModelList = userMenuData.getJSONArray("MenuModel");
		Long userId = userMenuData.getLong("userId");
		UserMenuDO userMenuDO = new UserMenuDO();
		int count = 0;
		
		userMenuService.removeByUserId(userId);
		
		for(int i=0; i<userMenuModelList.size(); i++) {
			Long menuId = Long.parseLong(new Integer((int)userMenuModelList.get(i)).toString());
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("menuId", menuId);
			if (userMenuService.count(map) == 0) {
				userMenuDO.setUserId(userId);
				userMenuDO.setMenuId(menuId);
				userMenuService.save(userMenuDO);
				count ++;
			}
		}
		return count;
	}
	
	@Log("操作-查询-获取菜单与用户的关系")
	@PostMapping("getUserMenu")
	@ResponseBody
	List<UserMenuDO> getUserMenu(@RequestBody JSONObject data){
		Long userId = data.getLong("userId");
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		return userMenuService.list(map);
	}
	
}
