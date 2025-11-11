package com.asn.trac.api.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asn.trac.api.entity.Student;
import org.hibernate.criterion.Restrictions;

@Repository
public class StudentDao {

	@Autowired
	private EntityManagerFactory emf;

	public List<Student> getAllStudentsById(List<Long> studentIds) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			return session.byMultipleIds(Student.class).multiLoad(studentIds);
		} finally {
			if (session != null) session.close();
		}
	}

	public List<Student> getAllStudents() {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(Student.class);
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public Student createStudent(Student student) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			session.save(student);
			tx.commit();
			return student;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}

	public Student getStudentsById(long id) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			return session.get(Student.class, id);
		} finally {
			if (session != null) session.close();
		}
	}

	public Student updateStudent(Student studentDetails) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			session.update(studentDetails);
			tx.commit();
			return studentDetails;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}

	public String deleteStudent(long id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			Student student = session.get(Student.class, id);
			if (student != null) {
				session.delete(student);
				tx.commit();
				return "Deleted !!";
			} else {
				return "Not Exists !!";
			}
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}

	public Student findByEmail(String email) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			org.hibernate.Criteria criteria = session.createCriteria(Student.class);
			criteria.add(Restrictions.eq("email", email));
			return (Student) criteria.uniqueResult();
		} finally {
			if (session != null) session.close();
		}
	}
}