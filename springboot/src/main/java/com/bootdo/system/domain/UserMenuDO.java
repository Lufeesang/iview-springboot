//package com.bootdo.system.domain;
//
//public class UserMenuDO {
//    private Long id;
//    private Long userId;
//    private Long menuId;
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public Long getUserId() {
//		return userId;
//	}
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//	public Long getMenuId() {
//		return menuId;
//	}
//	public void setMenuId(Long menuId) {
//		this.menuId = menuId;
//	}
//	@Override
//	public String toString() {
//		return "UserMenuDO [id=" + id + ", userId=" + userId + ", menuId=" + menuId + "]";
//	}
//}
package com.bootdo.system.domain;

public class UserMenuDO {
    private Long id;
    private Long userId;
    private Long menuId;
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
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	@Override
	public String toString() {
		return "UserMenuDO [id=" + id + ", userId=" + userId + ", menuId=" + menuId + "]";
	}
}