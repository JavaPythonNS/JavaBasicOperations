package com.basic.operations.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.basic.operations.dao.IUserDao;
import com.basic.operations.models.AppVersion;
import com.basic.operations.models.Role;
import com.basic.operations.models.User;
import com.basic.operations.services.IUserServices;

@Service
public class UserServices implements IUserServices,UserDetailsService{
	

	@Autowired
	IUserDao userDao;

	@Override
	public void persist(User user) {
		userDao.persist(user);
	}

	@Override
	public User save(User user) {
		return userDao.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		
		User user = userDao.findByEmail(email);
		if(user == null) {
			user = userDao.checkSocialId(email);
			if(user == null) {
	    		throw new UsernameNotFoundException(email + " was not found");	
	    	}	
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));
		    //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PARKING_OWNER"));
		    //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BOTH"));
		    return new org.springframework.security.core.userdetails.User(
		    		user.getSocialId(),
		    		User.PASSWORD_ENCODER.encode(user.getLoginType()),
		    		grantedAuthorities);	
		} else {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));
		    //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PARKING_OWNER"));
		    //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BOTH"));
		    return new org.springframework.security.core.userdetails.User(
		    		user.getEmail(),
		    		user.getPassword(),
		    		grantedAuthorities);
		}
	}

	@Override
	public User findById(String id) {
		return userDao.findById(id);
	}
	
	@Override
	public List<User> findAdmins(){
		return userDao.findAdmins();
	}

	@Override
	public List<AppVersion> getAppVersionsList() {
		return userDao.getAppVersionsList();
	}

	@Override
	public User checkEmail(String email) {
		return userDao.checkEmail(email);
	}

	@Override
	public Role getRoleById(int role) {
		return userDao.getRoleById(role);
	}

	@Override
	public User addUser(User user) {
		return userDao.addUser(user);
	}
	
	@Override
	public User checkSocialId(String socialId) {
		return userDao.checkSocialId(socialId);
	}

	@Override
	public User getUserById(String id) {
		return userDao.getUserById(id);
	}
	
	@Override
	public List<AppVersion> getAppVersionList() {
		return userDao.getAppVersionList();
	}
	
	@Override
	public AppVersion addAppVersions(AppVersion objAppVersions) {
		return userDao.addAppVersions(objAppVersions);
	}
	
	@Override
	public List<User> getUsersList(String format, String format2) {
		return userDao.getUsersList(format, format2);
	}
	
	@Override
	public List<User> getRegisteredUsersList() {
		return userDao.getRegisteredUsersList();
	}
	
	@Override
	public long loggedInUserCount() {
		return userDao.loggedInUserCount();
	}

	@Override
	public List<User> getUsersList() {
		return userDao.getUsersList();
	}
	
	@Override
	public User getUserDetailById(String id) {
		return userDao.getUserDetailById(id);
	}

	@Override
	public User updateUser(User users) {
		return userDao.updateUser(users);
	}
	
	@Override
	public long searchedUserCount(String text) {
		return userDao.searchedUserCount(text);
	}

	@Override
	public List<User> searchUser(int start, String text) {
		return userDao.searchUser(start, text);
	}
	
	@Override
	public User getAdminUser() {
		return userDao.getAdminUser();
	}

}

