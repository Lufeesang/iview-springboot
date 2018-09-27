package com.bootdo.system.domain;

import java.util.List;

public class Organization {
	private Integer id;
	private String title;
	private Integer pid;
	private Integer isParent;
	private String path;
	private List<Organization> children;
	private Integer status;
	private String type;
	private String manager;
	private String description;
	
	public Organization() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getIsParent() {
		return isParent;
	}
	public void setIsParent(Integer isParent) {
		this.isParent = isParent;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public List<Organization> getChildren() {
		return children;
	}
	public void setChildren(List<Organization> children) {
		this.children = children;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Organization [id=" + id + ", title=" + title + ", pid=" + pid + ", isParent=" + isParent + ", path="
				+ path + ", children=" + children + ", status=" + status + ", type=" + type + ", manager=" + manager
				+ ", description=" + description + "]";
	}
	
	
}
