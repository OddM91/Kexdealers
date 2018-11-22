package animation;

import java.util.HashMap;

public class KeyFrame {
	
	private final float timeStamp;
	private final HashMap<String, JointTransform> pose;
	
	/**
	 * @param timeStamp
	 *            - the time (in seconds) that this keyframe occurs during the
	 *            animation.
	 * @param jointKeyFrames
	 *            - the local-space transforms for all the joints at this
	 *            keyframe, indexed by the name of the joint that they should be
	 *            applied to.
	 */
	public KeyFrame(float timeStamp, HashMap<String, JointTransform> jointKeyFrames) {
		this.timeStamp = timeStamp;
		this.pose = jointKeyFrames;
	}

	/**
	 * @return The time in seconds of the keyframe in the animation.
	 */
	protected float getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return The desired bone-space transforms of all the joints at this
	 *         keyframe, of the animation, indexed by the name of the joint that
	 *         they correspond to. This basically represents the "pose" at this
	 *         keyframe.
	 */
	protected HashMap<String, JointTransform> getJointKeyFrames() {
		return pose;
	}
	
}
