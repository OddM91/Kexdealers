package loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;

import skybox.SkyboxMesh;
import utility.MiscUtility;

public class MeshLoader {
	
	private ArrayList<Integer> vaos = new ArrayList<>();
	private ArrayList<Integer> vbos = new ArrayList<>();
	private ArrayList<Integer> ebos = new ArrayList<>();
	
	// Variable dimension loader
	// TODO only load the skyybox mesh
	public SkyboxMesh loadToVAO(float[] positions, int dimensions){
		int vaoID = createVAO();
		// Store vertices
		storeInVBO(0, dimensions, positions);
		
		unbindVAO();
		return new SkyboxMesh(vaoID, positions.length / dimensions);
	}
	
	private int createVAO(){
		int vaoID = GL30C.glGenVertexArrays();
		vaos.add(vaoID);
		GL30C.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void unbindVAO(){
		GL30C.glBindVertexArray(0);
	}
	
	private void storeInVBO(int index, int coordSize, float[] vertices){
		int vboID = GL15C.glGenBuffers();
		vbos.add(vboID);
		GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = MiscUtility.storeDataInFloatBuffer(vertices);
		GL15C.glBufferData(GL15C.GL_ARRAY_BUFFER, buffer, GL15C.GL_STATIC_DRAW);
		GL20C.glVertexAttribPointer(index, coordSize, GL11C.GL_FLOAT, false, 0, 0);
		GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, 0);
	}
	
	private void storeInEBO(int[] indices){
		int eboID = GL15C.glGenBuffers();
		ebos.add(eboID);
		GL15C.glBindBuffer(GL15C.GL_ELEMENT_ARRAY_BUFFER, eboID);
		IntBuffer buffer = MiscUtility.storeDataInIntBuffer(indices);
		GL15C.glBufferData(GL15C.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15C.GL_STATIC_DRAW);
	}
	
	public void cleanUp(){
		for(int i : vaos){
			GL30C.glDeleteVertexArrays(i);
		}
		for(int i : vbos){
			GL15C.glDeleteBuffers(i);
		}
		for(int i : ebos){
			GL15C.glDeleteBuffers(i);
		}
	}
	
}
