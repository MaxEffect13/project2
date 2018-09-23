package com.revature.drivers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.util.ConnectionUtil;

public class PushJsonToDatabase {
	
	
	/**
	 * This program reads in the hero json, and uploads it to the server in a 
	 * series of insert requests. 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ObjectMapper om = new ObjectMapper();
		
		// Get the JSON, and unmarshal it to a hash map. The only element in this 
		// map is the list of heroes, which are made up of
		@SuppressWarnings("unchecked")
		HashMap<String, ArrayList> map = om.readValue(new File("src\\main\\resources\\heroes.json"), HashMap.class);
		ArrayList<LinkedHashMap> heroes = map.get("heroes");
		
		// A map that counts the number of times a certain number of null's appeared. 
		HashMap <Integer, AtomicLong> nullCount = new HashMap<>();
		
		// Experiment to get the output of the objects
		for (LinkedHashMap lhm : heroes) {
//			System.out.println(lhm.get("id"));
//			System.out.println(lhm.get("name"));
//			System.out.println(lhm.get("powerstats"));
//			System.out.println(lhm.get("powerstats").getClass());
			
			// Count the number of occurrences of the amount of nulls for each hero's stats. 
			int nullCounter = 0;
			LinkedHashMap stats = ((LinkedHashMap) lhm.get("powerstats"));
			for (Object stat : stats.keySet()) {
				if (stats.get(stat).equals("null")) {
					nullCounter++;
				}
			}
			if (!nullCount.containsKey(nullCounter)) {
				nullCount.put(nullCounter, new AtomicLong(1));
			} else {
				nullCount.get(nullCounter).incrementAndGet();
			}
		}
		
		System.out.println("Nulls: " + nullCount);
		
		// Below here, pull individual stats for each hero. 
		Long id;
		String name;
		String strength;
		String power;
		String durability;
		String intelligence;
		String speed;
		String combat;
		
		PreparedStatement ps = null;
		
		// Get a connection for the database. 
		try (Connection con = ConnectionUtil.getConnection()){
			// For each hero in the list, go through the id, name, and stats 
			// adding it to the database. 
			for (LinkedHashMap hero : heroes) {
				id = Long.valueOf((String) hero.get("id"));
				name = (String) hero.get("name");
				LinkedHashMap stats = (LinkedHashMap)hero.get("powerstats");
				strength = (String) stats.get("strength");
				intelligence = (String) stats.get("intelligence");
				durability = (String) stats.get("durability");
				power = (String) stats.get("power");
				speed = (String) stats.get("speed");
				combat = (String) stats.get("combat");
				
				// If any of the stats equal the string "null", don't add this 
				// hero to the database. 
				final String nullStr = "null";
				if (strength.equals(nullStr)
						|| intelligence.equals(nullStr)
						|| durability.equals(nullStr)
						|| power.equals(nullStr)
						|| speed.equals(nullStr)
						|| combat.equals(nullStr)) 
				{
					System.out.println("Skipping Hero:  " + id + ":" + name);
					continue;
				} // end if
				
				System.out.println("Uploading Hero: " + id + ":" + name);
				
				// Add the hero stats & name & id to the database. 
				final String SQL = "INSERT INTO Hero "
						+ "(hero_id, hero_name, strength, intelligence, "
						+ "durability, power, speed, combat) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				ps = con.prepareStatement(SQL);
				ps.setLong(1, id);
				ps.setString(2, name);
				ps.setString(3, strength);
				ps.setString(4, intelligence);
				ps.setString(5, durability);
				ps.setString(6, power);
				ps.setString(7, speed);
				ps.setString(8, combat);
				
				ps.executeUpdate();
				
				ps.close();
			} // end for
			
		} catch (SQLException e) {
			try { if (ps != null) ps.close(); } catch (SQLException ex2) {}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // end of main
	
} // end of class
