package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Team;

@Service
public interface TeamDAO {
	public List<Team> findAllTeams();
	public Team findTeamById(Long id);
	public Team findTeamByName(String name);
	public List<Team> findTeamByUserId(Long userid);
	public Team addTeam(Team newTeam);
	public Team updateTeam(Team team);
	public Team deleteTeam(Team team);
}
