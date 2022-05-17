package com.basic.operations.dao;

import java.util.List;

import com.basic.operations.models.Role;

public interface IRoleDAO {

	void persist(Role role);
	
	Role findById(Integer id);
	
	List<Role> getRoles();
	
}
