package com.crossly.components;

import com.crossly.components.subcomponents.Mesh;
import com.crossly.interfaces.Component;
import com.crossly.util.FileUtil;
import org.lwjgl.assimp.*;

import java.util.ArrayList;

/**
 * @author Jude Ogboru
 */
public class Model implements Component {

    protected ArrayList<Mesh> meshes = new ArrayList<>();
    protected int referenceCount = 0;

    public interface MeshLoader {
        default Mesh load(AIMesh mesh, AIScene scene) {
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
    }

    protected MeshLoader loader;

    public Model(String modelPath) {
        this(modelPath, new MeshLoader() {
            @Override
            public Mesh load(AIMesh mesh, AIScene scene) {
                return MeshLoader.super.load(mesh, scene);
            }
        });
    }

    public Model(String modelPath, MeshLoader loader) {
        this.loader = loader;
        load(modelPath);
    }

    protected void load(String path) {
        try (AIScene scene = Assimp.aiImportFile(FileUtil.getAbsoluteFilepath(path), Assimp.aiProcess_Triangulate)) {
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
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            meshes.add(loader.load(mesh, scene));
        }
        for (int i = 0; i < node.mNumChildren(); i++) {
            processNode(scene, AINode.create(node.mChildren().get(i)));
        }
    }

    @Override
    public void render() {
        meshes.forEach(Mesh::render);
    }

    @Override
    public void cleanup() {
        if (referenceCount == 0)
            meshes.forEach(Mesh::cleanup);
        else
            decrementReference();
    }

    @Override
    public void incrementReference() {
        referenceCount++;
    }

    @Override
    public void decrementReference() {
        referenceCount--;
        if (referenceCount <= 0)
            cleanup();
    }
}
