package com.revature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.MyUser;
import com.revature.util.StringHasher;

/** This service helps the process of validating a user's credentials. */
@Service
public class UserValidationService {
	
	@Autowired
	UserDAO userDao;
	
	/** 
	 * Returns a user if the provided credentials are valid, null otherwise. 
	 * @param username - The username of a user. 
	 * @param password - The password associated with the user. 
	 * @return - A user if valid credentials, null otherwise. 
	 */
	public MyUser validCredentials(String username, String password) {
		// First, hash the password for comparison on the DB side. 
		password = StringHasher.sha256Hash(password);
		
		// Then, if a matching username and password are found, return true. 
		return userDao.findUserByUsernameAndPassword(username, password);
	}
	
} // end of class UserValidationService
