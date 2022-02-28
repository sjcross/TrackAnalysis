
package io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component;

import org.lwjgl.BufferUtils;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import io.github.sjcross.TrackAnalysis.Plot3D.Utils.DataUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Mesh {

    public static final int FLOAT_SIZE = 4; // number of bytes in a Java float

    private final int indexedVertexCount;
    private final int vao;
    private final int vbo;
    private final int ibo;

    private final float boundingSphereRadius;
    private final boolean supportsTexture;

    public Mesh(MIMeshData meshData) {
        this(meshData.toSIMeshData());
    }

    public Mesh(SIMeshData meshData) {
        this(meshData.getVertices(), meshData.getFaces());
    }

    public Mesh(ArrayList<Vertex> vertices, ArrayList<FaceSI> faces) {
        this(vertices.toArray(new Vertex[vertices.size()]), faces.toArray(new FaceSI[faces.size()]));
    }

    public Mesh(Vertex[] vertices, ArrayList<FaceSI> faces) {
        this(vertices, faces.toArray(new FaceSI[faces.size()]));
    }

    public Mesh(Vertex[] vertices, FaceSI[] faces) {
        boolean hasTextureCoords = true;

        for (Vertex vertex : vertices) {
            if (vertex.getTextureCoord() == null) {
                hasTextureCoords = false;
                break;
            }
        }

        supportsTexture = hasTextureCoords;

        PrimitiveFaceSI[] primitiveFaces = triangulateFaces(faces);

        indexedVertexCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        // bind vao
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesToFloatBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, primitveFacesToIndexBuffer(primitiveFaces), GL_STATIC_DRAW);

        // vertex positions
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, Vector3f.SIZE, GL_FLOAT, false, Vertex.SIZE * FLOAT_SIZE, 0);
        // texture coordinates
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, Vector2f.SIZE, GL_FLOAT, false, Vertex.SIZE * FLOAT_SIZE, Vector3f.SIZE * FLOAT_SIZE);

        // unbind vao
        glBindVertexArray(0);

        boundingSphereRadius = calcBoundingSphereRadius(vertices);
    }

    public void render() {
        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, indexedVertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
    }

    public void dispose() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(ibo);

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    private FloatBuffer verticesToFloatBuffer(Vertex[] vertices) {
        final int floatCount = vertices.length * Vertex.SIZE;

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(floatCount);

        for (Vertex vertex : vertices) {
            DataUtils.putVector3f(floatBuffer, vertex.getPosition());
            DataUtils.putVector2f(floatBuffer, supportsTexture ? vertex.getTextureCoord() : Vector2f.Identity());
        }

        floatBuffer.flip();

        return floatBuffer;
    }

    private static IntBuffer primitveFacesToIndexBuffer(PrimitiveFaceSI[] primitiveFaces) {
        final int indiciesCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indiciesCount);

        for (PrimitiveFaceSI primitiveFace : primitiveFaces) {
            indicesBuffer.put(primitiveFace.getI0());
            indicesBuffer.put(primitiveFace.getI1());
            indicesBuffer.put(primitiveFace.getI2());
        }

        indicesBuffer.flip();

        return indicesBuffer;
    }

    private static float calcBoundingSphereRadius(Vertex[] vertices) {
        float boundingRadius = 0;

        for (Vertex vertex : vertices) {
            boundingRadius = Math.max(boundingRadius, vertex.getPosition().getLength());
        }

        return boundingRadius;
    }

    public float getBoundingSphereRadius() {
        return boundingSphereRadius;
    }

    public boolean isSupportsTexture() {
        return supportsTexture;
    }

    public int getVbo() {
        return vbo;
    }

    public int getIbo() {
        return ibo;
    }

    public int getIndexedVertexCount() {
        return indexedVertexCount;
    }

    private static PrimitiveFaceSI[] triangulateFaces(FaceSI[] faces) {
        ArrayList<PrimitiveFaceSI> Faces = new ArrayList<>();

        for (FaceSI face : faces) {
            for (int i = 1; i <= face.getPrimtiveFaceCount(); i++) {
                Faces.add(new PrimitiveFaceSI(
                        face.getIndexs()[0],
                        face.getIndexs()[i],
                        face.getIndexs()[i + 1]
                ));
            }
        }

        return Faces.toArray(new PrimitiveFaceSI[Faces.size()]);
    }

    private static class PrimitiveFaceSI {
        public static final int SIZE = 3;

        private int i0;
        private int i1;
        private int i2;

        public PrimitiveFaceSI(int i0, int i1, int i2) {
            this.i0 = i0;
            this.i1 = i1;
            this.i2 = i2;
        }

        public int getI0() {
            return i0;
        }

        public int getI1() {
            return i1;
        }

        public int getI2() {
            return i2;
        }
    }
}