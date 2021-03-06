package input;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.sun.nio.file.SensitivityWatchEventModifier;

import bus.MessageBus;
import render.Display;

public class SourceKeyboard implements InputSourceI {
	
	private enum Actions {
		FORWARD,
		BACKWARD,
		LEFT,
		RIGHT,
		JUMP,
		INTERACT,
		ABILITY,
		SHUTDOWN,
		
		LOOK_UP,
		LOOK_DOWN,
		LOOK_LEFT,
		LOOK_RIGHT,
		
		WIREFRAME,
		TELEPORT
	}
	private final HashMap<Actions, Integer> keyMappings;
	private final float sensitivity = 1.0f;
	
	
	// flags
	private boolean doJump = false;
	private boolean doInteract = false;
	private boolean doAbility = false;
	private boolean doTeleport = false;
	private boolean toggleWireframe = false;

	private final Display display;
	private final MessageBus bus;

	public SourceKeyboard(Display disp, MessageBus bus) {
		this.display = disp;
		this.bus = bus;
		// Configure key bindings
		keyMappings = new HashMap<>();
		keyMappings.put(Actions.FORWARD, GLFW.GLFW_KEY_W);
		keyMappings.put(Actions.BACKWARD, GLFW.GLFW_KEY_S);
		keyMappings.put(Actions.LEFT, GLFW.GLFW_KEY_A);
		keyMappings.put(Actions.RIGHT, GLFW.GLFW_KEY_D);
		keyMappings.put(Actions.JUMP, GLFW.GLFW_KEY_SPACE);
		
		keyMappings.put(Actions.INTERACT, GLFW.GLFW_KEY_E);
		keyMappings.put(Actions.ABILITY, GLFW.GLFW_KEY_Q);
		keyMappings.put(Actions.SHUTDOWN, GLFW.GLFW_KEY_ESCAPE);
		
		keyMappings.put(Actions.LOOK_UP, GLFW.GLFW_KEY_UP);
		keyMappings.put(Actions.LOOK_DOWN, GLFW.GLFW_KEY_DOWN);
		keyMappings.put(Actions.LOOK_LEFT, GLFW.GLFW_KEY_LEFT);
		keyMappings.put(Actions.LOOK_RIGHT, GLFW.GLFW_KEY_RIGHT);
		
		keyMappings.put(Actions.WIREFRAME, GLFW.GLFW_KEY_L);
		keyMappings.put(Actions.TELEPORT, GLFW.GLFW_KEY_T);
	}

	@Override
	public float getLookSensitivity() {
		return sensitivity;
	}
	
	@Override
	public Vector3f pollMoveDirection() {
		Vector3f ret = new Vector3f();
		
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.LEFT)) == GLFW.GLFW_PRESS) {
			ret.x = 1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.FORWARD)) == GLFW.GLFW_PRESS) {
			ret.z = 1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.BACKWARD)) == GLFW.GLFW_PRESS) {
			ret.z = -1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.RIGHT)) == GLFW.GLFW_PRESS) {
			ret.x = -1;
		}
		
		return ret;
	}

	@Override
	public boolean doJump() {
		int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.JUMP));

		// dojump is used to ensure that jump can be only triggered once per keypress

		if (!doJump && glfwResult == GLFW.GLFW_PRESS) {
			// key was just pressed
			doJump = true;
			return true;
		} else if (doJump && glfwResult == GLFW.GLFW_RELEASE) {
			// key was just released
			doJump = false;
		}
		// else: key is held down or not pressed

		return false;
	}

	@Override
	public Vector3f pollLookMove() {
		Vector3f ret = new Vector3f();
		
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.LOOK_LEFT)) == GLFW.GLFW_PRESS) {
			ret.x = -1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.LOOK_UP)) == GLFW.GLFW_PRESS) {
			ret.y = 1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.LOOK_DOWN)) == GLFW.GLFW_PRESS) {
			ret.y = -1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.LOOK_RIGHT)) == GLFW.GLFW_PRESS) {
			ret.x = 1;
		}
		
		return ret;
	}
	

	@Override
	public boolean doInteract() {
		System.out.println("hay");
		final int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.INTERACT));
		if(!doInteract && glfwResult == GLFW.GLFW_PRESS) {
			doInteract = true;
			return true;
		} else if (doInteract && glfwResult == GLFW.GLFW_RELEASE) {
			doInteract = false;
		}

		return false;
	}

	@Override
	public boolean doAbility() {
		System.out.println("hay");
		final int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.ABILITY));
		if(!doAbility && glfwResult == GLFW.GLFW_PRESS) {
			doAbility = true;
			System.out.println("true");
			return true;
		} else if(doAbility && glfwResult == GLFW.GLFW_RELEASE) {
			doAbility = false;
		}
		
		return false;
	}

	@Override
	public boolean doTeleport() {
		final int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.TELEPORT));
		if(!doTeleport && glfwResult == GLFW.GLFW_PRESS) {
			doTeleport = true;
			return true;
		} else if (doTeleport && glfwResult == GLFW.GLFW_RELEASE) {
			doTeleport = false;
		}

		return false;
	}

	@Override
	public boolean toggleWireframe() {
		final int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.WIREFRAME));
		if(!toggleWireframe && glfwResult == GLFW.GLFW_PRESS) {
			toggleWireframe = true;
			return true;
		} else if (toggleWireframe && glfwResult == GLFW.GLFW_RELEASE) {
			toggleWireframe = false;
		}

		return false;
	}

	@Override
	public boolean closeGame() {
		return GLFW.glfwGetKey(display.window, keyMappings.get(Actions.SHUTDOWN)) == GLFW.GLFW_PRESS;
	}

}
