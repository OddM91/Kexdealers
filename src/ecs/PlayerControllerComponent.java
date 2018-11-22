package ecs;

public class PlayerControllerComponent extends Component{
	
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
		StringBuilder s = new StringBuilder();
		s.append("PlayerControllerComponent<").append(eID).append(">");
		s.append("(");
		s.append(" )");
		return s.toString();
	}
}
