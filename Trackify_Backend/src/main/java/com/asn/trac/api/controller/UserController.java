package com.asn.trac.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asn.trac.api.entity.User;
import com.asn.trac.api.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:5176", "http://localhost:5177", "http://localhost:4200"})
public class UserController {

	@Autowired
	private UserService service;

	// NOTE: Authentication is handled by /auth/register and /auth/login

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-user-by-username/{username}")
	public User getUserById(@PathVariable String username) {
		return service.getUserByName(username);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-user")
	public List<User> getAllUser() {
		return service.getAllUser();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-admin")
	public List<User> getAllAdmins() {
		return service.getAllAdmins();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-faculty")
	public List<User> getAllFaculties() {
		return service.getAllFaculties();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-user-by-username")
	public String deleteUserById(@RequestParam String username) {
		return service.deleteUserById(username);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update-user")
	@CrossOrigin(methods = RequestMethod.PUT)
	public User updateUser(@RequestBody User user) {
		return service.updateUser(user);
	}

	// Optional: keep legacy endpoints but discourage their use
	@Deprecated
	@GetMapping("/legacy-note")
	public ResponseEntity<String> legacyNote() {
		return new ResponseEntity<String>(
			"Legacy login/register endpoints were moved to /auth/*; please use /auth/register and /auth/login.",
			HttpStatus.OK
		);
	}
}