package example;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.PhysicsComponent;
import ecs.Transformable;
import physics.PhysicsSystem;

public class PlayerSystem extends AbstractSystem {
	
	// opcodes
	public static final int INTERACT = 0;
	public static final int ABILITY = 1;
	public static final int LOOK = 2;
	public static final int JUMP = 3;
	public static final int MOVE = 4;

	// -- player params
	private static final int PLAYER_ID = 0; //look into file to choose the correct one :S
	private final float walkSpeed = 1600.0f;
	private final Vector3fc jumpForce = new Vector3f(0, 100.0f, 0);
	private final Vector3fc cameraOffset = new Vector3f(0.0f, 10.0f, 0.0f);
	
	public PlayerSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}
	
	@Override
	public void run() {
		update();
	}

	@Override
	public void update() {

		final Transformable transformable = entityController.getTransformable(PLAYER_ID);
				
		// Process messages
		Message message;
		while((message = messageBus.getNextMessage(Recipients.PLAYER)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case INTERACT:
				System.out.println("Player interacted");
				break;
			case ABILITY:
				System.out.println("Player used ability");
				break;
			case LOOK:
				final Vector3f cameraInput = (Vector3f) args[0];
				cameraInput.mul(super.getFrameTimeMillis());
				// update camera position and rotation
				final FPPCameraComponent camera = entityController.getFPPCameraComponent(PLAYER_ID);
				if (camera != null) {
					camera.rotateYaw(cameraInput.x());
					camera.rotatePitch(-cameraInput.y());
					final Vector3f newCamPos = new Vector3f(transformable.getPosition());
					newCamPos.add(cameraOffset, newCamPos);
					camera.setPosition(newCamPos);
				}
				// update player rotation
				transformable.rotateRadians(0.0f, -cameraInput.x(), 0.0f);
				break;
			case JUMP:
				messageBus.messageSystem(Recipients.PHYSICS_SYSTEM, PhysicsSystem.ADD_ACCELERATION, PLAYER_ID, "jump", jumpForce);
				break;
			case MOVE:
				final Vector3f move = (Vector3f) args[1];
				move.mul(walkSpeed);
				move.rotate(entityController.getTransformable(PLAYER_ID).getRotation());
				messageBus.messageSystem(Recipients.PHYSICS_SYSTEM, PhysicsSystem.ADD_ACCELERATION, PLAYER_ID, "move", move);
				break;
			default: System.err.println("Player operation not implemented" 
					+message.getBehaviorID());
			}
		}
		
	}
	
	@Override
	public void cleanUp() {
		// TODO: ???
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// TODO: ???
		
	}
	
}
