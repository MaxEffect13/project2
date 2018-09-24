package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
	public MyUser findUserByUsernameAndPassword(String username, String password);
	public MyUser findUserByUsername(String username);
}
