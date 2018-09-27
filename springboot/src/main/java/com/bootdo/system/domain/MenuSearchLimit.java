//package com.bootdo.system.domain;
//
//import java.util.Date;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//
//public class MenuSearchLimit {
//	private long id;
//	private long parient_id;
//	private String name;
//	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//	private Date startdate;
//	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//	private Date enddate;
//	private long beginId;
//	private long endId;
//	private long Ip;
//	public long getId() {
//		return id;
//	}
//	public void setId(long id) {
//		this.id = id;
//	}
//	public long getParient_id() {
//		return parient_id;
//	}
//	public void setParient_id(long parient_id) {
//		this.parient_id = parient_id;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public Date getStartdate() {
//		return startdate;
//	}
//	public void setStartdate(Date startdate) {
//		this.startdate = startdate;
//	}
//	public Date getEnddate() {
//		return enddate;
//	}
//	public void setEnddate(Date enddate) {
//		this.enddate = enddate;
//	}
//	public long getBeginId() {
//		return beginId;
//	}
//	public void setBeginId(long beginId) {
//		this.beginId = beginId;
//	}
//	public long getEndId() {
//		return endId;
//	}
//	public void setEndId(long endId) {
//		this.endId = endId;
//	}
//	@Override
//	public String toString() {
//		return "MenuSearchLimit [id=" + id + ", parient_id=" + parient_id + ", name=" + name + ", startdate="
//				+ startdate + ", enddate=" + enddate + ", beginId=" + beginId + ", endId=" + endId + "]";
//	}
//	
//	
//	
//}

package com.bootdo.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MenuSearchLimit {
	//
	private long id = 0;
	private long parent_id = -1;
	private String name;
	private String userOperation;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startdate;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date enddate;
	private long beginId = -1;
	private long endId = -1;
	private long user_id = 0;
	private int offset = -1;
	private int limit = -1;
	private int page = -1;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parient_id) {
		this.parent_id = parient_id;
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
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getUserOperation() {
		return userOperation;
	}
	public void setUserOperation(String userOperation) {
		this.userOperation = userOperation;
	}
	@Override
	public String toString() {
		return "MenuSearchLimit [id=" + id + ", parent_id=" + parent_id + ", name=" + name + ", userOperation="
				+ userOperation + ", startdate=" + startdate + ", enddate=" + enddate + ", beginId=" + beginId
				+ ", endId=" + endId + ", user_id=" + user_id + "]";
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	
	
	
	
}

