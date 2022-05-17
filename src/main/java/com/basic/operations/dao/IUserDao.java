package com.basic.operations.dao;

import java.util.List;
import com.basic.operations.models.AppVersion;
import com.basic.operations.models.Role;
import com.basic.operations.models.User;



public interface IUserDao {

	void persist(User user);
	
	User save(User user); 
	
	User findByEmail(String email);
	
	User findById(String id);
	
	List<User> findAdmins();
	
	List<AppVersion> getAppVersionsList();
	
	User checkEmail(String email);
	
	Role getRoleById(int role);
	
	User addUser(User user);
	
	User checkSocialId(String socialId);
		
	User getUserById(String id);
	
	List<AppVersion> getAppVersionList();
	
	AppVersion addAppVersions(AppVersion objAppVersions);
	
	List<User> getUsersList(String format, String format2);
	
	List<User> getRegisteredUsersList();
	
	long loggedInUserCount();
	
	List<User> getUsersList();
	
	User getUserDetailById(String id);
	
	User updateUser(User users);
	
	Long searchedUserCount(String text);
	
	List<User> searchUser(int start, String text);
	
	User getAdminUser();
}
