package com.revature.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Team;

@RestController
public class TeamController {
	
	@GetMapping("/team")
	public Team getTeamById(@RequestParam("teamId") String teamId,
							HttpServletResponse response)
	{
		try {
			// Attempt to get the team
			//TODO: Get the team using the DAO. 
			Team team = null;
			
			
			// If the team doesn't exist, send status code 400. 
			//TODO: Confirm that this is correct condition when new DAO is implemented
			if (team == null) {
				response.sendError(400);
				return null;
			}
			
			return team;
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return null;
		}
	} // end of getTeamById
	
	
	
	@GetMapping("/team/create")
	public long createTeam(HttpServletRequest request, HttpServletResponse response) {
		try {
			// Attempt to get a session from the user. 
			HttpSession session = request.getSession(false);
			
			// If ther is not a session, throw a 401. 
			if (session == null) {
				response.sendError(401);
				return -1;
			}
			
			// Get the user Id associated with the user. 
			//TODO: get the user by username. 
			
			// Create a new team, using the user id. 
			// TODO: Create a new team using the New DAO
			
			// Return the new team's ID
			throw new IOException("Not yet implemented");
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return -1;
		}
	} // end of createTeam
	
	
	@GetMapping("/team/remove")
	public long removeTeam(@RequestParam("teamId") String teamId, 
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// Attempt to get a session from the user. 
			HttpSession session = request.getSession(false);
			
			// If ther is not a session, throw a 401. 
			if (session == null) {
				response.sendError(401);
				return -1;
			}
			
			// Get the user Id associated with the user. 
			//TODO: get the user by username. 
			
			
			// Get the team associated with the provided teamId. 
			// TODO: Create a new team using the New DAO
			
			// If the user id of the user matches the user id of the team, 
			// remove the team
			// TODO: Remove user if user id = team user id. 
			throw new IOException("Not yet implemented");
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return -1;
		}
	} // end of createTeam
	
}
