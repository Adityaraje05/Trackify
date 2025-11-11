package com.asn.trac.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asn.trac.api.entity.User;
import com.asn.trac.api.entity.Role;
import com.asn.trac.api.model.AuthResponse;
import com.asn.trac.api.model.LoginRequest;
import com.asn.trac.api.model.RegisterRequest;
import com.asn.trac.api.security.JwtUtil;
import com.asn.trac.api.service.UserService;
import com.asn.trac.api.service.StudentService;
import com.asn.trac.api.service.RoleService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:5176", "http://localhost:5177", "http://localhost:4200"})
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RoleService roleService;
	@Autowired
	private StudentService studentService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
		try {
			User user = new User();
			user.setUsername(req.getUsername());
			user.setPassword(req.getPassword());
			user.setFirstName(req.getFirstName());
			user.setLastName(req.getLastName());
			user.setEmail(req.getEmail());
			Role role = roleService.getOrCreate(req.getRoleName() != null ? req.getRoleName() : "STUDENT");
			user.setRole(role);
			user.setEnabled(true);

			User created = userService.registerUser(user);
			if (created == null) {
				return ResponseEntity.badRequest().body("Username already exists or registration failed");
			}
			// If the registered user is a STUDENT, ensure a Student entity exists for attendance flows
			if ("STUDENT".equalsIgnoreCase(created.getRole().getName())) {
				final String fullName = (created.getFirstName() != null ? created.getFirstName() : "") +
					((created.getLastName() != null && !created.getLastName().isEmpty()) ? " " + created.getLastName() : "");
				studentService.createIfMissing(fullName.trim().isEmpty() ? created.getUsername() : fullName, created.getEmail());
			}
			String token = jwtUtil.generateToken(created.getUsername(), created.getRole().getName());
			return ResponseEntity.ok(new AuthResponse(token, created.getUsername(), created.getRole().getName()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
		User user = userService.loginUser(req);
		if (user == null) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
		String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getName());
		return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().getName()));
	}

	@GetMapping("/health")
	public ResponseEntity<?> health() {
		return ResponseEntity.ok().body("{\"status\":\"ok\",\"message\":\"Backend is running\"}");
	}

	@GetMapping("/me")
	public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
		if (token == null) return ResponseEntity.status(401).build();
		String username = jwtUtil.extractUsername(token);
		User user = userService.getUserByName(username);
		if (user == null) return ResponseEntity.status(404).build();
		return ResponseEntity.ok(user);
	}
}