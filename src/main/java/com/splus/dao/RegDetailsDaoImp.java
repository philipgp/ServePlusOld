package com.splus.dao;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.splus.pojo.Feedback;
import com.splus.pojo.Rating;
import com.splus.pojo.RatingQuestion;
import com.splus.pojo.Registration;
import com.splus.pojo.RegnHistory;

public class RegDetailsDaoImp implements RegDetailsDao {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public long checkRegnId(String regnId) {
		
		String hql = "SELECT COUNT(*) FROM Registration where regnId=:regnId";
		return (long) sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("regnId", regnId)
				.uniqueResult();
	}

	@Override
	public String registerNew(Registration newReg) {
		
		return (String) sessionFactory.getCurrentSession().save(newReg);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Registration> getUserRegistration(String userName) {
		
		String hql = "SELECT R FROM Registration R,User U WHERE U.userName=:userName "
				+"and R.user=U ORDER BY R.timestamp DESC";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("userName", userName)
				.list();
	}

	@Override
	public Registration getUserRegistrationFromId(String regId) {
		
		String hql = "FROM Registration WHERE regnId=:regnId";
		return (Registration) sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("regnId", regId)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Registration> getAllRegistrations() {
		
		String hql = "FROM Registration ORDER BY timestamp DESC";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.list();
	}

	@Override
	public Object[] getRegnStatus(Registration regnObj) {
		
		String hql = "SELECT RH.status,RH.time FROM RegnHistory RH WHERE RH.regn = :regn"
				+ " ORDER BY RH.time DESC";
		return (Object[]) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regn", regnObj)
				.setMaxResults(1)
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegnHistory> getAllHistory(Registration regnObj){
		
		String hql = "From RegnHistory WHERE regn=:regnObj";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnObj", regnObj)
				.list();
	}

	@Override
	public Timestamp getStatusTime(String statusname, Registration regn) {
		
		String hql = "SELECT H.time FROM RegnHistory H,RegnStatus S WHERE S.statusName = :name"
				+ " AND H.regn = :regn AND H.status=S";
		return (Timestamp) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regn", regn)
				.setParameter("name", statusname)
				.uniqueResult();
	}

	@Override
	public int updateAssignedTo(String regnId, String workerName) {
		
		String hql = "UPDATE Registration SET assignedTo=(FROM User WHERE userName=:userName)"
				+ " WHERE regnId = :regnId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnId", regnId)
				.setParameter("userName", workerName)
				.executeUpdate();
	}

	@Override
	public void saveOrUpdateRegnHistory(RegnHistory regnHistory) {
		
		sessionFactory.getCurrentSession().saveOrUpdate(regnHistory);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Registration> getAssignedWorks(String workerName) {
		
		String hql = "SELECT R FROM Registration R, User U WHERE U.userName=:userName"
				+ " AND R.assignedTo=U";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", workerName)
				.list();
	}

	@Override
	public int dropWork(String regnId) {
		
		String hql = "DELETE FROM Registration WHERE regnId = :regnId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnId", regnId)
				.executeUpdate();
	}

	@Override
	public int deleteAllRegnHistory(String regnId) {
		
		String hql = "DELETE FROM RegnHistory WHERE regn = (FROM Registration"
				+ " WHERE regnId = :regnId)";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnId", regnId)
				.executeUpdate();
	}

	@Override
	public int updatePayment(String regnId, float cash) {
		
		String hql = "UPDATE Registration SET payment=:cash WHERE regnId=:regnId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnId", regnId)
				.setParameter("cash", cash)
				.executeUpdate();
	}

	@Override
	public int updateRegistration(String hql, List<Hashtable<String, Object>> paramList) {
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(Hashtable<String, Object> param : paramList)			
			query.setParameter(param.get("key").toString(), param.get("value"));
		return query.executeUpdate();
	}
	
	@Override
	public void updateRegistration(Registration regnObj){
		
		sessionFactory.getCurrentSession().update(regnObj);
	}

	@Override
	public String insertFeedback(Feedback obj) {
		return (String) sessionFactory.getCurrentSession().save(obj);
	}

	@Override
	public String getFeedback(String regnId) {
		
		String hql = "SELECT feedback FROM Feedback WHERE regnId=:regnId";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnId", regnId)
				.uniqueResult();
	}

	@Override
	public String insertRating(Rating obj) {
		return (String) sessionFactory.getCurrentSession().save(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rating> getRating(String regnId) {
		
		String hql = "SELECT RAT FROM Rating RAT,Registration R WHERE R.regnId=:regnId"
				+ " AND R=RAT.regn";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("regnId", regnId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RatingQuestion> getRatingQuestions() {
		
		String hql = "FROM RatingQuestion";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.list();
	}

}
