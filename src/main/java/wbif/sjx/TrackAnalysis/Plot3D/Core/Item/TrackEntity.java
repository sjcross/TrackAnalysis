package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import org.lwjgl.BufferUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Vertex;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.MeshFactory;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Quaternion;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Track;

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
import static wbif.sjx.TrackAnalysis.Plot3D.Core.Scene.X_AXIS;
import static wbif.sjx.TrackAnalysis.Plot3D.Core.Scene.Y_AXIS;
import static wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh.FLOAT_SIZE;
import static wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f.ORDER;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class TrackEntity {

    private static final float TRAIL_RADIUS = 0.5f;
    private static final float PARTICLE_RADIUS = 2.5f;

    private static final int PARTICLE_RESOLUTION = 20;
    private static final int LOWEST_RESOLUTION = 3;
    private static final int LOW_RESOLUTION = 6;
    private static final int MEDIUM_RESOLUTION = 10;
    private static final int HIGH_RESOLUTION = 20;

    private static Mesh PARTICLE_MESH;
    private static Mesh PIPE_MESH_LOWEST;
    private static Mesh HINGE_POINT_MESH_LOWEST;
    private static Mesh PIPE_MESH_LOW;
    private static Mesh HINGE_POINT_MESH_LOW;
    private static Mesh PIPE_MESH_MEDIUM;
    private static Mesh HINGE_POINT_MESH_MEDIUM;
    private static Mesh PIPE_MESH_HIGH;
    private static Mesh HINGE_POINT_MESH_HIGH;

    private static boolean MESHES_INITIALISED = false;

    public static void initStaticMeshes() {
        if (!MESHES_INITIALISED) {
            PARTICLE_MESH = MeshFactory.sphere(PARTICLE_RADIUS, PARTICLE_RESOLUTION);
            PIPE_MESH_LOWEST = MeshFactory.pipe(TRAIL_RADIUS, LOWEST_RESOLUTION, 1);
            HINGE_POINT_MESH_LOWEST = MeshFactory.sphere(TRAIL_RADIUS, LOWEST_RESOLUTION);
            PIPE_MESH_LOW = MeshFactory.pipe(TRAIL_RADIUS, LOW_RESOLUTION, 1);
            HINGE_POINT_MESH_LOW = MeshFactory.sphere(TRAIL_RADIUS, LOW_RESOLUTION);
            PIPE_MESH_MEDIUM = MeshFactory.pipe(TRAIL_RADIUS, MEDIUM_RESOLUTION, 1);
            HINGE_POINT_MESH_MEDIUM = MeshFactory.sphere(TRAIL_RADIUS, MEDIUM_RESOLUTION);
            PIPE_MESH_HIGH = MeshFactory.pipe(TRAIL_RADIUS, HIGH_RESOLUTION, 1);
            HINGE_POINT_MESH_HIGH = MeshFactory.sphere(TRAIL_RADIUS, HIGH_RESOLUTION);
            MESHES_INITIALISED = true;
        }
    }

    public static void disposeStaticMeshes() {
        if (MESHES_INITIALISED) {
            PARTICLE_MESH.dispose();
            PIPE_MESH_LOWEST.dispose();
            HINGE_POINT_MESH_LOWEST.dispose();
            PIPE_MESH_LOW.dispose();
            HINGE_POINT_MESH_LOW.dispose();
            PIPE_MESH_MEDIUM.dispose();
            HINGE_POINT_MESH_MEDIUM.dispose();
            PIPE_MESH_HIGH.dispose();
            HINGE_POINT_MESH_HIGH.dispose();
            MESHES_INITIALISED = false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final RenderData renderDataParticle;
    private final RenderData renderDataHinge;
    private final RenderData renderDataPipe;

    private final int igmboHinge;
    private final int igmboPipe;
    private final int icboVelocity;
    private final int icboPathLength;

    private final Color colour;
    private final Matrix4f motilityPlotMatrix;

    public TrackEntity(Track track, float maxInstantaneousVelocity, float maxTotalPathLength) {
        this.colour = RNG.Colour();

        ArrayList<Vector3f> globalPositionList = new ArrayList<>();

        for (int frame : track.keySet()) {
            globalPositionList.add(frame, DataUtils.toVector3f(track.get(frame)));
        }

        this.motilityPlotMatrix = Matrix4f.Translation(Vector3f.Negative(globalPositionList.get(0)));

        FloatBuffer hingeMatricesFloatBuffer = BufferUtils.createFloatBuffer(globalPositionList.size() * ORDER * ORDER);
        FloatBuffer pipeMatricesFloatBuffer = BufferUtils.createFloatBuffer((globalPositionList.size() - 1) * ORDER * ORDER);

        for (int i = 0; i < globalPositionList.size() - 1; i++) {
            Vector3f currentPosition = globalPositionList.get(i);
            Vector3f nextPosition = globalPositionList.get(i + 1);
            Vector3f pipeVector = Vector3f.Subtract(nextPosition, currentPosition);
            Quaternion pipeRotation = new Quaternion(pipeVector.getPhi(), X_AXIS);
            pipeRotation.multiply(pipeVector.getTheta() + 90, Y_AXIS);

            Matrix4f globalMatrix = Matrix4f.Translation(currentPosition);
            globalMatrix.multiply(Matrix4f.QuaternionRotation(pipeRotation));


            DataUtils.putMatrix4f(hingeMatricesFloatBuffer, globalMatrix);

            globalMatrix.multiply(Matrix4f.StretchY(pipeVector.getLength()));

            DataUtils.putMatrix4f(pipeMatricesFloatBuffer, globalMatrix);
        }

        DataUtils.putMatrix4f(hingeMatricesFloatBuffer, Matrix4f.Translation(globalPositionList.get(globalPositionList.size() - 1)));

        hingeMatricesFloatBuffer.flip();
        igmboHinge = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, igmboHinge);
        glBufferData(GL_ARRAY_BUFFER, hingeMatricesFloatBuffer, GL_STATIC_DRAW);
        hingeMatricesFloatBuffer.clear();

        pipeMatricesFloatBuffer.flip();
        igmboPipe = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, igmboPipe);
        glBufferData(GL_ARRAY_BUFFER, pipeMatricesFloatBuffer, GL_STATIC_DRAW);
        pipeMatricesFloatBuffer.clear();


        FloatBuffer colourFloatBuffer = BufferUtils.createFloatBuffer(globalPositionList.size());

        TreeMap<Integer, Double> instantaneousVelocity = track.getInstantaneousSpeed();

        for (int i = 0; i < globalPositionList.size(); i++) {
            colourFloatBuffer.put(Maths.scaleRange(0, maxInstantaneousVelocity, 0, 1, instantaneousVelocity.get(i).floatValue()));
        }

        colourFloatBuffer.flip();
        icboVelocity = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, icboVelocity);
        glBufferData(GL_ARRAY_BUFFER, colourFloatBuffer, GL_STATIC_DRAW);
        colourFloatBuffer.clear();

        TreeMap<Integer, Double> totalPathLength = track.getRollingTotalPathLength();

        for (int i = 0; i < globalPositionList.size(); i++) {
            colourFloatBuffer.put(Maths.scaleRange(0, maxTotalPathLength, 0, 1, totalPathLength.get(i).floatValue()));
        }

        colourFloatBuffer.flip();
        icboPathLength = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, icboPathLength);
        glBufferData(GL_ARRAY_BUFFER, colourFloatBuffer, GL_STATIC_DRAW);
        colourFloatBuffer.clear();


        renderDataParticle = new RenderData(igmboHinge);
        renderDataParticle.bind();
        renderDataParticle.bindMeshBuffers(PARTICLE_MESH);

        renderDataHinge = new RenderData(igmboHinge);
        renderDataPipe = new RenderData(igmboPipe);
    }

    public void renderParticle(int frame) {
        renderDataParticle.bind();
        renderDataParticle.render(1, frame);
    }

    public void renderTrail(int frame, int trailLength) {
        final int baseInstance = trailLength > frame ? 0 : frame - trailLength;
        final int primCount = frame - baseInstance;

        renderDataHinge.bind();
        renderDataHinge.render(primCount, baseInstance);
        renderDataPipe.bind();
        renderDataPipe.render(primCount, baseInstance);
    }

    public void updateColourBuffer(TrackEntityCollection.DisplayColour colour) {
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

    public void updateMeshBuffer(TrackEntityCollection.RenderQuality quality) {
        switch (quality) { // Change vao's vbo and ibo
            case LOWEST:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(HINGE_POINT_MESH_LOWEST);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(PIPE_MESH_LOWEST);
                break;
            case LOW:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(HINGE_POINT_MESH_LOW);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(PIPE_MESH_LOW);
                break;
            case MEDIUM:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(HINGE_POINT_MESH_MEDIUM);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(PIPE_MESH_MEDIUM);
                break;
            case HIGH:
                renderDataHinge.bind();
                renderDataHinge.bindMeshBuffers(HINGE_POINT_MESH_HIGH);

                renderDataPipe.bind();
                renderDataPipe.bindMeshBuffers(PIPE_MESH_HIGH);
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
            glVertexAttribPointer(2, Vector4f.SIZE, GL_FLOAT, false, ORDER * ORDER * FLOAT_SIZE, 0);
            glVertexAttribDivisor(2, 1);
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, Vector4f.SIZE, GL_FLOAT, false, ORDER * ORDER * FLOAT_SIZE, Vector4f.SIZE * FLOAT_SIZE);
            glVertexAttribDivisor(3, 1);
            glEnableVertexAttribArray(4);
            glVertexAttribPointer(4, Vector4f.SIZE, GL_FLOAT, false, ORDER * ORDER * FLOAT_SIZE, 2 * Vector4f.SIZE * FLOAT_SIZE);
            glVertexAttribDivisor(4, 1);
            glEnableVertexAttribArray(5);
            glVertexAttribPointer(5, Vector4f.SIZE, GL_FLOAT, false, ORDER * ORDER * FLOAT_SIZE, 3 * Vector4f.SIZE * FLOAT_SIZE);
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
