package ecs;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import example.LinkStart;

public abstract class AbstractSystem {
	
	protected final MessageBus messageBus;
	protected final EntityController entityController;
	
	protected AbstractSystem(MessageBus messageBus, EntityController entityController) {
		this.messageBus = messageBus;
		this.entityController = entityController;
	}
	
	/*
	 * System thread's run() method. Runs when Thread.start() is called.
	 * Has a loop that runs according to it's own tick rate and calls update() each tick.
	 * As such the run() method is mostly for tick timing.
	 */
	public abstract void run();
	/*
	 * The update() method is called each tick from the run() method and is where each system's
	 * operations are executed in.
	 */
	protected abstract void update();
	/*
	 * The cleanUo() method is supposed to be called before this system's run() method exits and the thread terminates.
	 */
	public abstract void cleanUp();
	/*
	 * Loads data from a blueprint file into the ECS
	 */
	public abstract void loadBlueprint(ArrayList<String> blueprint);
	/**
	 * Returns current frame time in seconds
	 * @return current frame time in seconds
	 */
	protected float getFrameTimeSecs() {
		return LinkStart.FRAME_TIME / 1000;
	}
	/**
	 * Returns current frame time in milliseconds
	 * @return current frame time in milliseconds
	 */
	protected float getFrameTimeMillis() {
		return LinkStart.FRAME_TIME;
	}
}
