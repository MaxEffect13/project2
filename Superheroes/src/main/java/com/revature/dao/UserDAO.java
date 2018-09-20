package com.revature.dao;

import java.util.List;

import com.revature.models.MyUser;

public interface UserDAO {
	public List<MyUser> getUsers();
	public MyUser getUserById(int id);
	public List<MyUser> getUserByUsername(String username);
	public int createUser(MyUser u);
	public void updateUser(MyUser u);
	public void deleteUserById(int id);
}
