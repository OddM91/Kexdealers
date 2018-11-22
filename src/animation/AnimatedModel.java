package animation;

import java.util.HashMap;

import org.joml.Matrix4f;

import textures.Material;

public class AnimatedModel {
	
	private int referenceCounter = 0;
	
	// Skin
	private final AnimatedMesh animatedMesh;
	private final Material material;
	
	// Skeleton
	private final Joint rootJoint;
	private final int jointCount;
	
	/**
	 * Creates a new model capable of animation. The inverse bind transform for
	 * all joints is calculated in this constructor. The bind transform is
	 * simply the original (no pose applied) transform of a joint in relation to
	 * the model's origin (model-space). The inverse bind transform is simply
	 * that but inverted.
	 * 
	 * @param model
	 *            - the VAO containing the mesh data for this entity. This
	 *            includes vertex positions, normals, texture coords, IDs of
	 *            joints that affect each vertex, and their corresponding
	 *            weights.
	 * @param material
	 *            - the material for the model.
	 * @param rootJoint
	 *            - the root joint of the joint hierarchy which makes up the
	 *            "skeleton" of the model.
	 * @param jointCount
	 *            - the number of joints in the joint hierarchy (skeleton) for
	 *            this model.
	 * 
	 */
	public AnimatedModel(AnimatedMesh animatedMesh, Material material, Joint rootJoint, int jointCount) {
		this.animatedMesh = animatedMesh;
		this.material = material;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		rootJoint.calcInverseBindTransform(new Matrix4f());
	}
	
	public AnimatedMesh getAnimatedMesh() {
		return animatedMesh;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public Joint getRootJoint() {
		return rootJoint;
	}
	
	public void refCountUp() {
		referenceCounter++;
		animatedMesh.refCountUp();
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
		if(!animatedMesh.refCountDown()) {
			animatedMesh.deleteFromVideoMemory();
		}
		if(!material.refCountDown()) {
			material.deleteFromVideoMemory();
		}
		return (referenceCounter > 0) ? true : false;
	}
	
	/**
	 * Gets an array of the all important model-space transforms of all the
	 * joints (with the current animation pose applied) in the entity. The
	 * joints are ordered in the array based on their joint index. The position
	 * of each joint's transform in the array is equal to the joint's index.
	 * 
	 * @return The array of model-space transforms of the joints in the current
	 *         animation pose.
	 */
	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}
	
	/**
	 * This adds the current model-space transform of a joint (and all of its
	 * descendants) into an array of transforms. The joint's transform is added
	 * into the array at the position equal to the joint's index.
	 * 
	 * @param headJoint
	 *            - the current joint being added to the array. This method also
	 *            adds the transforms of all the descendants of this joint too.
	 * @param jointMatrices
	 *            - the array of joint transforms that is being filled.
	 */
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.getIndex()] = headJoint.getAnimatedTransform();
		for (Joint childJoint : headJoint.getChildren()) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}
	
}
