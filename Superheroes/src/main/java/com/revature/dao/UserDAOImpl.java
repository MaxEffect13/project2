package com.revature.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.models.MyUser;
import com.revature.util.HibernateUtil;

public class UserDAOImpl implements UserDAO{

	@Override
	public List<MyUser> getUsers() {
		Session s = HibernateUtil.getSession();
		String hql = "from User";
		Query q = s.createQuery(hql);
		List<MyUser> users = q.list();
		s.close();
		return users;
		
	}

	@Override
	public MyUser getUserById(int id) {
		Session s = HibernateUtil.getSession();

		MyUser u = (MyUser) s.get(MyUser.class, id);
		s.close();
		return u;
	
	}

	@Override
	public int createUser(MyUser u) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		int UserPK = (int) s.save(u);
		tx.commit();
		s.close();
		return UserPK;
	}

	@Override
	public void updateUser(MyUser u) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		s.update(u);
		tx.commit();
		s.close();
	}

	@Override
	public void deleteUserById(int id) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		MyUser u = (MyUser) s.get(MyUser.class, id);
		if(u != null) {
			s.delete(u);
		}
		tx.commit();
		s.close();
	}

	@Override
	public List<MyUser> getUserByUsername(String username) {
		Session s = HibernateUtil.getSession();
		Query q = s.createSQLQuery("SELECT * FROM MYUSER WHERE USER_NAME = ?").addEntity(MyUser.class);
		q.setString(0, username);
		List<MyUser> users = q.list();
		s.close();
		return users;
	}

}
