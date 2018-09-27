package com.bootdo.system.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;

import com.bootdo.system.domain.Organization;
import com.bootdo.system.dao.OrgMapper;
import com.bootdo.system.service.OrgService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bootdo.common.annotation.Log;
@RestController
@RequestMapping({ "/org" })
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class OrgController {
	@Autowired
	OrgService orgService;

	public OrgController() {

	}
	@Log("操作-查询组织信息")
	@RequestMapping(value = "/tree", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Organization> getOrgsInfoByPid(Integer pid) {
		List<Organization> orgs = orgService.getOrgsInfoByPid(pid);
		return orgs;
	}
	@Log("操作-查询组织树")
	@RequestMapping(value = "/info", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Organization> getOrgsInfo() {
		List<Organization> orgs = orgService.getOrgsInfo();
		return orgs;
	}
	@Log("操作-添加组织")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void addOrg(@RequestBody Organization org) {
		System.out.println(org.toString());
		orgService.addOrg(org);
	}
	
	@Log("操作-修改组织")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateOrg(@RequestBody Organization org) {
		System.out.println(org.toString());
		orgService.updateOrg(org);
	}

//	@RequestMapping(value = "/delete", method = RequestMethod.POST)
//	@ResponseBody
//	public void deleteOrg(@RequestBody Organization org) {
//		orgService.deleteOrg(org.getId());
//	}
	
	@Log("操作-删除组织")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public void deleteOrgs(@RequestBody String ids) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(ids);
		String idstr = jsonNode.get("ids").toString();
		String idfilter = idstr.substring(1,idstr.length()-1);
		String[] idList = idfilter.split(",");
		for (String id : idList) {
			System.out.println(Integer.valueOf(id));
			orgService.deleteOrg(Integer.valueOf(id));
		}
	}
	
	@RequestMapping(value = "/setIsParent", method = RequestMethod.POST)
	@ResponseBody
	public void updateIsParent(@RequestBody Organization org) {
		orgService.setIsParent(org.getId());
	}

}
