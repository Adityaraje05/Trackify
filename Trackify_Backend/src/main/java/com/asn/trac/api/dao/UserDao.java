package com.asn.trac.api.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asn.trac.api.entity.User;
import com.asn.trac.api.model.LoginRequest;

@Repository
public class UserDao {

	@Autowired
	private EntityManagerFactory emf;

	public User loginUser(LoginRequest request) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			User user = session.get(User.class, request.getUsername());
			if (user != null && user.getPassword().equals(request.getPassword())) {
				return user;
			}
			return null;
		} finally {
			if (session != null) session.close();
		}
	}

	public String deleteUserById(String username) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			User user = session.get(User.class, username);
			if (user != null) {
				tx = session.beginTransaction();
				session.delete(user);
				tx.commit();
				return "deleted";
			}
			return null;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) session.close();
		}
	}

	public User updateUser(User user) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			return user;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) session.close();
		}
	}

	public List<User> getAllUser() {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(User.class);
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public User getUserByName(String username) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			return session.get(User.class, username);
		} finally {
			if (session != null) session.close();
		}
	}

	public User registerUser(User user) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			User existing = session.get(User.class, user.getUsername());
			if (existing == null) {
				tx = session.beginTransaction();
				session.save(user);
				tx.commit();
				return user;
			}
			return null;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) session.close();
		}
	}

	public List<User> getAllAdmins() {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(User.class);
			criteria.createAlias("role", "r");
			criteria.add(Restrictions.eq("r.name", "ADMIN"));
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}

	public List<User> getAllFaculties() {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(User.class);
			criteria.createAlias("role", "r");
			criteria.add(Restrictions.eq("r.name", "FACULTY"));
			return criteria.list();
		} finally {
			if (session != null) session.close();
		}
	}
}