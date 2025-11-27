package com.crossly.coffee_engine.component.graphics;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author Jude Ogboru
 */
public final class StaticMesh extends Mesh {

	private static final Layout DEFAULT_LAYOUT = new Layout()
			.offset(3)
			.offset(3)
			.offset(2);

	public static final Mesh UNIT_RECT = new StaticMesh(
			DEFAULT_LAYOUT,
			new int[] { 0, 1, 2, 2, 3, 0 }, // indices
			new float[] { -1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0 }, // positions
			new float[] { 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0 }, // normals
			new float[] { 0, 0, 1, 0, 1, 1, 0, 1 } // texture coordinates
	);

	static {
		UNIT_RECT.get();
	}

	public StaticMesh(float[] vertexData, Layout layout) {
		vertexCount = vertexData.length / layout.getStride();
		drawElements = false;
		handle = glGenVertexArrays();
		glBindVertexArray(handle);
		int vbo = createArrayBuffer(vertexData, GL_STATIC_DRAW);
		bufferObjects.add(vbo);
		int offset = 0;
		for (int i = 0; i < layout.getSize(); i++) {
			interpretBuffer(i, layout.getSize(i), layout.getStride(), offset);
			offset += layout.sizes.get(i);
		}
	}

	public StaticMesh(float[] vertexData, int[] indicesData, Layout layout) {
		vertexCount = indicesData.length;
		drawElements = true;
		handle = glGenVertexArrays();
		glBindVertexArray(handle);
		int vbo = createArrayBuffer(vertexData, GL_STATIC_DRAW);
		bufferObjects.add(vbo);
		int ebo = createElementBuffer(indicesData, GL_STATIC_DRAW);
		bufferObjects.add(ebo);
		int offset = 0;
		for (int i = 0; i < layout.getSize(); i++) {
			interpretBuffer(i, layout.getSize(i), layout.getStride(), offset);
			offset += layout.sizes.get(i);
		}
	}

	public StaticMesh(Layout layout, int[] indicesData, float[] posData, float[]... vertexData) {
		vertexCount = indicesData.length;
		drawElements = true;
		handle = glGenVertexArrays();
		glBindVertexArray(handle);
		int ebo = createElementBuffer(indicesData, GL_STATIC_DRAW);
		bufferObjects.add(ebo);
		int posVbo = createArrayBuffer(posData, GL_STATIC_DRAW, 0, layout.getSize(0), 0, 0);
		bufferObjects.add(posVbo);
		if (layout.sizes.size() != vertexData.length + 1)
			throw new RuntimeException("Layouts and vertex data array do not match!");
		for (int i = 1; i < layout.getSize(); i++) {
			if (vertexData[i - 1] == null)
				continue;
			int vbo = createArrayBuffer(vertexData[i - 1], GL_STATIC_DRAW, i, layout.getSize(i), 0, 0);
			bufferObjects.add(vbo);
		}
	}

	public StaticMesh(String meshPath) {
		loadMeshes(meshPath);
	}

	@Override
	protected Mesh loadMesh(AIMesh mesh, AIScene scene) {
		float[] posData = new float[mesh.mNumVertices() * 3];
		float[] cordData = new float[mesh.mNumVertices() * 2];
		AIVector3D.Buffer cordBuffer = mesh.mTextureCoords(0);
		float[] normData = new float[mesh.mNumVertices() * 3];
		AIVector3D.Buffer normBuffer = mesh.mNormals();
		for (int i = 0; i < mesh.mNumVertices(); i++) {
			AIVector3D pos = mesh.mVertices().get(i);
			int offset3 = i * 3;
			int offset2 = i * 2;
			posData[offset3] = pos.x();
			posData[1 + offset3] = pos.y();
			posData[2 + offset3] = pos.z();
			if (cordBuffer == null) {
				cordData = null;
			} else {
				cordData[offset2] = cordBuffer.get(i).x();
				cordData[1 + offset2] = cordBuffer.get(i).y();
			}
			if (normBuffer == null) {
				normData = null;
			} else {
				normData[offset3] = normBuffer.get(i).x();
				normData[1 + offset3] = normBuffer.get(i).y();
				normData[2 + offset3] = normBuffer.get(i).z();
			}
		}
		ArrayList<Integer> indicesData = new ArrayList<>();
		for (int i = 0; i < mesh.mNumFaces(); i++) {
			AIFace face = mesh.mFaces().get(i);
			for (int j = 0; j < face.mNumIndices(); j++) {
				indicesData.add(face.mIndices().get(j));
			}
		}
		int[] indices = new int[indicesData.size()];
		for (int i = 0; i < indicesData.size(); i++) {
			indices[i] = indicesData.get(i);
		}
		return new StaticMesh(DEFAULT_LAYOUT, indices, posData, normData, cordData);
	}
}
