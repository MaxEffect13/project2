package com.revature.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Hero;
import com.revature.models.MyUser;
import com.revature.models.Team;
import com.revature.services.HeroDAO;
import com.revature.services.TeamDAO;
import com.revature.services.UserDAO;
import com.revature.util.TeamStatsHelper;

@RestController
public class TeamController {
	
	/** The interface used to interact with the teams repository. This is 
	 * automatically instantiated. */
	@Autowired
	private TeamDAO teamDao;
	
	/** The interface used to interact with the teams repository. This is 
	 * automatically instantiated. */
	@Autowired
	private UserDAO userDao;
	
	/** The interface used to interact with the hero repository. This is 
	 * automatically instantiated. */
	@Autowired
	private HeroDAO heroDao;
	
	@GetMapping(value="/team", produces=MediaType.APPLICATION_JSON_VALUE)
	public Team getTeamById(@RequestParam("teamId") Long teamId,
							HttpServletResponse response)
	{
		try {
			// Attempt to get the team
			Team team = teamDao.findTeamById(teamId);
			
			// If the team doesn't exist, send status code 410. 
			if (team == null) {
				response.sendError(410);
				return null;
			}
			
			return team;
		} catch(IOException ex) {
			// If an error occurs, attempt to send code 500. 
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return null;
		}
	} // end of getTeamById
	
	
	
	@GetMapping(value="/team/create")
	public Long createTeam(@RequestParam("name") String teamName,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// Attempt to get a session from the user. 
			HttpSession session = request.getSession(false);
			
			// If there is not a session, throw a 401. 
			if (session == null) {
				response.sendError(401);
				return null;
			}
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserByUsername(
					(String) session.getAttribute(LoginController.USER_SESSION_ATTR));
			
			// Create a new team, using the user id. 
			Team newTeam = new Team();
			newTeam.setUser(user);
			newTeam.setName(teamName);
			
			// Add the new team to the team repository, which auto-generates an id.
			teamDao.addTeam(newTeam);
			
			// Return the new team's ID
			return newTeam.getId();
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return null;
		}
	} // end of createTeam
	
	
	@GetMapping("/team/remove")
	public Long removeTeam(@RequestParam("teamId") Long teamId, 
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// Attempt to get a session from the user. 
			HttpSession session = request.getSession(false);
			
			// If there is not a session, throw a 401. 
			if (session == null) {
				response.sendError(401);
				return null;
			}
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserByUsername(
					(String) session.getAttribute(LoginController.USER_SESSION_ATTR));
						
			
			
			// Get the team associated with the provided teamId. 
			Team team = teamDao.findTeamById(teamId);
			
			
			// If the user id of the user matches the user id of the team, 
			// remove the team
			if (team.getUser().getId() == user.getId()) {
				teamDao.deleteTeam(team);
				return team.getId();
			}
			// Otherwise, send the 403 status code as the user isn't allowed 
			// to delete that team. 
			else {
				response.sendError(403);
				return null;
			}
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return null;
		}
	} // end of createTeam
	
	
	
	@GetMapping("/team/addhero")
	public void addHeroToTeam(@RequestParam("teamId") Long teamId, 
							@RequestParam("heroId") Long heroId, 
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// Attempt to get a session from the user. 
			HttpSession session = request.getSession(false);
			
			// If there is not a session, throw a 401. 
			if (session == null) {
				response.sendError(401);
				return;
			}
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserByUsername(
					(String) session.getAttribute(LoginController.USER_SESSION_ATTR));
			
			// Get the hero associated with the hero id.
			Hero hero = heroDao.findHeroById(heroId);
			
			// Get the team associated with the team id.
			Team team = teamDao.findTeamById(teamId);
			
			// Check to make sure that the values are valid. 
			if (team == null || hero == null) {
				response.sendError(410);
				return;
			}
			
			// Check if the user is allowed to access a team. 
			if (team.getUser().getId() != user.getId()) {
				// If not authorized to edit team, send 403, unauthorized. 
				response.sendError(403);
				return;
			}
			
			// If a hero is already on the team, send 418 to mean already on the
			// team.
			if (team.getHeroes().contains(hero)) {
				response.sendError(418);
				return;
			}
			
			// If we passed all other checks, add the hero to the team, and
			// update the database. Also update the stats of the team. 
			team.getHeroes().add(hero);
			TeamStatsHelper.updateTeamStats(team);
			teamDao.updateTeam(team);
		} catch (IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return;
		}
	} // end of addHeroToTeam
	
	
	
	@GetMapping("/team/removehero")
	public void removeHeroFromTeam(@RequestParam("teamId") Long teamId, 
							@RequestParam("heroId") Long heroId, 
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// Attempt to get a session from the user. 
			HttpSession session = request.getSession(false);
			
			// If there is not a session, throw a 401. 
			if (session == null) {
				response.sendError(401);
				return;
			}
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserByUsername(
					(String) session.getAttribute(LoginController.USER_SESSION_ATTR));
			
			// Get the hero associated with the hero id.
			Hero hero = heroDao.findHeroById(heroId);
			
			// Get the team associated with the team id.
			Team team = teamDao.findTeamById(teamId);
			
			// Check to make sure that the values are valid. 
			if (team == null || hero == null) {
				response.sendError(410);
				return;
			}
			
			// Check if the user is allowed to access a team. 
			if (team.getUser().getId() != user.getId()) {
				// If not authorized to edit team, send 403, unauthorized. 
				response.sendError(403);
				return;
			}
			
			// If we passed all other checks, remove the hero from the team, and
			// update the database. 
			if (team.getHeroes().remove(hero)) {
				// Only update the team stats and database if there was a 
				// hero removed. 
				TeamStatsHelper.updateTeamStats(team);
				teamDao.updateTeam(team);
			}
		} catch (IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
			return;
		}
	} // end of removeHeroFromTeam
			
	
	
} // end of class TeamController
