package com.revature.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.models.MyUser;
import com.revature.models.User;
import com.revature.util.StringHasher;

@Controller
public class LoginController {
	
	/** A string representing the name of the attribute in a session that 
	 * corresponds to a user's username. */
	public static final String USER_SESSION_ATTR = "username";
	
	
	/** 
	 * Handles the login request. Expects two parameters to be passed: 
	 * A 'user' for username, and a 'pass' for the password. 
	 * Sends status 200 on a good login. Also creates a session with the given 
	 * username as a session attribute called 'username'. 
	 * Sends status 401 on a bad username / password combination. 
	 * Sends status 400 on a bad request, like missing parameter fields. 
	 * Sends status 500 if there is server side error. 
	 */
	@PostMapping("/login")
	@ResponseBody
	public void postLogin(HttpServletRequest request, HttpServletResponse response) {
		
		// Get the username and password parameters
		String username = request.getParameter("user");
		String password = request.getParameter("pass");
		
		// Wrap in a try catch block just in case there is an error. 
		try {
			// If either field is null, send 400 status for a bad request. 
			if (username == null || password == null) {
				response.sendError(400);
				return;
			}
			
			// Query the database for the user's password. 
			UserDAO userDao = new UserDAOImpl();
			
			// Get a list of one or zero matching users. 
			List<MyUser> users = userDao.getUserByUsername(username);
			
			// If there are zero users, send a 401 code signifying bad user/pass
			if (users.size() == 0) {
				response.sendError(401);
				return;
			}
			
			// If the password hash doesn't match, send 401 code signifying bad 
			// username / password
			if (!users.get(0).getPassword().equals(StringHasher.sha256Hash(password))) {
				response.sendError(401);
				return;
			}
			
			// Otherwise, the login was a success. So create a session with the 
			// username. 
			HttpSession session = request.getSession();
			session.setAttribute(USER_SESSION_ATTR, users.get(0).getUsername());
			
			// Status code 200 is sent implicitly by returning naturally.
		} catch (IOException ex) {
			// If there was a problem, send a 500 code. 
			try {response.sendError(500);} catch (IOException ex2) {}
			//TODO: Add Logging module. 
			ex.printStackTrace();
		}
	}
	
}