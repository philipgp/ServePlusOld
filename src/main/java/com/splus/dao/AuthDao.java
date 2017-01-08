package com.splus.dao;

import java.sql.Timestamp;

import com.splus.pojo.ForgotUser;
import com.splus.pojo.UserSession;
import com.splus.pojo.WorkerType;

public interface AuthDao {
	
	Boolean authenticate(String userName,String password);
	Object[] getUserDetails(String userName);
	String checkToken(String token);
	String checkToken(String token,String userName);
	long getActiveLogins(String userName);
	int removeActiveLogins(String userName);
	String insertSession(UserSession sessionObj);
	void deleteSession(UserSession sessionObj);
	Object getFullName(String userName);
	Object getRoleName(String userName);
	String getEmailId(String userName);
	int updatePassword(String userName,String password,String newPassword);
	int updatePassword(String userName,String newPassword);
	
	String checkForgotToken(String token);
	String insertForgot(ForgotUser obj);
	String checkUserAccountFromEmail(String emailId);
	String checkUserAccountFromPhone(String phoneNo);
	int updatePassword(String userName, String newPassword, String token,
			String code, Timestamp date);
	long checkForgotCode(String userName, String token, String code, Timestamp date);
	int deleteExpiredForgot(Timestamp currentTimestamp);
	int deleteForgot(String token);
	
	int updateUserProfile(String userName, String fullName, String address, String email,
			String phoneNo);
	
	int suspendUser(String userName, boolean flag);	

	WorkerType getWorkerType(String userName);

	String getHostProp(String key);

}
