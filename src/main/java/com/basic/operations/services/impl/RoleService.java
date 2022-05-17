package com.basic.operations.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basic.operations.dao.IRoleDAO;
import com.basic.operations.models.Role;
import com.basic.operations.services.IRoleService;

@Service
public class RoleService implements IRoleService {

	@Autowired
	IRoleDAO roleDAO;
	
	@Override
	public void persist(Role role) {
		roleDAO.persist(role);
	}

	@Override
	public Role findById(Integer id) {
		return roleDAO.findById(id);
	}

	@Override
	public List<Role> getRoles() {
		return roleDAO.getRoles();
	}

}
