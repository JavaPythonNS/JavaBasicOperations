package com.basic.operations.services;

import java.util.List;

import com.basic.operations.models.Role;



public interface IRoleService {
	
	void persist(Role role);
	
	Role findById(Integer id);
	
	List<Role> getRoles();
	
}
