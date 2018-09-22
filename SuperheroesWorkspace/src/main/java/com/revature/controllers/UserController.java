package com.revature.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.User;
import com.revature.util.StringHasher;

@RestController
public class UserController {
	
	@PostMapping("/user/create")
	public void postCreateUser(@RequestParam("user") String username,
							@RequestParam("pass") String password,
							@RequestParam("email") String email,
							HttpServletResponse response) 
	{
		//If the username is taken, send a 401 status to signal no autorization
		//TODO: Query the DAO to see if the username exists. 
		
		// If the username isn't taken, add a new user. 
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(StringHasher.sha256Hash(password));
		
		//TODO: Add user to the database and send the success code. 
	}
	
}
