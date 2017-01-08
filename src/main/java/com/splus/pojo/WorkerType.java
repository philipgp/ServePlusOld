package com.splus.pojo;

public class WorkerType {
	
	private String userName;
	private User user;
	private Type type;
	private User company;
	private boolean suspendFlag;
	
	public WorkerType(){}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public User getCompany() {
		return company;
	}

	public void setCompany(User company) {
		this.company = company;
	}

	public boolean isSuspendFlag() {
		return suspendFlag;
	}

	public void setSuspendFlag(boolean suspendFlag) {
		this.suspendFlag = suspendFlag;
	}

}
