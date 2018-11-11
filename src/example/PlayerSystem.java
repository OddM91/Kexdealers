package example;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
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
	
	// op codes
	public static final int MOVE = 0;
	public static final int INTERACT = 1;
	public static final int JUMP = 2;
	public static final int LOOK = 3; 

	private Vector3f moveVec = new Vector3f();

	// -- player params
	private static final int PLAYER_ID = 0; //look into file to choose the correct one :S
	private final float walkSpeed = 32.5f;
	private final Vector3f jumpForce = new Vector3f(0, 90_000.0f, 0);
	private final Vector3f cameraOffset = new Vector3f(0.0f, 10.0f, 0.0f);
	/*
	 * private final Vector3f cameraFPoffset = new Vector3f(0.0f, 10.0f, 0.0f);
	 * private final Vector3f cameraTPoffset = new Vector3f(0.0f, 10.0f, 0.0f);
	 */

	// -- state tracking
	/*
	 * cameraDirective indicates how the camera is supposed to be updated 0: don't
	 * update 1: follow player (FP) 2: look at player 3: follow player (TP) private
	 * int cameraDirective = 1;
	 */

	public PlayerSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}
	
	@Override
	public void run() {
		update();
	}

	@Override
	public void update() {
		
		Vector2f inputMoveDir = new Vector2f();
		Vector2f inputLookDir = new Vector2f();
		boolean inputJump = false;
		boolean inputInteract = false;
		
		// Process messages
		Message message;
		while((message = messageBus.getNextMessage(Recipients.PLAYER)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case JUMP:
				inputJump = true;
				break;
			case INTERACT:
				inputInteract = true;
				break;
			case MOVE:
				inputMoveDir.add((Vector2f) args[0]);
				break;
			case LOOK:
				inputLookDir.add((Vector2f) args[0]);
				break;
			default: System.err.println("Player operation not implemented");
			}
		}
		
		Transformable transformable = entityController.getTransformable(PLAYER_ID);
		PhysicsComponent physics = entityController.getPhysicsComponent(PLAYER_ID);
		// -- Poll input
		Vector2f lookRot = new Vector2f(inputLookDir);
		lookRot.mul((float) super.getFrameTimeMillis());
		
		if (inputInteract) {
			System.out.println("Player interacted");
		}
		
		if (physics.isOnGround()) {
			// player is on ground
			physics.setOnGround(true);
			if (inputJump) {
				messageBus.messageSystem(Recipients.PHYSICS_SYSTEM, PhysicsSystem.SET_FORCE, PLAYER_ID, "jumpForce", jumpForce);
			}
			moveVec.x = inputMoveDir.x;
			moveVec.z = inputMoveDir.y;
			moveVec.mul(walkSpeed);
			moveVec.rotate(transformable.getRotation());
			
		} else {
			// player is mid air
			physics.setOnGround(false);
			messageBus.messageSystem(Recipients.PHYSICS_SYSTEM, PhysicsSystem.REMOVE_FORCE, PLAYER_ID, "jumpForce");
		}
		
		// -- Update player velocity and rotation
		// ..velocity
		physics.setVelocity(new Vector3f(moveVec.x, physics.getVelocity().y(), moveVec.z));
		// ..rotation
		transformable.rotateRadians(0.0f, -lookRot.x(), 0.0f);
		
		// -- Camera update
		FPPCameraComponent camera = entityController.getFPPCameraComponent(PLAYER_ID);
		if (camera != null) {
			camera.rotateYaw(lookRot.x());
			camera.rotatePitch(-lookRot.y());
			Vector3f newCamPos = new Vector3f(transformable.getPosition());
			newCamPos.add(cameraOffset, newCamPos);
			// newCamPos.y = 0.0f;
			camera.setPosition(newCamPos);
		}
	}
	
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// TODO Auto-generated method stub
		
	}
	
}
