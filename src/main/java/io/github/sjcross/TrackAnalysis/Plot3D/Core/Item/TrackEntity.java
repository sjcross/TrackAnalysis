package io.github.sjcross.TrackAnalysis.Plot3D.Core.Item;

import org.lwjgl.BufferUtils;
import io.github.sjcross.TrackAnalysis.GUI.TrackPlotControl.*;
import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component.Vertex;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Maths;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Matrix4f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Quaternion;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector4f;
import io.github.sjcross.TrackAnalysis.Plot3D.Utils.DataUtils;
import io.github.sjcross.TrackAnalysis.Plot3D.Utils.RNG;
import io.github.sjcross.common.object.tracks.Track;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import static org.lwjgl.opengl.GL42.glDrawElementsInstancedBaseInstance;
import static io.github.sjcross.TrackAnalysis.Plot3D.Core.Scene.X_AXIS;
import static io.github.sjcross.TrackAnalysis.Plot3D.Core.Scene.Y_AXIS;
import static io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component.Mesh.FLOAT_SIZE;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class TrackEntity {

    private final RenderData renderDataParticle;
    private final RenderData renderDataHinge;
    private final RenderData renderDataPipe;

    private final int igmboHinge;
    private final int igmboPipe;
    private final int icboVelocity;
    private final int icboPathLength;

    private final Color colour;
    private final Matrix4f motilityPlotMatrix;

    public TrackEntity(Track track, float maxInstSpeed, float maxPathLength, TrackEntityCollection tec) {
        colour = RNG.Colour();

        ArrayList<Vector3f> globalPosList = new ArrayList<>();

        for (int frame : track.keySet()) {
            globalPosList.add(frame, DataUtils.toVector3f(track.get(frame)));
        }

        motilityPlotMatrix = Matrix4f.Translation(Vector3f.Negative(globalPosList.get(0)));

        FloatBuffer hingeMatricesBuffer = BufferUtils.createFloatBuffer(globalPosList.size() * Matrix4f.ORDER * Matrix4f.ORDER);
        FloatBuffer pipeMatricesBuffer = BufferUtils.createFloatBuffer((globalPosList.size() - 1) * Matrix4f.ORDER * Matrix4f.ORDER);

        for (int i = 0; i < globalPosList.size() - 1; i++) {
            Vector3f currentPosition = globalPosList.get(i);
            Vector3f nextPosition = globalPosList.get(i + 1);
            Vector3f pipeVector = Vector3f.Subtract(nextPosition, currentPosition);
            Quaternion pipeRotation = new Quaternion(pipeVector.getPhi(), X_AXIS);
            pipeRotation.multiply(pipeVector.getTheta() + 90, Y_AXIS);

            Matrix4f globalMatrix = Matrix4f.Translation(currentPosition);
            globalMatrix.multiply(Matrix4f.QuaternionRotation(pipeRotation));


            DataUtils.putMatrix4f(hingeMatricesBuffer, globalMatrix);

            globalMatrix.multiply(Matrix4f.StretchY(pipeVector.getLength()));

            DataUtils.putMatrix4f(pipeMatricesBuffer, globalMatrix);
        }

        DataUtils.putMatrix4f(hingeMatricesBuffer, Matrix4f.Translation(globalPosList.get(globalPosList.size() - 1)));

        hingeMatricesBuffer.flip();
        igmboHinge = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, igmboHinge);
        glBufferData(GL_ARRAY_BUFFER, hingeMatricesBuffer, GL_STATIC_DRAW);
        hingeMatricesBuffer.clear();

        pipeMatricesBuffer.flip();
        igmboPipe = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, igmboPipe);
        glBufferData(GL_ARRAY_BUFFER, pipeMatricesBuffer, GL_STATIC_DRAW);
        pipeMatricesBuffer.clear();


        FloatBuffer colourFloatBuffer = BufferUtils.createFloatBuffer(globalPosList.size());

        TreeMap<Integer, Double> instantaneousVelocity = track.getInstantaneousSpeed();

        for (int i = 0; i < globalPosList.size(); i++) {
            colourFloatBuffer.put(Maths.scaleRange(0, maxInstSpeed, 0, 1, instantaneousVelocity.get(i).floatValue()));
        }

        colourFloatBuffer.flip();
        icboVelocity = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, icboVelocity);
        glBufferData(GL_ARRAY_BUFFER, colourFloatBuffer, GL_STATIC_DRAW);
        colourFloatBuffer.clear();

        TreeMap<Integer, Double> totalPathLength = track.getRollingTotalPathLength();

        for (int i = 0; i < globalPosList.size(); i++) {
            colourFloatBuffer.put(Maths.scaleRange(0, maxPathLength, 0, 1, totalPathLength.get(i).floatValue()));
        }

        colourFloatBuffer.flip();
        icboPathLength = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, icboPathLength);
        glBufferData(GL_ARRAY_BUFFER, colourFloatBuffer, GL_STATIC_DRAW);
        colourFloatBuffer.clear();


        renderDataParticle = new RenderData(igmboHinge);
        renderDataParticle.bind();
        renderDataParticle.bindMeshBuffers(tec.particleMesh);

        renderDataHinge = new RenderData(igmboHinge);
        renderDataPipe = new RenderData(igmboPipe);
    }

    public void renderParticle(int frame) {
        renderDataParticle.bind();
        renderDataParticle.render(1, frame);
    }

    public void renderTrail(int frame, int trailLength, boolean renderHinge) {
        final int baseInstance = trailLength > frame ? 0 : frame - trailLength;
        final int primCount = frame - baseInstance;

        if(renderHinge) {
            renderDataHinge.bind();
            renderDataHinge.render(primCount, baseInstance);
        }
        renderDataPipe.bind();
        renderDataPipe.render(primCount, baseInstance);
    }

    public void updateColourBuffer(DisplayColour colour) {
        switch (colour) { // Change vao's icbo
            case VELOCITY:
                renderDataParticle.bind();
                renderDataParticle.bindColourBuffer(icboVelocity);

                renderDataHinge.bind();
                renderDataHinge.bindColourBuffer(icboVelocity);

                renderDataPipe.bind();
                renderDataPipe.bindColourBuffer(icboVelocity);
                break;
            case TOTAL_PATH_LENGTH:
                renderDataParticle.bind();
                renderDataParticle.bindColourBuffer(icboPathLength);

                renderDataHinge.bind();
                renderDataHinge.bindColourBuffer(icboPathLength);

                renderDataPipe.bind();
                renderDataPipe.bindColourBuffer(icboPathLength);
                break;
        }
    }

    public void updateMeshBuffer(RenderQuality quality, TrackEntityCollection tec) {
        switch (quality) { // Change vao's vbo and ibo
            case LOWEST:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(tec.hingePointMeshLowest);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(tec.pipeMeshLowest);
                break;
            case LOW:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(tec.hingePointMeshLow);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(tec.pipeMeshLow);
                break;
            case MEDIUM:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(tec.hingePointMeshMedium);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(tec.pipeMeshMedium);
                break;
            case HIGH:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(tec.hingePointMeshHigh);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(tec.pipeMeshHigh);
                break;
        }
    }

    public Color getColour() {
        return colour;
    }

    public Matrix4f getMotilityPlotMatrix() {
        return motilityPlotMatrix;
    }

    public void dispose() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(igmboHinge);
        glDeleteBuffers(igmboPipe);
        glDeleteBuffers(icboVelocity);
        glDeleteBuffers(icboPathLength);

        glBindVertexArray(0);
        renderDataParticle.dispose();
        renderDataHinge.dispose();
        renderDataPipe.dispose();
    }

    private static class RenderData {
        private final int vao;
        private int indexedVertexCount;

        public RenderData(int igmbo) {
            vao = glGenVertexArrays();
            bind();

            // instanced matrices
            glBindBuffer(GL_ARRAY_BUFFER, igmbo);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, Vector4f.SIZE, GL_FLOAT, false, Matrix4f.ORDER * Matrix4f.ORDER * FLOAT_SIZE, 0);
            glVertexAttribDivisor(2, 1);
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, Vector4f.SIZE, GL_FLOAT, false, Matrix4f.ORDER * Matrix4f.ORDER * FLOAT_SIZE, Vector4f.SIZE * FLOAT_SIZE);
            glVertexAttribDivisor(3, 1);
            glEnableVertexAttribArray(4);
            glVertexAttribPointer(4, Vector4f.SIZE, GL_FLOAT, false, Matrix4f.ORDER * Matrix4f.ORDER * FLOAT_SIZE, 2 * Vector4f.SIZE * FLOAT_SIZE);
            glVertexAttribDivisor(4, 1);
            glEnableVertexAttribArray(5);
            glVertexAttribPointer(5, Vector4f.SIZE, GL_FLOAT, false, Matrix4f.ORDER * Matrix4f.ORDER * FLOAT_SIZE, 3 * Vector4f.SIZE * FLOAT_SIZE);
            glVertexAttribDivisor(5, 1);
        }

        public void bindMeshBuffers(Mesh mesh) {
            indexedVertexCount = mesh.getIndexedVertexCount();

            // mesh buffers
            glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo());
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, Vector3f.SIZE, GL_FLOAT, false, Vertex.SIZE * FLOAT_SIZE, 0);
        }

        public void bindColourBuffer(int icbo) {
            // instanced colour
            glBindBuffer(GL_ARRAY_BUFFER, icbo);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 1, GL_FLOAT, false, FLOAT_SIZE, 0);
            glVertexAttribDivisor(1, 1);
        }

        public void render(int primCount, int baseInstance) {
            glDrawElementsInstancedBaseInstance(GL_TRIANGLES, indexedVertexCount, GL_UNSIGNED_INT, 0, primCount, baseInstance);
        }

        public void bind() {
            glBindVertexArray(vao);
        }

        public void dispose() {
            glDeleteVertexArrays(vao);
        }
    }
}
