package com.bootdo.system.service;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.system.domain.Organization;
import com.bootdo.system.dao.OrgMapper;

@Service
@Transactional
public class OrgService {
	@Autowired
	OrgMapper orgMapper;
	

	public OrgService() {
		super();
	}

	public List<Organization> getOrgsInfoByPid(Integer pid) {
		List<Organization> orgs = orgMapper.findOrgsByPid(pid);
		List<Organization> children;
		for (Organization attribute : orgs) {
			if (attribute.getIsParent() == 1) {
				children = getOrgsInfoByPid(attribute.getId());
				attribute.setChildren(children);
			}
		}
		return orgs;
	}
	
	// 遍历一个树结构，返回平铺的组织id
	public List<Integer> getOrgsIdList(List<Organization> orgs) {
		List<Integer> ids = new ArrayList<Integer>();
		for (Organization org : orgs) {
			ids.add(org.getId());
			if (org.getIsParent() == 1) {
				List<Integer> idschild = new ArrayList<Integer>();
				idschild = getOrgsIdList(org.getChildren());
				ids.addAll(idschild);
			}
		}
		return ids;
	}
	
	// 根据组织id，返回该id和所有子id组成的数组
	public List<Integer> getOrgsIdById(Integer id) {
		List<Integer> ids = new ArrayList<Integer>();
		List<Organization> orgs = new ArrayList<Organization>();
		Organization org = new Organization();
		org = orgMapper.getOrgsIdById(id);
		ids.add(id);
		if (org.getIsParent() == 1) {
			orgs = getOrgsInfoByPid(org.getId());
			ids.addAll(getOrgsIdList(orgs)); 
		}
		return ids;
	}
	
	public List<Organization> getOrgsInfo(){
		List<Organization> orgs = orgMapper.findOrgsInfo();
		System.out.println(orgs);
		return orgs;
	}
	
	public void addOrg(Organization org) {
		orgMapper.addOrg(org);
	}
	
	public void updateOrg(Organization org) {
		orgMapper.updateOrg(org);
	}
	
	public void deleteOrg(Integer id) {
		orgMapper.deleteOrg(id);
	}
	
	public void setIsParent(Integer id) {
		orgMapper.setIsParent(id);
	}
}
