package com.splus.service;

import java.util.Hashtable;
import java.util.List;

import com.splus.pojo.Registration;

public interface GetDetails {
	
	List<Hashtable<String,Object>> getAllDepartmentTypes();
	List<Hashtable<String,Object>> getAllDepartments();
	List<Hashtable<String,Object>> getAllTypes(byte depId);
	String getTypeName(short typeId);
	String getDeptName(short deptId);	

	List<Hashtable<String,Object>> getAllUserRegistrations(String userName,boolean isAdmin);
	Hashtable<String,Object> getUserRegistrationDetail(String regnId,String userName);
	
	void insertAllDetails(Hashtable<String,Object> regnTable, Registration regnObj,boolean isAdmin);
	
	List<Hashtable<String,Object>> getAllWorkers();
	List<Hashtable<String,Object>> getAllUsers();
	List<Hashtable<String,Object>> getAllWorkers(short typeId);
	
	List<Hashtable<String, Object>> getAssignedWorks(String userName);
	Hashtable<String, Object> getAssignedWork(String userName,String regnId);
	
	List<Hashtable<String,Object>> getRatingQuestions();
	Hashtable<String, Object> getFeedback(String userName, String regnId,String roleName);
	
	List<Hashtable<String, Object>> getLatestNotifications(String userName,String notifId);
}
