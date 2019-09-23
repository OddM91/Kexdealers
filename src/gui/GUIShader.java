package gui;

import render.ShaderProgram;

public class GUIShader extends ShaderProgram {

	private static final String VERTEX_FILE = "guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "guiFragmentShader.txt";
	
	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}
	
	protected void getAllUniformLocations() {
		
	}

}
