package com.bootdo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.service.FileService;
import com.bootdo.common.utils.MD5Utils;
import com.bootdo.common.utils.R;
import com.bootdo.common.utils.ShiroUtils;
import com.bootdo.system.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class LoginController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MenuService menuService;
	@Autowired
	FileService fileService;
	
	@GetMapping("/login")
	@ResponseBody
	R login() {
		return R.error(401, "未登录");
	}
	
	/*
	* 请求接口: login
	* 功能: 请求登录
	* 参数格式:
	* data: {
	*   username: ''
	*   password: ''
	* }
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	* }
	*/
	@Log("操作-登陆")
	@PostMapping("/login")
	@ResponseBody
	R ajaxLogin(@RequestBody JSONObject params) {
		String username = params.getString("username");
		String password = params.getString("password");
		password = MD5Utils.encrypt(username, password);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();		
		try {
			subject.login(token);
			return R.ok();
		} catch (AuthenticationException e) {
			return R.error("用户或密码错误");
		}
	}
	/*
	* 请求接口: getUserInfo
	* 功能: 获取当前用户信息
	* 参数格式:
	* data: {
	* }
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	*   user_name: ''
	*   user_id: number
	*   access: ''
	* }
	*/
//	@Log("操作-查询用户信息")
	@PostMapping("/getUserInfo")
	@ResponseBody
	R getUserInfo() {
		R r = R.ok();
		r.put("user_name", getUsername());
		r.put("user_id", getUserId());
		Set<String> access = menuService.listPerms(getUserId());
		r.put("access", access);
		return r;
	}
	
	/*
	* 请求接口: logout
	* 功能: 请求登出
	* 参数格式:
	* data: {
	* }
	* 返回数据格式:
	* data: {
	*   code: number
	*   msg: ''
	* }
	*/
	@Log("操作-退出登陆")
	@RequestMapping("/logout")
	@ResponseBody
	R logout() {
		ShiroUtils.logout();
		return R.ok();
	}
}
