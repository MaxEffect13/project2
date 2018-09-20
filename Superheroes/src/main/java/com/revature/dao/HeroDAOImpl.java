package com.revature.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.models.Hero;
import com.revature.models.Team;
import com.revature.util.HibernateUtil;

public class HeroDAOImpl implements HeroDAO{

	@Override
	public List<Hero> getHeroes() {
		Session s = HibernateUtil.getSession();
		List<Hero> heroes = s.createQuery("from Hero").list();
		s.close();
		return heroes;
	}

	@Override
	public Team getTeamById(int id) {
		Session s = HibernateUtil.getSession();

		Team t = (Team) s.get(Team.class, id);
		s.close();
		return t;
	}

	@Override
	public int createHero(Hero h) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		int hPK = (int) s.save(h);
		tx.commit();
		s.close();
		return hPK;
	}

	@Override
	public void updateHero(Hero h) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteHero(int id) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		Hero h = (Hero) s.get(Hero.class, id);
		if(h != null) {
			s.delete(h);
		}
		tx.commit();
		s.close();
	}

}
