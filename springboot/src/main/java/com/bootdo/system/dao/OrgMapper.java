package com.bootdo.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bootdo.system.domain.Organization;;

@Mapper
public interface OrgMapper {

	List<Organization> findOrgsByPid(@Param("pid") Integer pid);
	
	Organization getOrgsIdById(@Param("id") Integer id);
	
	void addOrg(@Param("org") Organization org);
	
	void updateOrg(@Param("org") Organization org);
	
	void deleteOrg(@Param("id") Integer id);
	
	void setIsParent(@Param("id") Integer id);
	
	List<Organization> findOrgsInfo(); 
}
