package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.models.MyUser;
import com.revature.models.loginUsr;
import com.revature.services.UserDAO;

@Controller
@RequestMapping("/login")
public class LoginController {
	
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
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)

	public void postLogin(@RequestBody loginUsr usr ) {
		
		// Get the username and password parameters
		//String username = request.getParameter("user");
		//String password = request.getParameter("pass");
		
		String username = usr.user;
		String password = usr.pass;
		System.out.println(usr.user + usr.pass);
				
		
		
		// Wrap in a try catch block just in case there is an error. 
//		try {
//			// If either field is null, send 400 status for a bad request. 
//			if (username == null || password == null) {
//				response.sendError(400);
//				return;
//			}
			
			// Query the database for the user and user's password. 
			// Get a user by it's id or null?
			MyUser user = userDao.findUserByUsernameAndPassword(username, password);
			
			
			// If there are no users by that username, send a 401 code 
			// signifying bad user/pass
//			if (user == null) {
//				response.sendError(401);
//				return;
//			}
//			
//			
//			// If the password hash doesn't match, send 401 code signifying bad 
//			// username / password
//			if (!user.getPassword().equals(StringHasher.sha256Hash(password))) {
//				response.sendError(401);
//				return;
//			}
//			
//			// Otherwise, the login was a success. So create a session with the 
//			// username. 
//			HttpSession session = request.getSession();
//			session.setAttribute(USER_SESSION_ATTR, user.getUsername());
//			session.setMaxInactiveInterval(SESSION_TIMEOUT);
//			// Status code 200 is sent implicitly by returning naturally.
//		} catch (IOException ex) {
//			// If there was a problem, send a 500 code. 
//			try {response.sendError(500);} catch (IOException ex2) {}
//			//TODO: Add Logging module. 
//			ex.printStackTrace();
//		}
		}
	
}
