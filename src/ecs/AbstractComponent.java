package ecs;

public abstract class AbstractComponent {
	
	protected int eID;
	
	protected AbstractComponent(int eID) {
		this.eID = eID;
	}
	
	public int getEID() {
		return eID;
	}
	public void setEID(int eID) {
		this.eID = eID;
	}
	
	@Override
	public abstract AbstractComponent clone();
	@Override
	public abstract String toString();
}
