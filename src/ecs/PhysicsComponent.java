package ecs;

import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class PhysicsComponent extends Component {

	private int eID;

	private Vector3f velocity = new Vector3f();
	private Vector3f acceleration = new Vector3f();
	private float weight = 0.0f;
	private HashMap<String, Vector3f> listOfAccelerations = new HashMap<>();

	private boolean isAffectedByPhysics = true;
	private boolean isOnGround = false; // TODO replace with proper collision system

	public PhysicsComponent(int eID) {
		this.eID = eID;
	}

	@Override
	public int getEID() {
		return eID;
	}

	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}
	
	@Override
	public PhysicsComponent clone() {
		PhysicsComponent deepCopy = new PhysicsComponent(this.eID)
				.setVelocity(new Vector3f(velocity))
				.setAcceleration(new Vector3f(acceleration))
				.setWeight(this.weight)
				.setAffectedByPhysics(this.isAffectedByPhysics)
				.setOnGround(this.isOnGround);
		for(Entry<String, Vector3f> force : listOfAccelerations.entrySet()) {
			deepCopy.applyForce(force.getKey(), force.getValue());
		}
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("PhysicsComponent<").append(eID).append(">");
		s.append("(");
		s.append(" V: ").append(velocity.x).append("/").append(velocity.y).append("/").append(velocity.z);
		s.append(" A: ").append(acceleration.x).append("/").append(acceleration.y).append("/").append(acceleration.z);
		s.append(" M: ").append(weight);
		s.append(" Physics: ").append(isAffectedByPhysics);
		s.append(" Grounded: ").append(isOnGround);
		for(Entry<String, Vector3f> a : listOfAccelerations.entrySet()) {
			s.append(" ").append(a.getKey()).append(": ");
			s.append(a.getValue().x).append("/").append(a.getValue().y).append("/").append(a.getValue().z);
		}
		s.append(" )");
		return s.toString();
	}

	public Vector3fc getVelocity() {
		return velocity;
	}

	public PhysicsComponent setVelocity(Vector3f velocity) {
		this.velocity.set(velocity);
		return this;
	}

	public PhysicsComponent resetVelocity() {
		velocity.set(0.0f, 0.0f, 0.0f);
		return this;
	}
	
	public Vector3fc getAcceleration() {
		return acceleration;
	}

	public PhysicsComponent setAcceleration(Vector3f acceleration) {
		this.acceleration.set(acceleration);
		return this;
	}

	public PhysicsComponent resetAcceleration() {
		acceleration.set(0.0f, 0.0f, 0.0f);
		return this;
	}
	
	public float getWeight() {
		return weight;
	}

	public PhysicsComponent setWeight(float weight) {
		this.weight = weight;
		return this;
	}

	public Iterable<Vector3f> getAppliedForces() {
		return listOfAccelerations.values();
	}
	
	public Vector3f getAppliedForce(String forceName) {
		return listOfAccelerations.get(forceName);
	}

	public Vector3f removeForce(String forceName) {
		return listOfAccelerations.remove(forceName);
	}
	
	public PhysicsComponent applyForce(String forceName, Vector3f force) {
		if(!listOfAccelerations.containsKey(forceName)) {
			listOfAccelerations.put(forceName, force);
		}
		return this;
	}
	
	public PhysicsComponent updateForce(String forceName, Vector3f force) {
		listOfAccelerations.get(forceName).set(force);
		return this;
	}
	
	public PhysicsComponent increaseForce(String forceName, Vector3f forceAdditum) {
		listOfAccelerations.get(forceName).add(forceAdditum);
		return this;
	}

	public boolean isAffectedByPhysics() {
		return isAffectedByPhysics;
	}

	public PhysicsComponent setAffectedByPhysics(boolean isAffectedByPhysics) {
		this.isAffectedByPhysics = isAffectedByPhysics;
		return this;
	}

	public boolean isOnGround() {
		return isOnGround;
	}

	public PhysicsComponent setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
		return this;
	}

}
