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
public class NotificationController {
	
	static Logger log = Logger.getLogger(LoginController.class.getName());
	
	@Autowired
	private GetDetails getDetailService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping(value = "/getNotification", method = RequestMethod.POST)
	public String getNotification(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("notId") String notId){
		
		byte resultCode = 0;
		String message;
		List<Hashtable<String, Object>> result = null;
		
		//////////////////////
		log.debug("getNotification");
		log.debug(userName);
		log.debug(notId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else{
				result = getDetailService.getLatestNotifications(userName, notId);
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
		return respObj.parseResponse("getNotification", resultCode, message, result);
	}
	
	@RequestMapping(value = "/setReadFlag", method = RequestMethod.POST)
	public String setReadFlag(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("notIdList") List<String> notIdList){
		
		byte resultCode = 0;
		String message;
		String result = null;
		
		//////////////////////
		log.debug("setReadFlag");
		log.debug(userName);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else{
				registerService.setReadFlag(notIdList);
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
		return respObj.parseResponse("setReadFlag", resultCode, message, result);
	}

}
