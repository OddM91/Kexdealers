package utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.BufferUtils;

public class MiscUtility {
	
	public static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static int[] toIntArray(List<Integer> integers) {
		int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	public static float[] toFloatArray(List<Float> floats) {
		float[] ret = new float[floats.size()];
	    Iterator<Float> iterator = floats.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().floatValue();
	    }
	    return ret;
	}
	
	public static <T> T arrayConcatenation(T a, T b) {
	    if (!a.getClass().isArray() || !b.getClass().isArray()) {
	        throw new IllegalArgumentException();
	    }

	    Class<?> resCompType;
	    Class<?> aCompType = a.getClass().getComponentType();
	    Class<?> bCompType = b.getClass().getComponentType();

	    if (aCompType.isAssignableFrom(bCompType)) {
	        resCompType = aCompType;
	    } else if (bCompType.isAssignableFrom(aCompType)) {
	        resCompType = bCompType;
	    } else {
	        throw new IllegalArgumentException();
	    }

	    int aLen = Array.getLength(a);
	    int bLen = Array.getLength(b);

	    @SuppressWarnings("unchecked")
	    T result = (T) Array.newInstance(resCompType, aLen + bLen);
	    System.arraycopy(a, 0, result, 0, aLen);
	    System.arraycopy(b, 0, result, aLen, bLen);        

	    return result;
	}
	
	public static Properties loadProperties(String path) {
		try (FileInputStream inputStream = new FileInputStream(path)){
			final Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (IOException x) {
			System.err.println("Couldn't open " +path);
		}
		return null;
	}
	
	public static void saveProperties(Properties properties, String path) {
		try (FileOutputStream outputStream = new FileOutputStream(path)){
			properties.store(outputStream, "reactor_properties");
		} catch (IOException x) {
			System.err.println("Couldn't write " +path);
		}
	}
	
}
