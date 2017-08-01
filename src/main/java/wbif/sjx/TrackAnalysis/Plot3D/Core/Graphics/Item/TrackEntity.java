package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.GenerateMesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Track;

import java.awt.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackEntity {
    private static final Mesh PIPE_MESH = GenerateMesh.uncappedCylinder(1, 10, 1);
    private static final Mesh HINGE_POINT_MESH = GenerateMesh.sphere(1, 10);
    private static final Mesh PARTICLE_MESH = GenerateMesh.sphere(1, 20);
    private static float trailRadius = 0.5f;
    private static float particleRadius = 2.5f;

    private final Track track;
    private Color colour;
    private final Vector3f motilityPlotVector;

    public TrackEntity(Track track){
        this(track, RNG.Colour());
    }

    public TrackEntity(Track track, Color colour){
        this.track = track;
        this.colour = colour;
        motilityPlotVector = Vector3f.Negative(DataTypeUtils.toVector3f(track.get(track.getF()[0])));
    }

    public void render(ShaderProgram shaderProgram, FrustumCuller frustumCuller, int frame, boolean showTrail, boolean motilityPlot){
        if(track.get(frame) != null) {
            shaderProgram.setColourUniform("colour", colour);
            Matrix4f combinedTransformationMatrix;


            if (showTrail) {
                for (int f = 0; f < frame; f++) {
                    if (track.hasFrame(f) && track.hasFrame(f + 1)) {

                        Vector3f currentFramePoint = DataTypeUtils.toVector3f(track.get(f));
                        Vector3f nextFramePoint = DataTypeUtils.toVector3f(track.get(f + 1));
                        Vector3f midpointBetweenFrames = Maths.midpointBetweenVectorPositions(currentFramePoint, nextFramePoint);
                        Vector3f currentFramePointTonextFramePoint = Vector3f.Subtract(currentFramePoint, nextFramePoint);
                        Vector3f pipeStretchScaleFactors = new Vector3f(trailRadius, currentFramePointTonextFramePoint.getLength(), trailRadius);
                        Vector3f bearingBetweenFramePoints = new Vector3f(0, -currentFramePointTonextFramePoint.getTheta(), -currentFramePointTonextFramePoint.getPhi());


                        Vector3f hingePosition = currentFramePoint;
                        Vector3f pipePosition = midpointBetweenFrames;

                        if(motilityPlot){
                            hingePosition.change(motilityPlotVector);
                            pipePosition.change(motilityPlotVector);
                        }


                        if (frustumCuller.isInsideFrustum(hingePosition, trailRadius, HINGE_POINT_MESH)) {
                            //Renders a point at all previous frame locations, this point shares a radius with the cylinders
                            combinedTransformationMatrix = Matrix4f.identity();
                            combinedTransformationMatrix.apply(Matrix4f.translation(currentFramePoint));
                            combinedTransformationMatrix.apply(Matrix4f.enlargement(trailRadius));

                            shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", combinedTransformationMatrix);
                            HINGE_POINT_MESH.render();
                        }

                        if (frustumCuller.isInsideFrustum(midpointBetweenFrames, pipeStretchScaleFactors, PIPE_MESH)) {
                            //Renders the cylinders to connect the points at each frame
                            combinedTransformationMatrix = Matrix4f.identity();
                            combinedTransformationMatrix.apply(Matrix4f.translation(midpointBetweenFrames));
                            combinedTransformationMatrix.apply(Matrix4f.rotation(bearingBetweenFramePoints));
                            combinedTransformationMatrix.apply(Matrix4f.stretch(pipeStretchScaleFactors));

                            shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", combinedTransformationMatrix);
                            PIPE_MESH.render();
                        }
                    }
                }
            }

            //Renders a larger point at the current frame
            if (frame >= 0 && frame < track.size()) {
                Vector3f particlePosition = DataTypeUtils.toVector3f(track.get(frame));

                if(motilityPlot){
                    particlePosition.change(motilityPlotVector);
                }

                if(frustumCuller.isInsideFrustum(particlePosition, particleRadius, PARTICLE_MESH)) {
                    combinedTransformationMatrix = Matrix4f.identity();
                    combinedTransformationMatrix.apply(Matrix4f.translation(particlePosition));
                    combinedTransformationMatrix.apply(Matrix4f.enlargement(particleRadius));

                    shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", combinedTransformationMatrix);
                    PARTICLE_MESH.render();
                }
            }
        }
    }

    public Track getTrack() {
        return track;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

}
