package com.bootdo.system.domain;

public class TransferUser {
	private Long userId;
	private String name;
	private boolean hasAuthority;
	private long deptId;
	private String department;
	
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public long getDeptId() {
		return deptId;
	}
	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHasAuthority() {
		return hasAuthority;
	}
	public void setHasAuthority(boolean hasAuthority) {
		this.hasAuthority = hasAuthority;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return "TransferUser [user_id=" + userId + ", name=" + name + ", hasAuthority=" + hasAuthority + ", deptId="
				+ deptId + ", department=" + department + "]";
	}
	
	
	
}
