package com.revature.controllers;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
	
	/** A logger for this class. */
	private static final Logger LOG = Logger.getLogger(UserController.class);
	
	
	
	/** A string representing the user id key from input json. */
	private static final String USER_ID = "userId";
	/** A string representing the username key from input json. */
	private static final String USERNAME = "username";
	/** A string representing the password key from input json. */
	private static final String PASSWORD = "password";
	/** A string representing the email key from input json. */
	private static final String EMAIL = "email";
	/** A string representing the user role key from input json. */
	private static final String ROLE = "role";
	
	
	@PostMapping(value="/user", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public MyUser getUser(@RequestBody Map<String, Object> jsonMap, 
						HttpServletRequest request, 
						HttpServletResponse response) throws IOException 
	{
		try {
			// Validate all the parameters exist
			if (!jsonMap.containsKey(USER_ID)) {
				LOG.debug("missing param: '" + USER_ID + "'  RequestBody:" + jsonMap.toString());
				response.sendError(400);
				return null;
			}
			
			// Log the user id 
			LOG.debug("Request Body: " + jsonMap.toString());
			
			// Get the username
			long userId = ((Number)jsonMap.get(USER_ID)).longValue();
			
			
			
			// If the request has a session, get the user associated with it. 
			MyUser user = userDao.findUserById(userId);
			
			// Blank out the password field. No need to send that. 
			user.setPassword("");
			
			return user;
		} catch(EntityNotFoundException ex) {
			// If one of the entities doesn't have a valid id, send 410, GONE.
			// Needs to be done here as the repository does lazy initialization. 
			// This means we can't trap the actual call to the database. 
			try {response.sendError(410);} catch (IOException e) {/* Do nothing on failing to send 500*/}
			LOG.debug("User Id " + jsonMap.get(USER_ID) + " not in the database.");
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
	public MyUser postCreateUser(@RequestBody Map<String, Object> jsonMap,
							HttpServletResponse response) 
	{
		try {
			// Validate all the parameters exist
			if (!jsonMap.containsKey(USERNAME)
					|| !jsonMap.containsKey(PASSWORD)
					|| !jsonMap.containsKey(EMAIL)) {
				LOG.debug("Missing Param(s) '" + USERNAME + "', '" 
								+ PASSWORD + "', '" + EMAIL 
								+ "'  RequestBody: " + jsonMap.toString());
				response.sendError(400);
				return null;
			}
			
			// Get the parameters from the json
			String username = (String) jsonMap.get(USERNAME);
			String password = (String) jsonMap.get(PASSWORD);
			String email = (String) jsonMap.get(EMAIL);
			
			
			//If the username is taken, send a 401 status to signal no authorization
			if (userDao.findUserByUsername(username) != null) {
				LOG.debug("Username '" + username + "' is already in the database");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return null;
			}
			
			// If the username isn't taken, add a new user. 
			MyUser user = new MyUser();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(StringHasher.sha256Hash(password));
			user.setRole(DEFAULT_ROLE);
			
			// Add the user to the database
			userDao.addUser(user);
			
			// Log that the user was added
			LOG.debug("User Added: " + user.toString());
			
			return user;
		} catch (Exception ex) {
			// If a problem occurred, attempt to send a status 500. 
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch(IOException ex2) {/* Do nothing on failing to send 500*/}
			LOG.error("Exception!", ex);
			return null;
		}
	} // end of createUser
	
	
	@PostMapping("/user/update")
	public void postUpdateUser(@RequestBody Map<String, Object> jsonMap,
							HttpServletResponse response) 
	{
		try {
			// Validate all the parameters exist
			if (!jsonMap.containsKey(USERNAME)
					|| !jsonMap.containsKey(PASSWORD)
					|| !jsonMap.containsKey(EMAIL)
					|| !jsonMap.containsKey(ROLE)) {
				LOG.debug("Missing Param(s) '" + USERNAME + "', '" 
						+ PASSWORD + "', '" + EMAIL + "', '" + ROLE
						+ "'  RequestBody: " + jsonMap.toString());
				response.sendError(400);
				return;
			}
			
			LOG.debug("Request Body: " + jsonMap.toString());
			
			
			// Get the parameters from the json
			String username = (String) jsonMap.get(USERNAME);
			String password = (String) jsonMap.get(PASSWORD);
			String email = (String) jsonMap.get(EMAIL);
			String role = (String) jsonMap.get(ROLE);
			
			// Attempt to find the user by username
			MyUser user = userDao.findUserByUsername(username);
			
			//If the username isn't found, send a 401 status to signal no authorization
			if (user == null) {
				LOG.debug("Username '" + username + "' is NOT in the database");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			
			// If the exists, update it with new parameters. 
			user.setEmail(email);
			user.setPassword(StringHasher.sha256Hash(password));
			user.setRole(role);
			
			// Update the user on the database side
			userDao.updateUser(user);
			
			// Log that a user was updated. 
			LOG.debug("User Updated: " + user);
		} catch (Exception ex) {
			// If a problem occurred, attempt to send a status 500. 
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch(IOException ex2) {/* Do nothing on failing to send 500*/}
			LOG.error("Exception!", ex);
		}
	} // end of postUpdateUser
	
} // end of class UserController
