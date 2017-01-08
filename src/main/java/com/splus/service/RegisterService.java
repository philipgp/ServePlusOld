package com.splus.service;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

public interface RegisterService {
	
	Hashtable<String, String> registerNewComplaint(String userName,short typeId,
			String comment,String time,Float latitude,Float longitude,String location,
			String localAddress,
			MultipartFile file) throws IllegalStateException, IOException;
	boolean updateComplaint(String userName, String regnId, String comment, String time,
			Float latitude, Float longitude, String location, String localAddress, MultipartFile file,
			boolean deleteImageFlag)
					throws IllegalStateException, IOException;
	boolean assignWork(String userName,String regnId);
	boolean dropWork(String userName,String regnId);
	boolean changeWorkStatusByWorker(String regnId,String userName,
			String statusName,Object extraInfo);
	boolean insertFeedback(String userName, String regnId, String jsonString) throws JSONException;
	
	void setReadFlag(List<String> notId);

}
