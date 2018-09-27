package com.bootdo.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.compiler.ast.NewExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.android.Android10Instantiator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.R;
import com.bootdo.common.annotation.Log;
import com.bootdo.system.domain.RoleMenuDO;
import com.bootdo.system.domain.TransferUser;
import com.bootdo.system.domain.UserSearchLimit;
import com.bootdo.system.service.impl.GetUserServiceImpl;
import com.bootdo.system.service.impl.UserMenuServiceImpl;

/**
 * 用于菜单分配人员
 * @author zcchen
 *
 */
@Controller
public class UserController {
	
	@Autowired
	GetUserServiceImpl getUserServiceImpl;
	
	@Autowired
	UserMenuServiceImpl userMenuServiceImpl;
	
	/**
	 * 根据条件对象userSearchLImi 进行多条件查询
	 * @param userSearchLimit
	 * @return
	 */
	@Log("操作-查询-条件查询获取用户列表")
	@PostMapping("/getUserByLimit")
	@ResponseBody
	public Map<String, Object> getUserByLimit(@RequestBody UserSearchLimit userSearchLimit){
		Map map = new HashMap<>();
		System.out.println("UserSearchLimit"+userSearchLimit);
		List<TransferUser> transferUsers = getUserServiceImpl.listUser(userSearchLimit);
		int count = 1;
		long menuId = userSearchLimit.getMenu_id();
		
		System.out.println("TransferUsers"+transferUsers.toString());
		transferUsers = userMenuServiceImpl.hasUserMenuPerms(menuId, transferUsers);
		map.put("result", transferUsers);
		System.out.println(transferUsers);
		if(transferUsers != null) {
			map.put("message", "查询成功");
			
		}else {
			map.put("message", "无符合该条件的数据");
			
		}
		return map;
	}
	
	@Log("操作-修改-修改用户与菜单关系")
	@PostMapping("/UserMenu/updateUserMenu")
	@ResponseBody
	R updateRoleMenu(@RequestBody JSONObject params) {
		Long menuId;
		Long userId;
		int result = 0;
		System.out.println(params);
		List<Long> allUserIds = new ArrayList<>();
		List<Long> withPermsUserIds = new ArrayList<>();
		List<Long> withoutPermsUserIds = new ArrayList<>();
		menuId = Long.parseLong(params.getString("menuId"));
		JSONArray jsonArray = params.getJSONArray("allUserIds");
		JSONArray jsonArray2 = params.getJSONArray("whithPermsUserIds");
		try {
			for(Object object : jsonArray ) {
				userId = JSONObject.parseObject(object.toString(), Long.class);
				allUserIds.add(userId);
				withoutPermsUserIds.add(userId);
			}
			for(Object object : jsonArray2) {
				userId = JSONObject.parseObject(object.toString(), Long.class);
				withPermsUserIds.add(userId);
			}
			withoutPermsUserIds.removeAll(withPermsUserIds);
			System.out.println("without"+ withoutPermsUserIds);
			System.out.println("allUserIds"+allUserIds);
			System.out.println("with"+ withPermsUserIds);
			if(allUserIds != null  && (allUserIds.size() >= withPermsUserIds.size())) {
				result = userMenuServiceImpl.updateUserMenuPerms(allUserIds, withoutPermsUserIds, menuId);
				System.out.println("result"+result);
			}else {
				System.out.println("数据有误");
			}
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		
		if(result < 0)  return R.error("更新失败");
		return R.ok("更新成功");
		
	
	}
	
}
