package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.common.Object.TrackCollection;

import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackEntityCollection extends LinkedHashMap<Integer, TrackEntity>{
    private TrackCollection tracks;
    private final int highestFrame;
    private final Vector3f centreOfCollection;
    private double[][] spacialLimits;

    public TrackEntityCollection(TrackCollection tracks){
        this.tracks = tracks;
        for(int trackID: tracks.keySet()){
            put(trackID, new TrackEntity(this, tracks.get(trackID)));
        }

        centreOfCollection = DataTypeUtils.toVector3f(tracks.getMeanPoint(0));
        spacialLimits = tracks.getSpatialLimits(true);

        this.highestFrame = tracks.getHighestFrame();
        this.displayColour = displayColour_DEFAULT;
        this.displayQuality = displayQuality_DEFAULT;
        this.showTrail = showTrail_DEFAULT;
        this.motilityPlot = motilityPlot_DEFAULT;
        this.trailLength_MAXIMUM = highestFrame < 1 ? 1 : highestFrame;
        this.trailLength = trailLength_MAXIMUM;
    }

    public void render(ShaderProgram shaderProgram, FrustumCuller frustumCuller, int frame){
        for(TrackEntity trackEntity: values()){
            trackEntity.render(shaderProgram, frustumCuller, frame);
        }
    }

    public TrackCollection getTracks() {
        return tracks;
    }

    public Vector3f getGlobalCentreOfCollection() {
        return centreOfCollection;
    }

    public Vector3f getCurrentCentreOfCollection(){
        if(motilityPlot){
            return new Vector3f();
        }else {
            return getGlobalCentreOfCollection();
        }
    }

    public int getHighestFrame() {
        return highestFrame;
    }

    public double[][] getSpacialLimits() {
        return spacialLimits;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final boolean showTrail_DEFAULT = true;

    private boolean showTrail;

    public boolean isTrailVisibile(){
        return showTrail;
    }

    public void setTrailVisibility(boolean state){
        showTrail = state;
    }

    public void toggleTrailVisibility(){
        showTrail = !showTrail;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final boolean motilityPlot_DEFAULT = false;

    private boolean motilityPlot;

    public boolean ifMotilityPlot(){
        return motilityPlot;
    }

    public void setMotilityPlot(boolean state){
        motilityPlot = state;
    }

    public void toggleMotilityPlot(){
        motilityPlot = !motilityPlot;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int trailLength_MINIMUM = 1;
    public final int trailLength_MAXIMUM;

    private int trailLength;

    public int getTrailLength(){
        return trailLength;
    }

    public void setTrailLength(int value) {
        if (value < trailLength_MINIMUM) {
            trailLength = trailLength_MINIMUM;
        } else if (value > trailLength_MAXIMUM) {
            trailLength = trailLength_MAXIMUM;
        } else {
            trailLength = value;
        }
    }

    public void changeTrailLength(int deltaValue){
        setTrailLength(getTrailLength() + deltaValue);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public displayColourOptions displayColour;

    public static final displayColourOptions displayColour_DEFAULT = displayColourOptions.VELOCITY;

    public enum displayColourOptions{
        SOLID,
        VELOCITY
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public displayQualityOptions displayQuality;

    public static final TrackEntityCollection.displayQualityOptions displayQuality_DEFAULT = displayQualityOptions.MEDIUM;

    public enum displayQualityOptions{
        LOWEST,
        LOW,
        MEDIUM,
        HIGH
    }

}
