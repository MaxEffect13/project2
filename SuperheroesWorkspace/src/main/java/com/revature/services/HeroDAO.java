package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Hero;

@Service
public interface HeroDAO {
	public List<Hero> findAllHeroes();
	public Hero findHeroById(Long id);
	public Hero addHero(Hero newHero);
	public Hero deleteHero(Hero hero);
}
