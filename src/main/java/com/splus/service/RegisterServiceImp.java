package com.splus.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.splus.dao.AuthDao;
import com.splus.dao.NotificationDao;
import com.splus.dao.RegDetailsDao;
import com.splus.dao.ServiceDetailDao;
import com.splus.pojo.Feedback;
import com.splus.pojo.Notification;
import com.splus.pojo.NotificationKey;
import com.splus.pojo.Rating;
import com.splus.pojo.RatingQuestion;
import com.splus.pojo.Registration;
import com.splus.pojo.RegnHistory;
import com.splus.pojo.RegnStatus;
import com.splus.pojo.Type;
import com.splus.pojo.User;
import com.splus.subservice.JsonResponseFormatter;
import com.splus.subservice.ListenerClass;
import com.splus.websocket.WebSocketService;

public class RegisterServiceImp implements RegisterService {

	private RegDetailsDao regnDao;
	private WebSocketService wsService;
	private ServiceDetailDao serviceDao;
	private AuthDao authDao;
	private NotificationDao notDao;

	public void setRegnDao(RegDetailsDao regnDao) {
		this.regnDao = regnDao;
	}

	public void setWsService(WebSocketService wsService) {
		this.wsService = wsService;
	}

	public void setServiceDao(ServiceDetailDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public void setAuthDao(AuthDao authDao) {
		this.authDao = authDao;
	}

	public void setNotDao(NotificationDao notDao) {
		this.notDao = notDao;
	}

	@Override
	public Hashtable<String, String> registerNewComplaint(String userName, short typeId, String comment, String time,
			Float latitude, Float longitude, String location, String localAddress, MultipartFile file)
					throws IllegalStateException, IOException {
		
		
		if(serviceDao.getTypeNameWithoutSuspend(typeId)==null)
			return null;  //Checking suspended

		//Generating 5 character PK
		String pk= null;
		do
			pk = "SP"+((Long)(Math.round(Math.random()* 89999) + 10000)).toString();
		while(regnDao.checkRegnId(pk)>0);
		
		/////////////////////
		if(file!=null && !file.isEmpty()){

			File folder = new File(ListenerClass.getImageDir());
			if(!folder.exists())
				folder.mkdir();
			file.transferTo(new File(ListenerClass.getImageDir()+File.separatorChar+pk+".jpg"));
		}
		/////////////////////
		
		Registration newRegn = new Registration();
		User user = new User(userName);
		newRegn.setUser(user);
		newRegn.setType(new Type(typeId));
		Timestamp date = new Timestamp(new Date().getTime());
		newRegn.setTimestamp(date);
		newRegn.setUserComment(comment);
		newRegn.setPreferedTime(formatDate(time));
		newRegn.setLatitude(latitude);
		newRegn.setLongitude(longitude);
		newRegn.setLocation(location);
		newRegn.setLocalAddress(localAddress);
				
		newRegn.setRegnId(pk);		
		String regnId = regnDao.registerNew(newRegn);
		
		
		// NOTIFICATION
		
		Hashtable<String,Object> notData = new Hashtable<String,Object>();
		notData.put("userName", userName);
		notData.put("fullName", checkNull(authDao.getFullName(userName)));
		notData.put("regnId", pk);
		notData.put("time", formatDate(date));
		notData.put("comment", comment);
		notData.put("proPic", ListenerClass.getProPicLink(userName));
		
		JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
				new JsonResponseFormatter<Hashtable<String,Object>>();
		String wsMsg = respObj.parseNotificationMessage("REGISTERED", notData);
		
		Notification notObj = new Notification();
		notObj.setNotifJson(wsMsg);
		notObj.setNotifKey(notDao.getNotificationKey("REGISTERED"));
		notObj.setReadFlag(false);
		String adminName = serviceDao.getAdminUserName();
		notObj.setUser(new User(adminName));
		
		long millis = new Date().getTime();
		Timestamp notTime = new Timestamp(millis);
		String notId = userName+adminName+millis; // GENERATED COMBINING requestedUser+toUser+time
		notObj.setNotifId(notId);
		notObj.setTimestamp(notTime);
		
		if(notDao.insertNotification(notObj)!=null){
			
			///// SEND WEBSOCKET /////
			List<String> tokenList = serviceDao.getAdminTokens();
			if(!tokenList.isEmpty())
				wsService.sendTextMessage(tokenList, wsMsg);
			//////////////////////////			
		}
				
		
		Hashtable<String, String> result = new Hashtable<String, String>();
		result.put("regnId", regnId);
		result.put("date", formatDate(date));
		return result;
	}

	@Override
	public boolean updateComplaint(String userName, String regnId, String comment, String time,
			Float latitude, Float longitude, String location, String localAddress, MultipartFile file,
			boolean deleteImageFlag)
					throws IllegalStateException, IOException {		
		
		Registration regnObj = regnDao.getUserRegistrationFromId(regnId);
		if(regnObj==null)
			return false;
		if( ! regnObj.getUser().getUserName().equals(userName))
			return false;
		if(regnDao.getStatusTime("STARTED", regnObj)!=null)
			return false;

		/////////////////////
		if(file!=null && !file.isEmpty()){

			File folder = new File(ListenerClass.getImageDir());
			if(!folder.exists())
				folder.mkdir();
			file.transferTo(new File(ListenerClass.getImageDir()+File.separatorChar+regnId+".jpg"));
		}
		else if(deleteImageFlag){
			File fileToDelete = new File(ListenerClass.getImageDir()+File.separatorChar+regnId+".jpg");
			fileToDelete.delete();
		}
		/////////////////////
		
		/*
		
		String hql = "UPDATE Registration SET";
		String comma = "";
		ArrayList<Hashtable<String, Object>> paramList = new ArrayList<Hashtable<String, Object>>();
		if(comment!=null){
			hql += " userComment = :comment";
			Hashtable<String, Object> param = new Hashtable<String, Object>();
			param.put("key", "comment");
			param.put("value", comment);
			paramList.add(param);
			comma = ",";
		}
		if(time!=null && !time.isEmpty()){
			hql += comma+" preferedTime = :time";
			Hashtable<String, Object> param = new Hashtable<String, Object>();
			param.put("key", "time");
			param.put("value", formatDate(time));
			paramList.add(param);
			comma = ",";
		}
		if(latitude!=null){
			hql += comma+" latitude = :latitude";
			Hashtable<String, Object> param = new Hashtable<String, Object>();
			param.put("key", "latitude");
			param.put("value", latitude);
			paramList.add(param);
			comma = ",";
		}
		if(longitude!=null){
			hql += comma+" longitude = :longitude";
			Hashtable<String, Object> param = new Hashtable<String, Object>();
			param.put("key", "longitude");
			param.put("value", longitude);
			paramList.add(param);
			comma = ",";
		}
		if(location!=null){
			hql += comma+" location = :location";
			Hashtable<String, Object> param = new Hashtable<String, Object>();
			param.put("key", "location");
			param.put("value", location);
			paramList.add(param);
			comma = ",";
		}
		if(localAddress!=null){
			hql += comma+" localAddress = :localAddress";
			Hashtable<String, Object> param = new Hashtable<String, Object>();
			param.put("key", "localAddress");
			param.put("value", localAddress);
			paramList.add(param);
			comma = ",";
		}
		
		hql += " WHERE regnId = :regnId";
		Hashtable<String, Object> paramFinal = new Hashtable<String, Object>();
		paramFinal.put("key", "regnId");
		paramFinal.put("value", regnId);
		paramList.add(paramFinal);
		
		////////////////////////////////////
		//SEND NOTIFICATIONS TO ADMIN AND TECHNICIAN IF ASSIGNED		
		///////////////////////////////////
		if(",".equals(comma))
			if(regnDao.updateRegistration(hql, paramList)>0){
				
				
				// NOTIFICATION
				Hashtable<String,Object> notData = new Hashtable<String,Object>();				

				notData.put("userName", userName);
				notData.put("fullName", checkNull(authDao.getFullName(userName)));
				notData.put("regnId", regnId);
				notData.put("proPic", ListenerClass.getProPicLink(userName));
				
				JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
						new JsonResponseFormatter<Hashtable<String,Object>>();
				String wsMsg = respObj.parseNotificationMessage("UPDATED", notData);
				
				// To WorkeR IF ASSIGNED
				String id="dummy";
				User worker = regnObj.getAssignedTo();
				String workerName = null;
				NotificationKey notKey = notDao.getNotificationKey("UPDATED");
				if(worker!=null){
					
					id=null;
					workerName = worker.getUserName();
					Notification notObj = new Notification();
					notObj.setNotifJson(wsMsg);
					notObj.setNotifKey(notKey);
					notObj.setReadFlag(false);
					notObj.setUser(worker); // Set whose notification
					long millis = new Date().getTime();
					Timestamp notTime = new Timestamp(millis);
					String notId = userName+workerName+millis; // GENERATED COMBINING requestedUser+toUser+time
					notObj.setNotifId(notId);
					notObj.setTimestamp(notTime);
					
					id=notDao.insertNotification(notObj);
				}				
				
				if(id!=null){
					
					//To admin
					Notification notObj2 = new Notification();
					notObj2.setNotifJson(wsMsg);
					notObj2.setNotifKey(notKey);
					notObj2.setReadFlag(false);
					String adminName = serviceDao.getAdminUserName();
					notObj2.setUser(new User(adminName));
					long millis = new Date().getTime();
					Timestamp notTime2 = new Timestamp(millis);
					String notId2 = userName+adminName+millis; // GENERATED COMBINING requestedUser+toUser+time
					notObj2.setNotifId(notId2);
					notObj2.setTimestamp(notTime2);					
					if(notDao.insertNotification(notObj2)!=null){						

						///// SEND WEBSOCKET /////
						List<String> tokenList = serviceDao.getAdminTokens();
						if(workerName!=null)
							tokenList.addAll(serviceDao.getUserTokens(workerName));
						if(!tokenList.isEmpty())
							wsService.sendTextMessage(tokenList, wsMsg);
						//////////////////////////	

					}
				}				
			}
			
			*/
		
		regnObj.setLatitude(latitude);
		regnObj.setLocalAddress(localAddress);
		regnObj.setLocation(location);
		regnObj.setLongitude(longitude);
		regnObj.setPreferedTime(formatDate(time));
		regnObj.setUserComment(comment);
		
		// UPDATING
		regnDao.updateRegistration(regnObj);
		
		// NOTIFICATION
		Hashtable<String,Object> notData = new Hashtable<String,Object>();				

		notData.put("userName", userName);
		notData.put("fullName", checkNull(authDao.getFullName(userName)));
		notData.put("regnId", regnId);
		notData.put("proPic", ListenerClass.getProPicLink(userName));
		
		JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
				new JsonResponseFormatter<Hashtable<String,Object>>();
		String wsMsg = respObj.parseNotificationMessage("UPDATED", notData);
		
		// To WorkeR IF ASSIGNED
		String id="dummy";
		User worker = regnObj.getAssignedTo();
		String workerName = null;
		NotificationKey notKey = notDao.getNotificationKey("UPDATED");
		if(worker!=null){
			
			id=null;
			workerName = worker.getUserName();
			Notification notObj = new Notification();
			notObj.setNotifJson(wsMsg);
			notObj.setNotifKey(notKey);
			notObj.setReadFlag(false);
			notObj.setUser(worker); // Set whose notification
			long millis = new Date().getTime();
			Timestamp notTime = new Timestamp(millis);
			String notId = userName+workerName+millis; // GENERATED COMBINING requestedUser+toUser+time
			notObj.setNotifId(notId);
			notObj.setTimestamp(notTime);
			
			id=notDao.insertNotification(notObj);
		}				
		
		if(id!=null){
			
			//To admin
			Notification notObj2 = new Notification();
			notObj2.setNotifJson(wsMsg);
			notObj2.setNotifKey(notKey);
			notObj2.setReadFlag(false);
			String adminName = serviceDao.getAdminUserName();
			notObj2.setUser(new User(adminName));
			long millis = new Date().getTime();
			Timestamp notTime2 = new Timestamp(millis);
			String notId2 = userName+adminName+millis; // GENERATED COMBINING requestedUser+toUser+time
			notObj2.setNotifId(notId2);
			notObj2.setTimestamp(notTime2);					
			if(notDao.insertNotification(notObj2)!=null){						

				///// SEND WEBSOCKET /////
				List<String> tokenList = serviceDao.getAdminTokens();
				if(workerName!=null)
					tokenList.addAll(serviceDao.getUserTokens(workerName));
				if(!tokenList.isEmpty())
					wsService.sendTextMessage(tokenList, wsMsg);
				//////////////////////////	
				
				return true;

			}
		}	
		
		return false;
	}
	
	@Override
	public boolean assignWork(String userName, String regnId) {
		
		if(! "ROLE_WORKER".equals(  (  authDao.getRoleName(userName)  )  )) 
			return false;
		Registration regnObj = regnDao.getUserRegistrationFromId(regnId);
		if(regnObj==null || regnObj.getAssignedTo()!=null)
			return false;
		
		Timestamp time = new Timestamp(new Date().getTime());
		
		if(regnDao.updateAssignedTo(regnId, userName)>0){
			
			RegnHistory obj = new RegnHistory();
			obj.setRegn(regnObj);
			RegnStatus statusObj = serviceDao.getStatusObj("ASSIGNED");
			obj.setStatus(statusObj);
			obj.setTime(time);
			obj.setRegnHistoryId(regnObj.getRegnId()+statusObj.getStatusId());
			regnDao.saveOrUpdateRegnHistory(obj);
			
		}

		// NOTIFICATION
		
		Hashtable<String,Object> notData = new Hashtable<String,Object>();
		
		String complainUser = regnObj.getUser().getUserName();
		notData.put("userName", complainUser);
		notData.put("fullName", checkNull(authDao.getFullName(complainUser)));
		notData.put("regnId", regnId);
		notData.put("time", formatDate(regnObj.getTimestamp()));
		notData.put("comment", regnObj.getUserComment());
		notData.put("proPic", ListenerClass.getProPicLink(complainUser));
		
		JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
				new JsonResponseFormatter<Hashtable<String,Object>>();
		String wsMsg = respObj.parseNotificationMessage("ASSIGNED", notData);
		
		// To Worker		
		Notification notObj = new Notification();
		notObj.setNotifJson(wsMsg);
		NotificationKey notKey = notDao.getNotificationKey("ASSIGNED");
		notObj.setNotifKey(notKey);
		notObj.setReadFlag(false);
		notObj.setUser(new User(userName)); // Set whose notification
		String adminName = serviceDao.getAdminUserName();
		long millis = new Date().getTime();
		Timestamp notTime = new Timestamp(millis);
		String notId = adminName+userName+millis; // GENERATED COMBINING requestedUser+toUser+time
		notObj.setNotifId(notId);
		notObj.setTimestamp(notTime);
		
		if(notDao.insertNotification(notObj)!=null){
			
			//To complied user
			
			Hashtable<String,Object> notData2 = new Hashtable<String,Object>();
			notData2.put("regnId", regnId);
			notData2.put("comment", regnObj.getUserComment());
			String wsMsg2 = respObj.parseNotificationMessage("ASSIGNED", notData2);
			
			Notification notObj2 = new Notification();
			notObj2.setNotifJson(wsMsg2);
			notObj2.setNotifKey(notKey);
			notObj2.setReadFlag(false);
			notObj2.setUser(regnObj.getUser());
			long millis2 = new Date().getTime();
			Timestamp notTime2 = new Timestamp(millis2);
			String notId2 = adminName+regnObj.getUser().getUserName()
					+millis2; // GENERATED COMBINING requestedUser+toUser+time
			notObj2.setNotifId(notId2);
			notObj2.setTimestamp(notTime2);
			
			if(notDao.insertNotification(notObj2)!=null){
				
				///// SEND WEBSOCKET /////				
				wsService.sendTextMessage(serviceDao.getUserTokens(userName), wsMsg);
				wsService.sendTextMessage(serviceDao.getUserTokens(complainUser), wsMsg2);
				//////////////////////////
				return true;

			}
		}		
		
		return false;		
	}
	
	
	
	private String formatDate(Timestamp timestamp){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(timestamp);
	}
	
	private String checkNull(Object obj){
		
		if(obj==null)
			return "";
		return obj.toString();
	}
	
	private Timestamp formatDate(String timestamp){
		
		if(timestamp==null || timestamp.isEmpty())
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {			
			return new java.sql.Timestamp((sdf.parse(timestamp)).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean dropWork(String userName, String regnId) {
		
		Registration regn = regnDao.getUserRegistrationFromId(regnId);
		if(regn==null)
			return false;
		if(regnDao.getStatusTime("STARTED", regn)==null){
			// Work not yet started
			
			String complainUser = regn.getUser().getUserName();
			if(userName!=null)// Req from user
				if(! complainUser.equals(userName))
					return false;
			
			// GET NEEDED DATA FOR NOTIFICATION
			User worker = regn.getAssignedTo();
			String workerName = null;
			if(worker!=null)
				workerName=worker.getUserName();
			Hashtable<String,Object> notData = new Hashtable<String,Object>();
			notData.put("regnId", regnId);
			notData.put("comment", regn.getUserComment());
			
			/////////////
			regnDao.deleteAllRegnHistory(regnId);
			if(regnDao.dropWork(regnId)>0){
				
				/*if userName==null send notification to user
				 * else send to admin
				 * Both time send to worker if assigned
				 */
				JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
						new JsonResponseFormatter<Hashtable<String,Object>>();
				String wsMsg = respObj.parseNotificationMessage("DROPPED", notData);
				
				// To USER/ADMIN
				boolean isAdmin = userName==null;
				String adminName = serviceDao.getAdminUserName();
				String reqUser="",toUser="";
				if(isAdmin){
					reqUser = adminName;
					toUser = complainUser;
				}
				else{
					reqUser = complainUser;
					toUser = adminName;					
				}
				
				Notification notObj = new Notification();
				notObj.setNotifJson(wsMsg);
				NotificationKey notKey = notDao.getNotificationKey("DROPPED");
				notObj.setNotifKey(notKey);
				notObj.setReadFlag(false);
				notObj.setUser(new User(toUser)); // Set whose notification
				long millis = new Date().getTime();
				Timestamp notTime = new Timestamp(millis);
				String notId = reqUser+toUser+millis; // GENERATED COMBINING requestedUser+toUser+time
				notObj.setNotifId(notId);
				notObj.setTimestamp(notTime);
				boolean flag = false;
				if(notDao.insertNotification(notObj)!=null){
					
					flag = true;
					if(workerName!=null){
						
						flag = false;
						Notification notObj2 = new Notification();
						notObj2.setNotifJson(wsMsg);
						notObj2.setNotifKey(notKey);
						notObj2.setReadFlag(false);
						notObj2.setUser(new User(workerName)); // Set whose notification
						long millis2 = new Date().getTime();
						Timestamp notTime2 = new Timestamp(millis2);
						String notId2 = reqUser+workerName+millis; // GENERATED COMBINING requestedUser+toUser+time
						notObj2.setNotifId(notId2);
						notObj2.setTimestamp(notTime2);
						if(notDao.insertNotification(notObj2)!=null)							
							flag = true;
						
					}
					if(flag){
						
						List<String> tokenList = serviceDao.getUserTokens(toUser);
						if(workerName!=null )tokenList.addAll(serviceDao.getUserTokens(workerName));
						wsService.sendTextMessage(tokenList, wsMsg);
					}
				
				}
				return flag;
			}
			
		}
		//Work already started
		return false;
	}

	@Override
	public boolean changeWorkStatusByWorker(String regnId, String userName, String statusName, 
			Object extraInfo) {

		Registration regnObj = regnDao.getUserRegistrationFromId(regnId);
		if(regnObj==null)
			return false;
		User worker = regnObj.getAssignedTo();
		if(worker==null || ! worker.getUserName().equals(userName))
			return false;
		if(regnDao.getStatusTime(statusName,regnObj) != null)
			return false;
		
		RegnStatus statusObj = serviceDao.getStatusObj(statusName);
		if(statusObj==null)
			return false;

		Timestamp date = new Timestamp(new Date().getTime());
		RegnHistory history = new RegnHistory();
		history.setRegn(regnObj);
		history.setStatus(statusObj);
		history.setTime(date);
		history.setRegnHistoryId(regnObj.getRegnId()+statusObj.getStatusId());
		
		Hashtable<String,Object> wsData = new Hashtable<String,Object>();

		Hashtable<String,Object> notData = new Hashtable<String,Object>();// For notification
		
		if("CLOSED".equals(statusName)){
			
			regnDao.updatePayment(regnId,(float) extraInfo);
			wsData.put("payment", extraInfo);
			
			notData.put("payment", extraInfo);
		}
		regnDao.saveOrUpdateRegnHistory(history);
		
		// NOTIFICATION
		
		
		notData.put("regnId", regnId);
		notData.put("time", date);
		
		JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
				new JsonResponseFormatter<Hashtable<String,Object>>();
		String wsMsg = respObj.parseNotificationMessage(statusName, notData);
		
		// To User		
		Notification notObj = new Notification();
		notObj.setNotifJson(wsMsg);
		NotificationKey notKey = notDao.getNotificationKey(statusName);
		notObj.setNotifKey(notKey);
		notObj.setReadFlag(false);
		User complainedUser = regnObj.getUser();
		notObj.setUser(complainedUser); // Set whose notification
		long millis = new Date().getTime();
		Timestamp notTime = new Timestamp(millis);
		String notId = userName+complainedUser.getUserName()+millis; // GENERATED COMBINING requestedUser+toUser+time
		notObj.setNotifId(notId);
		notObj.setTimestamp(notTime);
		
		if(notDao.insertNotification(notObj)!=null){
			
			//To admin
			Notification notObj2 = new Notification();
			notObj2.setNotifJson(wsMsg);
			notObj2.setNotifKey(notKey);
			notObj2.setReadFlag(false);
			String adminName = serviceDao.getAdminUserName();
			notObj2.setUser(new User(adminName));
			long millis2 = new Date().getTime();
			Timestamp notTime2 = new Timestamp(millis2);
			String notId2 = userName+adminName+millis2; // GENERATED COMBINING requestedUser+toUser+time
			notObj2.setNotifId(notId2);
			notObj2.setTimestamp(notTime2);
			
			if(notDao.insertNotification(notObj2)!=null){
				
				///// SEND WEBSOCKET /////
				List<String> tokenList = serviceDao.getAdminTokens();
				tokenList.addAll(serviceDao.getUserTokens(complainedUser.getUserName()));
				if(!tokenList.isEmpty())
					wsService.sendTextMessage(tokenList, wsMsg);
				//////////////////////////
				return true;

			}
		}
		
		
		 return false;
	}

	@Override
	public boolean insertFeedback(String userName, String regnId, String jsonString) throws JSONException {
		
		Registration regn = regnDao.getUserRegistrationFromId(regnId);
		if(regn==null)
			return false;
		if( ! regn.getUser().getUserName().equals(userName))
			return false;
		if(regnDao.getStatusTime("CLOSED", regn)==null)
			return false;
		
		JSONObject feedJson = new JSONObject(jsonString);
		JSONArray ratingArray = feedJson.getJSONArray("ratings");
		int len = ratingArray.length();
		String feedback = feedJson.getString("feedback");

		Registration regnObj = new Registration();
		regnObj.setRegnId(regnId);
		
		Feedback feedObj = new Feedback();
		feedObj.setFeedback(feedback);
		feedObj.setRegn(regnObj);
		feedObj.setRegnId(regnId);
		regnDao.insertFeedback(feedObj);
		
		for(int i=0;i<len;i++){
			
			JSONObject eachRating = ratingArray.getJSONObject(i);
			short qnId = Short.parseShort(eachRating.get("questionId").toString());
			byte rating = Byte.parseByte(eachRating.get("rating").toString());
			
			String ratingId = regnId+qnId;
			RatingQuestion qnObj = new RatingQuestion();
			qnObj.setQnId(qnId);
			Rating ratingObj = new Rating(ratingId,regnObj,qnObj,rating);
			
			regnDao.insertRating(ratingObj);
		}
		if(regnDao.insertFeedback(feedObj)!=null){

			// Notification to admin			
			Hashtable<String,Object> notData = new Hashtable<String,Object>();
			
			notData.put("regnId", regnId);
			notData.put("feedback", feedback);
			
			JsonResponseFormatter<Hashtable<String,Object>> respObj	= 
					new JsonResponseFormatter<Hashtable<String,Object>>();
			String wsMsg = respObj.parseNotificationMessage("FEEDBACK", notData);
			

			Notification notObj = new Notification();
			notObj.setNotifJson(wsMsg);
			NotificationKey notKey = notDao.getNotificationKey("FEEDBACK");
			notObj.setNotifKey(notKey);
			notObj.setReadFlag(false);
			String adminName = serviceDao.getAdminUserName();
			notObj.setUser(new User(adminName));
			long millis = new Date().getTime();
			Timestamp notTime = new Timestamp(millis);
			String notId = userName+adminName+millis; // GENERATED COMBINING requestedUser+toUser+time
			notObj.setNotifId(notId);
			notObj.setTimestamp(notTime);
			
			if(notDao.insertNotification(notObj)!=null){
				
				List<String> tokenList = serviceDao.getAdminTokens();
				if(!tokenList.isEmpty())
					wsService.sendTextMessage(tokenList, wsMsg);

				return true;
			}
			
		}
		return false;
	}

	@Override
	public void setReadFlag(List<String> notId) {
		
		for(String id : notId)
			notDao.setReadFlag(id);
	}

}
