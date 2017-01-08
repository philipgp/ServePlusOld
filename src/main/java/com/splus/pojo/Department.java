package com.splus.pojo;

public class Department {
	
	private short deptId;
	private String deptName;
	private boolean suspendFlag;
	
	Department(){}
	public Department(short deptId){
		this.deptId = deptId;
	}
	public Department(String deptName){
		this.deptName = deptName;
	}

	public short getDeptId() {
		return deptId;
	}

	public void setDeptId(short deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public boolean isSuspendFlag() {
		return suspendFlag;
	}
	public void setSuspendFlag(boolean suspendFlag) {
		this.suspendFlag = suspendFlag;
	}

}
