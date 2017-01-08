package com.splus.dao;

import java.util.List;

import com.splus.pojo.Notification;
import com.splus.pojo.NotificationKey;

public interface NotificationDao {
	
	List<Notification> getLatestNotifications(String userName);
	List<Notification> getNotifications(String userName,String notifId);
	String insertNotification(Notification obj);
	NotificationKey getNotificationKey(String notifName);
	int setReadFlag(String notId);

}
