package com.splus.controller;

import java.util.Hashtable;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.splus.service.LoginService;
import com.splus.subservice.JsonResponseFormatter;

@RestController
public class LoginController {
	
	static Logger log = Logger.getLogger(LoginController.class.getName());
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("password") String password,
			@RequestParam("forceLogout") boolean forceLogout){
		
		byte resultCode = 0;
		String message;
		Hashtable<String, Object> result = null;
		
		//////////////////////
		log.debug("login");
		log.debug(userName);
		log.debug(password);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			Boolean suspendFlag = loginService.authenticate(userName, password);
			if(suspendFlag==null)
				message = "Username or password is wrong";
			else if(suspendFlag==true)
				message = "Your account has been suspended. Please contact the administrator.";
			else{
				result = loginService.getLoginResult(userName, password,forceLogout);
				if(result.isEmpty()){
					
					resultCode = 3;
					message = "Active login";
				}
				else if(result.get("roleName")=="")
					message = "Your registration not yet completed";
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
		JsonResponseFormatter<Hashtable<String,Object>> respObj = 
				new JsonResponseFormatter<Hashtable<String,Object>>();
		return respObj.parseResponse("login", resultCode, message, result);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token){

		byte resultCode = 0;
		String message;
		//////////////////////
		log.debug("logout");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////		
		
		//////////// CLEAR SESSION TABLE  ////////////
		try {
			loginService.logout(userName, token);
			sessionObj.invalidate();
			resultCode = 1;
			message = "Success";
		} catch (Exception e) {
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}		
		JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
		return respObj.parseResponse("logout", resultCode, message, "");
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("password") String password,@RequestParam("newPassword") String newPassword){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("changePassword");
		log.debug(userName);
		log.debug(token);
		log.debug(password);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		
		try {
			if(loginService.getSession(userName, token)==null)
				message = "Invalid session. Login again";
			else if(loginService.changePassword(userName, password, newPassword)==0)
				message = "Invalid current password";
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
		return respObj.parseResponse("changePassword", resultCode, message, result);
	}
	
	@RequestMapping(value = "/forgotPassword1", method = RequestMethod.POST)
	public String forgotPassword1(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("emailId") String emailId){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("forgotPassword1");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			loginService.deleteExpiredForgot();
			String token = loginService.forgotPasswordSendToken(emailId);
			if(token==null)
				message = "No such user account";
			else{
				
				result = token;
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
		return respObj.parseResponse("forgotPassword1", resultCode, message, result);
	}
	
	@RequestMapping(value = "/forgotPassword2", method = RequestMethod.POST)
	public String forgotPassword1(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName, @RequestParam("token") String token,
			@RequestParam("code") String code, @RequestParam("newPassword") String newPassword){
		
		byte resultCode = 0;
		String message = "";
		String result = "";
		
		//////////////////////
		log.debug("forgotPassword2");
		log.debug(userName);
		log.debug(token);
		log.debug(code);
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			loginService.deleteExpiredForgot();
			if(loginService.updateForgottenPassword(userName, token, code, newPassword)>0){
				
				resultCode = 1;
				message = "Success";
			}
			else
				message = "Invalid code";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
				
		JsonResponseFormatter<String> respObj = 
				new JsonResponseFormatter<String>();
		return respObj.parseResponse("forgotPassword2", resultCode, message, result);
	}

}
