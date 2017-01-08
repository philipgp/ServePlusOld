package com.splus.dao;

import java.util.List;

import com.splus.pojo.CompanyType;
import com.splus.pojo.Department;
import com.splus.pojo.RegnStatus;
import com.splus.pojo.Type;
import com.splus.pojo.User;
import com.splus.pojo.WorkerType;

public interface ServiceDetailDao {
	
	List<Department> getAllDepartments();
	List<Department> getAllDepartmentsWithSuspend();
	List<Type> getAllTypes(short depId);
	List<Type> getAllTypesWithSuspend(short depId);
//	Object[] getDeptTypeNames(short typeId);
	List<String> getAdminTokens();
	String getAdminUserName();
	String getUserToken(String userName);
	List<String> getUserTokens(String userName);
	String getTypeName(short typeId);
	String getTypeNameWithoutSuspend(short typeId);
	String getDeptName(short deptId);
	
	List<User> getAllUsers();
	List<WorkerType> getAllWorkerTypeObjects();
	List<WorkerType> getAllWorkerTypeObjects(short typeId);
	String checkWorkerAvailabilty(String workerName);
	
	Short insertNewType(Type type);
	Short insertNewDept(Department dept);
	int updateDeptName(short deptId, String newName);
	int updateTypeName(short typeId, String newName);
	
	RegnStatus getStatusObj(String statusName);
	
	int suspendDept(short deptId, boolean flag);
	
	CompanyType getCompanyType(String companyName, short typeId);
	Integer insertCompanyType(CompanyType obj);
	int suspendCompanyType(int cTypeId, boolean flag);
	
	
}
