package com.revature.util;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.revature.models.Hero;
import com.revature.models.Team;

public class TeamStatsHelper {
	
	/** Takes a team, and a hero dao, and updates the teams stats based on the 
	 * heroes that are on it. */
	public static void updateTeamStats(Team team) throws IOException {
		// Start all stats at 0.
		int combat = 0;
		int durability = 0;
		int intelligence = 0;
		int power = 0;
		int speed = 0;
		int strength = 0;
		
		// Add each heroes' stats to the team's overall stats. 
		for (Hero hero : team.getHeroes()) {
			combat += hero.getCombat();
			durability += hero.getDurability();
			intelligence += hero.getIntelligence();
			power += hero.getPower();
			speed += hero.getSpeed();
			strength += hero.getStrength();
		}
		
		// Update the team stats with the total. 
		team.setCombat(combat);
		team.setDurability(durability);
		team.setIntelligence(intelligence);
		team.setPower(power);
		team.setSpeed(speed);
		team.setStrength(strength);
	} // end of updateTeamStats
	
} // end of TeamStatsHelper
