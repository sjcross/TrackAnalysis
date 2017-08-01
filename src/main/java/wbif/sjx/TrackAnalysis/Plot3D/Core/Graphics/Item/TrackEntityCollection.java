package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item;

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
    }

    public void render(ShaderProgram shaderProgram, int frame){
        for(TrackEntity trackEntity: values()){
            trackEntity.render(shaderProgram, frame);
        }
    }
}
