package com.asn.trac.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;  // <-- add this


import com.asn.trac.api.entity.Student;
import com.asn.trac.api.service.StudentService;
import com.asn.trac.api.service.UserService;
import com.asn.trac.api.entity.User;
import com.asn.trac.api.security.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:5176", "http://localhost:5177", "http://localhost:4200"})
public class StudentController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/get-all-students")
	public List<Student> getAllStudents() {
		return studentService.getAllStudents();
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@PostMapping("/add-student")
	public Student createStudent(@RequestBody Student student) {
		return studentService.createStudent(student);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/get-student-by-id/{id}")
	public Student getStudentById(@PathVariable Long id) {
		return studentService.getStudentById(id);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
	@GetMapping("/me")
	public Student me(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
		if (token == null) return null;
		String username = jwtUtil.extractUsername(token);
		User user = userService.getUserByName(username);
		if (user == null) return null;
		return studentService.findByEmail(user.getEmail());
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@PutMapping("/update-student")
	public Student updateStudent(@RequestBody Student studentDetails) {
		return studentService.updateStudent(studentDetails);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@DeleteMapping("/delete-student/{id}")
	public String deleteStudent(@PathVariable long id) {
		return studentService.deleteStudent(id);
	}

	// Add pageable endpoint alongside existing ones
@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
@GetMapping("/page")
public List<Student> page(@RequestParam int page, @RequestParam int size) {
	// If you move to Spring Data Repos, return Page<Student>; with manual Hibernate, add limit/offset in DAO
	throw new UnsupportedOperationException("Implement DAO pagination or migrate to Spring Data JPA Repositories.");
}
}