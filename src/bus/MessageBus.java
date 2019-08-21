package bus;

import java.util.HashMap;
import java.util.LinkedList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class MessageBus {
	
	// Singleton instance
	private static MessageBus messageBusInstance = null;
		
	private HashMap<Recipients, LinkedList<Message>> queues = new HashMap<>();
	
	private MessageBus() {
		queues = new HashMap<>();
		for(Recipients recipient : Recipients.values()) {
			queues.put(recipient, new LinkedList<Message>());
		}
	}
	
	public static MessageBus getInstance() {
		if(messageBusInstance == null) {
			messageBusInstance = new MessageBus();
		}
		return messageBusInstance;
	}
	
	public Message getNextMessage(Recipients listener) {
		LinkedList<Message> queue = queues.get(listener);
		return (queue.isEmpty()) ? null : queue.removeFirst(); 
	}
	
	/** New generic message function to reduce API clutter.
	 * @param recipient = a registered recipient system.
	 * @param behaviourID = a specific behavior (function) to be applied to some data. Resolved inside the recipient system.
	 * @param args = data to be passed along to the behavior function that is mapped to behaviorID.
	 * @return
	 */
	public Message messageSystem(Recipients recipient, int behaviorID, Object... args) {
		Message message = new Message(recipient, behaviorID, args);
		queues.get(recipient).addLast(message);
		return message;
	}
	
}
