package com.asn.trac.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asn.trac.api.dao.RoleDao;
import com.asn.trac.api.entity.Role;

@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;

	public Role getOrCreate(String name) {
		Role existing = roleDao.findByName(name);
		if (existing != null) return existing;
		Role role = new Role(name);
		return roleDao.save(role);
	}

	public Role findByName(String name) {
		return roleDao.findByName(name);
	}
}


