package com.revature.dao;

import java.util.List;

import com.revature.models.Team;

public interface TeamDAO {
	public List<Team> getTeams();
	public Team getTeamById(int id);
	public int createTeam(Team t);
	public List<Team> getTeamsByUser(int userId);
	public List<Team> getTeamsByName(String name);
	public void deleteTeamById(int id);
	public long getTeamCount();
}
