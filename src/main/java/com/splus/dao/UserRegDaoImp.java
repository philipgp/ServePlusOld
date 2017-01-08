package com.splus.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.SessionFactory;

import com.splus.pojo.CompanyVoucher;
import com.splus.pojo.User;
import com.splus.pojo.UserCompany;
import com.splus.pojo.UserRegSession;
import com.splus.pojo.WorkerType;

public class UserRegDaoImp implements UserRegDao {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public String check(String userName, String email, String phNo) {
		
		String hql = "SELECT userName FROM User WHERE userName=:userName OR email=:email"
				+ " OR phoneNo=:phNo";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.setParameter("email", email)
				.setParameter("phNo", phNo)
				.setMaxResults(1)
				.uniqueResult();
	}

	@Override
	public String insertNewUser(User userObj) {
		return (String) sessionFactory.getCurrentSession().save(userObj);
	}

	@Override
	public String insertRegSession(UserRegSession regSessionObj) {
		return (String) sessionFactory.getCurrentSession().save(regSessionObj);
	}
	
	@Override
	public int deleteUser(String userName) {
		
		String hql = "DELETE FROM User WHERE userName=:userName";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.executeUpdate();
	}

	@Override
	public int deleteRegSession(String userName) {
		
		String hql = "DELETE FROM UserRegSession WHERE userName=:userName";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.executeUpdate();
	}

	@Override
	public int updateRole(String userName, String roleName) {
		
		String hql = "UPDATE User SET role=(FROM Role WHERE roleName=:roleName)"
				+ " WHERE userName=:userName";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.setParameter("roleName", roleName)
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getPendingRegistrations() {
		
		String hql = "FROM User WHERE Role IS NULL";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.list();
	}

	@Override
	public String checkRegCode(String userName, String token, String emailCode) {
		
		String hql = "SELECT userName FROM UserRegSession WHERE userName=:userName AND"
				+ " token=:token AND emailCode=:emailCode";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", userName)
				.setParameter("token", token)
				.setParameter("emailCode", emailCode)
				.setMaxResults(1)
				.uniqueResult();
	}

	@Override
	public int deleteExpiredReg(Timestamp currentTimestamp) {
		
		String hql = "DELETE FROM UserRegSession WHERE expiryDate<:current";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("current", currentTimestamp)
				.executeUpdate();
	}
	
	@Override
	public int deleteExpiredUser(Timestamp currentTimestamp) {
		
		String hql = "DELETE FROM User WHERE userName IN (SELECT userName from UserRegSession"
				+ " WHERE expiryDate<:current)";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("current", currentTimestamp)
				.executeUpdate();
	}

	@Override
	public String updateWorkerType(WorkerType obj) {
		
		return (String) sessionFactory.getCurrentSession().save(obj);
	}

	@Override
	public String checkVoucher(String voucher) {
		
		String hql = "SELECT voucher FROM CompanyVoucher WHERE voucher=:voucher";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("voucher", voucher)
				.uniqueResult();
	}

	@Override
	public String insertCompanyVoucher(CompanyVoucher voucherObj) {
		return (String) sessionFactory.getCurrentSession().save(voucherObj);
	}

	@Override
	public User loadCompanyFromVoucher(String voucher) {
		
		String hql = "SELECT user FROM CompanyVoucher WHERE voucher=:voucher";
		return (User) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("voucher", voucher)
				.uniqueResult();
	}

	@Override
	public String insertUserCompany(UserCompany obj) {
		return (String) sessionFactory.getCurrentSession().save(obj);
	}

}
