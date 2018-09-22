package com.revature.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.services.HeroDAO;
import com.revature.services.HeroDAOImpl;

@Controller
public class HeroController {
	
	@RequestMapping(value="/hero", method=RequestMethod.GET)
	@ResponseBody
	public String getHero(@RequestParam("id") int heroId) {
//		HeroDAO heroDao = new HeroDAOImpl();
		
//		Hero hero = heroDao.
		
		return null;
	}
	
	
	
}
