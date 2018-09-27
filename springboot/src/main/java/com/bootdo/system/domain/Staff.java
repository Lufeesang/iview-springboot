package com.bootdo.system.domain;

public class Staff {
	private Integer id;
	private String name;
	private Integer orgId;
	private Integer status;
	
	public Staff() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Staff [id=" + id + ", name=" + name + ", orgId=" + orgId + ", status=" + status + "]";
	}
	
}
