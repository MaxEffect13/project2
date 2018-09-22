package com.revature.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.MyUser;
import com.revature.services.UserDAO;
import com.revature.services.UserDAOImpl;
import com.revature.util.StringHasher;

@RestController
public class UserController {
	
	/** The interface used to interact with the users repository. This is 
	 * automatically instantiated. */
	@Autowired
	private UserDAO userDao;
	
	
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
	public void postCreateUser(@RequestParam("user") String username,
							@RequestParam("pass") String password,
							@RequestParam("email") String email,
							HttpServletResponse response) 
	{
		try {
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
			//TODO: Figure out the default role for a user. 
			
			userDao.addUser(user);
		} catch (IOException ex) {
			// If a problem occurred, attempt to send a status 500. 
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch(IOException ex2) {}
		}
	} // end of createUser
	
} // end of class UserController
