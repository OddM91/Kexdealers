package colladaLoader;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import dataStructures.MeshData;
import dataStructures.Vertex;
import dataStructures.VertexSkinData;
import xmlparser.XmlNode;


/**
 * Loads the mesh data for a model from a collada XML file.
 * @author Karl
 *
 */
public class GeometryLoader {

	private static final Matrix4f CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0,0));
	
	private final XmlNode meshData;

	private final List<VertexSkinData> vertexWeights;
	
	final ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	final ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
	final ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	final ArrayList<Integer> indices = new ArrayList<Integer>();

	private float[] verticesArray;
	private float[] normalsArray;
	private float[] texturesArray;
	private int[] indicesArray;
	private int[] jointIdsArray;
	private float[] weightsArray;
	
	public GeometryLoader(XmlNode geometryNode, List<VertexSkinData> vertexWeights) {
		this.vertexWeights = vertexWeights;
		this.meshData = geometryNode.getChild("geometry").getChild("mesh");
	}
	
	public MeshData extractModelData(){
		readRawData();
		assembleVertices();
		removeUnusedVertices();
		initArrays();
		convertDataToArrays();
		convertIndicesListToArray();
		return new MeshData(verticesArray, texturesArray, normalsArray, indicesArray, jointIdsArray, weightsArray);
	}

	private void readRawData() {
		readPositions();
		readNormals();
		readTextureCoords();
	}

	private void readPositions() {
		final String positionsId = meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
		final XmlNode positionsData = meshData.getChildWithAttribute("source", "id", positionsId).getChild("float_array");
		final int count = Integer.parseInt(positionsData.getAttribute("count"));
		final String[] posData = positionsData.getData().split(" ");
		for (int i = 0; i < count/3; i++) {
			final float x = Float.parseFloat(posData[i * 3]);
			final float y = Float.parseFloat(posData[i * 3 + 1]);
			final float z = Float.parseFloat(posData[i * 3 + 2]);
			final Vector4fc position = new Vector4f(x, y, z, 1).mul(CORRECTION);
			vertices.add(new Vertex(vertices.size(), new Vector3f(position.x(), position.y(), position.z()), vertexWeights.get(vertices.size())));
		}
	}

	private void readNormals() {
		final String normalsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "NORMAL")
				.getAttribute("source").substring(1);
		final XmlNode normalsData = meshData.getChildWithAttribute("source", "id", normalsId).getChild("float_array");
		final int count = Integer.parseInt(normalsData.getAttribute("count"));
		final String[] normData = normalsData.getData().split(" ");
		for (int i = 0; i < count/3; i++) {
			final float x = Float.parseFloat(normData[i * 3]);
			final float y = Float.parseFloat(normData[i * 3 + 1]);
			final float z = Float.parseFloat(normData[i * 3 + 2]);
			final Vector4fc norm = new Vector4f(x, y, z, 0f).mul(CORRECTION);
			normals.add(new Vector3f(norm.x(), norm.y(), norm.z()));
		}
	}

	private void readTextureCoords() {
		final String texCoordsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "TEXCOORD")
				.getAttribute("source").substring(1);
		final XmlNode texCoordsData = meshData.getChildWithAttribute("source", "id", texCoordsId).getChild("float_array");
		final int count = Integer.parseInt(texCoordsData.getAttribute("count"));
		final String[] texData = texCoordsData.getData().split(" ");
		for (int i = 0; i < count/2; i++) {
			final float s = Float.parseFloat(texData[i * 2]);
			final float t = Float.parseFloat(texData[i * 2 + 1]);
			textures.add(new Vector2f(s, t));
		}
	}
	
	private void assembleVertices(){
		final XmlNode poly = meshData.getChild("polylist");
		final int typeCount = poly.getChildren("input").size();
		final String[] indexData = poly.getChild("p").getData().split(" ");
		for(int i=0;i<indexData.length/typeCount;i++){
			final int positionIndex = Integer.parseInt(indexData[i * typeCount]);
			final int normalIndex = Integer.parseInt(indexData[i * typeCount + 1]);
			final int texCoordIndex = Integer.parseInt(indexData[i * typeCount + 2]);
			processVertex(positionIndex, normalIndex, texCoordIndex);
		}
	}
	

	private Vertex processVertex(int posIndex, int normIndex, int texIndex) {
		final Vertex currentVertex = vertices.get(posIndex);
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(texIndex);
			currentVertex.setNormalIndex(normIndex);
			indices.add(posIndex);
			return currentVertex;
		} else {
			return dealWithAlreadyProcessedVertex(currentVertex, texIndex, normIndex);
		}
	}

	private int[] convertIndicesListToArray() {
		this.indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}

	private float convertDataToArrays() {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			final Vertex currentVertex = vertices.get(i);
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			final Vector3fc position = currentVertex.getPosition();
			final Vector2fc textureCoord = textures.get(currentVertex.getTextureIndex());
			final Vector3fc normalVector = normals.get(currentVertex.getNormalIndex());
			verticesArray[i * 3] = position.x();
			verticesArray[i * 3 + 1] = position.y();
			verticesArray[i * 3 + 2] = position.z();
			texturesArray[i * 2] = textureCoord.x();
			texturesArray[i * 2 + 1] = 1 - textureCoord.y();
			normalsArray[i * 3] = normalVector.x();
			normalsArray[i * 3 + 1] = normalVector.y();
			normalsArray[i * 3 + 2] = normalVector.z();
			final VertexSkinData weights = currentVertex.getWeightsData();
			jointIdsArray[i * 3] = weights.jointIds.get(0);
			jointIdsArray[i * 3 + 1] = weights.jointIds.get(1);
			jointIdsArray[i * 3 + 2] = weights.jointIds.get(2);
			weightsArray[i * 3] = weights.weights.get(0);
			weightsArray[i * 3 + 1] = weights.weights.get(1);
			weightsArray[i * 3 + 2] = weights.weights.get(2);

		}
		return furthestPoint;
	}

	private Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex);
			} else {
				final Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition(), previousVertex.getWeightsData());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}

		}
	}
	
	private void initArrays(){
		this.verticesArray = new float[vertices.size() * 3];
		this.texturesArray = new float[vertices.size() * 2];
		this.normalsArray = new float[vertices.size() * 3];
		this.jointIdsArray = new int[vertices.size() * 3];
		this.weightsArray = new float[vertices.size() * 3];
	}

	private void removeUnusedVertices() {
		for (Vertex vertex : vertices) {
			vertex.averageTangents();
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
	
}