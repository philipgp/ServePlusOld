package com.splus.pojo;

public class UserCompany {
	
	private String userName;
	private User user;
	private User company;
	
	UserCompany(){}
	public UserCompany(User user, User company) {
		this.user = user;
		this.company = company;
	}

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

	public User getCompany() {
		return company;
	}

	public void setCompany(User company) {
		this.company = company;
	}

}
