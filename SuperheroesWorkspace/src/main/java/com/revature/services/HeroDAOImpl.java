package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.Hero;
import com.revature.repositories.HeroRepository;

@Service
public class HeroDAOImpl implements HeroDAO{

	@Autowired
	HeroRepository heroRepo;
	
	@Override
	public List<Hero> findAllHeroes() {
		return heroRepo.findAll();
	}

	@Override
	public Hero findHeroById(Long id) {
		return heroRepo.getOne(id);
	}

	@Override
	public Hero addHero(Hero newHero) {
		return heroRepo.save(newHero);
	}

	@Override
	public Hero deleteHero(Hero hero) {
		heroRepo.delete(hero);
		return hero;
		
	}

}
