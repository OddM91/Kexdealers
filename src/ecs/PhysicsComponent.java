package ecs;

import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class PhysicsComponent extends Component {
	
	private Vector3f velocity = new Vector3f();
	private HashMap<String, Vector3f> accelerations = new HashMap<>();
	
	private boolean isAffectedByPhysics = true;
	private boolean isOnGround = false;

	public PhysicsComponent(int eID) {
		super(eID);
	}

	@Override
	public PhysicsComponent clone() {
		PhysicsComponent deepCopy = new PhysicsComponent(this.eID)
				.setVelocity(this.velocity)
				.setAffectedByPhysics(this.isAffectedByPhysics)
				.setOnGround(this.isOnGround);
		for(Entry<String, Vector3f> force : accelerations.entrySet()) {
			deepCopy.addAcceleration(force.getKey(), force.getValue());
		}
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("PhysicsComponent<").append(eID).append(">");
		s.append("(");
		s.append(" V: ").append(velocity);
		s.append(" Physics: ").append(isAffectedByPhysics);
		s.append(" Grounded: ").append(isOnGround);
		for(Entry<String, Vector3f> a : accelerations.entrySet()) {
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
		this.velocity = velocity;
		return this;
	}

	public Iterable<Vector3f> getAccelerations() {
		return accelerations.values();
	}
	
	public Vector3f getAcceleration(String accelName) {
		return accelerations.get(accelName);
	}

	public Vector3f removeAcceleration(String accelName) {
		return accelerations.remove(accelName);
	}
	
	public PhysicsComponent addAcceleration(String accelName, Vector3f vector) {
		if(!accelerations.containsKey(accelName)) {
			accelerations.put(accelName, vector);
		}
		return this;
	}
	
	public PhysicsComponent updateAcceleration(String accelName, Vector3f force) {
		accelerations.get(accelName).set(force);
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
