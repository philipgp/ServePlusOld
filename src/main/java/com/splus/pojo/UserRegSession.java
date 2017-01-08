package com.splus.pojo;

import java.sql.Timestamp;

public class UserRegSession {
	
	private String userName;
	private User user;
	private String token;
	private String emailCode;
	private Timestamp expiryDate;
	
	public UserRegSession(){}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

}
