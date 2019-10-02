package dataStructures;

import java.util.ArrayList;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Vertex {
	
	private static final int NO_INDEX = -1;
	
	private final ArrayList<Vector3fc> tangents = new ArrayList<>();
	private final Vector3f averagedTangent = new Vector3f(0, 0, 0);
	private final Vector3fc position;
	private final int index;
	private final float length;
	
	private Vertex duplicateVertex = null;
	private VertexSkinData weightsData;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	
	
	public Vertex(int index, Vector3fc position, VertexSkinData weightsData){
		this.index = index;
		this.weightsData = weightsData;
		this.position = position;
		this.length = position.length();
	}
	
	public VertexSkinData getWeightsData(){
		return weightsData;
	}
	
	public void addTangent(Vector3f tangent){
		tangents.add(tangent);
	}
	
	public void averageTangents(){
		if(tangents.isEmpty()){
			return;
		}
		for(Vector3fc tangent : tangents){
			averagedTangent.add(tangent);
		}
		averagedTangent.normalize();
	}
	
	public Vector3fc getAverageTangent(){
		return averagedTangent;
	}
	
	public int getIndex(){
		return index;
	}
	
	public float getLength(){
		return length;
	}
	
	public boolean isSet(){
		return (textureIndex != NO_INDEX) && (normalIndex != NO_INDEX);
	}
	
	public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
		return (textureIndexOther == textureIndex) && (normalIndexOther == normalIndex);
	}
	
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}

	public Vector3fc getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
