package ecs;

public abstract class Component {
	
	protected int eID;
	
	protected Component(int eID) {
		this.eID = eID;
	}
	
	public abstract int getEID();
	public abstract void setEID(int eID);
	
	@Override
	public abstract Component clone();
	@Override
	public abstract String toString();
}
