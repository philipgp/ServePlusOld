package com.splus.pojo;

public class User {
	
	private String userName;
	private String password;
	private String fullName;
	private String address;
	private String email;
	private String phoneNo;
	private Role role;
	private boolean suspendFlag;
	
	public User(){}
	public User(String userName){
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public boolean isSuspendFlag() {
		return suspendFlag;
	}
	
	public void setSuspendFlag(boolean suspendFlag) {
		this.suspendFlag = suspendFlag;
	}

}
