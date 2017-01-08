package com.splus.pojo;

import java.sql.Timestamp;

public class RegnHistory {
	
	private String regnHistoryId;
	private Registration regn;
	private RegnStatus status;
	private Timestamp time;
	
	public RegnHistory(){}

	public String getRegnHistoryId() {
		return regnHistoryId;
	}

	public void setRegnHistoryId(String regnHistoryId) {
		this.regnHistoryId = regnHistoryId;
	}

	public Registration getRegn() {
		return regn;
	}

	public void setRegn(Registration regn) {
		this.regn = regn;
	}

	public RegnStatus getStatus() {
		return status;
	}

	public void setStatus(RegnStatus status) {
		this.status = status;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
