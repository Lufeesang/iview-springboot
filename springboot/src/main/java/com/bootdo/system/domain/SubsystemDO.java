package com.bootdo.system.domain;

import java.io.Serializable;

public class SubsystemDO implements Serializable{
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	//子系统识别号
	private String sysIdentification;
	//子系统url
	private String indexUrl;
	//子系统主机号或者域名
	private String host;
	//子系统端口号
	private String port;
	
	public SubsystemDO(Long id, String sysIdentification, String indexUrl, String host, String port) {
		super();
		this.id = id;
		this.sysIdentification = sysIdentification;
		this.indexUrl = indexUrl;
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public SubsystemDO() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSysIdentification() {
		return sysIdentification;
	}
	public void setSysIdentification(String sysIdentification) {
		this.sysIdentification = sysIdentification;
	}
	public String getIndexUrl() {
		return indexUrl;
	}
	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}
	@Override
	public String toString() {
		return "SubsystemDO [id=" + id + ", sysIdentification=" + sysIdentification + ", indexUrl=" + indexUrl + "]";
	}
	
}
