package ecs;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import utility.StringUtility;


public class Transformable extends AbstractComponent{
	
	private final Matrix4f transformation = new Matrix4f().identity();

	private final Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private final Quaternionf rotation = new Quaternionf().identity();
	private float scale = 1.0f;
	
	public Transformable(int eID){
		super(eID);
	}
	
	@Override
	public Transformable clone() {
		final Transformable deepCopy = new Transformable(this.eID)
				.setPosition(new Vector3f(this.position))
				.setRotation(new Quaternionf(this.rotation))
				.setScale(this.scale);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		final String[] tags = {"T", "R", "S"};
		final Object[] data = {StringUtility.toStringVector3f(position), 
				StringUtility.toStringVector3f(getEulerRotation()), 
				scale};
		return StringUtility.toStringHelper("Transformable", eID, tags, data);
	}
	
	public Vector3fc getPosition() {
		return position;
	}

	public Transformable setPosition(Vector3f position) {
		this.position.set(position);
		return this;
	}
	
	public Quaternionfc getRotation() {
		return rotation;
	}
	
	public Transformable setRotation(Quaternionf rotation) {
		this.rotation.set(rotation);
		return this;
	}
	
	public float getRotX() {
		Vector3f euler = new Vector3f();
		return (float) Math.toRadians(rotation.getEulerAnglesXYZ(euler).x);
	}

	public Transformable setRotX(float rotX) {
		rotation.rotateX((float) Math.toRadians(rotX));
		return this;
	}

	public float getRotY() {
		Vector3f euler = new Vector3f();
		return (float) Math.toRadians(rotation.getEulerAnglesXYZ(euler).y);
	}

	public Transformable setRotY(float rotY) {
		rotation.rotateY((float) Math.toRadians(rotY));
		return this;
	}

	public float getRotZ() {
		Vector3f euler = new Vector3f();
		return (float) Math.toRadians(rotation.getEulerAnglesXYZ(euler).z);
	}

	public Transformable setRotZ(float rotZ) {
		rotation.rotateZ((float) Math.toRadians(rotZ));
		return this;
	}

	public float getScale() {
		return scale;
	}

	public Transformable setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	// The M in MVP :ok_hand:
	public Matrix4fc getTransformation(){
		return transformation
				.identity()
				.translate(position)
				.rotate(rotation)
				.scale(scale);
	}
	
	public void increasePosition(float x, float y, float z){
		position.add(x, y, z);
	}
	
	public void increasePosition(Vector3f vec){
		position.add(vec);
	}
	
	public void rotateRadians(float angleX, float angleY, float angleZ){
		rotation.rotateLocal(angleX, angleY, angleZ);
	}
	
	public void rotateDegrees(float angleX, float angleY, float angleZ){
		rotateRadians((float) Math.toRadians(angleX),
						(float) Math.toRadians(angleY), 
						(float) Math.toRadians(angleZ));
	}
	
	public Vector3fc getEulerRotation() {
		return rotation.getEulerAnglesXYZ(new Vector3f());
	}
	
	public Vector3fc getDirectionVector(){
		final Vector3f directionVector = new Vector3f(0.0f, 0.0f, 1.0f);
		directionVector.rotate(rotation);
		return directionVector;
	}
}
