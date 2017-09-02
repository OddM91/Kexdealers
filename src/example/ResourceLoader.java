package example;

import java.util.HashMap;

import loaders.MaterialLoader;
import loaders.ModelLoader;
import loaders.OBJLoader;
import loaders.TerrainMeshLoader;
import terrain.Terrain;
import terrain.TerrainMesh;
import textures.Material;
import textures.MultiTexture;
import textures.Texture;
import wrapper.ModelData;
import wrapper.RawMesh;

public class ResourceLoader {
	
	private MaterialLoader materialLoader;
	
	private ModelLoader modelLoader;
	private TerrainMeshLoader terrainMeshLoader;
	
	private OBJLoader objLoader;
	
	private HashMap<String, Integer> pointerCounter = new HashMap<>();
	private HashMap<String, AssetData> assets = new HashMap<>();
	
	private Terrain terrain = null;
	
	public ResourceLoader(){
		// preload keys
		assets.put("lowPolyTree", null);
		pointerCounter.put("lowPolyTree", 0);
		
		assets.put("player", null);
		pointerCounter.put("player", 0);
		
		// create tools
		materialLoader = new MaterialLoader();
		modelLoader = new ModelLoader();
		terrainMeshLoader = new TerrainMeshLoader(modelLoader);
		objLoader = new OBJLoader();
	}
	
	public void load(String assetName){
		int x = pointerCounter.get(assetName);
		if(x == 0){
			// load fresh from HDD
			ModelData modelData = objLoader.loadOBJ(assetName);
			RawMesh rawMesh = modelLoader.loadToVAO(
					modelData.getVertices(), 
					modelData.getIndices(), 
					modelData.getTextureCoords(), 
					modelData.getNormals());
			// random hardcoded default value for shininess = 1
			Material material = materialLoader.loadMaterial(assetName, 1.0f);
			AssetData newAsset = new AssetData(rawMesh, material);
			assets.put(assetName, newAsset);
		}
		x++;
	}
	
	public void unload(String assetName){
		int x = pointerCounter.get(assetName);
		if(x == 1){
			x--;
			//unload completely
			// VAO deletion code missing at this spot
			assets.put(assetName, null);
		}else{
			x--;
		}
	}
	
	public AssetData getRessource(String assetName){
		return assets.get(assetName);
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
		MultiTexture multiTexture = materialLoader.loadMultiTexture(drgb[0], drgb[1], drgb[2], drgb[3]);
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
}
