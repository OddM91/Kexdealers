package bus;

public class Message {
	
	private final Recipients recipient;
	private final int behaviorID;
	private final Object[] args;

	private boolean complete = false;
	
	public Message(Recipients recipient, int behaviorID, Object... args) {
		this.recipient = recipient;
		this.behaviorID = behaviorID;
		this.args = args;
	}
	
	public Recipients getRecipient() {
		return recipient;
	}
	
	public int getBehaviorID() {
		return behaviorID;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setComplete() {
		complete = true;
	}
	public boolean isComplete() {
		return complete;
	}
}
