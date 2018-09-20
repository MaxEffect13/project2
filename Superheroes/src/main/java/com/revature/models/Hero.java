package com.revature.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table	
public class Hero implements Serializable{

	@Id
	@Column(name="HERO_ID", unique = true)
	private int heroId;
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
	
	@Column(name = "HERO_NAME")
	private String name;

	
	public Hero() {
		super();
		
	}

	
	public Hero(int heroId, int intelligence, int strength, int speed, int durability, int power, int combat,
			String name) {
		super();
		this.heroId = heroId;
		this.intelligence = intelligence;
		this.strength = strength;
		this.speed = speed;
		this.durability = durability;
		this.power = power;
		this.combat = combat;
		this.name = name;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + combat;
		result = prime * result + durability;
		result = prime * result + heroId;
		result = prime * result + intelligence;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + power;
		result = prime * result + speed;
		result = prime * result + strength;
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
		Hero other = (Hero) obj;
		if (combat != other.combat)
			return false;
		if (durability != other.durability)
			return false;
		if (heroId != other.heroId)
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
		return true;
	}

	@Override
	public String toString() {
		return "Hero [heroId=" + heroId + ", intelligence=" + intelligence + ", strength=" + strength + ", speed="
				+ speed + ", durability=" + durability + ", power=" + power + ", combat=" + combat + ", name=" + name
				+ "]";
	}

	
	
}