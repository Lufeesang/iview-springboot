package com.bootdo.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bootdo.common.annotation.Log;
import com.bootdo.system.service.oauth2service.OAuthService;


@Controller
public class ResourceController {
	
	@Autowired
	OAuthService oAuthService;
	@Log("操作-单点登陆-获取资源")
	@RequestMapping("/resource")
	public HttpEntity getUserInfoByaccessToken(HttpServletRequest request) throws OAuthSystemException {
		try {
			OAuthAccessResourceRequest oAuthAccessResourceRequest 
				= new OAuthAccessResourceRequest(request,ParameterStyle.QUERY);
			String accessToken = oAuthAccessResourceRequest.getAccessToken();
			System.out.println("resource token"+accessToken);
			
			 if (!oAuthService.checkAccessToken(accessToken)) {
	             // 如果不存在/过期了，返回未验证错误，需重新验证
				 System.out.println("token过期");
	             OAuthResponse oauthResponse = OAuthRSResponse
	                     .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
	                     .setRealm("资源认证中心，token 不存在或者过期")
	                     .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
	                     .buildHeaderMessage();
	
	             HttpHeaders headers = new HttpHeaders();
	             headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
	             return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
	         }
			String username = oAuthService.getUsernameByAccessToken(accessToken);
			System.out.println("用户名"+username);
			
			return new ResponseEntity(username,HttpStatus.OK);
		}catch(OAuthProblemException e) {
			 OAuthResponse oauthResponse = OAuthRSResponse
	                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
	                    .setRealm("资源认证授权服务器")
	                    .setError(e.getError())
	                    .setErrorDescription(e.getDescription())
	                    .setErrorUri(e.getUri())
	                    .buildHeaderMessage();

	            HttpHeaders headers = new HttpHeaders();
	            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
}
