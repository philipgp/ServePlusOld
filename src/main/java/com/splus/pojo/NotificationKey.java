package com.splus.pojo;

public class NotificationKey {
	
	private short keyId;
	private String keyName;
	private String keyDesc;
	
	NotificationKey(){}

	public short getKeyId() {
		return keyId;
	}

	public void setKeyId(short keyId) {
		this.keyId = keyId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyDesc() {
		return keyDesc;
	}

	public void setKeyDesc(String keyDesc) {
		this.keyDesc = keyDesc;
	}

}
