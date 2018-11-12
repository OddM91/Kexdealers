package input;

import org.joml.Vector3f;

public interface InputSourceI {
	
	default boolean sourceConnectionClosed() { return false; }

	//-- actions
	
	// system
	default boolean closeGame() { return false; }
	
	// movement
	default Vector3f pollMoveDirection() { // negative x: right, positive y: forward. clamped to [-1,1]
		return new Vector3f();
	}
	default public boolean doJump() { return false; }
	
	// look
	default Vector3f pollLookMove() { // negative x: yaw left, positive y: pitch up, negative z: roll left
		return new Vector3f();
	}
	default float getLookSensitivity() { return 1; }
	
	// action
	default boolean doInteract() {return false; } // interacting with world
	
	// debug
	default boolean doAbility() { return false; }
	default boolean doTeleport() { return false; }
	default boolean toggleWireframe() { return false; }
}
