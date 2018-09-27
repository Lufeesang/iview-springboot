package com.bootdo.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bootdo.system.domain.PersonDeptDO;

@Mapper
public interface PersonDeptDao {
	
	@Select("select * from sys_dept where dept_id = #{id}")
	public PersonDeptDO selectAllDept(int id);
	
	@Select("select * from sys_dept")
	public List<PersonDeptDO> selectDept();

}