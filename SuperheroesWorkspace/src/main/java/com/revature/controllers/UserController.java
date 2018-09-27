package com.revature.controllers;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.MyUser;
import com.revature.services.UserDAO;
import com.revature.util.StringHasher;

@RestController
@CrossOrigin
@ResponseBody
public class UserController {
	
	/** The interface used to interact with the users repository. This is 
	 * automatically instantiated. */
	@Autowired
	private UserDAO userDao;
	
	/** The default premium status symbol. It is 'n' to represent 'no, the user
	 * isn't premium'.  */
	public static final String DEFAULT_ROLE = "n";
	
	
	
	@PostMapping(value="/user", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public MyUser getUser(@RequestBody Map<String, Object> jsonMap, 
						HttpServletRequest request, 
						HttpServletResponse response) throws IOException 
	{
		try {
			// Validate all the parameters exist
			if (!jsonMap.containsKey("userId")) {
				response.sendError(400);
				return null;
			}
			
			// Get the username
			long userId = ((Number)jsonMap.get("userId")).longValue();
			
			// If the request has a session, get the user associated with it. 
			MyUser user = userDao.findUserById(userId);
			
			// Blank out the password field. No need to send that. 
			user.setPassword("");
			
			return user;
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {}
		}
		return null;
	} // end of getUser
	
	
	
	/**
	 * This method takes parameters, and attempts to create a new user. 
	 * Fails if the username is already taken (sends code 401), parameters are 
	 * otherwise invald (sends code 400), or if some internal error occurs 
	 * (sends code 500), like database connectivity. 
	 * @param username - The username of the new user.
	 * @param password - The password of the new user.
	 * @param email - The email of the new user.
	 * @param response - HttpServletResponse. Provided by servlet. 
	 * 					Allows custom responses.
	 */
	@PostMapping("/user/create")
	public void postCreateUser(@RequestBody Map<String, Object> jsonMap,
							HttpServletResponse response) 
	{
		try {
			// Validate all the parameters exist
			if (!jsonMap.containsKey("username")
					|| !jsonMap.containsKey("password")
					|| !jsonMap.containsKey("email")) {
				response.sendError(400);
				return;
			}
			
			// Get the parameters from the json
			String username = (String) jsonMap.get("username");
			String password = (String) jsonMap.get("password");
			String email = (String) jsonMap.get("email");
			
			
			//If the username is taken, send a 401 status to signal no authorization
			if (userDao.findUserByUsername(username) != null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			
			// If the username isn't taken, add a new user. 
			MyUser user = new MyUser();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(StringHasher.sha256Hash(password));
			user.setRole(DEFAULT_ROLE);
			
			
			userDao.addUser(user);
		} catch (IOException ex) {
			// If a problem occurred, attempt to send a status 500. 
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch(IOException ex2) {}
			ex.printStackTrace();
		}
	} // end of createUser
	
	
	@PostMapping("/user/update")
	public void postUpdateUser(@RequestBody Map<String, Object> jsonMap,
							HttpServletResponse response) 
	{
		try {
			// Validate all the parameters exist
			if (!jsonMap.containsKey("username")
					|| !jsonMap.containsKey("password")
					|| !jsonMap.containsKey("email")
					|| !jsonMap.containsKey("role")) {
				response.sendError(400);
				return;
			}
			
			// Get the parameters from the json
			String username = (String) jsonMap.get("username");
			String password = (String) jsonMap.get("password");
			String email = (String) jsonMap.get("email");
			String role = (String) jsonMap.get("role");
			
			// Attempt to find the user by username
			MyUser user = userDao.findUserByUsername(username);
			
			//If the username isn't found, send a 401 status to signal no authorization
			if (user == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			
			// If the username isn't taken, add a new user. 
			user.setEmail(email);
			user.setPassword(StringHasher.sha256Hash(password));
			user.setRole(role);
			
			
			userDao.updateUser(user);
		} catch (IOException ex) {
			// If a problem occurred, attempt to send a status 500. 
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch(IOException ex2) {}
			ex.printStackTrace();
		}
	} // end of postUpdateUser
	
} // end of class UserController
