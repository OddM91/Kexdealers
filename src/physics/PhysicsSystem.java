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
	public static final int SET_FORCE = 0;
	public static final int REMOVE_FORCE = 1;
	public static final int JUMP = 10;
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
			case SET_FORCE:
				// targetEID, forceName, vector
				entityController.getPhysicsComponent((int) args[0])
						.applyForce((String) args[1], (Vector3f) args[2]);
				break;
			case REMOVE_FORCE:
				// targetEID, forceName
				entityController.getPhysicsComponent((int) args[0])
						.removeForce((String) args[1]);
				break;
			case JUMP:
				// targetEID, jump acceleration
				final int jumpTargetEID = (int) args[0];
				final Vector3f jump = (Vector3f) args[1];
				entityController.getPhysicsComponent(jumpTargetEID)
						.applyForce("jump", jump);
				break;
			case MOVE:
				// targetEID, direction vector
				final int moveTargetEID = (int) args[0];
				final Vector3f move = (Vector3f) args[1];
				move.rotate(entityController.getTransformable(moveTargetEID).getRotation());
				entityController.getPhysicsComponent(moveTargetEID)
						.setVelocity(move);
				break;
			default: System.err.println("Physics operation not implemented");
			}
		}
		
		// Process all physics components
		for (PhysicsComponent comp : entityController.getPhysicsComponents()) {
			Transformable transformable = entityController.getTransformable(comp.getEID());
			if (comp.isAffectedByPhysics()) {
				
				// Terrain collision
				float terrainHeight = ZERO_Y_HEIGHT;
				
				if (Float.compare(terrainHeight, transformable.getPosition().y()) > 0) {
					System.out.println("correcting");
					// Whenever entity goes underneath terrain, teleport it back up.
					// Set flag
					comp.setOnGround(true);
					// Correct position
					transformable.setPosition(new Vector3f(
							transformable.getPosition().x(),
							terrainHeight, 
							transformable.getPosition().z()));
					// Reset vertical velocity
					comp.setVelocity(new Vector3f(
							comp.getVelocity().x(),
							0.0f,
							comp.getVelocity().z()));
				} else {
					System.out.println("airborne");
					comp.setOnGround(false);
				}
				if (Float.compare(terrainHeight, transformable.getPosition().y()) == 0) {
					System.out.println("freeze");
					//comp.setAffectedByPhysics(false);
					//System.out.println(comp.toString());
				} 
				
				
				// --- Apply Forces
				final Vector3f totalForce = new Vector3f();
				// basic force
				comp.getAcceleration().mul(comp.getWeight(), totalForce);
				// calculate sum of added forces
				for (Vector3f a : comp.getAppliedForces()) {
					totalForce.add(a).mul(comp.getWeight());
				}
				
				// --- Grounded physics
				if (comp.isOnGround()) {
					// remove gravity acceleration
					comp.removeForce("gravity");
				}
				// --- Airborne physics
				if (!comp.isOnGround()) {
					// add gravity acceleration
					comp.applyForce("gravity", new Vector3f(G_ACCEL));
					// remove jump acceleration
					comp.removeForce("jump");
				}
				
				// --- Velocity
				final Vector3f newVelocity = new Vector3f(comp.getVelocity());
				newVelocity.add(totalForce.mul(super.getFrameTimeMillis()));
				comp.setVelocity(newVelocity);
				
				// --- Position
				transformable.increasePosition(newVelocity.mul(super.getFrameTimeMillis()));
				
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
