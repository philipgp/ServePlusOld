package com.splus.pojo;

public class CompanyType {
	
	private int companyTypeId;
	private User company;
	private Type type;
	private boolean suspendFlag;
	
	public CompanyType(User company, Type type){
		this.company = company;
		this.type = type;
		this.suspendFlag = false;
	}

	public int getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(int companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public User getCompany() {
		return company;
	}

	public void setCompany(User company) {
		this.company = company;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isSuspendFlag() {
		return suspendFlag;
	}

	public void setSuspendFlag(boolean suspendFlag) {
		this.suspendFlag = suspendFlag;
	}

}
