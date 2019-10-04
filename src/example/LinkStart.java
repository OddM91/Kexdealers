package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import audio.AudioSystem;
import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.EntityController;
import ics.InventorySystem;
import ics.ItemController;
import input.InputMapper;
import loaders.BlueprintLoader;
import physics.PhysicsSystem;
import render.Display;
import render.RenderSystem;

public class LinkStart implements Runnable{
	
	private static boolean running = false;
	
	private static final int TARGET_FPS = 60;
	
	private final HashMap<String, AbstractSystem> systems = new HashMap<>();
	
	// opcodes
	public static final int SHUTDOWN = 0;
	
	// TIME
	public static float FRAME_TIME = 0;
	private float frameBegin = 0;
	private long tickCounter = 0;
	
	public static void main(String[] args){
		final LinkStart link = new LinkStart();
		final Thread gameloop = new Thread(link, "game_loop");
		running = true;
		gameloop.start();
	}
	
	@Override
	public void run(){
		
		// Window creation
		Display display = null;
		display = new Display(1920, 1080);
		display.create();
		
		// Managers
		EntityController entityController = new EntityController();
		ItemController itemController = new ItemController();
		
		// Message Bus
		MessageBus messageBus = MessageBus.getInstance();
		
		// Input
		InputMapper inputMapper = new InputMapper(display, messageBus);
		
		// Systems - Create a System here if you want to use it :)
		systems.put("RenderSystem", new RenderSystem(messageBus, entityController, display));
		systems.put("TeleportationSystem", new TeleportationSystem(messageBus, entityController));
		systems.put("AudioSystem", new AudioSystem(messageBus, entityController));
		systems.put("PhysicsSystem", new PhysicsSystem(messageBus, entityController));
		systems.put("PlayerSystem", new PlayerSystem(messageBus, entityController));
		systems.put("InventorySystem", new InventorySystem(messageBus, entityController, itemController));

		ArrayList<String> blueprint = BlueprintLoader.loadFromFile("./res/floatingTestingIsland.txt");
		// Entities
		String entityIDs = BlueprintLoader.getLineWith("ENTITIES", blueprint);
		for(String frag : BlueprintLoader.getDataFragments(entityIDs)){
			entityController.directAllocEID(Integer.valueOf(frag));
		} 
		// - Transformable
		ArrayList<String> transformableData = BlueprintLoader.getAllLinesWith("TRANSFORMABLE", blueprint);
		String[] frags = null;
		for(String dataSet : transformableData){
			int eID = BlueprintLoader.extractEID(dataSet);
			frags = BlueprintLoader.getDataFragments(dataSet);
			Vector3f position = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
			float rotX = Float.valueOf(frags[3]);
			float rotY = Float.valueOf(frags[4]);
			float rotZ = Float.valueOf(frags[5]);
			float scale = Float.valueOf(frags[6]);
			entityController.getTransformable(eID)
				.setPosition(position)
				.setRotX(rotX)
				.setRotY(rotY)
				.setRotZ(rotZ)
				.setScale(scale);
		}
		// Components are loaded by their system
		for(AbstractSystem system : systems.values()) {
			system.loadBlueprint(blueprint);
		}
		
		// Lucy's fix
		messageBus.messageSystem(Recipients.RENDER_SYSTEM, RenderSystem.WIREFRAME, false);
		
		// < The Loop >
		while(running){
			
			frameBegin = (float) GLFW.glfwGetTime();
			
			// Process messages
			Message message;
			while((message = messageBus.getNextMessage(Recipients.MAIN)) != null) {
				
				final Object[] args = message.getArgs();
				
				switch(message.getBehaviorID()) {
				case SHUTDOWN:
					running = false;
					break;
				default: System.err.println("Main operation not implemented " 
						+message.getBehaviorID());
				}
			
			}
			
			// Input
			inputMapper.updateInput();
			
			// Teleport
			systems.get("TeleportationSystem").run();
			
			// Physics
			systems.get("PhysicsSystem").run();

			// Player
			systems.get("PlayerSystem").run();
			
			// Audio
			systems.get("AudioSystem").run();
			
			// Render
			systems.get("RenderSystem").run();
			
			if(GLFW.glfwWindowShouldClose(display.window)){
				running = false;
			}
			
			FRAME_TIME = (float) GLFW.glfwGetTime() - frameBegin;
			
			if (tickCounter % (TARGET_FPS*2) == 0) {
				System.out.println((Math.floor(1000 / FRAME_TIME)) / 1000 + " FPS");
			}
			tickCounter++;
		}
		
		display.destroy();
		
		for(AbstractSystem system : systems.values()) {
			system.cleanUp();
		}
	}
	
}
