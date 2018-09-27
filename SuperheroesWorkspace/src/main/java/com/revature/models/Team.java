package com.revature.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teamSequence")
	@SequenceGenerator(name = "teamSequence", allocationSize = 1, sequenceName = "SQ_Team_PK")
	@Column(name = "TEAM_ID")
	private Long id;
	
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
	private MyUser user;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(
			name="TEAM_HERO",
			joinColumns = {@JoinColumn(name="TEAM_ID")},
			inverseJoinColumns = {@JoinColumn(name="HERO_ID")})
	private List<Hero> heroes = new ArrayList<>();
	
	

	public Team() {
		super();
		
	}
	
	public Team(Long id, String name, int intelligence, int strength, int speed, int durability, int power, int combat,
			MyUser user) {
		super();
		this.id = id;
		this.name = name;
		this.intelligence = intelligence;
		this.strength = strength;
		this.speed = speed;
		this.durability = durability;
		this.power = power;
		this.combat = combat;
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIntelligence() {
		return intelligence;
	}
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getCombat() {
		return combat;
	}
	public void setCombat(int combat) {
		this.combat = combat;
	}
	public MyUser getUser() {
		return user;
	}
	public void setUser(MyUser user) {
		this.user = user;
	}
	public List<Hero> getHeroes() {
		return heroes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + combat;
		result = prime * result + durability;
		result = prime * result + ((heroes == null) ? 0 : heroes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + intelligence;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + power;
		result = prime * result + speed;
		result = prime * result + strength;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (combat != other.combat)
			return false;
		if (durability != other.durability)
			return false;
		if (heroes == null) {
			if (other.heroes != null)
				return false;
		} else if (!heroes.equals(other.heroes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (intelligence != other.intelligence)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (power != other.power)
			return false;
		if (speed != other.speed)
			return false;
		if (strength != other.strength)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", intelligence=" + intelligence + ", strength=" + strength
				+ ", speed=" + speed + ", durability=" + durability + ", power=" + power + ", combat=" + combat
				+ ", user=" + user + ", heroes=" + heroes + "]";
	}
	
	
	
}
