package com.crossly.coffee_engine.component.graphics;

import com.crossly.coffee_engine.component.SharedComponent;
import com.crossly.util.FileUtil;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author Jude Ogboru
 */
public abstract class Mesh extends SharedComponent {

	protected int handle = 0;
	protected final List<Integer> bufferObjects = new ArrayList<>();
	protected int vertexCount = 0;
	protected boolean drawElements = false;
	protected final List<Mesh> subMeshes = new ArrayList<>();

	protected final void loadMeshes(String meshPath) {
		String tempFile = FileUtil.extractResourceToTempFile(
				meshPath,
				meshPath.substring(0, meshPath.lastIndexOf('.')),
				meshPath.substring(meshPath.lastIndexOf('.'))).getAbsolutePath();
		try (AIScene scene = Assimp.aiImportFile(tempFile, Assimp.aiProcess_Triangulate)) {
			if (scene == null)
				throw new RuntimeException(Assimp.aiGetErrorString());
			AINode node;
			if (scene.mFlags() == Assimp.AI_SCENE_FLAGS_INCOMPLETE || (node = scene.mRootNode()) == null)
				throw new RuntimeException(Assimp.aiGetErrorString());
			processNode(scene, node);
		}
	}

	protected void processNode(AIScene scene, AINode node) {
		for (int i = 0; i < node.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh
					.create(Objects.requireNonNull(scene.mMeshes()).get(Objects.requireNonNull(node.mMeshes()).get(i)));
			subMeshes.add(loadMesh(mesh, scene));
		}
		for (int i = 0; i < node.mNumChildren(); i++)
			processNode(scene, AINode.create(Objects.requireNonNull(node.mChildren()).get(i)));
	}

	protected abstract Mesh loadMesh(AIMesh mesh, AIScene scene);

	public final void render() {
		if (handle != 0) {
			glBindVertexArray(handle);
			if (drawElements)
				glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
			else
				glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		}
		subMeshes.forEach(Mesh::render);
	}

	protected static int createArrayBuffer(float[] data, int storage) {
		int buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, data, storage);
		return buffer;
	}

	protected static int createArrayBuffer(float[] data, int storage, int index, int dataSize, int stride, int offset) {
		int buffer = createArrayBuffer(data, storage);
		interpretBuffer(index, dataSize, stride, offset);
		return buffer;
	}

	protected static void interpretBuffer(int index, int dataSize, int stride, int offset) {
		if (index < 0)
			return;
		glVertexAttribPointer(index, dataSize, GL_FLOAT, false, stride * Float.BYTES, (long) offset * Float.BYTES);
		glEnableVertexAttribArray(index);
	}

	protected static int createElementBuffer(int[] data, int storage) {
		int buffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, storage);
		return buffer;
	}

	@Override
	protected void cleanup() {
		glDeleteVertexArrays(handle);
		bufferObjects.forEach(GL46::glDeleteBuffers);
		subMeshes.forEach(Mesh::cleanup);
	}

	public static class Layout {
		private int stride = 0;
		protected final ArrayList<Integer> sizes = new ArrayList<>();

		public Layout offset(int size) {
			sizes.add(size);
			stride += size;
			return this;
		}

		public final int getSize() {
			return sizes.size();
		}

		public final int getStride() {
			return stride;
		}

		public final int getSize(int i) {
			return sizes.get(i);
		}
	}
}
