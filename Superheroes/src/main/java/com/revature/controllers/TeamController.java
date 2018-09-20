package com.revature.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TeamController {
	
	
	@GetMapping("/team/create")
	@ResponseBody
	public String getTeamCreate() {
		return null;
	}
	
	
}
