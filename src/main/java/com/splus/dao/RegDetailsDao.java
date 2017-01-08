package com.splus.dao;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;

import com.splus.pojo.Feedback;
import com.splus.pojo.Rating;
import com.splus.pojo.RatingQuestion;
import com.splus.pojo.Registration;
import com.splus.pojo.RegnHistory;

public interface RegDetailsDao {
	
	long checkRegnId(String regnId);
	String registerNew(Registration newReg);
	List<Registration> getUserRegistration(String userName);
	List<Registration> getAllRegistrations();
	Registration getUserRegistrationFromId(String regId);
	
	Object[] getRegnStatus(Registration regnObj);
	List<RegnHistory> getAllHistory(Registration regnObj);
	Timestamp getStatusTime(String statusname,Registration regn);
	
	int updateAssignedTo(String regnId,String workerName);
	void saveOrUpdateRegnHistory(RegnHistory regnHistory);
	
	List<Registration> getAssignedWorks(String workerName);
	
	int dropWork(String regnId);
	int deleteAllRegnHistory(String regnId);
	
	int updatePayment(String regnId, float cash);
	
	int updateRegistration(String hql,List<Hashtable<String, Object>> paramList);
	void updateRegistration(Registration regnObj);
	
	String insertFeedback(Feedback obj);
	String getFeedback(String regnId);
	String insertRating(Rating obj);
	List<Rating> getRating(String regnId);
	List<RatingQuestion> getRatingQuestions();

}
