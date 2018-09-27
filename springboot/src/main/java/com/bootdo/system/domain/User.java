package com.bootdo.system.domain;

public class User {
	private String username;
	private String phone;
    private String email;
    private int status;
    private String money;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", phone=" + phone + ", email=" + email + ", status=" + status
				+ ", money=" + money + "]";
	}
	
}
