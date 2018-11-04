package loaders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import textures.Material;

public class Model {
	
	private int referenceCounter = 0;
	
	private Mesh mesh;
	private Material material;
	
	public Model(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void refCountUp() {
		referenceCounter++;
		mesh.refCountUp();
		material.refCountUp();
	}
	
	/**
	 * Decrease the reference counter towards this model instance. 
	 * Checks and deletes it's mesh and material automatically if this
	 * is the last instance referencing them.
	 * @return
	 * Returns whether this instance is still alive after the method call.
	 */
	public boolean refCountDown() {
		referenceCounter--;
		if(!mesh.refCountDown()) {
			mesh.deleteFromVideoMemory();
		}
		if(!material.refCountDown()) {
			material.deleteFromVideoMemory();
		}
		return (referenceCounter > 0) ? true : false;
	}
	
}
