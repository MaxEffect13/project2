package com.revature.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.models.MyUser;
import com.revature.models.LoginUser;
import com.revature.services.UserDAO;
import com.revature.util.StringHasher;

@Controller
public class LoginController {
	private static final Logger LOG = Logger.getLogger(TeamController.class);
	/** A string representing the name of the attribute in a session that 
	 * corresponds to a user's username. */
	public static final String USER_SESSION_ATTR = "username";
	
	/** The time in seconds between client requests before a session is 
	 * invalidated. */
	private static final int SESSION_TIMEOUT = 7200;
	
	/** The interface used to interact with the users repository. This is 
	 * automatically instantiated. */
	@Autowired
	private UserDAO userDao;
	
	/** 
	 * Handles the login request. Expects two parameters to be passed: 
	 * A 'user' for username, and a 'pass' for the password. 
	 * Sends status 200 on a good login. Also creates a session with the given 
	 * username as a session attribute called 'username'. 
	 * Sends status 401 on a bad username / password combination. 
	 * Sends status 400 on a bad request, like missing parameter fields. 
	 * Sends status 500 if there is server side error. 
	 */
	
	@ResponseBody
	@CrossOrigin
	@PostMapping(value="/login", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MyUser postLogin(@RequestBody LoginUser usr, 
				HttpServletRequest request, HttpServletResponse response) {
		
		// Get the username and password parameters from the input json file
		String username = usr.getUser();
		String password = usr.getPass();
		System.out.println("User:" + username + "  Pass:" + password);
		
		
		
		// Wrap in a try catch block just in case there is an error. 
		try {
			// If either field is null, send 400 status for a bad request. 
			if (username == null || password == null) {
				response.sendError(400);
				return null;
			}
			
			// Hash the password for comparison. 
			password = StringHasher.sha256Hash(password);
			
			// Query the database for the user and user's password. 
			// Get a user by it's id or null?
			MyUser user = userDao.findUserByUsernameAndPassword(username, password);
			
			
			// If there are no users by that username and password, send a 401 code 
			// signifying bad user/pass
			if (user == null) {
				response.sendError(401);
				return null;
			}
			
			
			// If the password hash doesn't match, send 401 code signifying bad 
			// username / password
//			if (!user.getPassword().equals(password)) {
//				response.sendError(401);
//				return null;
//			}
			
			// Otherwise, the login was a success. So create a session with the 
			// username. 
			HttpSession session = request.getSession();
			session.setAttribute(USER_SESSION_ATTR, user.getUsername());
			session.setMaxInactiveInterval(SESSION_TIMEOUT);
			
			return user;
			// Status code 200 is sent implicitly by returning naturally.
		} catch (IOException ex) {
			// If there was a problem, send a 500 code. 
			try {response.sendError(500);} catch (IOException ex2) {}
			
			LOG.error("exception", ex);
		}
		
		return null;
	} // end of postLogin
	
	
	/** 
	 * Handles a logout request. If there is a session for this request, this 
	 * function invalidates it. 
	 */
	@ResponseBody
	@CrossOrigin
	@PostMapping("/logout")
	public void postLogout(HttpServletRequest request) {
		// Attempt to get any existing session. 
		HttpSession session = request.getSession(false);
		
		// Put in try/catch in case the session is already invalid. 
		try {
			// If the http request has a session, invalidate it. 
			if (session != null) {
				session.invalidate();
			}
		} catch(IllegalStateException ex) {
			System.err.println("Session for '" 
								+ request.getAttribute(USER_SESSION_ATTR)
								+ "' is already invalid. ");
		}
	} // end of postLogout
	
	
} // end of class LoginController


