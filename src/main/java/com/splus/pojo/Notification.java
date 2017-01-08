package com.splus.pojo;

import java.sql.Timestamp;

public class Notification {
	
	private String notifId;
	private NotificationKey notifKey;
	private User user;
	private String notifJson;
	private Timestamp timestamp;
	private boolean readFlag;
	
	public Notification(){}

	public String getNotifId() {
		return notifId;
	}

	public void setNotifId(String notifId) {
		this.notifId = notifId;
	}

	public NotificationKey getNotifKey() {
		return notifKey;
	}

	public void setNotifKey(NotificationKey notifKey) {
		this.notifKey = notifKey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNotifJson() {
		return notifJson;
	}

	public void setNotifJson(String notifJson) {
		this.notifJson = notifJson;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isReadFlag() {
		return readFlag;
	}

	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}

}
