package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import org.lwjgl.system.MemoryUtil;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3i;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int vaoId;

    private int vertexVboId;

    private int indexVboId;

    private int textureCoordVboId;

    private int vertexCount;

    private float boundingSphereRadius;

    private boolean supportsTexture = false;

    public Mesh(List<Face> faces){
        Face[] Faces = new Face[faces.size()];
        faces.toArray(Faces);
        create(Faces);
    }

    public Mesh(Face[] faces){
        create(faces);
    }

    private void create(Face[] faces){
        Vector3f[] VertexPositions = new Vector3f[faces.length * 3];
        Vector3i[] VertexIndices = new Vector3i[faces.length];

        for(int i = 0; i < faces.length; i++){
            VertexPositions[i * 3    ] = faces[i].getvA();
            VertexPositions[i * 3 + 1] = faces[i].getvB();
            VertexPositions[i * 3 + 2] = faces[i].getvC();

            VertexIndices[i] = new Vector3i(i * 3, i * 3 + 1, i * 3 + 2);
        }

        calcBoundingSphereRadius(VertexPositions);

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        createVertexVbo(VertexPositions);
        createIndicesVbo(VertexIndices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public Mesh(TexturedFace[] faces){
        create(faces);
    }

    private void create(TexturedFace[] faces) {
        Vector3f[] VertexPositions = new Vector3f[faces.length * 3];
        Vector2f[] TextureCoords = new Vector2f[faces.length * 3];
        Vector3i[] VertexIndices = new Vector3i[faces.length];

        for (int i = 0; i < faces.length; i++) {
            VertexPositions[i * 3] = faces[i].getvA();
            VertexPositions[i * 3 + 1] = faces[i].getvB();
            VertexPositions[i * 3 + 2] = faces[i].getvC();

            TextureCoords[i * 3] = faces[i].gettA();
            TextureCoords[i * 3 + 1] = faces[i].gettB();
            TextureCoords[i * 3 + 2] = faces[i].gettC();

            VertexIndices[i] = new Vector3i(i * 3, i * 3 + 1, i * 3 + 2);
        }

        calcBoundingSphereRadius(VertexPositions);

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        createVertexVbo(VertexPositions);
        createIndicesVbo(VertexIndices);
        createTextureCoordVbo(TextureCoords);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void createVertexVbo(Vector3f[] VertexPositions){
        vertexCount = VertexPositions.length;
        float[] vertexPositions = DataTypeUtils.toFloatArray(VertexPositions);
        FloatBuffer vertexPositionsBuffer = null;

        try {
            vertexVboId = glGenBuffers();
            vertexPositionsBuffer = MemoryUtil.memAllocFloat(vertexPositions.length);
            vertexPositionsBuffer.put(vertexPositions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
            glBufferData(GL_ARRAY_BUFFER, vertexPositionsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        }finally {
            if (vertexPositionsBuffer != null) {
                MemoryUtil.memFree(vertexPositionsBuffer);
            }
        }
    }

    private void createIndicesVbo(Vector3i[] VertexIndices){
        int[] vertexIndices = DataTypeUtils.toIntArray(VertexIndices);
        IntBuffer indicesBuffer = null;

        try {
            // Index VBO
            indexVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(vertexIndices.length);
            indicesBuffer.put(vertexIndices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        }finally {
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    private void createTextureCoordVbo(Vector2f[] TextureCoords){
        supportsTexture = true;
        float[] textureCoords = DataTypeUtils.toFloatArray(TextureCoords);
        FloatBuffer textureCoordsBuffer = null;

        try {
            textureCoordVboId = glGenBuffers();
            textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
            textureCoordsBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, textureCoordVboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        }finally {
            if (textureCoordsBuffer != null) {
                MemoryUtil.memFree(textureCoordsBuffer);
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

    public void render(Texture texture){
        if(supportsTexture && texture != null) {
            // Bind Texture
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getTextureId());

            // Draw the mesh
            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

            // Restore state
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            // Unbind Texture
            glBindTexture(GL_TEXTURE_2D, 0);
        }else {
            render();
        }
    }

    public void dispose() {
        // Disable the VAO
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexVboId);
        glDeleteBuffers(indexVboId);
        if(supportsTexture){
            glDeleteBuffers(textureCoordVboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public boolean supportsTexture(){
        return supportsTexture;
    }
}
