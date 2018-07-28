package ui;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ecs.FPPCameraComponent;

public class LineRenderer {

	private class Line {
		public Vector3fc posStart = new Vector3f();
		public Vector3fc posEnd = new Vector3f();
		public Vector3fc colourStart = new Vector3f();
		public Vector3fc colourEnd = new Vector3f();
		public double lifeRemain = 0.0d;

		public Line(Vector3fc begin, Vector3fc end, Vector3fc colourBegin, Vector3fc colourEnd, double lifeRemain) {
			this.posStart = begin;
			this.posEnd = end;
			this.colourStart = colourBegin;
			this.colourEnd = colourEnd;
			this.lifeRemain = lifeRemain;
		}
	}

	private LineShader shader;

	private int vaoID = -1;
	private int vboVertsID = -1;
	private int vboColoursID = -1;

	private int vertCount = 0;
	private List<Vector3fc> currLineVertices = new ArrayList<>();
	private List<Vector3fc> currLineColours = new ArrayList<>();
	private List<Line> lineObjs = new LinkedList<>();

	public LineRenderer() {
		shader = new LineShader();
	}

	public void render(FPPCameraComponent camera, double deltaTime) {

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);

		addLineObjToCurrLines(deltaTime);
		updateBuffers();

		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera.getViewMatrix());
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawArrays(GL11.GL_LINES, 0, vertCount);
		shader.stop();

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	public void addLine(Vector3fc begin, Vector3fc end, Vector3fc colourBegin, Vector3fc colourEnd, double lifeTimeSecs) {
		lineObjs.add(new Line(begin, end, colourBegin, colourEnd, lifeTimeSecs));
	}

	public void addLine(Vector3fc begin, Vector3fc end, Vector3fc colour, double lifeTimeSecs) {
		addLine(begin, end, colour, colour, lifeTimeSecs);
	}

	private void addLineObjToCurrLines(double deltaTime) {
		for (int i = 0; i < lineObjs.size(); i++) {
			if (Double.compare(lineObjs.get(i).lifeRemain, 0.0d) < 0) {
				lineObjs.remove(i);
				i--;
			} else {
				lineObjs.get(i).lifeRemain -= deltaTime;
				currLineVertices.add(lineObjs.get(i).posStart);
				currLineVertices.add(lineObjs.get(i).posEnd);
				currLineColours.add(lineObjs.get(i).colourStart);
				currLineColours.add(lineObjs.get(i).colourEnd);
			}

		}
	}

	public void updateBuffers() {
		// we allocate a new buffer every frame because the line count may change.

		if (vboVertsID > 0) {
			GL15.glDeleteBuffers(vboVertsID);
		}
		if (vboColoursID > 0) {
			GL15.glDeleteBuffers(vboColoursID);
		}
		if (vaoID > 0) {
			GL30.glDeleteVertexArrays(vaoID);
		}

		// alloc and bind new buffers
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);

		vboVertsID = GL15.glGenBuffers();
		vboColoursID = GL15.glGenBuffers();

		// generate float buffers
		float[] vertexArray = new float[currLineVertices.size() * 3];
		for (int i = 0; i < currLineVertices.size(); i++) {
			vertexArray[i * 3 + 0] = currLineVertices.get(i).x();
			vertexArray[i * 3 + 1] = currLineVertices.get(i).y();
			vertexArray[i * 3 + 2] = currLineVertices.get(i).z();
		}
		float[] colourArray = new float[currLineColours.size() * 3];
		for (int i = 0; i < currLineColours.size(); i++) {
			colourArray[i * 3 + 0] = currLineColours.get(i).x();
			colourArray[i * 3 + 1] = currLineColours.get(i).y();
			colourArray[i * 3 + 2] = currLineColours.get(i).z();
		}
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray);
		vertexBuffer.flip();
		FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colourArray.length);
		colourBuffer.put(colourArray);
		colourBuffer.flip();
		vertCount = currLineVertices.size();

		// upload data
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVertsID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboColoursID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colourBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

		// clean up
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		currLineVertices.clear();
		currLineColours.clear();
	}
}