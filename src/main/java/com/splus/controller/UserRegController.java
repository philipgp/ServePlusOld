package com.splus.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.splus.service.UserRegService;
import com.splus.subservice.JsonResponseFormatter;

@RestController
public class UserRegController {
	
	static Logger log = Logger.getLogger(UserRegController.class.getName());
	
	@Autowired
	private UserRegService userRegService;
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String registerUser(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("password") String password,
			@RequestParam("fullName") String fullName,@RequestParam("address") String address,
			@RequestParam("email") String email,@RequestParam("phoneNo") String phoneNo){

		byte resultCode = 0;
		String message;
		String result = "";
		//////////////////////
		log.debug("registerUser");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			//////////////////////////////
			userRegService.deleteExpired();
			//////////////////////////////
			if(userName==null || userName.isEmpty()
					|| password==null || password.isEmpty()
					|| fullName==null || fullName.isEmpty()
					|| address==null  || address.isEmpty()
					|| email==null    || email.isEmpty()
					|| phoneNo==null  || phoneNo.isEmpty())
				message = "Empty fields found";
			
			else if( (result = userRegService.registerUser(userName, password, fullName,
					address, email, phoneNo))==null)
				message = "Username, email or phoneNo already exists";
			
			else{
				resultCode = 1;
				message = "Success";
			}
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}		
		JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
		return respObj.parseResponse("registerUser", resultCode, message, result);
	}
	
	@RequestMapping(value = "/confirmUserReg", method = RequestMethod.POST)
	public String confirmUserReg(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("emailCode") String emailCode,@RequestParam("voucher") String voucher){

		byte resultCode = 0;
		String message;
		String result = "";
		//////////////////////
		log.debug("confirmUserReg");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			///////////////////////////////
			userRegService.deleteExpired();
			///////////////////////////////
			boolean flag = true;
			if(voucher!=null && !voucher.isEmpty()){
				if(userRegService.checkVoucher(voucher)==null)
					flag = false;
			}
			else
				voucher = null;
			if(!flag)
				message = "No such voucher";
			else if(userRegService.confirmReg(userName, token, emailCode, voucher)){
				
				resultCode = 1;
				message = "Success";
			}
			else
				message = "Invalid email or sms code";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
		return respObj.parseResponse("confirmUserReg", resultCode, message, result);
	}
	
	@RequestMapping(value = "/confirmCompanyReg", method = RequestMethod.POST)
	public String confirmCompanyReg(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("emailCode") String emailCode){

		byte resultCode = 0;
		String message;
		String result = "";
		//////////////////////
		log.debug("confirmCompanyReg");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			///////////////////////////////
			userRegService.deleteExpired();
			///////////////////////////////
			String voucher = userRegService.confirmCompanyReg(userName, token, emailCode);
			if(voucher!=null){
				
				resultCode = 1;
				message = "Success";
				result = voucher;
			}
			else
				message = "Invalid email or sms code";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
		return respObj.parseResponse("confirmCompanyReg", resultCode, message, result);
	}

}
