package loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.mokiat.data.front.parser.IMTLParser;
import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.MTLLibrary;
import com.mokiat.data.front.parser.MTLMaterial;
import com.mokiat.data.front.parser.MTLParser;
import com.mokiat.data.front.parser.OBJDataReference;
import com.mokiat.data.front.parser.OBJFace;
import com.mokiat.data.front.parser.OBJMesh;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJNormal;
import com.mokiat.data.front.parser.OBJObject;
import com.mokiat.data.front.parser.OBJParser;
import com.mokiat.data.front.parser.OBJTexCoord;
import com.mokiat.data.front.parser.OBJVertex;

import render.DirectionalLight;
import skybox.Skybox;
import textures.Material;
import utility.MiscUtility;
import wrapper.ModelData;

/*
 * Textures & Meshes
 */
public class GraphicsLoader {
	
	private static final String MATERIAL_DIRECTORY = "./res/MTL/";
	private static final String MODEL_DIRECTORY = "./res/OBJ/";
	
	private final MaterialLoader materialLoader;
	private final CubeMapLoader cubeMapLoader;
	
	private final MeshLoader modelLoader;
	
	private final OBJLoader objLoader;
	
	// NEW OBJ LOADER STUFF
	private HashMap<String, Model> models = new HashMap<>();
	private HashMap<String, Mesh> meshes = new HashMap<>();
	private HashMap<String, Material> materials = new HashMap<>();
	// ---
	
	private Skybox skybox = null;
	
	private DirectionalLight sun = new DirectionalLight();
	
	public GraphicsLoader() {
		// create tools
		materialLoader = new MaterialLoader();
		cubeMapLoader = new CubeMapLoader();
		modelLoader = new MeshLoader();
		objLoader = new OBJLoader();
	}
	
	public void cleanUp() {
		for(Mesh mesh: meshes.values()) {
			mesh.deleteFromVideoMemory();
		}
		for(Material material : materials.values()) {
			material.deleteFromVideoMemory();
		}
		models.clear();
		meshes.clear();
		materials.clear();
		
		unloadSun();
		unloadSkybox();
	}
	
	public Model getModel(String ressourceName) {
		Model model = models.get(ressourceName);
		model.refCountUp();
		return model;
	}
	
	public void loadModel(String resourceName) {
		
		final IOBJParser objParser = new OBJParser();
		final IMTLParser mtlParser = new MTLParser();
		
		try (InputStream inputStream = new FileInputStream(MODEL_DIRECTORY + resourceName + ".obj")) {
			// parse model
			final OBJModel objModel = objParser.parse(inputStream);
			
			// extract all the materials used by this model
			for(String libraryReference : objModel.getMaterialLibraries()) {
				final InputStream mtlStream = new FileInputStream(MATERIAL_DIRECTORY + libraryReference);
				final MTLLibrary mtlLibrary = mtlParser.parse(mtlStream);
				for(MTLMaterial mtlMaterial : mtlLibrary.getMaterials()) {
					// check if a material has been loaded already
					if(materials.containsKey(mtlMaterial.getName())) {
						continue;
					} else {
						final Material material = materialLoader.loadMaterial(mtlMaterial);
						materials.put(libraryReference, material);
					}
				}
			}
			
			// Build Mesh ---
			// I have no idea how indices work with this library.
			// I will also only use the vertex-indices.
			final ArrayList<Float> vertices = new ArrayList<>();
			final ArrayList<Float> normals = new ArrayList<>();
			final ArrayList<Float> texCoords = new ArrayList<>();
			final ArrayList<Integer> indices = new ArrayList<>();
			// Vertices
			for(OBJVertex objVertex : objModel.getVertices()) {
				vertices.add(objVertex.x);
				vertices.add(objVertex.y);
				vertices.add(objVertex.z);
			}
			// Normals
			for(OBJNormal objNormal : objModel.getNormals()) {
				normals.add(objNormal.x);
				normals.add(objNormal.y);
				normals.add(objNormal.z);
			}
			// Texture Coordinates
			for(OBJTexCoord objTexCoord : objModel.getTexCoords()) {
				texCoords.add(objTexCoord.u);
				texCoords.add(objTexCoord.v);
				// texCoords.add(objTexCoord.w);
			}
			// Indices
			for(OBJObject objObject: objModel.getObjects()) {
				for(OBJMesh objMesh : objObject.getMeshes()) {
					for(OBJFace objFace : objMesh.getFaces()) {
						for(OBJDataReference dataRef : objFace.getReferences()) {
							// extract indices
							int index_normal = (dataRef.hasNormalIndex()) ? dataRef.normalIndex : -1;
							// -->
							indices.add(index_normal);
							int index_texCoord = (dataRef.hasTexCoordIndex()) ? dataRef.texCoordIndex : -1;
							int index_vertex = (dataRef.hasVertexIndex()) ? dataRef.vertexIndex : -1;
						}
					}
				}
			}
			for(Integer f : indices) {
				System.out.println(f);
			}
			Mesh mesh = new Mesh(
					MiscUtility.toFloatArray(vertices),
					MiscUtility.toFloatArray(texCoords),
					MiscUtility.toFloatArray(normals),
					MiscUtility.toIntArray(indices)
					);
			mesh.loadToVideoMemory();
			meshes.put(resourceName, mesh);
			
			// assemble Model wrapper
			Model model = new Model(mesh, materials.get(resourceName +".mtl"));
			models.put(resourceName, model);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unloadModel(String ressourceName) {
		Model model = models.get(ressourceName);
		if(model != null) {
			boolean alive = model.refCountDown();
			if(!model.refCountDown()) {
				models.put(ressourceName, null);
			}
		}
	}
	
	// grab an already loaded Skybox
	public Skybox getSkybox(){
		return skybox;
	}
	
	// There can only ever be one skybox active and loaded for now
	public Skybox loadSkybox(float size, String skyboxName){
		// unload whatever Terrain was loaded before
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
