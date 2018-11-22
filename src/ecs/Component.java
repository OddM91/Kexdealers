package ecs;

public abstract class Component {
	
	protected int eID;
	
	protected Component(int eID) {
		this.eID = eID;
	}
	
	public int getEID() {
		return eID;
	}
	public void setEID(int eID) {
		this.eID = eID;
	}
	
	@Override
	public abstract Component clone();
	@Override
	public abstract String toString();
}
