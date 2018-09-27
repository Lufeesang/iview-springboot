package com.bootdo.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.system.domain.Organization;
import com.bootdo.system.domain.Staff;
import com.bootdo.system.service.StaffService;

@RestController
@RequestMapping({ "/staff" })
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class StaffController {
	@Autowired
	StaffService staffService;

	public StaffController() {
	}

	@RequestMapping(value = "/allot", method = RequestMethod.POST)
	@ResponseBody
	public void updateOrgId(@RequestBody List<Staff> staffs) {
		System.out.println(staffs);
		staffService.updateOrgId(staffs);
	}

	@RequestMapping(value = "/getOrgStaffs", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Staff> getStaffsByOrg(Integer id) {
//		System.out.println(id);
		List<Staff> staffs = new ArrayList<Staff>();
		if (id == -1) {
			staffs = staffService.getAllStaffs();
			return staffs;
		}
		staffs = staffService.getStaffsByOrgId(id);
		return staffs;
	}

	@RequestMapping(value = "/getStaffById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Staff> getStaffById(Integer id) {
		List<Staff> staffs = new ArrayList<Staff>();
		staffs.add(staffService.getStaffById(id));
		return staffs;
	}

	@RequestMapping(value = "/getStaffByIds", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Staff> getStaffByIds(Integer start, Integer end) {
		List<Staff> staffs = new ArrayList<Staff>();
		staffs = staffService.getStaffByIds(start, end);
		return staffs;
	}
	
	@RequestMapping(value = "/getStaffByName", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Staff> getStaffByName(String name) {
		List<Staff> staffs = new ArrayList<Staff>();
		Staff staff = staffService.getStaffByName(name);
		staffs.add(staff);
		return staffs;
	}

}
