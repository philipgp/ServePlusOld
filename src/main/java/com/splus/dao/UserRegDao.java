package com.splus.dao;

import java.sql.Timestamp;
import java.util.List;

import com.splus.pojo.CompanyVoucher;
import com.splus.pojo.User;
import com.splus.pojo.UserCompany;
import com.splus.pojo.UserRegSession;
import com.splus.pojo.WorkerType;

public interface UserRegDao {	

	String check(String userName, String email, String phNo);
	String checkRegCode(String userName,String token,String emailCode);
	String checkVoucher(String voucher);
	String insertNewUser(User userObj);
	String insertRegSession(UserRegSession regSessionObj);
	String insertCompanyVoucher(CompanyVoucher voucherObj);
	String insertUserCompany(UserCompany obj);
	int deleteRegSession(String userName);
	int deleteUser(String user);
	int updateRole(String userName,String roleName);
	String updateWorkerType(WorkerType obj);
	List<User> getPendingRegistrations();
	int deleteExpiredReg(Timestamp currentTimestamp);
	int deleteExpiredUser(Timestamp currentTimestamp);
	User loadCompanyFromVoucher(String voucher);
	
}
