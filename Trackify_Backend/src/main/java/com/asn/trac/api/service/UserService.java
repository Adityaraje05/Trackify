package com.asn.trac.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.asn.trac.api.dao.UserDao;
import com.asn.trac.api.entity.User;
import com.asn.trac.api.model.LoginRequest;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User loginUser(LoginRequest request) {
		User user = dao.getUserByName(request.getUsername());
		if (user != null && user.isEnabled() && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return user;
		}
		return null;
	}

	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return dao.registerUser(user);
	}

	public User getUserByName(String username) {
		return dao.getUserByName(username);
	}

	public List<User> getAllUser() {
		return dao.getAllUser();
	}

	public User updateUser(User user) {
		// Hash password only if it's non-empty and not already encoded (basic heuristic)
		if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return dao.updateUser(user);
	}

	public String deleteUserById(String username) {
		return dao.deleteUserById(username);
	}

	public List<User> getAllAdmins() {
		return dao.getAllAdmins();
	}

	public List<User> getAllFaculties() {
		return dao.getAllFaculties();
	}
}