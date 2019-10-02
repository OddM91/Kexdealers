  
package dataStructures;

import java.util.ArrayList;

public class KeyFrameData {

	public final float time;
	public final ArrayList<JointTransformData> jointTransforms = new ArrayList<JointTransformData>();
	
	public KeyFrameData(float time){
		this.time = time;
	}
	
	public KeyFrameData addJointTransform(JointTransformData transform){
		jointTransforms.add(transform);
		return this;
	}
	
}
