package com.splus.controller;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.splus.service.GetDetails;
import com.splus.service.LoginService;
import com.splus.service.PutDetails;
import com.splus.service.RegisterService;
import com.splus.subservice.JsonResponseFormatter;

@RestController
public class AdminController {
	
	static Logger log = Logger.getLogger(AdminController.class.getName());
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private GetDetails getDetailService;
	@Autowired
	private RegisterService registerService;
	@Autowired
	private PutDetails putDetailService;
	
	@RequestMapping(value = "/getAllDeptType", method = RequestMethod.POST)
	public String getAllDeptType(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getAllDeptType");
		log.debug(userName);
		log.debug(token);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			String roleName = loginService.getSession(userName, token);
			if( "ROLE_ADMIN".equals(roleName) ){
				
				result = getDetailService.getAllDepartmentTypes();
				resultCode = 1;
				message = "Success";				
			}
			else
				message = "Invalid session. Login again";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getAllDeptType", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getAllWorkers", method = RequestMethod.POST)
	public String getWorkers(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("typeId") short typeId){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getAllWorkers");
		log.debug(typeId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				
				result = getDetailService.getAllWorkers(typeId);
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getWorkers", resultCode, message, result);
	}
	
	// Response with all registrations of all users
	@RequestMapping(value = "/getAllUserRegistrations", method = RequestMethod.POST)
	public String getAllUserRegistrations(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getAllUserRegistrations");
		log.debug("Username : "+userName);
		log.debug("token : "+token);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getAllUserRegistrations(null,true);
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getAllUserRegistrations", resultCode, message, result);
	}
	
	// Response with all registrations of given users
	@RequestMapping(value = "/getThisUserRegistrations", method = RequestMethod.POST)
	public String getThisUserRegistrations(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("thisUser") String thisUser){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getThisUserRegistrations");
		log.debug("Username : "+userName);
		log.debug("token : "+token);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getAllUserRegistrations(thisUser,true);
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getThisUserRegistrations", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getThisWorkerRegistrations", method = RequestMethod.POST)
	public String getThisWorkerRegistrations(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("thisWorker") String thisWorker){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getThisWorkerRegistrations");
		log.debug("Username : "+userName);
		log.debug("token : "+token);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getAssignedWorks(thisWorker);
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getThisWorkerRegistrations", resultCode, message, result);
	}
	
	@RequestMapping(value = "/assignWork", method = RequestMethod.POST)
	public String getNewRegistrations(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("worker") String workerName,@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		
		//////////////////////
		log.debug("assignWork");
		log.debug(regnId);
		log.debug(workerName);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(registerService.assignWork(workerName, regnId)){
				
				message = "Success";
				resultCode = 1;					
			}
			else
				message = "Some error occured. Try again";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
				
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("assignWork", resultCode, message, "");
	}
	
	@RequestMapping(value = "/getProfile", method = RequestMethod.POST)
	public String getProfile(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("user") String user){
		
		byte resultCode = 0;
		String message = "";
		Hashtable<String, Object> result = null;
		
		//////////////////////
		log.debug("getProfile");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				
				result = loginService.getProfile(user);
				if(result.isEmpty())
					message = "No such user";
				else{

					message = "Success";
					resultCode = 1;
				}
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Hashtable<String, Object>> respObj = 
				new JsonResponseFormatter<Hashtable<String, Object>>();
		return respObj.parseResponse("getProfile", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getAllWorkerDetails", method = RequestMethod.POST)
	public String getAllWorkerDetails(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getAllWorkerDetails");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				
				result = getDetailService.getAllWorkers();
				resultCode = 1;
				message = "Success";
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getAllWorkerDetails", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getAllUserDetails", method = RequestMethod.POST)
	public String getAllUserDetails(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getAllUserDetails");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				
				result = getDetailService.getAllUsers();
				resultCode = 1;
				message = "Success";
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String, Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String, Object>>>();
		return respObj.parseResponse("getAllUserDetails", resultCode, message, result);
	}	
	
	@RequestMapping(value = "/addNewType", method = RequestMethod.POST)
	public String addNewType(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("deptId") short deptId,@RequestParam("typeName") String typeName){
		
		byte resultCode = 0;
		String message = "";
		Short result = null;
		
		//////////////////////
		log.debug("addNewType");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////

		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if((result = putDetailService.insertNewType(typeName, deptId))!=null){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "New type insertion failed";
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Short> respObj = 
				new JsonResponseFormatter<Short>();
		return respObj.parseResponse("addNewType", resultCode, message, result);
	}
	
	@RequestMapping(value = "/addNewDepartment", method = RequestMethod.POST)
	public String addNewDepartment(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("jsonString") String jsonString){
		
		byte resultCode = 0;
		String message = "";
		Short result = null;
		
		//////////////////////
		log.debug("addNewDepartment");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////

		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if((result = putDetailService.insertDepartment(jsonString))!=null){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "New department insertion failed";
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Short> respObj = 
				new JsonResponseFormatter<Short>();
		return respObj.parseResponse("addNewDepartment", resultCode, message, result);
	}
	
	@RequestMapping(value = "/updateDeptName", method = RequestMethod.POST)
	public String updateDeptName(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("deptId") short deptId,@RequestParam("newName") String newName){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("updateDeptName");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////

		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(putDetailService.updateDeptName(deptId, newName)>0){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Failed to update";
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("updateDeptName", resultCode, message, result);
	}
	
	@RequestMapping(value = "/updateTypeName", method = RequestMethod.POST)
	public String updateTypeName(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("typeId") short typeId,@RequestParam("newName") String newName){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("updateTypeName");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////

		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(putDetailService.updateTypeName(typeId, newName)>0){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Failed to update";
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("updateTypeName", resultCode, message, result);
	}
	
	@RequestMapping(value = "/suspendUser", method = RequestMethod.POST)
	public String suspendUser(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("suspendName") String suspendName,
			@RequestParam("suspendFlag") boolean suspendFlag){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("suspendUser");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////

		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(loginService.suspendUser(suspendName, suspendFlag)>0){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Failed to update";
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("suspendUser", resultCode, message, result);
	}
	
	@RequestMapping(value = "/suspendDept", method = RequestMethod.POST)
	public String suspendDept(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("deptId") short deptId,
			@RequestParam("suspendFlag") boolean suspendFlag){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("suspendDept");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////

		try {
			if(!("ROLE_ADMIN".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(putDetailService.suspendDept(deptId, suspendFlag)>0){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Failed to update";
		} catch (Exception e) {

			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("suspendDept", resultCode, message, result);
	}

}
