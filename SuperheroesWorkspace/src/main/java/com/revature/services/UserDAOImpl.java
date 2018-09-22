package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.MyUser;
import com.revature.repositories.UserRepository;

@Service
public class UserDAOImpl implements UserDAO{

	@Autowired
	UserRepository userRepo;
	
	@Override
	public List<MyUser> findAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public MyUser findUserById(Long id) {
		return userRepo.getOne(id);
	}

	@Override
	public MyUser addUser(MyUser newUser) {
		return userRepo.save(newUser);
	}

	@Override
	public MyUser updateUser(MyUser user) {
		return userRepo.save(user);
	}

	@Override
	public MyUser deleteUser(MyUser user) {
		userRepo.delete(user);
		return user;
	}

	@Override
	public MyUser findUserByUsername(String username) {
		if (userRepo == null) {
			System.out.println("It's Not initializing correctly.");
		}
		return userRepo.findUserByUsername(username);
			}

	
}
