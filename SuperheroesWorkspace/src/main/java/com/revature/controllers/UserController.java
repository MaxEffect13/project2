package com.revature.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.MyUser;
import com.revature.services.UserDAO;
import com.revature.util.StringHasher;

@RestController
public class UserController {
	
	/** The interface used to interact with the users repository. This is 
	 * automatically instantiated. */
	@Autowired
	private UserDAO userDao;
	
	/** The default premium status symbol. It is 'n' to represent 'no, the user
	 * isn't premium'.  */
	public static final String DEFAULT_ROLE = "n";
	
	
	
	@CrossOrigin
	@GetMapping(value="/user", produces=MediaType.APPLICATION_JSON_VALUE)
	public MyUser getUser(HttpServletRequest request, 
						HttpServletResponse response) throws IOException 
	{
		// Attempt to get a session associated with the request, if one exists.
		HttpSession session = request.getSession(false);
		MyUser myUser = null;
		
		// If the request has a session, get the user associated with it. 
		if (session != null) {
			myUser = userDao.findUserByUsername(
					(String) session.getAttribute(LoginController.USER_SESSION_ATTR));
			// Blank out the password field. No need to send that. 
			myUser.setPassword("");
		}
		// Otherwise, send status 401 to signify that there isn't a session, 
		else {
			response.sendError(401);
		}
		
		return myUser;
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
	
} // end of class UserController
