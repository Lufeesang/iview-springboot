package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.Organization;
import com.example.demo.bean.Staff;
import com.example.demo.dao.OrgMapper;
import com.example.demo.dao.StaffMapper;

@Service
@Transactional
public class StaffService {
	@Autowired
	StaffMapper staffMapper;

	public StaffService() {
	}

	public List<Staff> getAllStaffs() {
		List<Staff> staffs = staffMapper.findAllStaffs();
		System.out.println(staffs);
		return staffs;
	}
	
	public void updateOrgId(List<Staff> staffs) {
		for (Staff staff : staffs) {
//			System.out.println(staff);
			staffMapper.updateOrgId(staff);	
		}
	}
	
	public List<Staff> getStaffsByOrgId(Integer id) {
		List<Staff> staffs = staffMapper.findStaffsByOrgId(id);
		return staffs;
	}
	
	public Staff getStaffById(Integer id) {
		Staff staff = staffMapper.findStaffById(id);
//		System.out.println(staff);
		return staff;
	}
	
	public List<Staff> getStaffByIds(Integer start, Integer end) {
		Staff staff = new Staff();
		List<Staff> staffs = new ArrayList<Staff>(); 
		for (int id = start; id <= end; id++) {
			staff = staffMapper.findStaffById(id);
			if(staff != null) {
				staffs.add(staff);
			}
		}
		System.out.println(staffs);
		return staffs;
	}
	
	public Staff getStaffByName(String name) {
		Staff staff = staffMapper.findStaffByName(name);
//		System.out.println(staff);
		return staff;
	}

}
