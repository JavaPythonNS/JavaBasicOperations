package com.basic.operations.config;

import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.basic.operations.models.Role;
import com.basic.operations.models.User;
import com.basic.operations.services.IRoleService;
import com.basic.operations.services.IUserServices;



@Component
public class DatabaseLoader implements CommandLineRunner {

	@Autowired
	IUserServices userService;

	@Autowired
	IRoleService roleService;
	
    @Override
    public void run(String... strings) throws Exception {
    	addRoles();
    	addAdmin();
    	setTimeZone();
    }
    
    void addRoles() {
    	List<Role> roles = roleService.getRoles();
    	if(roles == null || roles.size()==0) {
	    	roleService.persist(new Role("ROLE_ADMIN"));
	    	roleService.persist(new Role("ROLE_USER"));
    	}
    	
    }
    
    void addAdmin() {
    	List<User> findUsers = userService.findAdmins();
    	if(findUsers == null || findUsers.size()==0) {
	    	Role role = roleService.findById(1);
	    	String id = UUID.randomUUID().toString().toUpperCase();
	    	User user = new User(id, "admin@yopmail.com","admin1234",role);
	    	userService.save(user);
    	}
    }
    
    void setTimeZone(){
    	TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // It will set UTC timezone
    }
}
