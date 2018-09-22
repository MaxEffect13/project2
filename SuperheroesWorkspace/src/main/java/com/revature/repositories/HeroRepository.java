package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Hero;

public interface HeroRepository extends JpaRepository<Hero, Long> {
	public Hero findHeroByname(String name);
	
}
