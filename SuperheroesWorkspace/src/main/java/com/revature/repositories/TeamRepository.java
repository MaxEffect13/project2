package com.revature.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	public Team findTeamByName(String name);
	public List<Team> findTeamsByUserId(Long userId);
}
