package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Scene;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Process.DoubleTreeMapConverter;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackEntity extends HashMap<Integer, PointEntity>{
    private final TrackEntityCollection trackEntityCollection;
    private final Track track;
    private Color colour;
    private final Vector3f motilityPlotVector;
    private TreeMap<Integer,Double> instantaneousVelocity;
    private double maxInstantaneousVelocity = -1;
    private TreeMap<Integer,Double> totalPathLength;
    private double maxTotalPathLength = -1;

    public TrackEntity(TrackEntityCollection trackEntityCollection, Track track){
        this.trackEntityCollection = trackEntityCollection;
        this.track = track;
        this.colour = RNG.Colour();
        motilityPlotVector = Vector3f.Negative(DataTypeUtils.toVector3f(track.get(track.getF()[0])));
        int[] f = track.getF();
        instantaneousVelocity = track.getInstantaneousSpeed(true);
        totalPathLength = track.getRollingTotalPathLength(true);

        for(int frame: track.keySet()){
            put(frame, new PointEntity(this, track.get(frame)));
        }

        for(int i = 0; i < size() - 1; i++){
            if(containsKey(i) && containsKey(i + 1)) {
                get(i).createPipe(get(i + 1).getGlobalPosition());
            }
        }
    }

    public void render(ShaderProgram shaderProgram, int frame){
        if(trackEntityCollection.isTrailVisibile()){
            for (int f = frame - trackEntityCollection.getTrailLength(); f < frame; f++) {
                if(track.hasFrame(f) && get(f).hasPipe()){
                    get(f).renderPipe(shaderProgram);
                }
            }
        }

        //Renders a larger point at the current frame
        if(track.hasFrame(frame)) {
            get(frame).renderParticle(shaderProgram);
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

    public TrackEntityCollection getTrackEntityCollection() {
        return trackEntityCollection;
    }

    public Vector3f getMotilityPlotVector() {
        return motilityPlotVector;
    }

    public double getInstantaneousVelocity(int frame) {
        if(instantaneousVelocity.containsKey(frame)) {
            return instantaneousVelocity.get(frame);
        }else {
            return 0;
        }
    }

    public double getTotalPathLength(int frame) {
        if(totalPathLength.containsKey(frame)) {
            return totalPathLength.get(frame);
        }else {
            return 0;
        }
    }

    public double getMaximumInstantaneousVelocity() {
        // Only calculate it the first time it's requested
        if (maxInstantaneousVelocity == -1) {
            for (double velocity : instantaneousVelocity.values()) {
                maxInstantaneousVelocity = Math.max(maxInstantaneousVelocity, velocity);
            }
        }

        return maxInstantaneousVelocity;

    }

    public double getMaximumTotalPathLength() {
        // Only calculate it the first time it's requested
        if (maxTotalPathLength == -1) {
            for (double pathLength : totalPathLength.values()) {
                maxTotalPathLength = Math.max(maxTotalPathLength, pathLength);
            }
        }

        return maxTotalPathLength;

    }
}
