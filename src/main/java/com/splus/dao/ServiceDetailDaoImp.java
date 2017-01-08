package com.splus.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.splus.pojo.CompanyType;
import com.splus.pojo.Department;
import com.splus.pojo.RegnStatus;
import com.splus.pojo.Type;
import com.splus.pojo.User;
import com.splus.pojo.WorkerType;

public class ServiceDetailDaoImp implements ServiceDetailDao {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> getAllDepartments() {
		
		String hql = "FROM Department WHERE suspendFlag!=:flag";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("flag", true)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> getAllDepartmentsWithSuspend() {
		
		String hql = "FROM Department";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Type> getAllTypes(short depId) {
		
		String hql = "SELECT T FROM Department D,Type T WHERE T.dept=D AND D.deptId=:depId"
				+ " AND D.suspendFlag!=:flag";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("depId", depId)
				.setParameter("flag", true)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Type> getAllTypesWithSuspend(short depId) {
		
		String hql = "SELECT T FROM Department D,Type T WHERE T.dept=D AND D.deptId=:depId";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("depId", depId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAdminTokens() {
		
		String hql = "SELECT S.token FROM UserSession S,User U,Role R WHERE"
				+ " R.roleName = :roleName and U.role=R and S.user=U";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("roleName", "ROLE_ADMIN")
				.list();
	}

	@Override
	public String getAdminUserName() {
		
		String hql = "SELECT U.userName FROM User U,Role R WHERE"
				+ " R.roleName = :roleName and U.role=R";
		return (String) sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("roleName", "ROLE_ADMIN")
				.uniqueResult();
	}

//	@Override
//	public Object[] getDeptTypeNames(short typeId) {
//		
//		String hql = "SELECT D.deptName,T.typeName FROM Department D,Type T"
//				+ " WHERE T.typeId =:typeId AND T.dept=D AND D.suspendFlag!=:flag";
//		return (Object[]) sessionFactory.getCurrentSession()
//				.createQuery(hql)
//				.setParameter("typeId", typeId)
//				.setParameter("flag", true)
//				.uniqueResult();
//	}

	@Override
	public String getUserToken(String userName) {
		
		String hql = "SELECT S.token FROM UserSession S,User U WHERE"
				+ " U.userName=:userName and S.user=U";
		return (String) sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("userName", userName)
				.setMaxResults(1)
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserTokens(String userName) {
		
		String hql = "SELECT S.token FROM UserSession S,User U WHERE"
				+ " U.userName=:userName and S.user=U";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("userName", userName)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		
		String hql = "FROM User WHERE role=(FROM Role WHERE roleName=:roleName)";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("roleName", "ROLE_USER")
				.list();
	}

	@Override
	public Short insertNewType(Type type) {
		
		return (Short) sessionFactory.getCurrentSession().save(type);
	}
	
	@Override
	public Short insertNewDept(Department dept) {
		
		return (Short) sessionFactory.getCurrentSession().save(dept);
	}

	@Override
	public int updateDeptName(short deptId, String newName) {
		
		String hql = "UPDATE Department SET deptName=:newName WHERE deptId=:deptId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("newName", newName)
				.setParameter("deptId", deptId)
				.executeUpdate();
	}

	@Override
	public int updateTypeName(short typeId, String newName) {
		
		String hql = "UPDATE Type SET typeName=:newName WHERE typeId=:typeId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("newName", newName)
				.setParameter("typeId", typeId)
				.executeUpdate();
	}

	@Override
	public String getTypeName(short typeId) {
		
		String hql = "SELECT typeName FROM Type WHERE typeId=:typeId";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("typeId", typeId)
				.uniqueResult();
	}

	@Override
	public String getTypeNameWithoutSuspend(short typeId) {
		
		String hql = "SELECT T.typeName FROM Department D,Type T"
				+ " WHERE T.typeId =:typeId AND T.dept=D AND D.suspendFlag!=:flag";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("typeId", typeId)
				.setParameter("flag", true)
				.uniqueResult();
	}

	@Override
	public String getDeptName(short deptId) {
		
		String hql = "SELECT deptName FROM Department WHERE deptId=:deptId";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("deptId", deptId)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkerType> getAllWorkerTypeObjects() {
		
		String hql = "FROM WorkerType";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WorkerType> getAllWorkerTypeObjects(short typeId) {
		
		String hql = "SELECT WT FROM WorkerType WT,Type T,User U,Department D WHERE T.typeId=:typeId"
				+ " AND WT.type = T AND WT.user = U AND U.suspendFlag = :suspendFlag"
				+ " AND T.dept=D AND D.suspendFlag!=:flag";
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setParameter("typeId", typeId)
				.setParameter("suspendFlag", false) // user flag
				.setParameter("flag", true) // dept flag
				.list();
	}

	@Override
	public RegnStatus getStatusObj(String statusName) {
		
		String hql = "FROM RegnStatus WHERE statusName=:name";
		return (RegnStatus) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("name", statusName)
				.uniqueResult();
	}

	@Override
	public int suspendDept(short deptId, boolean flag) {
		
		String hql = "UPDATE Department SET suspendFlag=:flag WHERE deptId=:deptId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("deptId", deptId)
				.setParameter("flag", flag)
				.executeUpdate();
	}

	@Override
	public String checkWorkerAvailabilty(String workerName) {
		
		String hql = "SELECT R.regnId FROM Registration R, RegnHistory RH, User U, RegnStatus RS"
				+ " WHERE U.userName=:userName AND U.suspendFlag=:suspendFlag AND"
				+ " RS.statusName=:assignedStatus"
				+ " AND RS.statusName!=:closedStatus AND R.assignedTo=U AND RH.regn=R"
				+ " AND RH.status=RS";
		return (String) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userName", workerName)
				.setParameter("assignedStatus", "ASSIGNED")
				.setParameter("closedStatus", "CLOSED")
				.setParameter("suspendFlag", false)
				.setMaxResults(1)
				.uniqueResult();
	}

	@Override
	public CompanyType getCompanyType(String companyName, short typeId) {
		
		String hql = "SELECT cType FROM User user, CompanyType cType, Type type WHERE type.typeId=:typeId"
				+ " AND cType.type=type AND cType.company=user AND user.userName=:companyName";
		return (CompanyType) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("companyName", companyName)
				.setParameter("typeId", typeId)
				.setMaxResults(1)
				.uniqueResult();
	}

	@Override
	public Integer insertCompanyType(CompanyType obj) {
		return (Integer) sessionFactory.getCurrentSession().save(obj);
	}

	@Override
	public int suspendCompanyType(int cTypeId, boolean flag) {
		
		String hql = "UPDATE CompanyType SET suspendFlag=:flag WHERE companyTypeId=:companyTypeId";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("companyTypeId", cTypeId)
				.setParameter("flag", flag)
				.executeUpdate();
	}

}
