package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Hero;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {
	public Hero findHeroByName(String name);
	
}
