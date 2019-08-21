package ecs;

import org.joml.Vector3f;

import utility.StringUtility;

	public class PointLightComponent extends AbstractComponent{
	
	private final Vector3f position = new Vector3f();
	private final Vector3f ambient = new Vector3f();
	private final Vector3f diffuse = new Vector3f();
	private final Vector3f specular = new Vector3f();
	private float radius = 0.0f;
	private float cutoff = 0.005f;
	
	public PointLightComponent(int eID){
		super(eID);
	}
	
	@Override
	public PointLightComponent clone() {
		PointLightComponent deepCopy = new PointLightComponent(eID)
				.setPosition(new Vector3f(this.position))
				.setAmbient(new Vector3f(this.ambient))
				.setDiffuse(new Vector3f(this.diffuse))
				.setSpecular(new Vector3f(this.specular))
				.setRadius(this.radius)
				.setCutoff(this.cutoff);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		String[] tags = {"T", "A", "D", "S", "R", "C"};
		Object[] data = {StringUtility.toStringVector3f(position),
				StringUtility.toStringVector3f(ambient),
				StringUtility.toStringVector3f(diffuse),
				StringUtility.toStringVector3f(specular),
				radius,
				cutoff
		};
		return StringUtility.toStringHelper("PointLightComponent", eID, tags, data);
	}
	
	public Vector3f getPosition(){
		return position;
	}

	public PointLightComponent setPosition(Vector3f position){
		this.position.set(position);
		return this;
	}

	public Vector3f getAmbient(){
		return ambient;
	}

	public PointLightComponent setAmbient(Vector3f ambient){
		this.ambient.set(ambient);
		return this;
	}

	public Vector3f getDiffuse(){
		return diffuse;
	}

	public PointLightComponent setDiffuse(Vector3f diffuse){
		this.diffuse.set(diffuse);
		return this;
	}

	public Vector3f getSpecular(){
		return specular;
	}

	public PointLightComponent setSpecular(Vector3f specular){
		this.specular.set(specular);
		return this;
	}

	public float getRadius() {
		return radius;
	}

	public PointLightComponent setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	public float getCutoff() {
		return cutoff;
	}

	public PointLightComponent setCutoff(float cutoff) {
		this.cutoff = cutoff;
		return this;
	}
	
}

