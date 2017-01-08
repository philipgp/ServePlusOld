package com.splus.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.splus.service.LoginService;
import com.splus.service.PutDetails;
import com.splus.service.UserRegService;
import com.splus.subservice.JsonResponseFormatter;

@RestController
public class CompanyController {
	
	static Logger log = Logger.getLogger(AdminController.class.getName());

	@Autowired
	private LoginService loginService;
	@Autowired
	private UserRegService userRegService;
	@Autowired
	private PutDetails putDetailService;
	
	@RequestMapping(value = "/registerWorker", method = RequestMethod.POST)
	public String registerWorker(HttpSession sessionObj,HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("workerName") String workerName,@RequestParam("password") String password,
			@RequestParam("fullName") String fullName,@RequestParam("address") String address,
			@RequestParam("email") String email,@RequestParam("phoneNo") String phoneNo,
			@RequestParam("typeId") short typeId){

		byte resultCode = 0;
		String message;
		String result = "";
		//////////////////////
		log.debug("registerWorker");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			if(!("ROLE_COMPANY".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			
			else if(workerName==null || workerName.isEmpty()
					|| password==null || password.isEmpty()
					|| fullName==null || fullName.isEmpty()
					|| address==null  || address.isEmpty()
					|| email==null    || email.isEmpty()
					|| phoneNo==null  || phoneNo.isEmpty())
				message = "Empty fields found";
			
			else if(userRegService.registerWorker(userName, workerName, password, fullName,
					address, email, phoneNo,typeId)){

				resultCode = 1;
				message = "Success";
			}		
			else
				message = "Username, email or phoneNo already exists";
		} catch (Exception e) {
			
			resultCode = 2;
			message = "Server Exception";
			e.printStackTrace();
		}
		
		JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
		return respObj.parseResponse("registerWorker", resultCode, message, result);
	}
	
	@RequestMapping(value = "/addCompanyType", method = RequestMethod.POST)
	public String addCompanyType(HttpServletResponse response,
			@RequestParam("userName") String userName,@RequestParam("token") String token,
			@RequestParam("typeId") short typeId){

		byte resultCode = 0;
		String message;
		String result = "";
		//////////////////////
		log.debug("addCompanyType");
		response.addHeader("Access-Control-Allow-Origin", "*");
		/////////////////////
		try {
			if(!("ROLE_COMPANY".equals(loginService.getSession(userName, token))))
				message = "Invalid session. Login again";
			else {
				Integer id = putDetailService.insertCompanyType(userName, typeId);
				if(id==null || id==0)
					message = "Some error occured";
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
		
		JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
		return respObj.parseResponse("addCompanyType", resultCode, message, result);
	}

}
