package com.asn.trac.api.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asn.trac.api.entity.Subject;

@Repository
public class SubjectDao {

	@Autowired
	private EntityManagerFactory emf;

	public Subject getSubjectById(long subjectId) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			return session.get(Subject.class, subjectId);
		} finally {
			if (session != null) session.close();
		}
	}

	public List<Subject> getAllSubjects() {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(Subject.class);
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public Subject createSubject(Subject subject) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Subject.class);
			criteria.add(Restrictions.eq("name", subject.getName()));
			List<Subject> list = criteria.list();
			if (list.isEmpty()) {
				session.save(subject);
				tx.commit();
				return subject;
			}
			return null;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}

	public Subject updateSubject(Subject subjectDetails) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			session.update(subjectDetails);
			tx.commit();
			return subjectDetails;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}

	public String deleteSubject(long id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Subject subject = session.get(Subject.class, id);
			tx = session.beginTransaction();
			session.delete(subject);
			tx.commit();
			return "deleted";
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			if (session != null) session.close();
		}
	}
}