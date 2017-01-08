package com.splus.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.splus.pojo.Notification;
import com.splus.pojo.NotificationKey;

public class NotificationDaoImp implements NotificationDao {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notification> getLatestNotifications(String userName) {
		
		String hql = "SELECT N FROM Notification N, User U WHERE U.userName=:userName"
				+ " AND N.user=U ORDER BY N.timestamp DESC";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.setMaxResults(5)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notification> getNotifications(String userName, String notifId) {
		
		String hql = "SELECT N FROM Notification N, User U WHERE U.userName=:userName"
				+ " AND N.user=U AND"
				+ " N.timestamp<(SELECT timestamp FROM Notification WHERE notifId=:id)"
				+ " ORDER BY N.timestamp DESC";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.setParameter("id", notifId)
				.setMaxResults(5)
				.list();
	}

	@Override
	public String insertNotification(Notification obj) {
		return (String) sessionFactory.getCurrentSession().save(obj);
	}

	@Override
	public NotificationKey getNotificationKey(String notifName) {
		
		String hql = "FROM NotificationKey WHERE keyName=:notifName";
		return (NotificationKey) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("notifName", notifName)
				.uniqueResult();
	}

	@Override
	public int setReadFlag(String notId) {
		
		String hql = "UPDATE Notification SET readFlag=:flag WHERE notifId=:notifId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("notifId", notId)
				.setParameter("flag", true)
				.executeUpdate();
	}

}
