package com.splus.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.splus.dao.AuthDao;
import com.splus.dao.UserRegDao;
import com.splus.pojo.Type;
import com.splus.pojo.User;
import com.splus.pojo.UserCompany;
import com.splus.pojo.UserRegSession;
import com.splus.pojo.WorkerType;
import com.splus.subservice.GmailRunnable;

public class UserRegServiceImp implements UserRegService {
	
	private AuthDao authDao;
	private UserRegDao userRegDao;
	private VelocityEngine velocityEngine;

	public void setAuthDao(AuthDao authDao) {
		this.authDao = authDao;
	}

	public void setUserRegDao(UserRegDao userRegDao) {
		this.userRegDao = userRegDao;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	public String registerUser(String userName, String password, String fullName, String address,
			String email, String phoneNo) {
			
		if(userRegDao.check(userName, email, phoneNo)!=null)
			return null;

		String token = ((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		String emailCode = ((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		/////////////////////////////
		// SEND SMS AND EMAIL // 
		/////////////////////////////
		User user =new User();
		user.setUserName(userName);
		user.setFullName(fullName);
		user.setEmail(email);
		user.setPhoneNo(phoneNo);
		user.setAddress(address);
		user.setPassword(password);
		user.setSuspendFlag(false);
		userRegDao.insertNewUser(user);
		/////////////////////////////
		UserRegSession obj = new UserRegSession();
		obj.setUser(user);
		obj.setToken(token);
		obj.setEmailCode(emailCode);
		Timestamp time = new Timestamp((new Date().getTime())+1000*3600);
		obj.setExpiryDate(time);
		userRegDao.insertRegSession(obj);
		/////////////////////////////
		
		//SendMail//
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("code", emailCode);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"registration_code.vm", "UTF-8", model);
		
		GmailRunnable mailObj = new GmailRunnable(
				authDao.getHostProp("MAIL_USER"),email,"ServePlus Registration",text				
				);
		if(mailObj.setSession(
				authDao.getHostProp("MAIL_SERVER"),	authDao.getHostProp("MAIL_PORT"),
				authDao.getHostProp("MAIL_USER"), authDao.getHostProp("MAIL_PASSWORD"))!=null)
			mailObj.start();
		
		/*

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
        	
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo("ramanujan.ramu@gmail.com");
                Map<String,Object> model = new HashMap<String,Object>();
                model.put("code", code);
                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                		"registration_code.vm", "UTF-8", model);
                message.setText(text, true);
            }
        };
        try {
			mailSender.send(preparator);
		} catch (MailException e) {
			e.printStackTrace();
		}
		*/
		return token;
	}

	@Override
	public boolean confirmReg(String userName, String token, String emailCode, String voucher) {
		
		if(userRegDao.checkRegCode(userName, token, emailCode)==null)
			return false;
		userRegDao.deleteRegSession(userName);
		userRegDao.updateRole(userName, "ROLE_USER");
		if(voucher!=null) {
			User company = userRegDao.loadCompanyFromVoucher(voucher);
			if(company!=null){
				UserCompany obj = new UserCompany(new User(userName),company);
				userRegDao.insertUserCompany(obj);
			}
		}
		
		//////////////////
		//SendMail//
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"registration_completed.vm", "UTF-8", null);
		
		GmailRunnable mailObj = new GmailRunnable(
				authDao.getHostProp("MAIL_USER"),authDao.getEmailId(userName),"ServePlus Registration",text				
				);
		if(mailObj.setSession(
				authDao.getHostProp("MAIL_SERVER"),	authDao.getHostProp("MAIL_PORT"),
				authDao.getHostProp("MAIL_USER"), authDao.getHostProp("MAIL_PASSWORD"))!=null)
			mailObj.start();
		
		return true;
	}
	
	@Override
	public String confirmCompanyReg(String userName, String token, String emailCode) {
		
		if(userRegDao.checkRegCode(userName, token, emailCode)==null)
			return null;
		userRegDao.deleteRegSession(userName);		
		userRegDao.updateRole(userName, "ROLE_COMPANY");
		String voucher= null;
		do
			voucher = ((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		while(userRegDao.checkVoucher(voucher)!=null);
		
		//////////////////
		//SendMail//
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"registration_completed.vm", "UTF-8", null);
		
		GmailRunnable mailObj = new GmailRunnable(
				authDao.getHostProp("MAIL_USER"),authDao.getEmailId(userName),"ServePlus Registration",text				
				);
		if(mailObj.setSession(
				authDao.getHostProp("MAIL_SERVER"),	authDao.getHostProp("MAIL_PORT"),
				authDao.getHostProp("MAIL_USER"), authDao.getHostProp("MAIL_PASSWORD"))!=null)
			mailObj.start();
		
		return voucher;
	}
	
	@Override
	public int deleteExpired() {
		
		// Clear all expired registrations
		Timestamp time = new Timestamp(new Date().getTime());
		if(userRegDao.deleteExpiredUser(time)>0)
			return userRegDao.deleteExpiredReg(time);
		return 0;
	}

	@Override
	public boolean registerWorker(String companyName, String userName, String password, String fullName,
			String address, String email, String phoneNo,short typeId) {
		
		if(userRegDao.check(userName, email, phoneNo)!=null)
			return false;
		User user =new User();
		user.setUserName(userName);
		user.setFullName(fullName);
		user.setEmail(email);
		user.setPhoneNo(phoneNo);
		user.setAddress(address);
		user.setPassword(password);
		user.setSuspendFlag(false);
		WorkerType statusObj = new WorkerType();
		statusObj.setUser(user);
		statusObj.setType(new Type(typeId));
		statusObj.setCompany(new User(companyName));
		if(userRegDao.insertNewUser(user)!=null)
			if(userRegDao.updateRole(userName, "ROLE_WORKER")>0)
				if(userRegDao.updateWorkerType(statusObj)!=null)
				return true;
		return false;
	}

	@Override
	public String checkVoucher(String voucher) {
		return userRegDao.checkVoucher(voucher);
	}

}
