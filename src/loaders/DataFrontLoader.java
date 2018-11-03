package loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.mokiat.data.front.parser.IMTLParser;
import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.MTLColor;
import com.mokiat.data.front.parser.MTLLibrary;
import com.mokiat.data.front.parser.MTLMaterial;
import com.mokiat.data.front.parser.MTLParser;
import com.mokiat.data.front.parser.OBJDataReference;
import com.mokiat.data.front.parser.OBJFace;
import com.mokiat.data.front.parser.OBJMesh;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJNormal;
import com.mokiat.data.front.parser.OBJObject;
import com.mokiat.data.front.parser.OBJParser;
import com.mokiat.data.front.parser.OBJTexCoord;
import com.mokiat.data.front.parser.OBJVertex;

/**
 * New OBJLoader using the java-data-front library https://github.com/mokiat/java-data-front
 */
public class DataFrontLoader {
	
	public void obj() {
		
		try (InputStream is = new FileInputStream("example.obj")) {
			// Create an OBJParser and parse the resource
			final IOBJParser parser = new OBJParser();
			final OBJModel model =  parser.parse(is);
			
			// Use the model representation to get some basic info
			System.out.println(MessageFormat.format(
		          "OBJ model has {0} vertices, {1} normals, {2} texture coordinates, and {3} objects.",
		          model.getVertices().size(),
		          model.getNormals().size(),
		          model.getTexCoords().size(),
		          model.getObjects().size())); 
			// Vertices
			for(OBJVertex objVertex : model.getVertices()) {
				Vector3f vertex = new Vector3f(objVertex.x, objVertex.y, objVertex.z);
			}
			// Normals
			for(OBJNormal objNormal : model.getNormals()) {
				Vector3f normal = new Vector3f(objNormal.x, objNormal.y, objNormal.z);
			}
			// Texture Coordinates
			for(OBJTexCoord objTexCoord : model.getTexCoords()) {
				Vector2f texCoord = new Vector2f(objTexCoord.u, objTexCoord.v);
			}
			// Indices
			for(OBJObject objObject: model.getObjects()) {
				for(OBJMesh objMesh : objObject.getMeshes()) {
					for(OBJFace objFace : objMesh.getFaces()) {
						for(OBJDataReference dataRef : objFace.getReferences()) {
							// extract indices
							int index_normal = (dataRef.hasNormalIndex()) ? dataRef.normalIndex : -1;
							int index_texCoord = (dataRef.hasTexCoordIndex()) ? dataRef.texCoordIndex : -1;
							int index_vertex = (dataRef.hasVertexIndex()) ? dataRef.vertexIndex : -1;
						}
						
					}
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void mtl() {
		try (InputStream in = new FileInputStream("example.mtl")) {
			  final IMTLParser parser = new MTLParser();
			  final MTLLibrary library = parser.parse(in);
			  for (MTLMaterial material : library.getMaterials()) {
			  	System.out.println(MessageFormat.format("Material with name `{0}`.", material.getName()));
			  }  
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
}
