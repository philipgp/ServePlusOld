package com.splus.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.splus.dao.AuthDao;
import com.splus.pojo.ForgotUser;
import com.splus.pojo.Role;
import com.splus.pojo.User;
import com.splus.pojo.UserSession;
import com.splus.pojo.WorkerType;
import com.splus.subservice.GmailRunnable;
import com.splus.subservice.ListenerClass;

public class LoginServiceImp implements LoginService {
	
	private AuthDao authDao;
	private VelocityEngine velocityEngine;

	public void setAuthDao(AuthDao authDao) {
		this.authDao = authDao;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	public Boolean authenticate(String userName, String password) {
		return authDao.authenticate(userName, password);
	}

	@Override
	public Hashtable<String,Object> getLoginResult(String userName, String password,
			boolean forceLogout) {
		
		if(forceLogout)
			authDao.removeActiveLogins(userName);
		
		Hashtable<String,Object> result = new Hashtable<String,Object>();
		
		// COMMENTED FOR DEVELOPMENT //
//		if(authDao.getActiveLogins(userName)>0)
//			return result;
		
		Object[] userDetail = authDao.getUserDetails(userName);		
		insertProfile(result,userDetail,userName);
		
		if("ROLE_WORKER".equals(result.get("roleName"))){
			
			WorkerType workerStatusObj = authDao.getWorkerType(userName);
			result.put("deptName", workerStatusObj.getType().getDept().getDeptName());
			result.put("typeName", workerStatusObj.getType().getTypeName());
		}
		
		//Generating 5 character PK
		String token= null;
		do
			token = ((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		while(authDao.checkToken(token)!=null);
		UserSession sessionObj = new UserSession();
		sessionObj.setToken(token);
		sessionObj.setUser(new User(userName));
		token = authDao.insertSession(sessionObj);
		if(token!=null)
			result.put("token", token);
		
		return result;		
	}

	@Override
	public String getSession(String userName, String token) {
		
		return authDao.checkToken(token, userName);
	}
	
	@Override
	public void insertProfile(Hashtable<String,Object> result,Object[] userDetail,
			String userName) {

		if(userDetail!=null){
			
			result.put("fullName", (userDetail[0]==null)?"":userDetail[0]);
			result.put("address", (userDetail[1]==null)?"":userDetail[1]);
			result.put("email", (userDetail[2]==null)?"":userDetail[2]);
			result.put("phoneNo", (userDetail[3]==null)?"":userDetail[3]);
			result.put("proPic", com.splus.subservice.ListenerClass.getProPicLink(userName));
			String roleName = (userDetail[4]==null) ? "" :  ((Role)userDetail[4]).getRoleName();
			result.put("roleName", roleName);
			result.put("suspendFlag", userDetail[5]);
		}
	}

	@Override
	public void logout(String userName, String token) {
		
		if(authDao.checkToken(token, userName)!=null){
			
			UserSession sessionObj = new UserSession();
			sessionObj.setToken(token);
			authDao.deleteSession(sessionObj);
		}
	}
	
	@Override
	public String changeProfilePic(String userName, String imageUrl) {
		
		boolean flag = true;
		File folder=new File(ListenerClass.getProPicDir());
		if(!folder.exists())
			flag=folder.mkdir();
		if(flag){
			
			InputStream is = null;
			OutputStream os = null;
			try {
				URL url = new URL(imageUrl);
				is = url.openStream();
				os = new FileOutputStream(ListenerClass.getProPicDir()
						+File.separatorChar+userName+".jpg");

				byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1)
					os.write(b, 0, length);
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			}finally{
				try {
					is.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	        if(flag) return ListenerClass.getProPicLink(userName);
	        else return null;
		}
		return null;
	}

	@Override
	public int changePassword(String userName, String password,String newPassword) {
		
		return authDao.updatePassword(userName, password, newPassword);
	}

	@Override
	public String forgotPasswordSendToken(String emailId) {
				
		String userName=null;
		String code = ((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		if(emailId!=null && !emailId.isEmpty())
			userName = authDao.checkUserAccountFromEmail(emailId);
		else
			return null;
		
		//Generating 5 character PK
		String token= null;
		do
			token = ((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		while(authDao.checkForgotToken(token)!=null);
		ForgotUser obj = new ForgotUser();
		obj.setToken(token);
		obj.setUser(new User(userName));
		obj.setCode(code);
		obj.setExpiryDate(new Timestamp((new Date().getTime())+1000*3600));
		authDao.insertForgot(obj);
		
		//SendMail//
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("code", code);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"forgot_code.vm", "UTF-8", model);
		
		GmailRunnable mailObj = new GmailRunnable(
				authDao.getHostProp("MAIL_USER"),emailId,"ServePlus Password Reset",text				
				);
		if(mailObj.setSession(
				authDao.getHostProp("MAIL_SERVER"),	authDao.getHostProp("MAIL_PORT"),
				authDao.getHostProp("MAIL_USER"), authDao.getHostProp("MAIL_PASSWORD"))!=null)
			mailObj.start();
		
		return token;
	}

	@Override
	public int deleteExpiredForgot() {
		
		Timestamp time = new Timestamp(new Date().getTime());
		return authDao.deleteExpiredForgot(time);
	}

	@Override
	public int updateForgottenPassword(String userName, String token, String code, String newPassword) {
		
		if(authDao.checkForgotCode(userName, token, code, new Timestamp(new Date().getTime()))>0){
			
			int rows = authDao.updatePassword(userName, newPassword);
			if(rows>0){
				
				authDao.deleteForgot(token);
				
				//SendMail//
				
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
						"forgot_success.vm", "UTF-8", null);
				
				GmailRunnable mailObj = new GmailRunnable(
						authDao.getHostProp("MAIL_USER"),authDao.getEmailId(userName),"ServePlus Password Reset",text				
						);
				if(mailObj.setSession(
						authDao.getHostProp("MAIL_SERVER"),	authDao.getHostProp("MAIL_PORT"),
						authDao.getHostProp("MAIL_USER"), authDao.getHostProp("MAIL_PASSWORD"))!=null)
					mailObj.start();				
			}
			return rows;
		}
		return 0;
	}

	@Override
	public int updateUserProfile(String userName, String fullName, String address, String email, String phoneNo) {
		
		return authDao.updateUserProfile(userName, fullName, address, email, phoneNo);
	}

	@Override
	public int suspendUser(String userName, boolean flag) {
		
		authDao.removeActiveLogins(userName);
		return authDao.suspendUser(userName, flag);
	}

	@Override
	public Hashtable<String, Object> getProfile(String userName) {		

		Object[] userDetail = authDao.getUserDetails(userName);
		Hashtable<String,Object> result = new Hashtable<String,Object>();		
		insertProfile(result,userDetail,userName);
		return result;
	}

}
