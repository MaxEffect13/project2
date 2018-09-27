package com.revature.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Team;
import com.revature.services.CombatService;

@RestController
@RequestMapping("/combat")
@CrossOrigin
public class CombatController {

	@Autowired
	CombatService combatService;
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public Team compareStats(@RequestParam("teamId1") Team team1, @RequestParam("teamId2") Team team2, HttpServletResponse response) throws IOException{
		return combatService.compareStats(team1, team2);
	}

	
}