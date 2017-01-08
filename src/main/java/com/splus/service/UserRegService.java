package com.splus.service;

public interface UserRegService {
	
	String registerUser(String userName, String password, String fullName,
			String address, String email, String phoneNo);
	boolean confirmReg(String userName,String token,String emailCode, String voucher);
	String confirmCompanyReg(String userName,String token,String emailCode);
	int deleteExpired();
	boolean registerWorker(String companyName, String userName, String password, String fullName,
			String address, String email, String phoneNo, short typeId);
	String checkVoucher(String voucher);

}
