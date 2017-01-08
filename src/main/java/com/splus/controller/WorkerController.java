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
import com.splus.service.RegisterService;
import com.splus.subservice.JsonResponseFormatter;

@RestController
public class WorkerController {

	static Logger log = Logger.getLogger(WorkerController.class.getName());
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private GetDetails getDetailService;
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping(value = "/getAssignedWorks", method = RequestMethod.POST)
	public String getAssignedWorks(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){
		
		byte resultCode = 0;
		String message = "";
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getAssignedWorks");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_WORKER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else{
				
				result = getDetailService.getAssignedWorks(userName);
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
		return respObj.parseResponse("getAssignedWorks", resultCode, message, result);
	}
	
	@RequestMapping(value = "/startWork", method = RequestMethod.POST)
	public String startWork(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		String result = null;
		
		//////////////////////
		log.debug("startWork");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_WORKER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(registerService.changeWorkStatusByWorker(regnId, userName, "STARTED", null)){
				
				resultCode = 1;
				message = "Success";
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
		return respObj.parseResponse("startWork", resultCode, message, result);
	}
	
	@RequestMapping(value = "/workCompleted", method = RequestMethod.POST)
	public String workCompleted(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		String result = null;
		
		//////////////////////
		log.debug("workCompleted");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_WORKER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(registerService.changeWorkStatusByWorker(regnId, userName, "COMPLETED", null)){
				
				resultCode = 1;
				message = "Success";
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
		return respObj.parseResponse("workCompleted", resultCode, message, result);
	}
	
	@RequestMapping(value = "/workClosed", method = RequestMethod.POST)
	public String workCompleted(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId,@RequestParam("payment") Float payment){
		
		byte resultCode = 0;
		String message = "";
		String result = null;
		
		//////////////////////
		log.debug("workClosed");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(!("ROLE_WORKER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if(registerService.changeWorkStatusByWorker(regnId, userName, "CLOSED", payment)){
				
				resultCode = 1;
				message = "Success";
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
		return respObj.parseResponse("workClosed", resultCode, message, result);
	}
	
	@RequestMapping(value = "/getRegistrationDetailForWorker", method = RequestMethod.POST)
	public String getRegistrationDetailForWorker(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("regnId") String regnId){
		
		byte resultCode = 0;
		String message = "";
		Hashtable<String, Object> result = null;
		
		//////////////////////
		log.debug("getRegistrationDetailForWorker");
		log.debug(regnId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			if(!("ROLE_WORKER".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else if((result=getDetailService.getAssignedWork(userName, regnId))==null)
				message = "No such registration";
			else{
				message = "Success";
				resultCode = 1;
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<Hashtable<String, Object>> respObj = 
				new JsonResponseFormatter<Hashtable<String, Object>>();
		return respObj.parseResponse("getRegistrationDetailForWorker", resultCode, message, result);
	}

}
