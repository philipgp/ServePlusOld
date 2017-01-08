package com.splus.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.splus.dao.NotificationDao;
import com.splus.dao.RegDetailsDao;
import com.splus.dao.ServiceDetailDao;
import com.splus.pojo.Department;
import com.splus.pojo.Notification;
import com.splus.pojo.Rating;
import com.splus.pojo.RatingQuestion;
import com.splus.pojo.Registration;
import com.splus.pojo.RegnHistory;
import com.splus.pojo.RegnStatus;
import com.splus.pojo.Type;
import com.splus.pojo.User;
import com.splus.pojo.WorkerType;
import com.splus.subservice.ListenerClass;

public class GetDetailsImp implements GetDetails {
	
	private ServiceDetailDao serviceDao;
	private RegDetailsDao regnDao;
	private NotificationDao notDao;

	public void setServiceDao(ServiceDetailDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public void setRegnDao(RegDetailsDao regnDao) {
		this.regnDao = regnDao;
	}

	public void setNotDao(NotificationDao notDao) {
		this.notDao = notDao;
	}

	@Override
	public List<Hashtable<String,Object>> getAllDepartmentTypes() {
		
		List<Department> deptList = serviceDao.getAllDepartmentsWithSuspend();
		ArrayList<Hashtable<String,Object>> eachDeptList = new ArrayList<Hashtable<String,Object>>();
		for(Department dept : deptList){
			
			List<Type> typeList = serviceDao.getAllTypesWithSuspend(dept.getDeptId());
			Hashtable<String,Object> deptTable = new Hashtable<String,Object>();
			ArrayList<Hashtable<String,Object>> eachTypeList = new ArrayList<Hashtable<String,Object>>();
			for(Type type : typeList){
				
				Hashtable<String,Object> typeTable = new Hashtable<String,Object>();
				typeTable.put("typeId", type.getTypeId());
				typeTable.put("typeName", type.getTypeName());
				eachTypeList.add(typeTable);
			}
			deptTable.put("deptId", dept.getDeptId());
			deptTable.put("deptName", dept.getDeptName());
			deptTable.put("suspendFlag", dept.isSuspendFlag());
			deptTable.put("type", eachTypeList);
			eachDeptList.add(deptTable);
			
		}
		return eachDeptList;
	}

	@Override
	public List<Hashtable<String, Object>> getAllDepartments() {
		
		List<Department> deptList = serviceDao.getAllDepartments();
		ArrayList<Hashtable<String,Object>> eachDeptList = new ArrayList<Hashtable<String,Object>>();
		for(Department dept : deptList){			

			Hashtable<String,Object> deptTable = new Hashtable<String,Object>();
			deptTable.put("deptId", dept.getDeptId());
			deptTable.put("deptName", dept.getDeptName());
			eachDeptList.add(deptTable);
		}
		return eachDeptList;
	}

	@Override
	public List<Hashtable<String, Object>> getAllTypes(byte depId) {
		
		List<Type> typeList = serviceDao.getAllTypes(depId);
		ArrayList<Hashtable<String,Object>> eachTypeList = new ArrayList<Hashtable<String,Object>>();
		for(Type type : typeList){			

			Hashtable<String,Object> typeTable = new Hashtable<String,Object>();
			typeTable.put("typeId", type.getTypeId());
			typeTable.put("typeName", type.getTypeName());
			eachTypeList.add(typeTable);
		}
		return eachTypeList;
	}
	
	private String formatDate(Timestamp timestamp){
		
		if(timestamp==null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(timestamp);
	}
	
	private String checkNull(Object obj){
		
		if(obj==null)
			return "";
		return obj.toString();
	}
	
	@Override
	public void insertAllDetails(Hashtable<String,Object> regnTable,
			Registration regnObj,boolean isAdmin){

		regnTable.put("regnId", regnObj.getRegnId());
		regnTable.put("userName", regnObj.getUser().getUserName());
		regnTable.put("fullName", regnObj.getUser().getFullName());
		regnTable.put("phoneNo", regnObj.getUser().getPhoneNo());
		regnTable.put("dept", regnObj.getType().getDept().getDeptName());
		regnTable.put("type", regnObj.getType().getTypeName());
		regnTable.put("typeId", regnObj.getType().getTypeId());
		regnTable.put("date", formatDate(regnObj.getTimestamp()));
		regnTable.put("preferedTime", formatDate(regnObj.getPreferedTime()));
		regnTable.put("comment", checkNull(regnObj.getUserComment()));
		regnTable.put("latitude", checkNull(regnObj.getLatitude()));
		regnTable.put("longitude", checkNull(regnObj.getLongitude()));
		regnTable.put("location", checkNull(regnObj.getLocation()));
		regnTable.put("localAddress", checkNull(regnObj.getLocalAddress()));
		regnTable.put("payment", checkNull(regnObj.getPayment()));
		regnTable.put("image", ListenerClass.getRegnImageLink(regnTable.get("regnId").toString()));
		
		/////////////////////////////////////////
		Object[] statusArray = regnDao.getRegnStatus(regnObj); // Last known status
		if(statusArray==null || statusArray.length==0){
			regnTable.put("status", "REGISTERED");
		}
		else{
			regnTable.put("status", ((RegnStatus)statusArray[0]).getStatusName());
			regnTable.put("statusTime", formatDate((Timestamp)statusArray[1]));
		}
		////////////////////////////////////////
		if(isAdmin){
			
			User worker = regnObj.getAssignedTo();
			if(worker==null)
				regnTable.put("assignedTo", "");
			else{

				Hashtable<String,Object> assignedTo = new Hashtable<String,Object>();
				assignedTo.put("userName", worker.getUserName());
				assignedTo.put("fullName", worker.getFullName());
				assignedTo.put("phoneNo", worker.getPhoneNo());
				assignedTo.put("proPic", com.splus.subservice.ListenerClass
						.getProPicLink(assignedTo.get("userName").toString()));
				assignedTo.put("assignedTime", regnDao.getStatusTime("ASSIGNED", regnObj));
				regnTable.put("assignedTo", assignedTo);
			}
		}	
	}

	@Override
	public List<Hashtable<String, Object>> getAllWorkers() {
		
		List<WorkerType> statusList = serviceDao.getAllWorkerTypeObjects();
		ArrayList<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
		for(WorkerType statusObj : statusList){
			
			Hashtable<String, Object> eachTable = new Hashtable<String, Object>();
			eachTable.put("userName", statusObj.getUserName());
			eachTable.put("fullName", checkNull(statusObj.getUser().getFullName()));
			eachTable.put("phoneNo", checkNull(statusObj.getUser().getPhoneNo()));
			eachTable.put("deptName", checkNull(statusObj.getType().getDept().getDeptName()));
			eachTable.put("typeName", checkNull(statusObj.getType().getTypeName()));
			eachTable.put("suspendFlag", statusObj.getUser().isSuspendFlag());
			boolean available = serviceDao.checkWorkerAvailabilty(statusObj.getUserName())==null;
			eachTable.put("available", available);
			
			result.add(eachTable);
		}
		return result;
	}

	@Override
	public List<Hashtable<String, Object>> getAllUsers() {
		
		List<User> userList = serviceDao.getAllUsers();
		ArrayList<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
		for(User userObj : userList){
			
			Hashtable<String, Object> eachTable = new Hashtable<String, Object>();
			eachTable.put("userName", userObj.getUserName());
			eachTable.put("fullName", checkNull(userObj.getFullName()));
			eachTable.put("address", checkNull(userObj.getAddress()));
			eachTable.put("email", checkNull(userObj.getEmail()));
			eachTable.put("phoneNo", checkNull(userObj.getPhoneNo()));
			eachTable.put("proPic", com.splus.subservice.ListenerClass
					.getProPicLink(eachTable.get("userName").toString()));
			eachTable.put("suspendFlag", userObj.isSuspendFlag());
			
			result.add(eachTable);
		}
		return result;
	}

	@Override
	public String getTypeName(short typeId) {
		return serviceDao.getTypeName(typeId);
	}

	@Override
	public String getDeptName(short deptId) {
		return serviceDao.getDeptName(deptId);
	}
	


	@Override
	public List<Hashtable<String, Object>> getAllUserRegistrations(String userName,
			boolean isAdmin) {
		
		ArrayList<Hashtable<String,Object>> result = new ArrayList<Hashtable<String,Object>>();
		
		List<Registration> regnList = null;
		if(userName == null)
			regnList = regnDao.getAllRegistrations();
		else
			regnList = regnDao.getUserRegistration(userName);
		
		for(Registration regn : regnList){
			
			Hashtable<String,Object> regnTable = new Hashtable<String,Object>();
			insertAllDetails(regnTable,regn,isAdmin);
			
			result.add(regnTable);
		}
		return result;
	}
	
	@Override
	public Hashtable<String, Object> getUserRegistrationDetail(String regnId,String userName) {
		
		Registration regnObj = regnDao.getUserRegistrationFromId(regnId);
		if(regnObj==null)
			return null;
		boolean isAdmin = false;
		if(userName!=null){
			// Check the requested user is the same who registered the complaint
			if(!userName.equals(regnObj.getUser().getUserName()))
				return null;
		}
		else
			isAdmin = true;	//Requested user is Admin					

		Hashtable<String,Object> regnTable = new Hashtable<String,Object>();
		insertAllDetails(regnTable,regnObj,isAdmin);
		
		//
		
		// GET FULL HISTORY
		List<RegnHistory> historyList = regnDao.getAllHistory(regnObj);
		ArrayList<Hashtable<String,Object>> history = new ArrayList<Hashtable<String,Object>>();
		for(RegnHistory eachHis : historyList){
			
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			table.put("status", eachHis.getStatus().getStatusName());
			table.put("statusDesc", eachHis.getStatus().getStatusDesc());
			table.put("time", formatDate(eachHis.getTime()));
			history.add(table);
		}
		regnTable.put("statusHistory", history);
		//////////////////
		return regnTable;
	}
	
	@Override
	public List<Hashtable<String, Object>> getAllWorkers(short typeId) {
		
		List<WorkerType> workerList = serviceDao.getAllWorkerTypeObjects(typeId);
		ArrayList<Hashtable<String,Object>> eachWorkerList = new ArrayList<Hashtable<String,Object>>();
		for(WorkerType worker : workerList){

			Hashtable<String,Object> workerTable = new Hashtable<String,Object>();
			User userObj = worker.getUser();
			if("ROLE_WORKER".equals(userObj.getRole().getRoleName())){

				workerTable.put("userName", userObj.getUserName());
				workerTable.put("fullName", userObj.getFullName());
				workerTable.put("address", userObj.getAddress());
				workerTable.put("email", userObj.getEmail());
				workerTable.put("phoneNo", userObj.getPhoneNo());
				boolean available = serviceDao.checkWorkerAvailabilty(userObj.getUserName())==null;
				workerTable.put("available", available);
				eachWorkerList.add(workerTable);
			}
		}
		return eachWorkerList;
	}
	
	@Override
	public List<Hashtable<String, Object>> getAssignedWorks(String userName) {
		
		List<Registration> workList = regnDao.getAssignedWorks(userName);
		ArrayList<Hashtable<String,Object>> result = new ArrayList<Hashtable<String,Object>>();
		for(Registration work : workList){
			
			Hashtable<String,Object> workTable = new Hashtable<String,Object>();
			insertAllDetails(workTable,work,false);
			workTable.put("assignedTime", regnDao.getStatusTime("ASSIGNED", work));
			result.add(workTable);
		}
		return result;
	}

	@Override
	public Hashtable<String, Object> getAssignedWork(String userName, String regnId) {
		
		Registration regnObj = regnDao.getUserRegistrationFromId(regnId);
		String worker = regnObj.getAssignedTo().getUserName();
		if(worker==null || ! worker.equals(userName))
			return null;
		
		Hashtable<String,Object> workTable = new Hashtable<String,Object>();
		insertAllDetails(workTable,regnObj,false);
		workTable.put("assignedTime", regnDao.getStatusTime("ASSIGNED", regnObj));
		
		// GET FULL HISTORY
		List<RegnHistory> historyList = regnDao.getAllHistory(regnObj);
		ArrayList<Hashtable<String,Object>> history = new ArrayList<Hashtable<String,Object>>();
		for(RegnHistory eachHis : historyList){
			
			Hashtable<String,Object> table = new Hashtable<String,Object>();
			table.put("status", eachHis.getStatus().getStatusName());
			table.put("statusDesc", eachHis.getStatus().getStatusDesc());
			table.put("time", formatDate(eachHis.getTime()));
			history.add(table);
		}
		workTable.put("statusHistory", history);
		
		return workTable;
		
	}

	@Override
	public Hashtable<String, Object> getFeedback(String userName, String regnId, String roleName) {
		
		String feedBack = regnDao.getFeedback(regnId);
		if(feedBack==null)
			return null;
		Registration regnObj = regnDao.getUserRegistrationFromId(regnId);
		if("ROLE_USER".equals(roleName))
			if( ! regnObj.getUser().getUserName().equals(userName))
				return null;
		
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		ArrayList<Hashtable<String, Object>> list = new ArrayList<Hashtable<String, Object>>();
		List<Rating> ratingList = regnDao.getRating(regnId);
		for(Rating ratingObj : ratingList){
			
			Hashtable<String, Object> eachTab = new Hashtable<String, Object>();
			eachTab.put("question", ratingObj.getRatingQn().getQuestion());
			eachTab.put("rating", ratingObj.getRating());			
			list.add(eachTab);
		}
		result.put("feedback", feedBack);
		result.put("ratings", list);
		return result;
	}

	@Override
	public List<Hashtable<String, Object>> getRatingQuestions() {
		
		ArrayList<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
		List<RatingQuestion> qnList = regnDao.getRatingQuestions();
		for(RatingQuestion qn : qnList){
			
			Hashtable<String, Object> table = new Hashtable<String, Object>();
			table.put("questionId", qn.getQnId());
			table.put("question", qn.getQuestion());
			result.add(table);
		}
		return result;
	}

	@Override
	public List<Hashtable<String, Object>> getLatestNotifications(String userName, String notifId) {
		
		ArrayList<Hashtable<String, Object>> result= new ArrayList<Hashtable<String, Object>>();
		List<Notification> notList = null;
		if(notifId==null || notifId.isEmpty())
			notList = notDao.getLatestNotifications(userName);
		else
			notList = notDao.getNotifications(userName, notifId);
		for(Notification notObj : notList){
			
			Hashtable<String, Object> table = new Hashtable<String, Object>();
			table.put("notId", notObj.getNotifId());
			JSONObject json =null;
			try {
				json = new JSONObject(notObj.getNotifJson());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			table.put("notJson", json==null?"":json);
			table.put("readFlag", notObj.isReadFlag());
			table.put("timestamp", formatDate(notObj.getTimestamp()));
			result.add(table);
		}
		return result;
	}

}
