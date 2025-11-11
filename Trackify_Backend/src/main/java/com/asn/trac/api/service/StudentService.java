package com.asn.trac.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asn.trac.api.dao.StudentDao;
import com.asn.trac.api.entity.Student;

@Service
public class StudentService {

	@Autowired
	private StudentDao dao;

	public List<Student> getAllStudentsById(List<Long> studentIds) {
		return dao.getAllStudentsById(studentIds);
	}

	public List<Student> getAllStudents() {
		return dao.getAllStudents();
	}

	public Student createStudent(Student student) {
		return dao.createStudent(student);
	}

	public Student getStudentById(long id) {
		return dao.getStudentsById(id);
	}

	public Student updateStudent(Student studentDetails) {
		return dao.updateStudent(studentDetails);
	}

	public String deleteStudent(long id) {
		return dao.deleteStudent(id);
	}

	public Student findByEmail(String email) {
		return dao.findByEmail(email);
	}

	/**
	 * If a student with the given email does not exist, create it using the provided name.
	 * Returns the existing or newly created student.
	 */
	public Student createIfMissing(String name, String email) {
		Student existing = dao.findByEmail(email);
		if (existing != null) {
			return existing;
		}
		Student s = new Student();
		s.setName(name);
		s.setEmail(email);
		return dao.createStudent(s);
	}
}
