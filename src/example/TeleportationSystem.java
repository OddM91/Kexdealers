package example;

import java.util.HashMap;
import java.util.Set;

import org.joml.Vector3f;

import bus.MessageBus;
import bus.MessageListener;
import bus.TeleportationSysMessage;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.Transformable;

public class TeleportationSystem {
	
	private MessageBus messageBus = MessageBus.getInstance();
	
	private EntityController entityController;
	
	private HashMap<String, Teleportation> teleportations;

	public TeleportationSystem(EntityController entityController) {
		this.entityController = entityController;
		
		// some tps
		Teleportation tp00 = new Teleportation("tp00", 
				new Vector3f(100.0f, 0.0f, 100.0f), 
				new Vector3f(660.0f, 0.0f, 500.0f), 
				30.0f);
		Teleportation tp01 = new Teleportation("tp00", 
				new Vector3f(560.0f, 0.0f, 400.0f), 
				new Vector3f(0.0f, 0.0f, 0.0f), 
				30.0f);
		
		
		teleportations = new HashMap<>();
		teleportations.put("tp00", tp00);
		teleportations.put("tp01", tp01);
	}

	public void run() {
		// work message queue
		TeleportationSysMessage message;
		while((message = (TeleportationSysMessage) messageBus.getNextMessage(MessageListener.TELEPORTATION_SYSTEM)) != null) {
			switch(message.getOP()) {
			case SYS_TELEPORTATION_TARGETCOORDS: teleportTo(message.getTargetEID(), message.getDestination());
				break;
			default: System.err.println("Teleportation operation not implemented");
			}
		}
		// for all player entties,
		// check all teleportations
		
		for (FPPCameraComponent player : entityController.getFPPCameraComponents()) {
			for (Teleportation tp : teleportations.values()) {
				Transformable transformable = entityController.getTransformable(player.getEID());
				if (tp.checkTrigger(transformable)) {
					transformable.setPosition(tp.getDestination());
				}
			}
		}
	}

	public Set<String> getAllTeleportations() {
		return teleportations.keySet();
	}
	
	private void teleportTo(int targetEID, Vector3f destination) {
		// > fancy effects <
		// wheeeeeee~~~
		entityController.getTransformable(targetEID).setPosition(destination);
	}
}