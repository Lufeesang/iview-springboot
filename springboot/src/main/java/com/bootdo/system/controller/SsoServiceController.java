package com.bootdo.system.controller;

//import static org.assertj.core.api.Assertions.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.codehaus.groovy.syntax.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.domain.InterSystemConnectionDO;
import com.bootdo.common.redis.shiro.RedisManager;
import com.bootdo.common.utils.R;
import com.bootdo.common.utils.StringUtils;
import com.bootdo.system.dao.MenuDao;
import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.SubsystemMenuDO;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;
import com.bootdo.system.service.MenuService;
import com.bootdo.system.service.SSOService;
import com.bootdo.system.service.impl.SSOServiceImpl;

@Controller
@RequestMapping(value = "/SSOservice")
public class SsoServiceController extends BaseController {
	@Autowired
	SSOService ssoService;
	@Autowired
	MenuService menuService;

	/*
	* 请求接口: deliverToken
	* 功能: 请求生成token
	* 参数格式:
	* data: {
	*   menuId: number
	* }
	* 返回数据格式:
	* data: {
    *   code: number
	*   msg: ''
	*   token: ''
	*   url: ''
	* }
	*/
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping("/deliverToken")  //登录成功后将生成token
	public R deliverToken(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject pObject) throws Exception {
//		System.out.println("Mapping:deliverToken");
		Long menuId = null;
		Long userId = null;
		String token = null;
		String redirectUrl = null;
		SubsystemDO subsystem = null;
		UserSubsystemSsuserDO userSubsystemSsuser =null;
		InterSystemConnectionDO interSystemConnection = null;
		R r = null;
		
		userId = getUserId();
		menuId = pObject.getLong("menuId");
		
		HashMap<String, Object> params = new HashMap<>();
		subsystem = ssoService.getByMenuId(menuId);
		if(subsystem != null) {
			params = new HashMap<>();
			params.put("subsystemId", subsystem.getId());
			params.put("userId", userId);
			List<UserSubsystemSsuserDO> l2 = ssoService.getUserSubsystemSsuserList(params);
			if(!l2.isEmpty()) {
				userSubsystemSsuser = l2.get(0);
//				interSystemConnection = ssoService.getConnection(userSubsystemSsuser.getSsuserId(), sysIdentification, "");
				if(interSystemConnection == null || !interSystemConnection.isVerify()) {
					token = ssoService.createToken(userSubsystemSsuser);
					//在缓存中存入
					RedisManager redisManager = new RedisManager();
					redisManager.init();
					interSystemConnection = new 
							InterSystemConnectionDO(userSubsystemSsuser, subsystem, token, false);
					ssoService.saveConnection(redisManager, interSystemConnection);
					r = R.ok();
					r.put("token", token);
					r.put("url", subsystem.getIndexUrl());
				}
			} else {
				r = R.error();
			}
		}else {
//			redirectUrl = "error";
			r = R.error();
		}
//		response.sendRedirect(redirectUrl);
		return r;
	}
	/*
	* 请求接口: verifyToken
	* 功能: 验证token,返回用户信息
	* 参数格式:
	* data: {
	*   token: ''
	* }
	* 返回数据格式:
	* data: {
    *   code: number
	*   msg: ''
	*   userId: ''
	* }
	*/
	@RequestMapping("/verifyToken")
	@ResponseBody
	public R verifyToken(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject userEntity) throws Exception {
//		System.out.println("Service:Ready to verify");
		String token = userEntity.getString("token");
		String subsystemUserId = userEntity.getString("subsystemUserId");  //无效项
		String sysIdentification = userEntity.getString("sysIdentification");  //无效项
		R r = null;
		InterSystemConnectionDO connection = ssoService.getConnection(subsystemUserId, sysIdentification, token);
		if (connection == null) {
			r = R.error();
		} else {
			r = R.ok();
			r.put("userId", connection.getUserSubsystemSsuser().getSsuserId());
			RedisManager redisManager = new RedisManager();
			redisManager.init();
			ssoService.deleteConnection(redisManager, token);
		}
		return r;
	}
	/*
	* 请求接口: list
	* 功能: 请求在数据库的单点登录菜单
	* 参数格式:
	* data: {
	* }
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	*   list: [
	*     {
	*       menu_id: number
	*       name: ''
	*       url: ''
	*     }...
	*   ]
	* }
	*/
	@Log("操作-获取子系统列表")
	@RequestMapping("/list")
	@ResponseBody
	public R list(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Long userId = getUserId();
		List<MenuDomain> menus = menuService.listMenusByUserId(userId);
		List<MenuDomain> res = new ArrayList<>();
		for(MenuDomain menu : menus) {
			if (menu.getType().equals(new Integer(3))) {
				res.add(menu);
			}
		}
		R r = R.ok();
		r.put("list", res);
		return r;
	}
	
//	@GetMapping("/SSOServiceLogin")
//	public String ssoServiceLogin(HttpServletRequest request, Model model) throws Exception {
//		UserDO currentUser = getUser();
//		if(currentUser != null) {
//			System.out.println("id:" + getUserId() + ",name:" + getUsername());
//		}
//		// String attributeSysName = (String) request.getAttribute("sysName");
//		String sysIdentification = (String) request.getParameter("sysIdentification");
//
//		// System.out.println("Service-attributeSysName:" + attributeSysName);
//		// System.out.println("Service-PathSysName:" + sysName);
//		// System.out.println("Service-paramSysName:" + sysIdentification);
//		
//		model.addAttribute("sysIdentification", sysIdentification);
//		System.out.println("sysIdentification:" + sysIdentification);
//		return "login";
//	}
	
}
