package com.bootdo.common.domain;

import java.io.Serializable;

import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;

public class InterSystemConnectionDO implements Serializable{  
	private static final long serialVersionUID = 1L;
	//对应：用户-子系统-子系统用户
	private UserSubsystemSsuserDO userSubsystemSsuser;
	//子系统实体
	private SubsystemDO subsystem;
	//口令
	private String token;
	//是否已验证连接
	private boolean isVerify;
	public InterSystemConnectionDO(UserSubsystemSsuserDO userSubsystemSsuser, SubsystemDO subsystem, String token,
			boolean isVerify) {
		super();
		this.userSubsystemSsuser = userSubsystemSsuser;
		this.subsystem = subsystem;
		this.token = token;
		this.isVerify = isVerify;
	}
	public InterSystemConnectionDO() {
		super();
	}
	public UserSubsystemSsuserDO getUserSubsystemSsuser() {
		return userSubsystemSsuser;
	}
	public void setUserSubsystemSsuser(UserSubsystemSsuserDO userSubsystemSsuser) {
		this.userSubsystemSsuser = userSubsystemSsuser;
	}
	public SubsystemDO getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(SubsystemDO subsystem) {
		this.subsystem = subsystem;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isVerify() {
		return isVerify;
	}
	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}
	@Override
	public String toString() {
		return "InterSystemConnectionDO [userSubsystemSsuser=" + userSubsystemSsuser + ", subsystem=" + subsystem
				+ ", token=" + token + ", isVerify=" + isVerify + "]";
	}
}
