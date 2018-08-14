package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.bean.Organization;
import com.example.demo.bean.Staff;

@Mapper
public interface StaffMapper {

	List<Staff> findAllStaffs();
	
	void updateOrgId(@Param("staff") Staff staff);
	
	List<Staff> findStaffsByOrgId(@Param("id") Integer id);
	
	Staff findStaffById(@Param("id") Integer id);
	
	Staff findStaffByName(@Param("name") String name);
}
