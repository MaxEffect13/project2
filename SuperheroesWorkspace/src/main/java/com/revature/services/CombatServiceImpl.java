package com.revature.services;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.revature.models.Team;

@Service
public class CombatServiceImpl implements CombatService{
	
	public Team compareStats(Team team1, Team team2) {
		
		try {
			if((team1.getCombat()+team1.getDurability()+team1.getIntelligence()+team1.getPower()+team1.getSpeed()+team1.getStrength())>(team2.getCombat()+team2.getDurability()+team2.getIntelligence()+team2.getPower()+team2.getSpeed()+team2.getStrength())) {
			return team1;
			}
			else {
				return team2;
			}
		} catch (EntityNotFoundException e) {
			
			e.printStackTrace();
		}
		return null;
		
	}
}
