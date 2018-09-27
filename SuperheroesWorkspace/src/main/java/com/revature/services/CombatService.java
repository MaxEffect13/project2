package com.revature.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.revature.models.Team;

@Service
public interface CombatService {
	public Team compareStats(Team team1, Team team2);
}
