package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.bean.Organization;
import com.example.demo.bean.Staff;
import com.example.demo.service.OrgService;
import com.example.demo.service.StaffService;
import com.mysql.fabric.xmlrpc.base.Array;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IviewadminApplicationTests {
	@Autowired
	OrgService orgService;
	@Autowired
	StaffService staffService;

	// test OrgService.getOrgsInfoByPid()
	@Test
	public void contextLoads() {
		List<Organization> orgs;
		orgs = orgService.getOrgsInfoByPid(1);
		System.out.println(orgs);
	}

	// test OrgService.addOrg()
	@Test
	public void addOrgTest() {
		Organization org = new Organization();
		org.setTitle("佛山分公司");
		org.setPid(3);
		org.setIsParent(0);
		org.setPath(".1.3.7");
		org.setStatus(1);
		org.setType("广东省");
		org.setManager("Z030001");
		org.setDescription("佛山分公司...");
		orgService.addOrg(org);
	}

	// test OrgService.updateOrg()
	@Test
	public void updateOrgTest() {
		Organization org = new Organization();
		org.setId(13);
		org.setTitle("sdaf");
		org.setPid(2);
		org.setIsParent(1);
		org.setStatus(1);
		org.setType("北京市");
		org.setManager("Z030001");
		org.setDescription("sdaf...");
		orgService.updateOrg(org);
	}

	// test OrgService.deleteOrg()
	@Test
	public void deleteOrgTest() {
		orgService.deleteOrg(15);
	}

	// test OrgService.setIsParent()
	@Test
	public void setIsParentTest() {
		orgService.setIsParent(19);
	}

	// test StaffService.getAllStaffs()
	@Test
	public void getAllStaffsTest() {
		staffService.getAllStaffs();
	}

	// test StaffService.updateOrgId()
	@Test
	public void updateOrgIdTest() {
		List<Staff> staffs = new ArrayList<Staff>();
		Staff staff1 = new Staff();
		staff1.setId(1);
		staff1.setName("小丽");
		staff1.setOrgId(1);
		Staff staff2 = new Staff();
		staff2.setId(4);
		staff2.setName("小王");
		staff2.setOrgId(1);
		staffs.add(staff1);
		staffs.add(staff2);
		System.out.println(staffs);
		staffService.updateOrgId(staffs);
	}

	// test StaffService.getStaffsByOrg()
	@Test
	public void getStaffsByOrgTest() {
		staffService.getStaffsByOrgId(1);
	}

	// test StaffService.getStaffById()
	@Test
	public void getStaffByIdTest() {
		staffService.getStaffById(1);
	}

	// test StaffService.getStaffByIds()
	@Test
	public void getStaffByIdsTest() {
		staffService.getStaffByIds(5, 8);
	}

	// test StaffService.getStaffByIdName()
	@Test
	public void getStaffByNameTest() {
		staffService.getStaffByName("小");
	}
}
