package ecs;

import utility.StringUtility;

public class Renderable extends AbstractComponent {
	
	private String modelName;
	
	public Renderable(int eID){
		super(eID);
	}
	
	@Override
	public Renderable clone() {
		Renderable deepCopy = new Renderable(super.eID)
				.setModelName(this.modelName);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		final String[] tags = {"MODEL"};
		final Object[] data = {modelName};
		return StringUtility.toStringHelper("Renderable", eID, tags, data);
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public Renderable setModelName(String modelName) {
		this.modelName = modelName;
		return this;
	}
	
}
