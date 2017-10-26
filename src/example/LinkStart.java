package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import ecs.Transformable;
import render.RenderSystem;
import terrain.Terrain;



public class LinkStart implements Runnable{

	private Thread gameloop = null;
	private boolean running = false;
	
	private static int targetFPS = 120;
	public static double timeDelta = 1000 / targetFPS;
	
	public static void main(String[] args){
		LinkStart link = new LinkStart();
		link.start();
		
	}
	
	public void start(){
		running = true;
		gameloop = new Thread(this, "Game Loop");
		gameloop.start();
	}
	
	@Override
	public void run(){
		
		// Window creation
		Display display = new Display(1920, 1080);//1280, 720
		display.create();
		
		// Managers
		EntityController entityController = new EntityController();
		ResourceLoader resourceLoader = new ResourceLoader();
		
		// Systems
		RenderSystem renderSystem = new RenderSystem(entityController, resourceLoader);
		
		// Load an instance from local
		InstanceLoader instanceLoader = new InstanceLoader(entityController, resourceLoader, renderSystem);
		instanceLoader.loadInstanceFromLocal("./res/floatingTestingIsland.txt");
		
		FPPCamera fppCamera = new FPPCamera();
		Player player = new Player(fppCamera, entityController, 0); //look into file to choose the correct one :S
		
		// testing block
		String username = "kekzdealer";
		try {
			System.out.println("connecting");
			Socket socket = new Socket("localhost", 2222);
			System.out.println("connected");
			// User name verification
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(bufferedReader.readLine());
			outputStream.writeChars(username +"\n");
			
			bufferedReader.close();
			outputStream.close();
			socket.close();
		}catch (ConnectException x) {
			System.err.println("Firewall blocking or no server listening");
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		// ---
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			player.update((float)timeDelta);
			gravity(entityController, resourceLoader);
			resourceLoader.getSkybox().updateRotation((float)timeDelta);
			// Render
			renderSystem.run(fppCamera);
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			// FPS: System.out.println((int) (Math.floor(1000 / timeDelta)) / 1000);
		}
		
		display.destroy();
	}
	
	private void gravity(EntityController entityController, ResourceLoader resourceLoader){
		
		HashSet<Transformable> transformables = entityController.getTransformables();
		Terrain terrain = resourceLoader.getTerrain();
		for(Transformable transformable : transformables){
			Vector3f correctedPos = transformable.getPosition();
			correctedPos.set(correctedPos.x, terrain.getHeightAtPoint(correctedPos.x, correctedPos.z), correctedPos.z);
			transformable.setPosition(correctedPos);
		}
	}
	
}
