package skybox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Skybox {
	
	private SkyboxMesh mesh;
	private int textureID;
	
	private float speed = 0.01f;
	private Vector3f axis = new Vector3f(0.3f, 1.0f, 0.0f);
	
	private Matrix4f modelMatrix = new Matrix4f();
	
	public Skybox(SkyboxMesh mesh, int textureID){
		this.mesh = mesh;
		this.textureID = textureID;
	}
	
	public void updateRotation(float delta){
		modelMatrix.rotate(speed * delta, axis);
	}

	public SkyboxMesh getMesh(){
		return mesh;
	}

	public int getTextureID(){
		return textureID;
	}
	
	public Matrix4f getModelMatrix(){
		return modelMatrix;
	}
	
}
