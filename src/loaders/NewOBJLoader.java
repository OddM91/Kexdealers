package loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJParser;

public class NewOBJLoader {
	
	private static final String OBJ_PATH = "./res/models";
	private static final String MTL_PATH = "./res/materials";
	private static final String TEX_PATH = "./res/textures";
	
	
	public void loadOBJModel(String filename) {
		
		OBJModel objModel = null;
		List<String> materials;
		
		try (InputStream is = new FileInputStream(OBJ_PATH + filename +".obj")){
			// Create an OBJParser and parse the resource
			final IOBJParser parser = new OBJParser();
			objModel = parser.parse(is);
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
		
		
	}
}
