package com.revature.services;

import java.util.List;

import com.revature.models.Hero;

public interface HeroDAO {
	public List<Hero> findAllHeroes();
	public Hero findHeroById(Long id);
	public Hero addHero(Hero newHero);
	public Hero deleteHero(Hero hero);
}
