package com.crossly.components;

import com.crossly.interfaces.Component;
import com.crossly.util.FileUtil;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL46.*;

public class Mesh implements Component {

    private final int vertexArrayObject;
    private final int count;
    private final ArrayList<Integer> buffers = new ArrayList<>();

    public Mesh(float[] vertexPositions, float[] vertexNormals, float[] vertexTexCoords, float[] vertexColors, int[] indices) {
        assert vertexPositions != null && indices != null;
        count = indices.length;
        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);
        int posBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, posBuffer);
        glBufferData(GL_ARRAY_BUFFER, vertexPositions, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        buffers.add(posBuffer);
        if (vertexNormals != null) {
            int normalBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
            glBufferData(GL_ARRAY_BUFFER, vertexNormals, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(1);
            buffers.add(normalBuffer);
        }
        if (vertexTexCoords != null) {
            int texCoordBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texCoordBuffer);
            glBufferData(GL_ARRAY_BUFFER, vertexTexCoords, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(2);
            buffers.add(texCoordBuffer);
        }
        if (vertexColors != null) {
            int colorBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
            glBufferData(GL_ARRAY_BUFFER, vertexColors, GL_STATIC_DRAW);
            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(3);
            buffers.add(colorBuffer);
        }
        int elementBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        buffers.add(elementBuffer);
    }

    @Override
    public void use() {
        glBindVertexArray(vertexArrayObject);
    }

    @Override
    public void render() {
        use();
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void cleanup() {
        glDeleteVertexArrays(vertexArrayObject);
        buffers.forEach(GL46::glDeleteBuffers);
    }

    public static class Buffer implements Component {

        private final ArrayList<Mesh> meshes = new ArrayList<>();

        public static Buffer create(String path) {
            try (AIScene scene = Assimp.aiImportFile(FileUtil.getAbsoluteFilepath(path), Assimp.aiProcess_Triangulate)) {
                if (scene == null)
                    throw new RuntimeException(Assimp.aiGetErrorString());

                AINode node;
                if (scene.mFlags() == Assimp.AI_SCENE_FLAGS_INCOMPLETE || (node = scene.mRootNode()) == null)
                    throw new RuntimeException(Assimp.aiGetErrorString());

                Buffer root = new Buffer();
                processNode(scene, node, root);
                return root;
            }
        }

        private static void processNode(AIScene scene, AINode node, Buffer parent) {
            for (int i = 0; i < node.mNumMeshes(); i++) {
                AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
                parent.meshes.add(processMesh(mesh));
            }
            for (int i = 0; i < node.mNumChildren(); i++) {
                processNode(scene, AINode.create(node.mChildren().get(i)), parent);
            }
        }

        private static Mesh processMesh(AIMesh mesh) {
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
                posData[1+offset3] = pos.y();
                posData[2+offset3] = pos.z();
                if (cordBuffer == null) {
                    cordData = null;
                } else {
                    cordData[offset2] = cordBuffer.get(i).x();
                    cordData[1+offset2] = cordBuffer.get(i).y();
                }
                if (normBuffer == null) {
                    normData = null;
                } else {
                    normData[offset3] = normBuffer.get(i).x();
                    normData[1+offset3] = normBuffer.get(i).y();
                    normData[2+offset3] = normBuffer.get(i).z();
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
            return new Mesh(posData, normData, cordData, null, indices);
        }

        @Override
        public void render() {
            meshes.forEach(Mesh::render);
        }

        @Override
        public void cleanup() {
            meshes.forEach(Mesh::cleanup);
        }
    }
}
