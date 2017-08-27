package example;

import org.joml.Vector3f;

public class DirectionalLight {
	
	private Vector3f direction;
	private Vector3f ambient;
	private Vector3f diffuse;
	private Vector3f specular;
	
	public DirectionalLight(Vector3f direction, Vector3f ambient, Vector3f diffuse, Vector3f specular){
		this.direction = direction;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}
	
	public Vector3f getDirection(){
		return direction;
	}
	
	public DirectionalLight setDirection(Vector3f direction){
		this.direction = direction;
		return this;
	}

	public Vector3f getAmbient(){
		return ambient;
	}

	public DirectionalLight setAmbient(Vector3f ambient){
		this.ambient = ambient;
		return this;
	}

	public Vector3f getDiffuse(){
		return diffuse;
	}

	public DirectionalLight setDiffuse(Vector3f diffuse){
		this.diffuse = diffuse;
		return this;
	}

	public Vector3f getSpecular(){
		return specular;
	}

	public DirectionalLight setSpecular(Vector3f specular){
		this.specular = specular;
		return this;
	}
	
}
