package input;

import java.nio.DoubleBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import render.Display;

public class SourceMouse implements InputSourceI {

	private final float sensitivity = 0.04f;
	
	private boolean dointeract = false;
	private Vector3f prevMousePos = new Vector3f();

	private final Display display;
	private final MessageBus bus;

	public SourceMouse(Display disp, MessageBus bus) {
		this.display = disp;
		this.bus = bus;
	}
	
	@Override
	public float getLookSensitivity() {
		return sensitivity;
	}

	@Override
	public Vector3f pollLookMove() {
		DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(display.window, b1, b2);
		
		// yaw -> x, pitch -> y, roll -> z
		Vector3f ret = new Vector3f((float) b1.get(0), (float) b2.get(0), 0.0f);
		ret.sub(prevMousePos);
		ret.mul(new Vector3f(1.0f, -1.0f, 1.0f));

		prevMousePos.set((float) b1.get(0), (float) b2.get(0), 0.0f);

		return ret;
	}

	@Override
	public boolean doInteract() {
		int glfwResult = GLFW.glfwGetMouseButton(display.window, GLFW.GLFW_MOUSE_BUTTON_1);

		// dointeract is used to ensure that interact can be only triggered once per
		// buttonpress

		if (!dointeract && glfwResult >= 1) {
			// key was just pressed
			dointeract = true;
			return true;
		} else if (dointeract && glfwResult <= 0) {
			// key was just released
			dointeract = false;
		}
		// else: key is held down or not pressed

		return false;
	}
}
