package ecs;

import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import utility.StringUtility;

public class PhysicsComponent extends Component {
	
	private final Vector3f velocity = new Vector3f();
	private final HashMap<String, Vector3f> accelerations = new HashMap<>();
	
	private boolean isAffectedByPhysics = true;
	private boolean isOnGround = false;

	public PhysicsComponent(int eID) {
		super(eID);
	}

	@Override
	public PhysicsComponent clone() {
		final PhysicsComponent deepCopy = new PhysicsComponent(this.eID)
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
		final String[] tags = {"V", "Physics", "Grounded", "Accelerations"};
		
		final StringBuilder accelerationsString = new StringBuilder();
		for(Entry<String, Vector3f> a : accelerations.entrySet()) {
			accelerationsString.append(" ").append(a.getKey()).append(": ");
			accelerationsString.append(StringUtility.toStringVector3f(a.getValue()));
		}
		
		final Object[] data = {StringUtility.toStringVector3f(velocity),
				isAffectedByPhysics,
				isOnGround,
				accelerationsString.toString()};
		return StringUtility.toStringHelper("PhysicsComponent", eID, tags, data);
	}
	
	public Vector3fc getVelocity() {
		return velocity;
	}
	
	public PhysicsComponent setVelocity(Vector3f velocity) {
		this.velocity.set(velocity);
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
