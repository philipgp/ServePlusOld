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
import org.springframework.web.multipart.MultipartFile;

import com.splus.service.GetDetails;
import com.splus.service.LoginService;
import com.splus.service.RegisterService;
import com.splus.subservice.JsonResponseFormatter;

@RestController
public class UserController {	

	static Logger log = Logger.getLogger(UserController.class.getName());
	
	@Autowired
	private GetDetails getDetailService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping(value = "/getDepartments", method = RequestMethod.POST)
	public String getDepartments(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getDepartments");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getAllDepartments();
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
		return respObj.parseResponse("getDepartments", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getTypes", method = RequestMethod.POST)
	public String getTypes(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("deptId") byte deptId){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getTypes");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getAllTypes(deptId);
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
		return respObj.parseResponse("getTypes", resultCode, message, result);
	}
	
	@RequestMapping(value = "/changeProPic", method = RequestMethod.POST)
	public String changeProPic(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("url") String imageUrl){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("changeProPic");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else{
				result = loginService.changeProfilePic(userName, imageUrl);
				if(result==null)
					message = "Some error occured. Try again.";
				else{
					resultCode = 1;
					message = "Success";
				}
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("changeProPic", resultCode, message, result);
	}
	
	@RequestMapping(value = "/changeProfile", method = RequestMethod.POST)
	public String changeProPic(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("fullName") String fullName,@RequestParam("address") String address,
			@RequestParam("email") String email,@RequestParam("phoneNo") String phoneNo){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("changeProfile");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else if(fullName==null || fullName.isEmpty()
					|| address==null  || address.isEmpty()
					|| email==null    || email.isEmpty()
					|| phoneNo==null  || phoneNo.isEmpty())
				message = "Empty fields found";
			else if(loginService.updateUserProfile(userName, fullName, address, email, phoneNo)==0)
				message = "Some error occured";
			else{
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("changeProfile", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getTypeName", method = RequestMethod.POST)
	public String getTypeName(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("typeId") short typeId){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("getTypeName");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else if((result=getDetailService.getTypeName(typeId))==null)
				message="No such type";
			else{
				message="Success";
				resultCode=1;
			}
			
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("getTypeName", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getDeptName", method = RequestMethod.POST)
	public String changeProPic(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("deptId") short deptId){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("getDeptName");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else if((result=getDetailService.getDeptName(deptId))==null)
				message = "No such department";
			else{
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("getDeptName", resultCode, message, result);
	}
	
	@RequestMapping(value = "/registerComplaint", method = RequestMethod.POST)
	public String registerComplaint(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("typeId") short typeId,
			@RequestParam("comment") String comment,
			@RequestParam("time") String time,
			@RequestParam("latitude") Float latitude,
			@RequestParam("longitude") Float longitude,
			@RequestParam("location") String location,
			@RequestParam("localAddress") String localAddress,
			@RequestParam("image") MultipartFile file){
		
		byte resultCode = 0;
		String message = "";
		Hashtable<String, String> result = null;
		
		//////////////////////
		log.debug("registerComplaint");
		log.debug(file.isEmpty());
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_USER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if((result = registerService.registerNewComplaint(userName, typeId,
					comment,time, latitude, longitude,location,
					localAddress,file))!=null){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Complaint registration failed";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Hashtable<String, String>> respObj = 
				new JsonResponseFormatter<Hashtable<String, String>>();
		return respObj.parseResponse("registerComplaint", resultCode, message, result);
	}
	
	@RequestMapping(value = "/registerComplaint2", method = RequestMethod.POST)
	public String registerComplaint2(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("typeId") short typeId,
			@RequestParam("comment") String comment,
			@RequestParam("time") String time,
			@RequestParam("latitude") Float latitude,
			@RequestParam("longitude") Float longitude,
			@RequestParam("location") String location,
			@RequestParam("localAddress") String localAddress){
		
		byte resultCode = 0;
		String message = "";
		Hashtable<String, String> result = null;
		
		//////////////////////
		log.debug("registerComplaint2");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_USER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if((result = registerService.registerNewComplaint(userName, typeId,
					comment,time, latitude, longitude,location,
					localAddress,null))!=null){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Complaint registration failed";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Hashtable<String, String>> respObj = 
				new JsonResponseFormatter<Hashtable<String, String>>();
		return respObj.parseResponse("registerComplaint2", resultCode, message, result);
	}
	
	// Response with all registrations of requested user
	@RequestMapping(value = "/getUserRegistrations", method = RequestMethod.POST)
	public String getUserRegistrations(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getUserRegistrations");
		log.debug(userName);
		log.debug(token);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_USER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getAllUserRegistrations(userName,false);
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
		return respObj.parseResponse("getUserRegistrations", resultCode, message, result);
	}
	
	
	// NEED TO CHECK IF ADMIN OR SAME USER WHO REGISTERED. THEN ONLY ALLOW THIS  //
	// ABOVE THING CHECKED IN SERVICE
	
	// Response with status of requested registration id
	@RequestMapping(value = "/getRegistrationDetail", method = RequestMethod.POST)
	public String getRegistrationDetail(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		Hashtable<String, Object> result = null;
		
		//////////////////////
		log.debug("getRegistrationDetail");
		log.debug(regnId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			String roleName = loginService.getSession(userName, token);
			if(roleName==null)
				message = "Invalid session. Login again";
			else{

				if(roleName.equals("ROLE_ADMIN"))
					result = getDetailService.getUserRegistrationDetail(regnId,null);
				else if(roleName.equals("ROLE_USER"))
					result = getDetailService.getUserRegistrationDetail(regnId, userName);
				
				if((result==null))
					message = "No such registration found.. Try again with a valid ref. number..";
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
		return respObj.parseResponse("getRegistrationDetail", resultCode, message, result);
	}
	
	@RequestMapping(value = "/dropWork", method = RequestMethod.POST)
	public String dropWork(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("dropWork");
		log.debug(regnId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			String roleName = loginService.getSession(userName, token);
			if(roleName==null)
				message = "Invalid session. Login again";
			else{
				boolean flag = false;
				if(roleName.equals("ROLE_ADMIN"))
					flag = registerService.dropWork(null, regnId);
				else if(roleName.equals("ROLE_USER"))
					flag = registerService.dropWork(userName, regnId);
				if(flag){
					resultCode = 1;
					message = "Success";
				}
				else
					message = "Can't update. Work already started";
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("dropWork", resultCode, message, result);
	}
	
	@RequestMapping(value = "/updateComplaint", method = RequestMethod.POST)
	public String updateComplaint(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId,
			@RequestParam("comment") String comment,
			@RequestParam("time") String time,
			@RequestParam("latitude") Float latitude,
			@RequestParam("longitude") Float longitude,
			@RequestParam("location") String location,
			@RequestParam("localAddress") String localAddress,
			@RequestParam("image") MultipartFile file){
		
		byte resultCode = 0;
		String message = "";
		String result = null;
		
		//////////////////////
		log.debug("updateComplaint");
		log.debug(regnId);
		log.debug(comment);
		log.debug(time);
		log.debug(latitude);
		log.debug(longitude);
		log.debug(location);
		log.debug(localAddress);
		log.debug(file.isEmpty());
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_USER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if (registerService.updateComplaint(userName,regnId,comment,time,
						latitude, longitude,location,localAddress,file,false)){
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Work already started. Failed to update.";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("updateComplaint", resultCode, message, result);
	}
	
	@RequestMapping(value = "/updateComplaint2", method = RequestMethod.POST)
	public String updateComplaint2(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId,
			@RequestParam("comment") String comment,
			@RequestParam("time") String time,
			@RequestParam("latitude") Float latitude,
			@RequestParam("longitude") Float longitude,
			@RequestParam("location") String location,
			@RequestParam("localAddress") String localAddress,
			@RequestParam("deleteImageFlag") boolean deleteImageFlag){
		
		byte resultCode = 0;
		String message = "";
		String result = null;
		
		//////////////////////
		log.debug("updateComplaint2");
		log.debug(regnId);
		log.debug(comment);
		log.debug(time);
		log.debug(latitude);
		log.debug(longitude);
		log.debug(location);
		log.debug(localAddress);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_USER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if (registerService.updateComplaint(userName,regnId,comment,time,
						latitude, longitude,location,localAddress,null,deleteImageFlag)){
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Work already started. Failed to update.";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("updateComplaint2", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getRatingQuestions", method = RequestMethod.POST)
	public String getRatingQuestions(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String,Object>> result = null;
		
		//////////////////////
		log.debug("getRatingQuestions");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			String roleName = loginService.getSession(userName, token);
			if( ! ("ROLE_USER".equals(roleName) || "ROLE_ADMIN".equals(roleName)))
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getRatingQuestions();
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<List<Hashtable<String,Object>>> respObj = 
				new JsonResponseFormatter<List<Hashtable<String,Object>>>();
		return respObj.parseResponse("getRatingQuestions", resultCode, message, result);
	}
	
	@RequestMapping(value = "/setFeedback", method = RequestMethod.POST)
	public String setFeedback(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId,
			@RequestParam("feedbackJson") String feedbackJson){
		
		byte resultCode = 0;
		String message = "";
		String result = null;
		
		//////////////////////
		log.debug("setFeedback");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			String roleName = loginService.getSession(userName, token);
			if( ! "ROLE_USER".equals(roleName))
				message = "Invalid session. Login again";
			else if (registerService.insertFeedback(userName, regnId, feedbackJson)){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "Failed to update.";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("setFeedback", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getFeedback", method = RequestMethod.POST)
	public String getFeedback(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		Hashtable<String,Object> result = null;
		
		//////////////////////
		log.debug("getFeedback");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			String roleName = loginService.getSession(userName, token);
			if( ! ("ROLE_USER".equals(roleName) || "ROLE_ADMIN".equals(roleName)))
				message = "Invalid session. Login again";
			else if ((result=getDetailService.getFeedback(userName, regnId, roleName))!=null){
				
				message = "Success";
				resultCode = 1;
			}
			else
				message = "No feedback found";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Hashtable<String,Object>> respObj = 
				new JsonResponseFormatter<Hashtable<String,Object>>();
		return respObj.parseResponse("getFeedback", resultCode, message, result);
	}

}
