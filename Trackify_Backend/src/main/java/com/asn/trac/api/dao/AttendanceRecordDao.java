package com.asn.trac.api.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asn.trac.api.entity.AttendanceRecord;

@Repository
public class AttendanceRecordDao {

	@Autowired
	private EntityManagerFactory emf;

	public List<AttendanceRecord> getAllAttendanceRecords() {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(AttendanceRecord.class);
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public List<AttendanceRecord> getAllAttendanceRecords(String date, long subjectId) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(AttendanceRecord.class, "a");
			criteria.createAlias("a.subject", "sub");
			SimpleExpression dateEq = Restrictions.eq("a.date", date);
			SimpleExpression subjectEq = Restrictions.eq("sub.id", subjectId);
			criteria.add(Restrictions.and(dateEq, subjectEq));
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public AttendanceRecord saveAttendance(AttendanceRecord attendanceRecord) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			session.save(attendanceRecord);
			tx.commit();
			return attendanceRecord;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}

	public List<AttendanceRecord> getByStudentId(Long studentId) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(AttendanceRecord.class, "a");
			criteria.createAlias("a.students", "s");
			criteria.add(Restrictions.eq("s.id", studentId));
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public List<AttendanceRecord> getBySubjectAndRange(Long subjectId, String from, String to) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(AttendanceRecord.class, "a");
			criteria.createAlias("a.subject", "sub");
			criteria.add(Restrictions.eq("sub.id", subjectId));
			criteria.add(Restrictions.ge("a.date", from));
			criteria.add(Restrictions.le("a.date", to));
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}
}