package ecs;

import utility.StringUtility;

public class PlayerControllerComponent extends AbstractComponent{
	
	public PlayerControllerComponent(int eID) {
		super(eID);
	}
	
	@Override
	public PlayerControllerComponent clone() {
		PlayerControllerComponent deepCopy = new PlayerControllerComponent(this.eID);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		final String[] tags = {};
		final Object[] data = {};
		return StringUtility.toStringHelper("PlayerControllerComponent", eID, tags, data);
	}
}
