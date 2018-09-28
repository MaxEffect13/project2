package com.revature.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Hero;
import com.revature.models.MyUser;
import com.revature.models.Team;
import com.revature.services.HeroDAO;
import com.revature.services.TeamDAO;
import com.revature.services.UserDAO;
import com.revature.services.UserValidationService;
import com.revature.util.TeamStatsHelper;

@RestController
@CrossOrigin
@ResponseBody
public class TeamController {
	
	private static final Logger LOG = Logger.getLogger(TeamController.class);
	
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
	
	
	/** Used to validate user credentials, and get user objects. */
	@Autowired
	private UserValidationService validService;
	
	
	@GetMapping(value="/team/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Team> getTeamById(HttpServletResponse response)
	{
		try {
			// Attempt to get the team
			List<Team> teams = teamDao.findAllTeams();
			
			// If the teams don't exist, send status code 410. 
			if (teams == null) {
				LOG.debug("findAllTeams() method called, no teams found");
				response.sendError(410);
				return new LinkedList<>();
			}
			
			return teams;
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch(IOException ex) {
			// If an error occurs, attempt to send code 500. 
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
		return new LinkedList<>();
	} // end of getTeamById
	
	
	@GetMapping(value="/team", produces=MediaType.APPLICATION_JSON_VALUE)
	public Team getAllTeams(@RequestParam("teamId") Long teamId,
							HttpServletResponse response)
	{
		try {
			// Attempt to get the team
			Team team = teamDao.findTeamById(teamId);
			
			// If the team doesn't exist, send status code 410. 
			if (team == null) {
				LOG.debug("Team called by id, no team found for id: '" + teamId);
				response.sendError(410);
				return null;
			}
			
			return team;
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch(IOException ex) {
			// If an error occurs, attempt to send code 500. 
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
		return null;
	} // end of getTeamById
	
	
	
	@PostMapping(value="/team/byuser", consumes=MediaType.APPLICATION_JSON_VALUE)
	public List<Team> getTeamsByUserId(@RequestBody Map<String, Object> json,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// If the provided json map doesn't contain the specified fields, 
			// send a 400 status, BAD_REQUEST. 
			if (!json.containsKey("userId"))
			{
				response.sendError(400, "Required JSON Parameters: teamName, userId");
				return new LinkedList<>(); // Return an empty list instead of null
			}
			
			// Get the parameters, which may be Integer or Long objects. 
			long userId = ((Number) json.get("userId")).longValue();
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserById(userId);
			
			// If the user credentials are not valid, send status 401. 
			if (user == null) {
				response.sendError(401);
				return new LinkedList<>(); // Return an empty list instead of null
			}
			
			// Create a new team, using the user id. 
			List<Team> teams = teamDao.findTeamByUserId(userId);
			
//			for (Team t : teams) {
//				System.out.println(t);
//			}
//			System.out.println("Heroes: " + teams.get(0).getHeroes());
//			System.out.println("Heroes: " + teams.get(0).getUser());
//			ArrayList<Team> arrTeams = new ArrayList<>();
			
			// Return the new team's ID
			return teams;
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
		return new LinkedList<>(); // Return an empty list instead of null
	} // end of getTeamsByUserId
	
	
	
	
	
	@PostMapping(value="/team/create", consumes=MediaType.APPLICATION_JSON_VALUE)
	public Long createTeam(@RequestBody Map<String, Object> json,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// If the provided json map doesn't contain the specified fields, 
			// send a 400 status, BAD_REQUEST. 
			if (!json.containsKey("userId") || !json.containsKey("teamName"))
			{
				response.sendError(400, "Required JSON Parameters: teamName, userId");
				return null;
			}
			
			// Get the parameters, which may be Integer or Long objects. 
			long userId = ((Number) json.get("userId")).longValue();
			String teamName = ((String) json.get("teamName"));
			
			
			// Get the user Id and user role associated with the user. 
			MyUser user = userDao.findUserById(userId);
			
			// If the user credentials are not valid, send status 401. 
			if (user == null) {
				LOG.debug("User credentials are not valid for user with id: " + userId);
				response.sendError(401);
				return null;
			}
			
			if(user.getRole().equals("n")&&teamDao.findTeamByUserId(userId).size()>5) {
				LOG.debug("Non-premium user with id: " + userId + "tried to create more than 5 teams");
				response.sendError(403);
				return null;
				
			}
			else{
				// Create a new team, using the user id. 
				Team newTeam = new Team();
				newTeam.setUser(user);
				newTeam.setName(teamName);
				
				// Add the new team to the team repository, which auto-generates an id.
				teamDao.addTeam(newTeam);
				
				// Return the new team's ID
				return newTeam.getId();
					
				
			}
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
		return null;
	} // end of createTeam
	
	
	@PostMapping(value="/team/remove", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void removeTeam(@RequestBody Map<String, Object> json,
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// If the provided json map doesn't contain the specified fields, 
			// send a 400 status, BAD_REQUEST. 
			if (!json.containsKey("userId") || !json.containsKey("teamId")) 
			{
				LOG.debug("missing params: userId and teamId'" + "'  RequestBody:" + json.toString());
				response.sendError(400, "Required JSON Parameters: teamId, userId");
				return;
			}
			
			// Get the parameters, which may be Integer or Long objects. 
			long userId = ((Number) json.get("userId")).longValue();
			long teamId = ((Number) json.get("teamId")).longValue();
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserById(userId);
			
			// If the user credentials are not valid, send status 401. 
			if (user == null) {
				response.sendError(401);
				return;
			}
			
			
			// Get the team associated with the provided teamId. 
			Team team = teamDao.findTeamById(teamId);
			
			if (team == null) {
				response.sendError(410);
				return;
			}
			
			// If the user id of the user matches the user id of the team, 
			// remove the team
			if (team.getUser().getId() == user.getId()) {
				teamDao.deleteTeam(team);
			}
			// Otherwise, send the 403 status code as the user isn't allowed 
			// to delete that team. 
			else {
				response.sendError(403);
			}
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch(IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
	} // end of removeTeam
	
	
	
	@PostMapping(value="/team/addhero", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addHeroToTeam(@RequestBody Map<String, Object> json,
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// If the provided json map doesn't contain the specified fields, 
			// send a 400 status, BAD_REQUEST. 
			if (!json.containsKey("userId") || !json.containsKey("teamId") 
					|| !json.containsKey("heroId")) 
			{
				response.sendError(400, "Required JSON Parameters: heroId, teamId, userId");
				return;
			}
			
			// Get the parameters, which may be Integer or Long objects. 
			long userId = ((Number) json.get("userId")).longValue();
			long teamId = ((Number) json.get("teamId")).longValue();
			long heroId = ((Number) json.get("heroId")).longValue();
			
			System.out.println("U:" + userId + "  T:" + teamId + "  H:" + heroId);
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserById(userId);
			
			// Get the hero associated with the hero id.
			Hero hero = heroDao.findHeroById(heroId);
			
			// Get the team associated with the team id.
			Team team = teamDao.findTeamById(teamId);
			
			System.out.println(team);
			System.out.println(hero);
			
			System.out.println("asdf");
			
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
			if(team.getHeroes().size()<6) {
			team.getHeroes().add(hero);
			TeamStatsHelper.updateTeamStats(team);
			teamDao.updateTeam(team);
			}
			else {
				response.sendError(403);
				return;
			}
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch (IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
	} // end of addHeroToTeam
	
	
	
	@PostMapping(value="/team/removehero", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void removeHeroFromTeam(@RequestBody Map<String, Object> json,
							HttpServletRequest request, 
							HttpServletResponse response) 
	{
		try {
			// If the provided json map doesn't contain the specified fields, 
			// send a 400 status, BAD_REQUEST. 
			if (!json.containsKey("userId") || !json.containsKey("teamId") 
					|| !json.containsKey("heroId")) 
			{
				response.sendError(400, "Required JSON Parameters: heroId, teamId, userId");
				return;
			}
			
			// Get the parameters, which may be Integer or Long objects. 
			long userId = ((Number) json.get("userId")).longValue();
			long teamId = ((Number) json.get("teamId")).longValue();
			long heroId = ((Number) json.get("heroId")).longValue();
			
			// Get the user Id associated with the user. 
			MyUser user = userDao.findUserById(userId);
			
			// If the user credentials are not valid, send status 401. 
			if (user == null) {
				response.sendError(401);
				return;
			}
			
			// Get the hero associated with the hero id.
			Hero hero = heroDao.findHeroById(heroId);
			
			// Get the team associated with the team id.
			Team team = teamDao.findTeamById(teamId);
			
			// If either is null, depending on repo implementation, send 410, GONE.
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
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		} catch (IOException ex) {
			try {response.sendError(500);} catch (IOException e) {}
			ex.printStackTrace();
		}
	} // end of removeHeroFromTeam
	
	
	
} // end of class TeamController
