package com.example.demo.service;

import java.util.List;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.Organization;
import com.example.demo.dao.OrgMapper;

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
