package com.splus.service;

import java.util.Hashtable;

public interface LoginService {
	
	Boolean authenticate(String userName, String password);
	Hashtable<String,Object> getLoginResult(String userName,String password,boolean forceLogout);
	void insertProfile(Hashtable<String,Object> result,Object[] userDetail,
			String userName);
	Hashtable<String,Object> getProfile(String userName);
	String getSession(String userName,String token);
	void logout(String userName,String token);
	String changeProfilePic(String userName,String imageString);
	int changePassword(String userName,String password,String newPassword);
	
	String forgotPasswordSendToken(String emailId);
	int deleteExpiredForgot();
	int updateForgottenPassword(String userName, String token, String code, String newPassword);
	
	int updateUserProfile(String userName, String fullName, String address, String email,
			String phoneNo);
	int suspendUser(String userName, boolean flag);
	
}
