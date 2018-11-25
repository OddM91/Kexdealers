package utility;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public class StringUtility {

	public static String toStringVector2f(Vector2fc vector) {
		StringBuilder s = new StringBuilder();
		s.append(vector.x()).append("/");
		s.append(vector.y());
		return s.toString();
	}
	
	public static String toStringVector3f(Vector3fc vector) {
		StringBuilder s = new StringBuilder();
		s.append(vector.x()).append("/");
		s.append(vector.y()).append("/");
		s.append(vector.z());
		return s.toString();
	}
	
	public static String toStringVector4f(Vector4fc vector) {
		StringBuilder s = new StringBuilder();
		s.append(vector.x()).append("/");
		s.append(vector.y()).append("/");
		s.append(vector.z()).append("/");
		s.append(vector.w());
		return s.toString();
	}
	
	public static String toStringHelper(String compType, int eID, String[] tags, Object[] data) {
		StringBuilder s = new StringBuilder();
		s.append(compType);
		s.append("<").append(eID).append(">(");
		if(tags.length != data.length) {
			throw new IllegalArgumentException("One tag for each data point required");
		}
		for(int i = 0; i < tags.length; i++) {
			s.append(" " +tags[i] +": ");
			s.append(data[i]);
		}
		s.append(" )");
		return s.toString();
	}
	
}
