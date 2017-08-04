package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import org.lwjgl.system.MemoryUtil;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3i;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int vaoId;

    private int vertexVboId;

    private int indexVboId;

    private int vertexCount;

    private float boundingSphereRadius;

    public Mesh(ArrayList<Face> faces){
        create(faces);
    }

    public void create(ArrayList<Face> faces){
        Face[] Faces = new Face[faces.size()];
        faces.toArray(Faces);
        create(Faces);
    }

    public Mesh(Face[] faces){
        create(faces);
    }


    public void create(Face[] faces){
        Vector3f[] VertexPositions = new Vector3f[faces.length * 3];
        Vector3i[] VertexIndices = new Vector3i[faces.length];

        for(int i = 0; i < faces.length; i++){
            VertexPositions[i * 3    ] = faces[i].getvA();
            VertexPositions[i * 3 + 1] = faces[i].getvB();
            VertexPositions[i * 3 + 2] = faces[i].getvC();

            VertexIndices[i] = new Vector3i(i * 3, i * 3 + 1, i * 3 + 2);
        }

        calcBoundingSphereRadius(VertexPositions);

        float[] vertexPositions = DataTypeUtils.toFloatArray(VertexPositions);
        int[] vertexIndices = DataTypeUtils.toIntArray(VertexIndices);

        FloatBuffer vertexBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = vertexIndices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            vertexVboId = glGenBuffers();
            vertexBuffer = MemoryUtil.memAllocFloat(vertexPositions.length);
            vertexBuffer.put(vertexPositions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            indexVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(vertexIndices.length);
            indicesBuffer.put(vertexIndices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (vertexBuffer != null) {
                MemoryUtil.memFree(vertexBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public void calcBoundingSphereRadius(Vector3f[] vertexPositions){
        for(Vector3f vertex: vertexPositions) {
            float length = vertex.getLength();
            boundingSphereRadius = length > boundingSphereRadius ? length : boundingSphereRadius;
        }
    }

    public float getBoundingSphereRadius() {
        return boundingSphereRadius;
    }

    public void render(){
        // Draw the mesh
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void dispose() {
        // Disable the VAO
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexVboId);
        glDeleteBuffers(indexVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
