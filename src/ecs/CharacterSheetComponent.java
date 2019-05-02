package ecs;

import utility.StringUtility;

public class CharacterSheetComponent extends AbstractComponent {
	
	private String name = "default";
	
	private int level = 1;
	
	private int maxHealth;
	private int remHealth;
	private int attack;
	
	
	
	public CharacterSheetComponent(int eID) {
		super(eID);
	}
	
	@Override
	public CharacterSheetComponent clone() {
		final CharacterSheetComponent deepCopy = new CharacterSheetComponent(this.eID)
				.setName(this.name)
				.setLevel(this.level)
				.setMaxHealth(this.maxHealth)
				.setRemainingHealth(this.remHealth)
				.setAttack(this.attack);
		return deepCopy;
	}

	@Override
	public String toString() {
		final String[] tags = {"Name", "Lvl", "HP", "ATK"};
		final Object[] data = {name, level, 
				remHealth +"/" +maxHealth,
				attack};
		return StringUtility.toStringHelper("CharacterSheetComponent", eID, tags, data);
	}
	
	public String getName() {
		return name;
	}
	
	public CharacterSheetComponent setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getLevel() {
		return level;
	}
	
	public CharacterSheetComponent setLevel(int level) {
		this.level = level;
		return this;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public CharacterSheetComponent setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		return this;
	}
	
	public int getRemainingHealth() {
		return remHealth;
	}
	
	public CharacterSheetComponent setRemainingHealth(int remHealth) {
		this.remHealth = remHealth;
		return this;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public CharacterSheetComponent setAttack(int attack) {
		this.attack = attack;
		return this;
	}
}
