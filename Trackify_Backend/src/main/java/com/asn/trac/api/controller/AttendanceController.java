package com.asn.trac.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;

import com.asn.trac.api.entity.AttendanceRecord;
import com.asn.trac.api.entity.Student;
import com.asn.trac.api.entity.Subject;
import com.asn.trac.api.entity.User;
import com.asn.trac.api.model.AttendanceRecordRequest;
import com.asn.trac.api.service.AttendanceRecordService;
import com.asn.trac.api.service.StudentService;
import com.asn.trac.api.service.SubjectService;
import com.asn.trac.api.service.UserService;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:5176", "http://localhost:5177", "http://localhost:4200"})
public class AttendanceController {

	@Autowired
	private AttendanceRecordService attendanceRecordService;

	@Autowired
	private UserService userService;

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private StudentService studentService;

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/get-all-attendance-records")
	public List<AttendanceRecord> getAllAttendanceRecords() {
		return attendanceRecordService.getAllAttendanceRecords();
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/get-attendance-by-date-subjet/{date}/{subjectId}")
	public List<AttendanceRecord> getAllAttendanceRecords(@PathVariable String date, @PathVariable long subjectId) {
		return attendanceRecordService.getAllAttendanceRecords(date, subjectId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@PostMapping("/take-attendance")
	public AttendanceRecord createAttendanceRecord(@RequestBody AttendanceRecordRequest request) {
		User user = userService.getUserByName(request.getUsername());
		Subject subject = subjectService.getSubjectById(request.getSubjectId());
		List<Student> students = studentService.getAllStudentsById(request.getStudentIds());

		AttendanceRecord attendanceRecord = new AttendanceRecord();
		attendanceRecord.setUser(user);
		attendanceRecord.setSubject(subject);
		attendanceRecord.setDate(request.getDate());
		attendanceRecord.setTime(request.getTime());
		attendanceRecord.setStudents(students);
		attendanceRecord.setNumberOfStudents(request.getStudentIds().size());

		return attendanceRecordService.saveAttendance(attendanceRecord);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
	@GetMapping("/by-student/{studentId}")
	public List<AttendanceRecord> byStudent(@PathVariable Long studentId) {
		return attendanceRecordService.getByStudentId(studentId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
	@GetMapping("/report/subject")
	public List<AttendanceRecord> subjectReport(@RequestParam Long subjectId, @RequestParam String from, @RequestParam String to) {
		return attendanceRecordService.getBySubjectAndRange(subjectId, from, to);
	}
}