package com.splus.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.splus.dao.ServiceDetailDao;
import com.splus.pojo.CompanyType;
import com.splus.pojo.Department;
import com.splus.pojo.Type;
import com.splus.pojo.User;

public class PutDetailsImp implements PutDetails {
	
	private ServiceDetailDao serviceDao;

	public void setServiceDao(ServiceDetailDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@Override
	public Short insertNewType(String typeName, short deptId) {
		
		Type type = new Type(typeName,new Department(deptId));
		return serviceDao.insertNewType(type);
	}

	@Override
	public Short insertDepartment(String jsonString) throws JSONException {
		
		JSONObject jsonObj = new JSONObject(jsonString);
		String deptName = jsonObj.getString("deptName");
		JSONArray typeArray = jsonObj.getJSONArray("types");
		if(deptName==null || typeArray==null)
			return null;
		Department deptObj = new Department(jsonObj.getString("deptName"));
		deptObj.setSuspendFlag(false);
		Short deptId = serviceDao.insertNewDept(deptObj);
		deptObj.setDeptId(deptId);
		int len = typeArray.length();
		for(int i=0;i<len;i++){
			String typeName = typeArray.getString(i);
			if(typeName!=null){
				Type typeObj = new Type(typeName,deptObj);
				serviceDao.insertNewType(typeObj);
			}
		}
		return deptId;
	}

	@Override
	public int updateDeptName(short deptId, String newName) {
		return serviceDao.updateDeptName(deptId, newName);
	}

	@Override
	public int updateTypeName(short typeId, String newName) {
		return serviceDao.updateTypeName(typeId, newName);
	}

	@Override
	public int suspendDept(short deptId, boolean flag) {
		return serviceDao.suspendDept(deptId, flag);
	}

	@Override
	public Integer insertCompanyType(String companyName, short typeId) {
		CompanyType type = serviceDao.getCompanyType(companyName, typeId);
		if(type!=null)
			return serviceDao.suspendCompanyType(typeId, true);
		type = new CompanyType(new User(companyName), new Type(typeId));
		return serviceDao.insertCompanyType(type);
	}

}
