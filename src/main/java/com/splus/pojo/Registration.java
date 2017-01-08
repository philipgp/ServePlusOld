package com.splus.pojo;

import java.sql.Timestamp;

public class Registration {
	
	private String regnId;
	private User user;
	private Type type;
	private Timestamp timestamp;
	private Timestamp preferedTime;
	private String userComment;
	private Float latitude;
	private Float longitude;
	private String location;
	private String localAddress;
	private User assignedTo;
	private float payment;
	
	public Registration(){}

	public String getRegnId() {
		return regnId;
	}

	public void setRegnId(String regnId) {
		this.regnId = regnId;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp date) {
		this.timestamp = date;
	}

	public Timestamp getPreferedTime() {
		return preferedTime;
	}

	public void setPreferedTime(Timestamp preferedTime) {
		this.preferedTime = preferedTime;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public float getPayment() {
		return payment;
	}

	public void setPayment(float payment) {
		this.payment = payment;
	}

}
