package com.bootdo.system.domain;

import java.io.Serializable;

public class UserSubsystemSsuserDO implements Serializable{
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	//用户id
	private Long userId;
	//子系统id
	private Long subsystemId;
	//子系统用户id
	private String ssuserId;
	public UserSubsystemSsuserDO(Long id, Long userId, Long subsystemId, String ssuserId) {
		super();
		this.id = id;
		this.userId = userId;
		this.subsystemId = subsystemId;
		this.ssuserId = ssuserId;
	}
	public UserSubsystemSsuserDO() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getSubsystemId() {
		return subsystemId;
	}
	public void setSubsystemId(Long subsystemId) {
		this.subsystemId = subsystemId;
	}
	public String getSsuserId() {
		return ssuserId;
	}
	public void setSsuserId(String ssuserId) {
		this.ssuserId = ssuserId;
	}
	@Override
	public String toString() {
		return "UserSubsystemSsuserDO [id=" + id + ", userId=" + userId + ", subsystemId=" + subsystemId + ", ssuserId="
				+ ssuserId + "]";
	}
}
