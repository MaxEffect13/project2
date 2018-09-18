package com.revature.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeamSequence")
	@SequenceGenerator(name = "InvoiceSequence", allocationSize = 1, sequenceName = "SQ_Team_PK")
	
	@Column(name = "TEAM_ID")
	
	private int id;
	@Column(name = "TEAM_NAME", unique = true)
	private String name;
	@Column
	private int intelligence;
	@Column
	private int strength;
	@Column
	private int speed;
	@Column
	private int durability;
	@Column
	private int power;
	@Column
	private int combat;
	@ManyToOne
	@JoinColumn(name= "USER_ID")
	private User user;
	
	
	
}
