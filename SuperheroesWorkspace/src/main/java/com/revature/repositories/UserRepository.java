package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.MyUser;

public interface UserRepository extends JpaRepository<MyUser, Long> {
	public MyUser findUserByUsername(String username);
}
