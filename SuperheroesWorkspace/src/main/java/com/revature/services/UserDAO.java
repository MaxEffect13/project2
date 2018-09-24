package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.MyUser;

@Service
public interface UserDAO {
	
	public List<MyUser> findAllUsers();
	public MyUser findUserById(Long id);
	public MyUser addUser(MyUser newUser);
	public MyUser updateUser(MyUser user);
	public MyUser deleteUser(MyUser user);
	public MyUser findUserByUsername(String username);
	public MyUser findUserByUsernameAndPassword(String username, String password);
	public MyUser login(String user, String pass);
}
