package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.common.Object.TrackCollection;

import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackEntityCollection extends LinkedHashMap<Integer, TrackEntity>{
    public TrackEntityCollection(TrackCollection tracks){
        for(int trackID: tracks.keySet()){
            put(trackID, new TrackEntity(tracks.get(trackID)));
        }

        showTrail = showTrail_DEFAULT;
        motilityPlot = motilityPlot_DEFAULT;
    }

    public void render(ShaderProgram shaderProgram, FrustumCuller frustumCuller, int frame){
        for(TrackEntity trackEntity: values()){
            trackEntity.render(shaderProgram, frustumCuller, frame, showTrail, motilityPlot);
        }
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
}
