package ecs;

import utility.StringUtility;

public class Renderable extends AbstractComponent {
	
	private String resourceName = "default";
	
	// unused right now
	private String modelName = "default";
	private String materialName = "default";
	
	public Renderable(int eID){
		super(eID);
	}
	
	@Override
	public Renderable clone() {
		Renderable deepCopy = new Renderable(this.eID)
				.setResourceName(this.resourceName)
				.setModelName(this.modelName)
				.setMaterialName(this.materialName);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		final String[] tags = {"RES", "3D", "MAT"};
		final Object[] data = {resourceName, modelName, materialName};
		return StringUtility.toStringHelper("Renderable", eID, tags, data);
	}
	
	public String getResourceName(){
		return resourceName;
	}
	
	public Renderable setResourceName(String assetName) {
		this.resourceName = assetName;
		return this;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public Renderable setModelName(String modelName) {
		this.modelName = modelName;
		return this;
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	public Renderable setMaterialName(String materialName) {
		this.materialName = materialName;
		return this;
	}
	
}
