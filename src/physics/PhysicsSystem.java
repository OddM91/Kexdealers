package physics;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.sun.org.apache.bcel.internal.util.SyntheticRepository;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.EntityController;
import ecs.PhysicsComponent;
import ecs.Transformable;
import loaders.BlueprintLoader;
import terrain.Terrain;

public class PhysicsSystem extends AbstractSystem {
	
	// opcodes
	public static final int ADD_ACCELERATION = 0;
	public static final int REMOVE_ACCELERATION = 1;
	public static final int MOVE = 11;
	
	private static final Vector3f G_ACCEL = new Vector3f(0, -98.1f, 0).mul(10.0f);
	private static final float ZERO_Y_HEIGHT = 20.0f;
	
	public PhysicsSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}

	@Override
	public void run() {
		update();
	}

	@Override
	public void update() {
		
		// Process messages
		Message message;
		while((message = messageBus.getNextMessage(Recipients.PHYSICS_SYSTEM)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case ADD_ACCELERATION:
				// targetEID, acceleration name, vector
				final int targetEID = (int) args[0];
				final String accelerationName = (String) args[1];
				final Vector3f vector = (Vector3f) args[2];
				entityController.getPhysicsComponent(targetEID)
						.addAcceleration(accelerationName, vector);
				break;
			case REMOVE_ACCELERATION:
				// targetEID, forceName
				entityController.getPhysicsComponent((int) args[0])
						.removeAcceleration((String) args[1]);
				break;
			default: System.err.println("Physics operation not implemented");
			}
		}
		
		// Process all physics components
		for (PhysicsComponent comp : entityController.getPhysicsComponents()) {
			Transformable transformable = entityController.getTransformable(comp.getEID());
			if (comp.isAffectedByPhysics()) {
				
				// Remove gravity if entity is standing on ground
				if(transformable.getPosition().y() == ZERO_Y_HEIGHT) {
					comp.removeAcceleration("gravity");
				}
				
				// Add gravity acceleration to entities that are airborne
				// Remove jumping acceleration to entities that have no ground contact
				if(transformable.getPosition().y() > ZERO_Y_HEIGHT) {
					comp.addAcceleration("gravity", G_ACCEL);
					comp.removeAcceleration("jump");
				}
				
				// aTotal = a1 + a2 + ... +aN
				Vector3f totalAcceleration = new Vector3f();
				for(Vector3f a : comp.getAccelerations()) {
					totalAcceleration.add(a);
				}
				
				// v = a * t
				Vector3f velocity = totalAcceleration.mul(super.getFrameTimeMillis());
				comp.setVelocity(velocity);
				
				// deltaDistance = v * t
				velocity.mul(super.getFrameTimeMillis());
				Vector3f position = new Vector3f(transformable.getPosition());
				position.add(velocity);
				transformable.setPosition(position);
				
				// Teleport everything below ground to ground level
				if(transformable.getPosition().y() < ZERO_Y_HEIGHT) {
					transformable.setPosition(new Vector3f(
							transformable.getPosition().x(),
							ZERO_Y_HEIGHT,
							transformable.getPosition().z()
							));
				}
				
				// Clean up some accelerations
				comp.removeAcceleration("move");
			}
		}
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// - PhysicsComponent
		ArrayList<String> physicsComponentData = BlueprintLoader.getAllLinesWith("PHYSICSCOMPONENT", blueprint);
		String[] frags = null;
		for (String dataSet : physicsComponentData) {
				int eID = -1;

			try {
				eID = BlueprintLoader.extractEID(dataSet);
				if (!entityController.isEntity(eID)) {
					System.err.printf("Physics: couldn't load component. %d is not a valid eID.%n", eID);
					continue;
				}
				
				// extract data and add component
				frags = BlueprintLoader.getDataFragments(dataSet);
				entityController.addPhysicsComponent(eID)
						.setWeight(Float.valueOf(frags[0]))
						.setAffectedByPhysics(Boolean.valueOf(frags[1]));
				
			} catch (NullPointerException | IndexOutOfBoundsException e) {
				System.err.printf("Physics: couldn't load component for entity %d. Too few arguments.%n", eID);
			} catch (IllegalArgumentException e) {
				System.err.printf("Physics: couldn't load component for entity %d. %s%n", eID, e.toString());
			}
		}
	}
}
