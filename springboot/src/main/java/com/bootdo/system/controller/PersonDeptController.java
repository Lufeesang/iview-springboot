package com.bootdo.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.common.annotation.Log;
import com.bootdo.system.dao.PersonDeptDao;
import com.bootdo.system.domain.PersonDeptDO;

@RestController
@RequestMapping("/persondept")
public class PersonDeptController {
	@Autowired
	PersonDeptDao deptMapper;

	@CrossOrigin("http://localhost:8080")
	@Log("操作-查询-获取所有部门信息")
	@RequestMapping(value = {"/selectAllDept"}, method = RequestMethod.GET)
	public PersonDeptDO selectAllDept(String id) {
		PersonDeptDO dept = deptMapper.selectAllDept(Integer.parseInt(id));
        return dept;
	}
	
	@Log("操作-查询-获取部门信息")
	@RequestMapping(value = {"/selectDept"}, method = RequestMethod.GET)
	public List<PersonDeptDO> selectDept() {
		List<PersonDeptDO> deptlist = deptMapper.selectDept();
        return deptlist;
	}
}
