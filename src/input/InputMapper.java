package input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import bus.Recipients;
import example.LinkStart;
import example.PlayerSystem;
import example.TeleportationSystem;
import render.Display;
import render.RenderSystem;

public class InputMapper {
	private Display display;
	private MessageBus messageBus;

	// action state tracking
	private boolean wireframeMode = false;

	private List<InputSourceI> inputSources = new ArrayList<>();

	public InputMapper(Display disp, MessageBus bus) {
		if (!disp.isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		this.display = disp;
		this.messageBus = bus;
		// TODO detect if future input libs present
		GLFW.glfwPollEvents();

		inputSources.add(new SourceKeyboard(disp, bus));
		inputSources.add(new SourceMouse(disp, bus));
		
		// initial scan for gamepads
		for (int i = 0; i <= GLFW.GLFW_JOYSTICK_LAST; i++) {
			if (GLFW.glfwJoystickIsGamepad(i)) {
				inputSources.add(new SourceGamepad(disp, bus, i));
			}
		}

		// joystick callback that adds new gamepads once they are detected
		GLFW.glfwSetJoystickCallback((int jid, int event) -> {
			System.out.println("joy event");
			if (event == GLFW.GLFW_CONNECTED && GLFW.glfwJoystickIsGamepad(jid)) {
				inputSources.add(new SourceGamepad(disp, bus, jid));
			}
		});
	}

	public void updateInput() {
		GLFW.glfwPollEvents();

		// remove source if connection closed
		Iterator<InputSourceI> iter = inputSources.iterator();
		while (iter.hasNext()) {
			if (iter.next().sourceConnectionClosed()) {
				iter.remove();
			}
		}
		
		// process events
		for (InputSourceI is : inputSources) {
			handleActions(is);
		}
	}

	private void handleActions(InputSourceI is) {
		if (is.sourceConnectionClosed()) { // skip if source inactive
			return;
		}
		
		// system
		if (is.closeGame()) {
			messageBus.messageSystem(Recipients.MAIN, LinkStart.SHUTDOWN, null);
		}

		// movement
		messageBus.messageSystem(Recipients.PLAYER, PlayerSystem.MOVE, is.pollMoveDirection());
		if (is.doJump()) {
			messageBus.messageSystem(Recipients.PLAYER, PlayerSystem.JUMP, null);
		}

		// look
		messageBus.messageSystem(Recipients.PLAYER, PlayerSystem.LOOK, is.pollLookMove().mul(is.getLookSensitivity()));

		// action
		if (is.doInteract()) { // interacting with world
			messageBus.messageSystem(Recipients.PLAYER, PlayerSystem.INTERACT, null);
		}

		// debug
		if (is.doAbility()) {
			// TODO
		}
		if (is.doTeleport()) {
			messageBus.messageSystem(Recipients.TELEPORTATION_SYSTEM, TeleportationSystem.TARGETED_TELEPORTATION, 0/*playerID*/, new Vector3f(450.0f, 25.0f, 350.0f));
		}
		if (is.toggleWireframe()) {
			wireframeMode = !wireframeMode;

			if (wireframeMode) {
				messageBus.messageSystem(Recipients.RENDER_SYSTEM, RenderSystem.WIREFRAME, true);
			} else {
				messageBus.messageSystem(Recipients.RENDER_SYSTEM, RenderSystem.WIREFRAME, false);
			}
		}

	}
}
