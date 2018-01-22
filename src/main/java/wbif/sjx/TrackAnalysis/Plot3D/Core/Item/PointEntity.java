package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;


import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.GenerateMesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Quaternion;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Timepoint;

import java.awt.*;

import static wbif.sjx.TrackAnalysis.Plot3D.Core.Scene.X_AXIS;
import static wbif.sjx.TrackAnalysis.Plot3D.Core.Scene.Y_AXIS;

/**
 * Created by sc13967 on 02/08/2017.
 */
public class PointEntity {
    private final static float TRAIL_RADIUS = 0.5f;
    private final static float PARTICLE_RADIUS = 2.5f;

    private static final Mesh PARTICLE_MESH = GenerateMesh.sphere(PARTICLE_RADIUS, 20);

    private static final int LOWEST_RESOLUTION = 3;
    private static final Mesh PIPE_MESH_LOWEST = GenerateMesh.pipe(TRAIL_RADIUS, LOWEST_RESOLUTION, 1);
    private static final Mesh HINGE_POINT_MESH_LOWEST = GenerateMesh.sphere(TRAIL_RADIUS, LOWEST_RESOLUTION);

    private static final int LOW_RESOLUTION = 6;
    private static final Mesh PIPE_MESH_LOW = GenerateMesh.pipe(TRAIL_RADIUS, LOW_RESOLUTION, 1);
    private static final Mesh HINGE_POINT_MESH_LOW = GenerateMesh.sphere(TRAIL_RADIUS, LOW_RESOLUTION);

    private static final int MEDIUM_RESOLUTION = 10;
    private static final Mesh PIPE_MESH_MEDIUM = GenerateMesh.pipe(TRAIL_RADIUS, MEDIUM_RESOLUTION, 1);
    private static final Mesh HINGE_POINT_MESH_MEDIUM = GenerateMesh.sphere(TRAIL_RADIUS, MEDIUM_RESOLUTION);

    private static final int HIGH_RESOLUTION = 20;
    private static final Mesh PIPE_MESH_HIGH = GenerateMesh.pipe(TRAIL_RADIUS, HIGH_RESOLUTION, 1);
    private static final Mesh HINGE_POINT_MESH_HIGH = GenerateMesh.sphere(TRAIL_RADIUS, HIGH_RESOLUTION);

    private TrackEntity trackEntity;
    private Vector3f globalPosition;
    private Vector3f motilityPosition;
    private Vector3f displayPosition;
    private Color velocityColour;
    private Color totalPathLengthColour;

    public PointEntity(TrackEntity trackEntity, Timepoint<Double> point){
        this.trackEntity = trackEntity;
        globalPosition = DataTypeUtils.toVector3f(point);
        motilityPosition = Vector3f.Add(globalPosition, trackEntity.getMotilityPlotVector());
        velocityColour = measurementToColour((float) trackEntity.getInstantaneousVelocity(point.getF()), (float) trackEntity.getMaximumInstantaneousVelocity());
        totalPathLengthColour = measurementToColour((float) trackEntity.getTotalPathLength(point.getF()), (float) trackEntity.getMaximumTotalPathLength());

    }

    public Color measurementToColour(float measurement, float highestMeasurement){
        float colourIndex = Maths.interpolateRangeLinearly(0, highestMeasurement, 0, 255, measurement);
        return Color.getHSBColor((float) colourIndex/255f,(float)1f,(float)1f);
//        return new Color(255, colourIndex, colourIndex);
    }

    private boolean hasPipe = false;
    private float pipeLength;
    private Quaternion pipeRotation;

    public void createPipe(Vector3f nextPointGlobalPosition) {
        Vector3f pointToNextPoint = Vector3f.Subtract(nextPointGlobalPosition, globalPosition);

        pipeRotation = new Quaternion(pointToNextPoint.getPhi(), X_AXIS);
        pipeRotation.multiply(pointToNextPoint.getTheta() + 90, Y_AXIS);

        pipeLength = pointToNextPoint.getLength();
        hasPipe = true;
    }

    public void renderParticle(ShaderProgram shaderProgram){
        updateDisplayPosition();

        if(FrustumCuller.getInstance().isInsideFrustum(displayPosition, PARTICLE_MESH.getBoundingSphereRadius())) {
            setDisplayColourUniform(shaderProgram);
            Matrix4f globalMatrix = Matrix4f.Translation(displayPosition);
            shaderProgram.setMatrix4fUniform("globalMatrix", globalMatrix);

            PARTICLE_MESH.render();
        }
    }

    public void renderPipe(ShaderProgram shaderProgram){
        updateDisplayPosition();
        setDisplayColourUniform(shaderProgram);
        Matrix4f globalMatrix = Matrix4f.Translation(displayPosition);

        if(FrustumCuller.getInstance().isInsideFrustum(displayPosition, HINGE_POINT_MESH_HIGH.getBoundingSphereRadius())) {
            shaderProgram.setMatrix4fUniform("globalMatrix", globalMatrix);

            switch (trackEntity.getTrackEntityCollection().displayQuality) {
                case LOWEST:
                    HINGE_POINT_MESH_LOWEST.render();
                    break;
                case LOW:
                    HINGE_POINT_MESH_LOW.render();
                    break;
                case MEDIUM:
                    HINGE_POINT_MESH_MEDIUM.render();
                    break;
                case HIGH:
                    HINGE_POINT_MESH_HIGH.render();
                    break;
            }
        }

        if(FrustumCuller.getInstance().isInsideFrustum(displayPosition,pipeLength * PIPE_MESH_HIGH.getBoundingSphereRadius())) {
            globalMatrix.multiply(Matrix4f.QuaternionRotation(pipeRotation));
            globalMatrix.multiply(Matrix4f.StretchY(pipeLength));
            shaderProgram.setMatrix4fUniform("globalMatrix", globalMatrix);

            switch (trackEntity.getTrackEntityCollection().displayQuality) {
                case LOWEST:
                    PIPE_MESH_LOWEST.render();
                    break;
                case LOW:
                    PIPE_MESH_LOW.render();
                    break;
                case MEDIUM:
                    PIPE_MESH_MEDIUM.render();
                    break;
                case HIGH:
                    PIPE_MESH_HIGH.render();
                    break;
            }
        }
    }

    private void updateDisplayPosition(){
        if(trackEntity.getTrackEntityCollection().ifMotilityPlot()){
            displayPosition = motilityPosition;
        }else {
            displayPosition = globalPosition;
        }
    }

    private void setDisplayColourUniform(ShaderProgram shaderProgram){
        Color displayColour;

        switch (trackEntity.getTrackEntityCollection().displayColour){
            case ID:
                displayColour = trackEntity.getColour();
                break;
            case VELOCITY:
                displayColour = velocityColour;
                break;
            case TOTAL_PATH_LENGTH:
                displayColour = totalPathLengthColour;
                break;
            default:
                displayColour = trackEntity.getColour();
                break;
        }

        shaderProgram.setColourUniform("colour", displayColour);
    }

    public Vector3f getGlobalPosition() {
        return globalPosition;
    }

    public boolean hasPipe() {
        return hasPipe;
    }
}
