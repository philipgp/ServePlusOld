package com.splus.service;

import org.json.JSONException;

public interface PutDetails {
	
	Short insertNewType(String typeName, short deptId);
	Short insertDepartment(String jsonString) throws JSONException;
	Integer insertCompanyType(String companyName, short typeId);
	int updateDeptName(short deptId,String newName);
	int updateTypeName(short typeId,String newName);
	int suspendDept(short deptId, boolean flag);
}
