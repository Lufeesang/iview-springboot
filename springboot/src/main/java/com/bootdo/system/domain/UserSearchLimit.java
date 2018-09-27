package com.bootdo.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用于查找User 时候对应的多条件查询得实体类
 * @author zcchen
 *
 */
public class UserSearchLimit {
	private String name;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startdate;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date enddate;
	private long beginId = -1;
	private long endId = -1;
	@JsonProperty("department")
	private long dept_id = -1;
	private Integer  status = -1;
	//menu_id 用来存放菜单分配人员时，菜单id
	@JsonProperty("menuId")
	private long menu_id;
	
	public long getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(long menu_id) {
		this.menu_id = menu_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public long getBeginId() {
		return beginId;
	}
	public void setBeginId(long beginId) {
		this.beginId = beginId;
	}
	public long getEndId() {
		return endId;
	}
	public void setEndId(long endId) {
		this.endId = endId;
	}
	public long getDept_id() {
		return dept_id;
	}
	public void setDept_id(long dept_id) {
		this.dept_id = dept_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserSearchLimit [name=" + name + ", startdate=" + startdate + ", enddate=" + enddate + ", beginId="
				+ beginId + ", endId=" + endId + ", dept_id=" + dept_id + ", status=" + status + ", menu_id=" + menu_id
				+ "]";
	}
	
	
}
