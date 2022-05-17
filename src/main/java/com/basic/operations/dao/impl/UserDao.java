package com.basic.operations.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.basic.operations.dao.IUserDao;
import com.basic.operations.models.AppVersion;
import com.basic.operations.models.Role;
import com.basic.operations.models.User;



@Repository
@Transactional
public class UserDao implements IUserDao{
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public void persist(User user) {
		entityManager.persist(user);
	}

	@Override
	public User save(User user) {
		return entityManager.merge(user);
	}

	@Override
	public User findByEmail(String email) {
		try {
		 TypedQuery<User> query = entityManager.createQuery("from User where email =:email",User.class);
		 return query.setParameter("email", email).getSingleResult();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public User findById(String id) {
		return entityManager.find(User.class, id);
	}
	
	@Override
	public List<User> findAdmins(){
		try {
			@SuppressWarnings("unchecked")
			List<User> query = entityManager.createQuery("from User where role=1").getResultList();
			return query;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public List<AppVersion> getAppVersionsList() {
		try {
			Query query= entityManager.createQuery("from AppVersion order by id desc");
			@SuppressWarnings("unchecked")
			List<AppVersion> appVersions = query.getResultList();
			return appVersions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public User checkEmail(String email) {
		try {
			String hql = "FROM User where email =:email" ;
			Query query =entityManager.createQuery(hql);
			query.setParameter("email",email);
			User users = (User)query.getSingleResult();
			if(users != null) {
				return users;
			}
			else {
				return null;	
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Role getRoleById(int role) {
		return entityManager.find(Role.class, role);
	}
	
	@Override
	public User addUser(User user) {
		return entityManager.merge(user);
	}
	
	@Override
	public User checkSocialId(String socialId) {
		try {
			String hql = "FROM User where socialId =:socialId";
			Query query =entityManager.createQuery(hql);
			query.setParameter("socialId",socialId);
			User users = (User)query.getSingleResult();
			return users;
		}catch (Exception e) {
			return null;
		}
	}
		
	@Override
	public User getUserById(String id) {
		return entityManager.find(User.class, id);
	}
	
	
	@Override
	public AppVersion addAppVersions(AppVersion objAppVersions) {
		return entityManager.merge(objAppVersions);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUsersList(String format, String format2) {
		try {
			Query query = entityManager.createQuery("from User where action_performed Between '"+format +"' AND '"+format2 +"' group by id");
			List<User> list = query.getResultList();
			return list;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getRegisteredUsersList() {
		try {
			Query query = entityManager.createQuery("from User where role = 2 OR role=3 OR role=4 ORDER BY id DESC");
			List<User> list = query.getResultList();
			return list;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	
	
	@Override
	public long loggedInUserCount() {
		try {
			String queryString = "SELECT Count(*) FROM user where role=2 OR role=3 OR role=4";  
			Query query = entityManager.createNativeQuery(queryString); 
			BigInteger bigInteger =(BigInteger) query.getSingleResult();
			return bigInteger.longValue();
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUsersList() {
		try {
			Query query = entityManager.createQuery("FROM User where role!=1");
			List<User> list = query.getResultList();
			return list;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	
	
	@Override
	public User getUserDetailById(String id) {
		return entityManager.find(User.class, id);
	}
	
	
	@Override
	public User updateUser(User users) {
		return entityManager.merge(users);
	}
	
	
	@Override
	public Long searchedUserCount(String text) {
		try {
			Long count=0L;
			if(text.equals(""))
				count = (Long) entityManager.createQuery("select count(*) from User where role!=1").getSingleResult();	
			else
				count = (Long) entityManager.createQuery("select count(*) from User where role!=1 and (upper(concat(firstName,' ', lastName)) like '%"+text+"%' or firstName like '%"+text+"%' )")
				.getSingleResult();
			
			return count;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AppVersion> getAppVersionList() {
		try {
			Query query = entityManager.createQuery("FROM AppVersion");
			List<AppVersion> list = query.getResultList();
			return list;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchUser(int start, String text) {
		try {
			if(text.equals(""))
				return entityManager.createQuery("FROM User where role!=1 " 
					+ "order by id desc").setFirstResult(start).setMaxResults(20).getResultList();	
			else
				return entityManager.createQuery("FROM User where role!=1 and (upper(concat(firstName,' ', lastName)) like '%"+text+"%' or firstName like '%"+text+"%' ) " 
						+ "order by id desc").setFirstResult(start).setMaxResults(20).getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public User getAdminUser() {
		try {
			User user = (User)entityManager.createQuery("from User where role = 1").getSingleResult();
			return user;
		} catch (Exception e) {
			return null;
		}
	}

}
