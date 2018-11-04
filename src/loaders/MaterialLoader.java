package loaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL14C;
import org.lwjgl.opengl.GL21C;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.stb.STBImage;

import com.mokiat.data.front.parser.MTLColor;
import com.mokiat.data.front.parser.MTLMaterial;

import textures.Material;

public class MaterialLoader {
	
	private static final String DEFAULT_TEXTURE = "./res/TEX/default";
	
	private class Texture2D{
		private int id;
		private int width;
		private int height;
		
		public Texture2D(int id, int width, int height){
			this.id = id;
			this.width = width;
			this.height = height;
		}

		public int getID(){
			return id;
		}

		public int getWidth(){
			return width;
		}

		public int getHeight(){
			return height;
		}
		
		public void delete(){
			GL11C.glDeleteTextures(id);
		}
	}
	
	private ArrayList<Integer> textures = new ArrayList<>();
	
	public Material loadMaterial(MTLMaterial material) {
		String name = material.getName();
		// Colors
		MTLColor color = material.getAmbientColor();
		Vector3f ambient = new Vector3f(color.r, color.g, color.b);
		color = material.getDiffuseColor();
		Vector3f diffuse = new Vector3f(color.r, color.g, color.b);
		color = material.getSpecularColor();
		Vector3f specular = new Vector3f(color.r, color.g, color.b);
		color = material.getTransmissionColor();
		Vector3f transmission = new Vector3f(color.r, color.g, color.b);
		// Textures
		final String ambientFile = material.getAmbientTexture();
		final String diffuseFile = material.getDiffuseTexture();
		final String specularFile = material.getSpecularTexture();
		final String specularExponentFile = material.getSpecularExponentTexture();
		final String dissolveFile = material.getDissolveTexture();
		int ambientID = loadTexture(
				(ambientFile == null) ? DEFAULT_TEXTURE : ambientFile,
				false);
		int diffuseID = loadTexture(
				(diffuseFile == null) ? DEFAULT_TEXTURE : diffuseFile,
				true);
		int specularID = loadTexture(
				(specularFile == null) ? DEFAULT_TEXTURE : specularFile,
				false);
		int specularExponentID = loadTexture(
				(specularExponentFile == null) ? DEFAULT_TEXTURE : specularExponentFile,
				false);
		int dissolveID = loadTexture(
				(dissolveFile == null) ? DEFAULT_TEXTURE : dissolveFile, false);
		// Parameters
		float dissolve = material.getDissolve();
		float specularExponent = material.getSpecularExponent();
		
		return new Material(name, ambient, diffuse, specular, transmission,
				ambientID, diffuseID, specularID, specularExponentID, dissolveID,
				dissolve, specularExponent);
	}
	
	private Texture2D loadTexture2D(String filename, boolean gammaCorrected){
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load("res/" +filename +".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11C.glGenTextures();
		// textures.add(textureID);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL11C.GL_REPEAT);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL11C.GL_REPEAT);
		// Set texture filtering
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_LINEAR);
		// Write buffer to buffer bound to GL_TEXTURE_2D
		if(gammaCorrected){
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL21C.GL_SRGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}else{
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}
		// Generate Mipmaps
		GL30C.glGenerateMipmap(GL11C.GL_TEXTURE_2D);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR_MIPMAP_LINEAR);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL14C.GL_TEXTURE_LOD_BIAS, -1.0f);
		// Anisotropic Filtering - Availability not checked. May not be supported.
		float amount = Math.min(4f, GL11C.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		// Un-bind Texture
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		return new Texture2D(textureID, widthInt, heightInt);
	}
	
	private int loadTexture(String filename, boolean gammaCorrected){
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load(filename +".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11C.glGenTextures();
		// textures.add(textureID);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL11C.GL_REPEAT);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL11C.GL_REPEAT);
		// Set texture filtering
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_LINEAR);
		// Write buffer to buffer bound to GL_TEXTURE_2D
		if(gammaCorrected){
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL21C.GL_SRGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}else{
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}
		// Generate Mipmaps
		GL30C.glGenerateMipmap(GL11C.GL_TEXTURE_2D);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR_MIPMAP_LINEAR);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL14C.GL_TEXTURE_LOD_BIAS, -1.0f);
		// Anisotropic Filtering - Availability not checked. May not be supported.
		float amount = Math.min(4f, GL11C.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		// Un-bind Texture
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		return textureID;
	}
	
	public void cleanUp(){
		for(int texture : textures){
			GL11C.glDeleteTextures(texture);
		}
	}
	
}
