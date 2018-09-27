package com.bootdo.system.controller;




import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.security.SecurityUtil;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.as.response.OAuthASResponse.OAuthAuthorizationResponseBuilder;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.service.oauth2service.OAuthService;

import ch.qos.logback.core.net.LoginAuthenticator;

@Controller
public class AuthorizeController {
	
	@Autowired
	OAuthService OAuthService ;
	
	@Log("操作-单点登陆-获取Code")
	 	@RequestMapping("/authorize")
	 	@ResponseBody
	    public Object authorize(
	    		@RequestBody JSONObject params)
	            throws URISyntaxException, OAuthSystemException, OAuthProblemException {
		 	String client_id = params.getString("client_id");
		 	String response_type = params.getString("response_type");
		 	String redirect_url = params.getString("redirect_url");
		 	System.out.println(client_id + response_type + redirect_url);
	        //检查传入的客户端id是否正确
			if (!OAuthService.checkClientId(client_id)) {
			    OAuthResponse response =
			            OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
			                    .setError(OAuthError.TokenResponse.INVALID_CLIENT)
			                    .setErrorDescription("客户端验证失败，如错误的client_id/client_secret。")
			                    .buildJSONMessage();
			    return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}


			Subject subject = SecurityUtils.getSubject();
			//如果用户没有登录，跳转到登陆页面
        //如果用户没有登录，跳转到登陆页面
        //如果用户没有登录，跳转到登陆页面
        //如果用户没有登录，跳转到登陆页面
			if(!subject.isAuthenticated()) {
//	                if(!login(subject, request)) {//登录失败时跳转到登陆页面
			    	System.out.println("没有登陆");
			        //model.addAttribute("client", clientService.findByClientId(oauthRequest.getClientId()));
//	                    return "login";
//	                }
			}
//			System.out.println(subject.getPrincipal());
//			String username = (String)subject.getPrincipal();
			UserDO user = (UserDO) subject.getPrincipal();
			String username = user.getUsername();
//			String username = "";
			System.out.println(subject.getPrincipal().toString());
			//生成授权码
			String authorizationCode = null;
			//responseType目前仅支持CODE，另外还有TOKEN
         // String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			String responseType = response_type;
			if (responseType.equals(ResponseType.CODE.toString())) {
			    OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			    authorizationCode = oauthIssuerImpl.authorizationCode();
			    System.out.println("code"+authorizationCode);
			    System.out.println("username"+username);
			    OAuthService.addAuthCode(authorizationCode, username);
			}
			Map map = new HashMap<>();
			map.put("code",authorizationCode );
			map.put("redirect_url", redirect_url);
			return map;
//	            //进行OAuth响应构建
//	            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
//	                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
//	            //设置授权码
//	            builder.setCode(authorizationCode);
//	            //得到到客户端重定向地址
//	            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
//
//	            //构建响应
//	            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
//
//	            //根据OAuthResponse返回ResponseEntity响应
//	            HttpHeaders headers = new HttpHeaders();
//	            headers.setLocation(new URI(response.getLocationUri()));
//	            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
	    }

	    private boolean login(Subject subject, HttpServletRequest request) {
	        if("get".equalsIgnoreCase(request.getMethod())) {
	            return false;
	        }
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");

	        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
	            return false;
	        }

	        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

	        try {
	            subject.login(token);
	            return true;
	        } catch (Exception e) {
	            request.setAttribute("error", "登录失败:" + e.getClass().getName());
	            return false;
	        }
	    }
}