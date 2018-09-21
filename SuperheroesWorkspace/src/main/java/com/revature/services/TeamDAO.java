package com.revature.services;

import java.util.List;

import com.revature.models.Team;

public interface TeamDAO {
	public List<Team> findAllTeams();
	public Team findTeamById(Long id);
	public Team findTeamByName(String name);
	public Team findTeamByUserId(Long userid);
	public Team addTeam(Team newTeam);
	public Team updateTeam(Team team);
	public Team deleteTeam(Team team);
}
