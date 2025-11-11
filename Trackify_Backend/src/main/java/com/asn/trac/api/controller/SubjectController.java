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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asn.trac.api.entity.Subject;
import com.asn.trac.api.service.SubjectService;

@RestController
@RequestMapping("/subject")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:5176", "http://localhost:5177", "http://localhost:4200"})
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/get-all-subjects")
	public List<Subject> getAllSubjects() {
		return subjectService.getAllSubjects();
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@PostMapping("/add-subject")
	@CrossOrigin(methods = RequestMethod.POST)
	public Subject createSubject(@RequestBody Subject subject) {
		return subjectService.createSubject(subject);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/get-subject-by-id/{id}")
	public Subject getSubjectById(@PathVariable long id) {
		return subjectService.getSubjectById(id);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@PutMapping("/update-subject")
	@CrossOrigin(methods = RequestMethod.PUT)
	public Subject updateSubject(@RequestBody Subject subjectDetails) {
		return subjectService.updateSubject(subjectDetails);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@DeleteMapping("/delete-subject/{id}")
	public String deleteSubject(@PathVariable long id) {
		return subjectService.deleteSubject(id);
	}
}