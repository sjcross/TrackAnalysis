package wbif.sjx.TrackAnalysis.Plot3D.Core;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.GenerateMesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Scene {
    private static Entity[] axes;
    private static final Color AXIS_COLOUR = Color.white;
    private boolean showAxes;

    private TrackCollection tracks;
    private Mesh pointMesh;
    private HashMap<Integer, Color> trackColourMap;
    private int frame;

    public Scene(TrackCollection tracks, Mesh pointMesh){
        this.tracks = tracks;
        this.pointMesh = pointMesh;
        this.frame = 0;

        trackColourMap = new HashMap<>();
        for(int ID: tracks.keySet()){
            trackColourMap.put(ID, RNG.Colour());
        }
    }

    public void render(ShaderProgram shaderProgram){
        renderAxes(shaderProgram);

        for(int ID: tracks.keySet()) {
            Track currentTrack = tracks.get(ID);
            if(frame < currentTrack.size()) {
                shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", Matrix4f.translation(DataTypeUtils.toVector3f(currentTrack.get(frame))));
                shaderProgram.setColourUniform("colour", trackColourMap.get(ID));
                pointMesh.render();
            }
        }
    }

    public static void initAxes(){
        Mesh axesMesh = GenerateMesh.cylinder(0.2f, 30, 10);
        Entity xAxis = new Entity(axesMesh, AXIS_COLOUR);
        Entity yAxis = new Entity(axesMesh, AXIS_COLOUR);
        Entity zAxis = new Entity(axesMesh, AXIS_COLOUR);
        xAxis.getRotation().setZ(90f);
        zAxis.getRotation().setX(90f);
        axes = new Entity[]{
                xAxis,
                yAxis,
                zAxis
        };
    }

    private static void renderAxes(ShaderProgram shaderProgram){
        if(axes == null){
            initAxes();
        }

        for(Entity axis: axes){
            axis.render(shaderProgram);
        }
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame){
        if(frame < 0){
            this.frame = 0;
        }else if(frame > tracks.getHighestFrame()){
            this.frame = tracks.getHighestFrame();
        }else {
            this.frame = frame;
        }
    }

    public void incrementFrame(){
        setFrame(getFrame() + 1);
    }

    public void decrementFrame(){
        setFrame(getFrame() - 1);
    }

    public void dispose(){
        pointMesh.dispose();
    }
}
