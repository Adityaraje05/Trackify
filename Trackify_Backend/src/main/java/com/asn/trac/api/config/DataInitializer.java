package com.asn.trac.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asn.trac.api.entity.Role;
import com.asn.trac.api.entity.User;
import com.asn.trac.api.service.RoleService;
import com.asn.trac.api.service.UserService;

@Configuration
public class DataInitializer {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Bean
	public CommandLineRunner seedDefaults() {
		return args -> {
			Role admin = roleService.getOrCreate("ADMIN");
			Role faculty = roleService.getOrCreate("FACULTY");
			Role student = roleService.getOrCreate("STUDENT");

			// Create default admin if missing
			if (userService.getUserByName("admin") == null) {
				User u = new User();
				u.setUsername("admin");
				u.setPassword("Admin@123"); // will be encoded in service
				u.setFirstName("System");
				u.setLastName("Admin");
				u.setEmail("admin@example.com");
				u.setRole(admin);
				u.setEnabled(true);
				userService.registerUser(u);
			}
		};
	}
}


