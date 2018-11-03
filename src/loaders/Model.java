package loaders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Model {
	
	private int referenceCounter = 0;
	
	private String meshName = "default";
	private String materialName = "default";
	
	public Model(String meshName, String materialName) {
		this.meshName = meshName;
		this.materialName = materialName;
	}
	
	public String getMeshName() {
		return meshName;
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	public void refCountUp() {
		referenceCounter++;
	}
	
	/**
	 * Decrease the reference counter towards this model instance. 
	 * @return
	 * Returns whether this instance is still alive after the method call.
	 */
	public boolean refCountDown() {
		referenceCounter--;
		return (referenceCounter > 0) ? true : false;
	}
	
}
