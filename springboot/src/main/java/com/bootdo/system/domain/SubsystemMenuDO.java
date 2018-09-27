package com.bootdo.system.domain;

import java.io.Serializable;

public class SubsystemMenuDO implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	//子系统id
	private Long subsystemId;
	//菜单id
	private Long menuId;

	public SubsystemMenuDO(Long id, Long subsystemId, Long menuId) {
		super();
		this.id = id;
		this.subsystemId = subsystemId;
		this.menuId = menuId;
	}

	public SubsystemMenuDO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubsystemId() {
		return subsystemId;
	}

	public void setSubsystemId(Long subsystemId) {
		this.subsystemId = subsystemId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Override
	public String toString() {
		return "SubsystemMenu [id=" + id + ", subsystemId=" + subsystemId + ", menuId=" + menuId + "]";
	}
	
	
	
}
