package colladaLoader;

import java.util.ArrayList;
import java.util.List;

import dataStructures.SkinningData;
import dataStructures.VertexSkinData;
import xmlparser.XmlNode;

public class SkinLoader {

	private final XmlNode skinningData;
	private final int maxWeights;

	public SkinLoader(XmlNode controllersNode, int maxWeights) {
		this.skinningData = controllersNode.getChild("controller").getChild("skin");
		this.maxWeights = maxWeights;
	}

	public SkinningData extractSkinData() {
		final List<String> jointsList = loadJointsList();
		final float[] weights = loadWeights();
		final XmlNode weightsDataNode = skinningData.getChild("vertex_weights");
		final int[] effectorJointCounts = getEffectiveJointsCounts(weightsDataNode);
		final List<VertexSkinData> vertexWeights = getSkinData(weightsDataNode, effectorJointCounts, weights);
		return new SkinningData(jointsList, vertexWeights);
	}

	private List<String> loadJointsList() {
		final XmlNode inputNode = skinningData.getChild("vertex_weights");
		final String jointDataId = inputNode.getChildWithAttribute("input", "semantic", "JOINT").getAttribute("source")
				.substring(1);
		final XmlNode jointsNode = skinningData.getChildWithAttribute("source", "id", jointDataId).getChild("Name_array");
		final String[] names = jointsNode.getData().split(" ");
		final List<String> jointsList = new ArrayList<String>();
		for (String name : names) {
			jointsList.add(name);
		}
		return jointsList;
	}

	private float[] loadWeights() {
		final XmlNode inputNode = skinningData.getChild("vertex_weights");
		final String weightsDataId = inputNode.getChildWithAttribute("input", "semantic", "WEIGHT").getAttribute("source")
				.substring(1);
		final XmlNode weightsNode = skinningData.getChildWithAttribute("source", "id", weightsDataId).getChild("float_array");
		final String[] rawData = weightsNode.getData().split(" ");
		final float[] weights = new float[rawData.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Float.parseFloat(rawData[i]);
		}
		return weights;
	}

	private int[] getEffectiveJointsCounts(XmlNode weightsDataNode) {
		final String[] rawData = weightsDataNode.getChild("vcount").getData().split(" ");
		final int[] counts = new int[rawData.length];
		for (int i = 0; i < rawData.length; i++) {
			counts[i] = Integer.parseInt(rawData[i]);
		}
		return counts;
	}

	private List<VertexSkinData> getSkinData(XmlNode weightsDataNode, int[] counts, float[] weights) {
		final String[] rawData = weightsDataNode.getChild("v").getData().split(" ");
		final ArrayList<VertexSkinData> skinningData = new ArrayList<VertexSkinData>();
		int pointer = 0;
		for (int count : counts) {
			VertexSkinData skinData = new VertexSkinData();
			for (int i = 0; i < count; i++) {
				int jointId = Integer.parseInt(rawData[pointer++]);
				int weightId = Integer.parseInt(rawData[pointer++]);
				skinData.addJointEffect(jointId, weights[weightId]);
			}
			skinData.limitJointNumber(maxWeights);
			skinningData.add(skinData);
		}
		return skinningData;
	}

}