package loaders;

import animation.AnimatedModel;
import utility.File;
import utility.XmlNode;
import utility.XmlParser;

public class ColladaLoader {
	
	
	public Model loadModel(File file) {
		XmlNode node = XmlParser.loadXmlFile(file);
		
		return new Model(null, null);
	}
	
	public AnimatedModel loadAnimatedModel(File file) {
		XmlNode node = XmlParser.loadXmlFile(file);
		
		return new AnimatedModel(null, null, null, 0);
	}
}
