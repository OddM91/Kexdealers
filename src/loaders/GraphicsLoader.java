package loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import animation.AnimatedModel;
import animation.Animation;
import colladaLoader.ColladaLoader;
import example.AssetData;
import render.DirectionalLight;
import skybox.Skybox;
import terrain.Terrain;
import terrain.TerrainMesh;
import textures.Material;
import textures.MultiTexture;
import textures.Texture;
import utility.File;
import utility.MiscUtility;
import wrapper.ModelData;
import wrapper.RawMesh;

/*
 * Textures & Meshes
 */
public class GraphicsLoader {
	
	private static final String MATERIAL_DIRECTORY = "./res/texures/";
	private static final String MODEL_DIRECTORY = "./res/models/";
	
	private final MaterialLoader materialLoader;
	private final CubeMapLoader cubeMapLoader;
	
	private final MeshLoader modelLoader;
	private final TerrainMeshLoader terrainMeshLoader;
	
	private final OBJLoader objLoader;
	
	private final ColladaLoader colladaLoader;
	
	private final HashMap<String, Model> models = new HashMap<>();
	private final HashMap<String, Mesh> meshes = new HashMap<>();
	private final HashMap<String, AnimatedModel> animatedModels = new HashMap<>();
	private final HashMap<String, AnimatedMesh> animatedMesh = new HashMap<>();	
	private final HashMap<String, HashMap<String, Animation>> animations = new HashMap<>();
	private final HashMap<String, Material> materials = new HashMap<>();
	
	private HashMap<String, Integer> pointerCounter3D = new HashMap<>();
	private HashMap<String, AssetData> assets3D = new HashMap<>();
	
	private Terrain terrain = null;
	
	private Skybox skybox = null;
	
	private DirectionalLight sun = new DirectionalLight();
	
	public GraphicsLoader() {
		// create tools
		materialLoader = new MaterialLoader();
		cubeMapLoader = new CubeMapLoader();
		modelLoader = new MeshLoader();
		terrainMeshLoader = new TerrainMeshLoader(modelLoader);
		objLoader = new OBJLoader();
		colladaLoader = new ColladaLoader();
	}
	
	public Model getModel(String resourceName) {
		Model model = models.get(resourceName);
		model.refCountUp();
		return model;
	}
	
	public void loadModel(String resourceName) {
		final File file = new File(MODEL_DIRECTORY, resourceName, ".dae");
		final Model model = colladaLoader.loadModel(file);
	}
	
	public void unloadModel(String resourceName) {
		Model model = models.get(resourceName);
		if(model != null) {
			boolean alive = model.refCountDown();
			if(!model.refCountDown()) {
				models.put(resourceName, null);
			}
		}
	}
	
	public AnimatedModel getAnimatedModel(String resourceName) {
		AnimatedModel animatedModel = animatedModels.get(resourceName);
		animatedModel.refCountUp();
		return animatedModel;
	}
	
	public void loadAnimatedModel(String resourceName) {
		final File file = new File(MODEL_DIRECTORY, resourceName, ".dae");
		final AnimatedModel animatedModel = colladaLoader.loadAnimatedModel(file);
		
	}
	
	public void unloadAnimatedModel(String resourceName) {
		AnimatedModel animatedModel = animatedModels.get(resourceName);
		if(animatedModel != null) {
			boolean alive = animatedModel.refCountDown();
			if(!animatedModel.refCountDown()) {
				animatedModels.put(resourceName, null);
			}
		}
	}

	public Animation getAnimation(String resourceName, String animationName) {
		return animations.get(resourceName).get(animationName);
	}
	
	public AssetData getRessource(String assetName){
		return assets3D.get(assetName);
	}
	
	public void load(String assetName){
		if(!pointerCounter3D.containsKey(assetName) || pointerCounter3D.get(assetName) == 0){
			// load fresh from HDD
			final ModelData modelData = objLoader.loadOBJ(assetName);
			final RawMesh rawMesh = modelLoader.loadToVAO(
					modelData.getVertices(),
					modelData.getIndices(),
					modelData.getTextureCoords(),
					modelData.getNormals());
			final float shininess = 1.0f;
			final Material material = materialLoader.loadMaterial(assetName, shininess);
			final AssetData newAsset = new AssetData(rawMesh, material);
			assets3D.put(assetName, newAsset);
			pointerCounter3D.put(assetName, 1);
		}
	}
	
	public void unload(String assetName){
		if (!pointerCounter3D.containsKey(assetName)) {
			return;
		}
		
		int x = pointerCounter3D.get(assetName);
		if(x == 1){
			pointerCounter3D.put(assetName, x--);
			//unload completely
			// TODO Clean up on sound deletion
			// ^ what does this mean? :S
			assets3D.put(assetName, null);
		}else{
			pointerCounter3D.put(assetName, x--);
		}
	}
	
	// grab an already loaded Terrain
	public Terrain getTerrain(){
		return terrain;
	}
	
	// There can only ever be one one terrain active and loaded for now
	public Terrain loadTerrain(int size, int maxHeight, String heightMap, String[] drgb, String blendMap){
		// unload whatever Terrain was loaded before
		unloadTerrain();
		// build TerrainMesh
		TerrainMesh terrainMesh = terrainMeshLoader.loadTerrainMesh(heightMap, maxHeight, size);
		// build MultiTexture
		String d = "_diffuse";
		String n = "_normal";
		String[] drgb_n = {drgb[0] + d, drgb[1] + d, drgb[2] + d, drgb[3] + d, 
				drgb[0] + n, drgb[1] + n, drgb[2] + n, drgb[3] + n, 
		};
		MultiTexture multiTexture = materialLoader.loadMultiTexture(drgb_n[0], drgb_n[1], drgb_n[2], drgb_n[3], 
				drgb_n[4], drgb_n[5], drgb_n[6], drgb_n[7]);
		// load blendMap
		Texture blendMapTexture = materialLoader.loadBlendMap(blendMap);
		// bundle together and return
		terrain = new Terrain(terrainMesh, multiTexture, blendMapTexture);
		return terrain;
	}
	
	public void unloadTerrain(){
		// blah blah delete stuff
		terrain = null;
	}
	
	// grab an already loaded Skybox
	public Skybox getSkybox(){
		return skybox;
	}
	
	// There can only ever be one skybox active and loaded for now
	public Skybox loadSkybox(float size, String skyboxName){
		// unload whatever Terrain was loaded before
		unloadTerrain();
		// build cube vertices
		float[] vertices = {        
		    -size,  size, -size,
		    -size, -size, -size,
		    size, -size, -size,
		     size, -size, -size,
		     size,  size, -size,
		    -size,  size, -size,

		    -size, -size,  size,
		    -size, -size, -size,
		    -size,  size, -size,
		    -size,  size, -size,
		    -size,  size,  size,
		    -size, -size,  size,

		     size, -size, -size,
		     size, -size,  size,
		     size,  size,  size,
		     size,  size,  size,
		     size,  size, -size,
		     size, -size, -size,

		    -size, -size,  size,
		    -size,  size,  size,
		     size,  size,  size,
		     size,  size,  size,
		     size, -size,  size,
		    -size, -size,  size,

		    -size,  size, -size,
		     size,  size, -size,
		     size,  size,  size,
		     size,  size,  size,
		    -size,  size,  size,
		    -size,  size, -size,

		    -size, -size, -size,
		    -size, -size,  size,
		     size, -size, -size,
		     size, -size, -size,
		    -size, -size,  size,
		     size, -size,  size
		};
		// build texture file paths
		String[] rltBoBaF = {
				skyboxName + "_right",
				skyboxName + "_left",
				skyboxName + "_top",
				skyboxName + "_bottom",
				skyboxName + "_back",
				skyboxName + "_front",
		};
		// loader mesh and textures
		skybox = new Skybox(modelLoader.loadToVAO(vertices, 3), cubeMapLoader.loadCubeMap(rltBoBaF));
		return skybox;
	}
	
	public void unloadSkybox(){
		// blah blah delete stuff
		skybox = null;
	}
	
	public DirectionalLight loadSun(Vector3f directionInverse, Vector3f ambient, Vector3f diffuse, Vector3f specular){
		sun = new DirectionalLight()
				.setDirection(directionInverse)
				.setAmbient(ambient)
				.setDiffuse(diffuse)
				.setSpecular(specular);
		return sun;
	}
	
	public DirectionalLight getSun(){
		return sun;
	}
	
	public void unloadSun(){
		sun = new DirectionalLight();
	}

}
