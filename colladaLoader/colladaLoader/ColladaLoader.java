package colladaLoader;

import dataStructures.AnimationData;
import dataStructures.MeshData;
import dataStructures.SkeletonData;
import dataStructures.SkinningData;
import utility.File;
import xmlparser.XmlNode;
import xmlparser.XmlParser;

public class ColladaLoader {

	private final XmlNode rootNode;
	
	private final SkinningData skinningData;
	private final MeshData meshData;
	private final SkeletonData skeletonData;
	
	private final AnimationData animationData;
	
	public ColladaLoader(File colladaFile, int maxWeights) {
		rootNode = XmlParser.loadXmlFile(colladaFile);
		
		final SkinLoader skinLoader = new SkinLoader(rootNode.getChild("library_controllers"), maxWeights);
		skinningData = skinLoader.extractSkinData();
		
		final XmlNode visualScenesNode = rootNode.getChild("library_visual_scenes");
		final SkeletonLoader jointsLoader = new SkeletonLoader(visualScenesNode, skinningData.jointOrder);
		skeletonData = jointsLoader.extractBoneData();

		final GeometryLoader g = new GeometryLoader(rootNode.getChild("library_geometries"), skinningData.verticesSkinData);
		meshData = g.extractModelData();
		
		final XmlNode animNode = rootNode.getChild("library_animations");
		final AnimationLoader loader = new AnimationLoader(animNode, visualScenesNode);
		animationData = loader.extractAnimation();
	}
	
	public MeshData getMeshData() {
		return meshData;
	}
	
	public SkeletonData getSkeletonData() {
		return skeletonData;
	}
	
	public AnimationData getAnimatonData() {
		return animationData;
	}
	
}
