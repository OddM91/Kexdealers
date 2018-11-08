package ecs;

import java.util.ArrayList;

public class Entity {
	
	private int eID;
	
	private ArrayList<EntityController.CompType> componentTypes = new ArrayList<>();
	private ArrayList<Component> components = new ArrayList<>();
	
	public Entity(int eID) {
		this.eID = eID;
	}
	
	public void addComponent(EntityController.CompType type, Component component) {
		componentTypes.add(type);
		components.add(component);
	}
	
	public int getEID() {
		return eID;
	}
	
	public int setEID(int eID) {
		this.eID = eID;
		return eID;
	}
	
	public ArrayList<EntityController.CompType> getComposition(){
		return componentTypes;
	}
	
	public Component getComponentOfType(EntityController.CompType type) {
		return components.get(componentTypes.indexOf(type));
	}
}
