package com.revature.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.revature.models.MyUser;
import com.revature.models.Team;
import com.revature.util.HibernateUtil;

public class TeamDAOImpl implements TeamDAO{

	@Override
	public List<Team> getTeams() {
		Session s = HibernateUtil.getSession();
		List<Team> teams = s.createQuery("from Team").list();
		s.close();
		return teams;
	}

	@Override
	public Team getTeamById(int id) {
		Session s = HibernateUtil.getSession();

		Team t = (Team) s.get(Team.class, id);
		s.close();
		return t;
	}

	@Override
	public int createTeam(Team t) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		int teamPK = (int) s.save(t);
		tx.commit();
		s.close();
		return teamPK;
	}

	@Override
	public List<Team> getTeamsByUser(int userId) {
		Session s = HibernateUtil.getSession();
//		
		Query q = s.getNamedQuery("getTeamByUser");
		q.setInteger("userVar", userId);
		List<Team> teams = q.list();
		s.close();
		return teams;
	}

	@Override
	public List<Team> getTeamsByName(String name) {
		Session s = HibernateUtil.getSession();
		Query q = s.createSQLQuery("SELECT * FROM TEAM WHERE TEAM_NAME = ?").addEntity(Team.class);
		q.setString(0, name);
		List<Team> teams = q.list();
		s.close();
		return teams;
	}

	@Override
	public void deleteTeamById(int id) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Team t = (Team) s.get(Team.class, id);
		if(t != null) {
			s.delete(t);
		}
		tx.commit();
		s.close();
	}

	@Override
	public long getTeamCount() {
		Session s = HibernateUtil.getSession();
		Criteria c = s.createCriteria(Team.class);
		c.setProjection(Projections.rowCount());
		List<Long> rows = c.list();
		s.close();
		return (Long) rows.get(0);
	}

}
