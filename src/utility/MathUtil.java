package utility;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathUtil {
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static float getHeightAtPoint(float x, float z, int size, float[][] heights){
		//float terrainX = x - this.x;
		//float terrainZ = z - this.z;
		float terrainX = x;
		float terrainZ = z;
		// Vertex level grid!
		float gridSquareSize = size / (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		// check if within boundary
		boolean one = gridX >= heights.length - 1;
		boolean two = gridZ >= heights.length - 1;
		boolean three = gridX < 0;
		boolean four = gridZ < 0;
		if(one || two || three || four){
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize);
		float zCoord = (terrainZ % gridSquareSize);
		float answer;
		if(xCoord <= (1 -zCoord)){// if true, upper left triangle, else bottom right
			answer = MathUtil
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}else{
			answer = MathUtil
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}
	
}
