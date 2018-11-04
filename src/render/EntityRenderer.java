package render;

import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;

import ecs.FPPCameraComponent;
import ecs.PointLightComponent;
import ecs.Transformable;
import loaders.GraphicsLoader;
import loaders.Model;

public class EntityRenderer {

	private EntityShader shader;
	
	public EntityRenderer(){
		shader = new EntityShader();
	}
	
	public void render(GraphicsLoader graphicsLoader,
			FPPCameraComponent camera,
			HashMap<String, HashSet<Transformable>> entitiesToRender,
			HashSet<PointLightComponent> pointLights){
		
		GL11C.glEnable(GL11C.GL_DEPTH_TEST);
		GL11C.glDepthMask(true);
		
		shader.start();
		shader.uploadViewPos(camera.getPosition());
		shader.uploadDirectionalLight(graphicsLoader.getSun());
		shader.uploadPointLights(pointLights);
		
		for(String appearance : entitiesToRender.keySet()){
			Model model = graphicsLoader.getModel(appearance);
			bindEntityAppearance(model);
			
			HashSet<Transformable> transformations = entitiesToRender.get(appearance);
			for(Transformable transformation : transformations){
				shader.uploadMVP(transformation.getTransformation(), camera.getViewMatrix(), camera.getProjectionMatrix());
				GL11C.glDrawElements(GL11C.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11C.GL_UNSIGNED_INT, 0);
			}
			unbindEntityAppearance(model);
		}
		
		shader.stop();
	}
	
	private void bindEntityAppearance(Model model){
		// Bind VAO
		GL30C.glBindVertexArray(model.getMesh().getVaoID());
		GL20C.glEnableVertexAttribArray(0);
		GL20C.glEnableVertexAttribArray(1);
		GL20C.glEnableVertexAttribArray(2);
		// Bind textures
		GL13C.glActiveTexture(GL13C.GL_TEXTURE0);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, model.getMaterial().getDiffuseID());
		GL13C.glActiveTexture(GL13C.GL_TEXTURE1);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, model.getMaterial().getSpecularID());
		// Upload Phong-Shading data
		shader.uploadMaterial(0, 1, model.getMaterial().getSpecularExponent());
	}
	
	private void unbindEntityAppearance(Model model){
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		GL20C.glDisableVertexAttribArray(0);
		GL20C.glDisableVertexAttribArray(1);
		GL20C.glDisableVertexAttribArray(2);
		GL30C.glBindVertexArray(0);
	}
	
}
