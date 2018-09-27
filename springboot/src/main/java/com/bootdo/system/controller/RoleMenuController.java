//package com.bootdo.system.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.poi.poifs.storage.ListManagedBlock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.bootdo.common.R;
//import com.bootdo.system.domain.RoleMenuDO;
//import com.bootdo.system.service.impl.RoleMenuServiceImpl;
//
//@Controller
//public class RoleMenuController {
//	
//	@Autowired
//	RoleMenuServiceImpl roleMenuServiceImpl;
//	List<Long> RoleMenuList = new ArrayList<>();
//	long roleId;
//	
//	@PostMapping("/roleMenu/listMenuIdByRoleId")
//	@ResponseBody
//	List<Long> listMenuIdByRoleId(@RequestBody JSONObject params){
//		roleId = Long.parseLong(params.getString("roleId"));
//		RoleMenuList = roleMenuServiceImpl.listMenuIdByRoleId(roleId);
//		System.out.println("RoleMenuList"+RoleMenuList);
//		return RoleMenuList;
//	}
//	
//	@PostMapping("/roleMenu/updateRoleMenu")
//	@ResponseBody
//	R updateRoleMenu(@RequestBody JSONObject params) {
//		Long menuId;
//		List<Long> newRoleMenuList = new ArrayList<>();
//		roleId = Long.parseLong(params.getString("roleId"));
//		RoleMenuList = roleMenuServiceImpl.listMenuIdByRoleId(roleId);
//		JSONArray jsonArray = params.getJSONArray("menuIds");
//		try {
//			for(Object jsonObject : jsonArray) {
//				menuId = JSONObject.parseObject(jsonObject.toString(), Long.class);
//				newRoleMenuList.add(menuId);
//			}
//			System.out.println("接收到的menuIds"+newRoleMenuList);
//		}catch (Exception e) {
//			System.out.println("updateMenuRoleController出错");
//			return R.error("接收不到 menuIds");
//		}
//		if(newRoleMenuList.equals(RoleMenuList) || newRoleMenuList.isEmpty()) return R.ok();
//		List<RoleMenuDO> roleMenuDOs = new ArrayList<>();
//		for(long Id : newRoleMenuList) {
//			RoleMenuDO roleMenuDO = new RoleMenuDO();
//			roleMenuDO.setMenuId(Id);
//			roleMenuDO.setRoleId(roleId);
//			roleMenuDOs.add(roleMenuDO);
//		}
//		System.out.println("List<RoleMenuDO>"+roleMenuDOs);
//		roleMenuServiceImpl.updateRoleMenu(roleId, roleMenuDOs, RoleMenuList);
//		return R.ok();
//	}
//}
package com.bootdo.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.poifs.storage.ListManagedBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.R;
import com.bootdo.common.annotation.Log;
import com.bootdo.system.domain.RoleMenuDO;
import com.bootdo.system.service.impl.RoleMenuServiceImpl;

@Controller
public class RoleMenuController {
	
	@Autowired
	RoleMenuServiceImpl roleMenuServiceImpl;
	List<Long> RoleMenuList = new ArrayList<>();
	long roleId;
	@Log("操作-查询-获取角色觉有的菜单")
	@PostMapping("/roleMenu/listMenuIdByRoleId")
	@ResponseBody
	List<Long> listMenuIdByRoleId(@RequestBody JSONObject params){
		roleId = Long.parseLong(params.getString("roleId"));
		RoleMenuList = roleMenuServiceImpl.listMenuIdByRoleId(roleId);
		System.out.println("RoleMenuList"+RoleMenuList);
		return RoleMenuList;
	}
	
	@Log("操作-修改-修改角色具有的菜单")
	@PostMapping("/roleMenu/updateRoleMenu")
	@ResponseBody
	R updateRoleMenu(@RequestBody JSONObject params) {
		Long menuId;
		List<Long> newRoleMenuList = new ArrayList<>();
		roleId = Long.parseLong(params.getString("roleId"));
		RoleMenuList = roleMenuServiceImpl.listMenuIdByRoleId(roleId);
		JSONArray jsonArray = params.getJSONArray("menuIds");
		try {
			for(Object jsonObject : jsonArray) {
				menuId = JSONObject.parseObject(jsonObject.toString(), Long.class);
				newRoleMenuList.add(menuId);
			}
			System.out.println("接收到的menuIds"+newRoleMenuList);
		}catch (Exception e) {
			System.out.println("updateMenuRoleController出错");
			return R.error("接收不到 menuIds");
		}
		if(newRoleMenuList.equals(RoleMenuList) || newRoleMenuList.isEmpty()) return R.ok();
		List<RoleMenuDO> roleMenuDOs = new ArrayList<>();
		for(long Id : newRoleMenuList) {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setMenuId(Id);
			roleMenuDO.setRoleId(roleId);
			roleMenuDOs.add(roleMenuDO);
		}
		System.out.println("List<RoleMenuDO>"+roleMenuDOs);
		roleMenuServiceImpl.updateRoleMenu(roleId, roleMenuDOs, RoleMenuList);
		return R.ok();
	}
}
