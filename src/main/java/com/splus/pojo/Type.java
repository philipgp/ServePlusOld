package com.splus.pojo;

public class Type {
	
	private short typeId;
	private String typeName;
	private Department dept;
	
	public Type(){}
	public Type(short typeId){
		this.typeId = typeId;
	}
	public Type(String typeName,Department dept){
		this.typeName = typeName;
		this.dept = dept;
	}

	public short getTypeId() {
		return typeId;
	}

	public void setTypeId(short typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

}
