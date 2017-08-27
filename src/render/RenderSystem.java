package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import ecs.EntityController;
import ecs.Transformable;
import example.DirectionalLight;
import example.Display;
import example.ResourceLoader;
import terrain.TerrainRenderer;

public class RenderSystem {

	EntityController entityController;
	
	ResourceLoader resourceLoader;
	
	LatchOnCamera camera;
	
	EntityRenderer entityRenderer;
	TerrainRenderer terrainRenderer;
	
	private HashMap<String, HashSet<Transformable>> entitiesToRender = new HashMap<>(); // All the currently active transforms for one asset
	
	public RenderSystem(EntityController entityController, ResourceLoader resourceLoader){
		this.entityController = entityController;
		this.resourceLoader = resourceLoader;
		
		// Back-face culling
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BACK);
		// Automatic Gamma-correction
		GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
		// Wire frame mode: GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

		camera = new LatchOnCamera(entityController);
		
		entityRenderer = new EntityRenderer();
		/*terrainRenderer = new TerrainRenderer(
				resourceLoader.getMultiTexture(),
				resourceLoader.getBlendMap()
				);*/
	}
	
	public void run(DirectionalLight sun){
		// Do all the message processing
		// ???
		// Do other processing
		
		// render
		renderScene(sun);
	}
	
	public void materialize(int eID, String assetName){
		// Generate a new Renderable for <eID>
		entityController.addRenderable(eID, assetName);
		// Sort in the new reference for instanced rendering
		if(entitiesToRender.get(assetName) == null){
			entitiesToRender.put(assetName, new HashSet<Transformable>());
			// add +1 to pointer count for this asset
			resourceLoader.load(assetName);
		}
		entitiesToRender.get(assetName).add(entityController.getTransformable(eID));
	}
	
	public void dematerialize(int eID){
		// Get <eID>'s Renderable to access the assetName. Use that as key to narrow down the search.
		String assetName = entityController.getRenderable(eID).getAssetName();
		// Set a reference to the correct list to reduce map queries
		HashSet<Transformable> temp = entitiesToRender.get(assetName);
		// Search the list for the correct Transformable and remove it when found.
		for(Transformable transformable : temp){
			if(transformable.getEID() == eID){
				temp.remove(transformable);
				break;
			}
		}
		resourceLoader.unload(assetName);
		// Make change "official"
		entityController.removeRenderable(eID);
	}
	
	// ============================================================================
	// =================== MASTER RENDERER ACTIVITIES =============================
	// ============================================================================
	
	private void prepareForRendering(){
		glClearColor(0.529f, 0.807f, 0.95f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	private void renderScene(DirectionalLight sun){
		prepareForRendering();
		
		//terrainRenderer.render(resourceLoader, camera, sun, entityController.getPointLightComponents());
		
		entityRenderer.render(resourceLoader, camera, sun, entitiesToRender, entityController.getPointLightComponents());
		// Swap buffer to make changes visible
		GLFW.glfwSwapBuffers(Display.window);
	}
}