package com.basic.operations.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basic.operations.dao.IRoleDAO;
import com.basic.operations.models.Role;


@Transactional
@Repository
public class RoleDAO implements IRoleDAO{

	@Autowired
	EntityManager entityManager;
	
	@Override
	public void persist(Role role) {
		entityManager.persist(role);
	}

	@Override
	public Role findById(Integer id) {
		return entityManager.find(Role.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRoles() {
		return entityManager.createQuery("from Role").getResultList();
	}
	
}
