package com.revature.dao;

import java.util.List;

import com.revature.models.Hero;
import com.revature.models.Team;

public interface HeroDAO {
	public List<Hero> getHeroes();
	public int createHero(Hero h);
	public void updateHero(Hero h);
	public void deleteHero(int id);
}
