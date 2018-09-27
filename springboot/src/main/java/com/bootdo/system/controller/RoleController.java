package com.bootdo.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.config.Constant;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.R;
import com.bootdo.system.dao.UserMenuDao;
import com.bootdo.system.dao.UserRoleDao;
import com.bootdo.system.domain.RoleDO;
import com.bootdo.system.service.MenuService;
import com.bootdo.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {
	String prefix = "system/role";
	@Autowired
	RoleService roleService;
	@Autowired
	MenuService menuService;
	
	/*
	* 请求接口: listRole
	* 功能: 请求在数据库的角色列表
	* 参数格式: -- 均为可选项
	* data: {
	  roleId: number
	  parentId: number
	  roleName: ''
	  roleSign: ''
	  remark: ''
	  userIdCreate: number
	  gmtCreate: '' --  日期格式为: yyyy-MM-dd HH:mm:ss
	  gmtModified: ''
	* }
	* 返回数据格式:
	* data: [
	*   RoleDO
	* ]
	*/
	@Log("操作-查询-获取角色列表")
	@RequiresPermissions("sys:role:list")
	@PostMapping("/list")
	@ResponseBody()
	List<RoleDO> listRole(HttpServletRequest request, @RequestBody JSONObject params) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("roleId", params.getString("roleId"));
		map.put("parentId", params.getString("parentId"));
		map.put("roleName", params.getString("roleName"));
		map.put("roleSign", params.getString("roleSign"));
		map.put("remark", params.getString("remark"));
		map.put("userIdCreate", params.getString("userIdCreate"));
		map.put("gmtCreate", params.getString("gmtCreate"));
		map.put("gmtModified", params.getString("gmtModified"));
		List<RoleDO> roles = roleService.list(map);
		return roles;
	}
	/*
	* 请求接口: addRole
	* 功能: 请求在数据库添加一个角色
	* 参数格式:
	* params: {
	*   parentId: number
	*   remark: ''
	*   roleName: ''
	*   roleSign: ''
	* }
	* 返回数据格式:
	* data: RoleDO
	*/
	@Log("操作-添加-角色")
	@RequiresPermissions("sys:role:add")
	@PostMapping("/add")
	@ResponseBody()
	RoleDO addRole(@RequestBody JSONObject params) {
		RoleDO newRole = new RoleDO();
		RoleDO res = null;
		newRole.setRoleName(params.getString("roleName"));
		newRole.setParentId(new Long(params.getString("parentId")));
		newRole.setRoleSign(params.getString("roleSign"));
		newRole.setRemark(params.getString("remark"));
		newRole.setGmtCreate(new Timestamp(new Date().getTime()));
		newRole.setUserIdCreate(getUserId());
		newRole.setMenuIds(new ArrayList<>());
		roleService.save(newRole);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userIdCreate", newRole.getUserIdCreate());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("gmtCreate", format.format(newRole.getGmtCreate()));
		List<RoleDO> list = roleService.list(map);
		res = list.size() == 0 ? null : list.get(0);
		return res;
	}
	/*
	* 请求接口: updateRole
	* 功能: 请求在数据库更改一个角色
	* 参数格式:
	* params: {
	*   parentId: number -- 必须项
	*   remark: ''
	*   roleName: ''
	*   roleSign: ''
	* }
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	* }
	*/
	@Log("操作-修改角色")
	@RequiresPermissions("sys:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	R updateRole(@RequestBody JSONObject params) {
		RoleDO updateRole = new RoleDO();
		if (!params.keySet().contains("roleId")) {
			return R.error();
		} else {
			updateRole.setRoleId(params.getLong("roleId"));
			updateRole.setRoleName(params.getString("roleName"));
			updateRole.setRoleSign(params.getString("roleSign"));
			updateRole.setRemark(params.getString("remark"));
			updateRole.setGmtModified(new Timestamp(new Date().getTime()));
			roleService.update(updateRole);
			return R.ok();
		}
	}
	/*
	* 请求接口: deleteRoles
	* 功能: 请求在数据库删除复数角色
	* 参数格式:
    * data: {
    *   ids: [
    *     number...
    *   ]
    * }
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	* }
	*/
	@RequiresPermissions("sys:role:batchRemove")
	@Log("操作-批量删除角色")
	@PostMapping("/batchRemove")
	@ResponseBody
	R deleteRoles(@RequestBody JSONObject params) {
		JSONArray jsonArray = params.getJSONArray("ids");
		try {
			Long[] ids = new Long[jsonArray.toArray().length];
			int i = 0;
			for (Object jsonObject : jsonArray) {
		        Long roleId = JSONObject.parseObject(jsonObject.toString(), Long.class);
		        ids[i] = roleId;
		        i++;
		    }
			roleService.batchremove(ids);
			return R.ok();
		} catch (Exception e) {
			return R.error();
		}
	}
	/*
	* 请求接口: batchAddRole
	* 功能: 请求在数据库批量导入角色
	* 参数格式:
	* data: {
	*   roles: [
	*     {
	*       roleId: number
	*       roleName: ''
	*       parentId: number
	*       roleSign: ''
	*       remark: ''
	*     }
	*   ]
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	* }
	*/
	@Log("操作-批量添加角色")
//	@RequiresPermissions("sys:role:batchAdd")
	@PostMapping("/batchAdd")
	@ResponseBody()
	R batchAddRole(@RequestBody JSONObject params) {
		JSONArray jsonArray = params.getJSONArray("roles");
		List<RoleDO> roles = new ArrayList<RoleDO>();
		for (Object jsonObject : jsonArray) {
			JSONObject roleInfo = JSONObject.parseObject(jsonObject.toString(), JSONObject.class);
//			System.out.println(roleInfo.toJSONString());
			RoleDO newRole = new RoleDO();
			newRole.setRoleId(new Long(roleInfo.getString("roleId")));
			newRole.setRoleName(roleInfo.getString("roleName"));
			newRole.setParentId(new Long(roleInfo.getString("parentId")));
			newRole.setRoleSign(roleInfo.getString("roleSign"));
			newRole.setRemark(roleInfo.getString("remark"));
			newRole.setGmtCreate(new Timestamp(new Date().getTime()));
			newRole.setUserIdCreate(getUserId());
			roles.add(newRole);
		}
		roleService.batchSave(roles);
		return R.ok();
	}
	
	@RequiresPermissions("sys:unk")
	@PostMapping("/test")
	@ResponseBody
	Set<String> test(@RequestBody JSONObject params){
		return menuService.listPerms(params.getLong("userId"));
	}
}
