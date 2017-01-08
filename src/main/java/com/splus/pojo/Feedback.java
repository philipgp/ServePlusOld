package com.splus.pojo;

public class Feedback {
	
	private String regnId;
	private Registration regn;
	private String feedback;
	
	public Feedback(){}

	public String getRegnId() {
		return regnId;
	}

	public void setRegnId(String regnId) {
		this.regnId = regnId;
	}

	public Registration getRegn() {
		return regn;
	}

	public void setRegn(Registration regn) {
		this.regn = regn;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
