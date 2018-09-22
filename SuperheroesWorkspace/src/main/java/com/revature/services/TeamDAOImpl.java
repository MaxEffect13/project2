package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.Team;
import com.revature.repositories.TeamRepository;

@Service
public class TeamDAOImpl implements TeamDAO{

	@Autowired
	TeamRepository teamRepo;
	
	@Override
	public List<Team> findAllTeams() {
		return teamRepo.findAll();
	}

	@Override
	public Team findTeamById(Long id) {
		return teamRepo.getOne(id);
	}

	@Override
	public Team findTeamByName(String name) {
		return teamRepo.findTeamByName(name);
	}
	
	@Override
	public List<Team> findTeamByUserId(Long userid) {
		return teamRepo.findTeamsByUserId(userid);
	}

	@Override
	public Team addTeam(Team newTeam) {
		return teamRepo.save(newTeam);
	}

	@Override
	public Team updateTeam(Team team) {
		return teamRepo.save(team);
	}

	@Override
	public Team deleteTeam(Team team) {
		teamRepo.delete(team);
		return team;
	}

	
}
