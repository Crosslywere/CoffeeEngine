package com.crossly.components.subcomponents;

import com.crossly.interfaces.SubComponent;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL46.*;

/**
 * @author Jude Ogboru
 */
public class Mesh implements SubComponent {

    protected static final Mesh DEFAULT_UNIT_Z_PLANE = new Mesh(
            new float[] {
                    -1, -1, 0,
                     1, -1, 0,
                     1,  1, 0,
                    -1,  1, 0,
            },
            new float[] {
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1,
            },
            new float[] {
                    0, 0,
                    1, 0,
                    1, 1,
                    0, 1,
            },
            new float[] {
                    1, 1, 1,
                    1, 1, 1,
                    1, 1, 1,
                    1, 1, 1,
            },
            new int[] {
                    0, 1, 2,
                    2, 3, 0
            }
    );

    public static Mesh getDefaultUnitZPlane() {
        return DEFAULT_UNIT_Z_PLANE;
    }

    protected final int vertexArrayObject;
    protected final int count;
    protected final ArrayList<Integer> buffers = new ArrayList<>();

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
}
