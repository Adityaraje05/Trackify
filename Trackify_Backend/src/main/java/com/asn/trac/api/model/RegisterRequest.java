package com.asn.trac.api.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {
	@NotBlank
	private String username;

	@NotBlank
	@Size(min = 6, max = 64)
	private String password;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Email
	private String email;

	// Role name string: ADMIN, FACULTY, STUDENT
	private String roleName = "STUDENT";

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getRoleName() { return roleName; }
	public void setRoleName(String roleName) { this.roleName = roleName; }
}