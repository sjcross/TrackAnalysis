
package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import org.apache.commons.math3.util.FastMath;
import org.lwjgl.BufferUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class Mesh {
    private static final int FLOAT_SIZE = 4;

    private final int indexedVertexCount;
    private final int vao;
    private final int vbo;
    private final int ibo;

    private final float boundingSphereRadius;

    public Mesh(RawMeshData meshData){
        this(meshData.getVertices(), meshData.getFaces());
    }

    public Mesh(ArrayList<Vector3f> vertices, ArrayList<Face> faces) {
        this(vertices.toArray(new Vector3f[vertices.size()]), faces.toArray(new Face[faces.size()]));
    }

    public Mesh(Vector3f[] vertices, ArrayList<Face> faces) {
        this(vertices, faces.toArray(new Face[faces.size()]));
    }

    public Mesh(Vector3f[] vertices, Face[] faces){
        PrimitiveFaceSI[] primitiveFaces = triangulateFaces(faces);

        indexedVertexCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesToFloatBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, primitveFacesToIndexBuffer(primitiveFaces), GL_STATIC_DRAW);

        // vertex positions
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * FLOAT_SIZE, 0);

        glBindVertexArray(0);

        boundingSphereRadius = calcBoundingSphereRadius(vertices);
    }

    public void render() {
        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, indexedVertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);

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

    private FloatBuffer verticesToFloatBuffer(Vector3f[] vertices){
        final int floatCount = vertices.length * 3;

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(floatCount);

        for(Vector3f vertex : vertices){
            floatBuffer.put(vertex.getX());
            floatBuffer.put(vertex.getY());
            floatBuffer.put(vertex.getZ());
        }

        floatBuffer.flip();

        return floatBuffer;
    }

    private static IntBuffer primitveFacesToIndexBuffer(PrimitiveFaceSI[] primitiveFaces){
        final int indiciesCount = primitiveFaces.length * PrimitiveFaceSI.SIZE;

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indiciesCount);

        int[] indicesArray = new int[indiciesCount];

        for(int i = 0; i < primitiveFaces.length; i++){
            indicesArray[i * 3    ] = primitiveFaces[i].getI0();
            indicesArray[i * 3 + 1] = primitiveFaces[i].getI1();
            indicesArray[i * 3 + 2] = primitiveFaces[i].getI2();
        }

        indicesBuffer.put(indicesArray);
        indicesBuffer.flip();

        return indicesBuffer;
    }

    private static float calcBoundingSphereRadius(Vector3f[] vertices){
        float boundingSphereRadius = 0;

        for(Vector3f vertex: vertices) {
            boundingSphereRadius = FastMath.max(boundingSphereRadius, vertex.getLength());
        }

        return boundingSphereRadius;
    }

    public float getBoundingSphereRadius() {
        return boundingSphereRadius;
    }

    private static PrimitiveFaceSI[] triangulateFaces(Face[] faces){
        ArrayList<PrimitiveFaceSI> Faces = new ArrayList<>();

        for(Face face : faces) {
            for (int i = 1; i <= face.getPrimtiveFaceCount(); i++) {
                Faces.add(new PrimitiveFaceSI(
                        face.getIndexs()[0    ],
                        face.getIndexs()[i    ],
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

        public PrimitiveFaceSI(int i0, int i1, int i2){
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
