package com.asn.trac.api.dao;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asn.trac.api.entity.Role;

@Repository
public class RoleDao {

	@Autowired
	private EntityManagerFactory emf;

	public Role findByName(String name) {
		Session session = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			Criteria criteria = session.createCriteria(Role.class);
			criteria.add(Restrictions.eq("name", name));
			return (Role) criteria.uniqueResult();
		} finally {
			if (session != null) session.close();
		}
	}

	public Role save(Role role) {
		Session session = null;
		Transaction tx = null;
		try {
			session = emf.unwrap(org.hibernate.SessionFactory.class).openSession();
			tx = session.beginTransaction();
			session.save(role);
			tx.commit();
			return role;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) session.close();
		}
	}
}


