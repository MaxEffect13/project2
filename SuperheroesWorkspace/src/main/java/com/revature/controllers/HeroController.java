package com.revature.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Hero;
import com.revature.services.HeroDAO;
import com.revature.services.HeroDAOImpl;

@RestController
public class HeroController {
	
	/**
	 * Returns a list of all the heroes in the database. 
	 * @return Returns a list of all heroes. 
	 */
	@RequestMapping(value="/allheroes", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Hero> getHeroes() {
		HeroDAO heroDao = new HeroDAOImpl();
		
		// Return a list of all the heroes. 
		return heroDao.findAllHeroes();
	}
	
	/**
	 * Returns the hero specified by the provided ID. 
	 * @param id - The ID of the hero to get. 
	 * @return
	 */
	@RequestMapping(value="/hero", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Hero getHeroById(@RequestParam("id") long id) {
		HeroDAO heroDao = new HeroDAOImpl();
		// Return the hero corresponding to the provided id.
		return heroDao.findHeroById(id);
	}
	
	@GetMapping(value="/rangeheroes", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Hero> getHeroesInRange(@RequestParam("low") long low, 
										@RequestParam("high") long high) 
	{
		HeroDAO heroDao = new HeroDAOImpl();
		
		// TODO: Currently need a dao method to search for a range of heroes. 
		// TODO: in the meantime, this will have to do. 
		// Create a list large enough to hold all the results
		List<Hero> heroes = new ArrayList<>((int)(high-low+1));
		
		// TODO: This is the temporary way of populating the list until a 
		// TODO: Better DAO method for this is available. 
		for (long i=low; i<=high; i++) {
			heroes.add(heroDao.findHeroById(i));
		}
		
		return heroes;
	}
	
}
