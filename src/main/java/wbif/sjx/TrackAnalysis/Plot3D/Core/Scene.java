package wbif.sjx.TrackAnalysis.Plot3D.Core;

import ij.Prefs;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.*;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntityCollection;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;


/**
 * Created by sc13967 on 31/07/2017.
 */
public class Scene {
    private static Entity[] axes;
    private static final Color AXIS_COLOUR = Color.white;
    private TrackCollection tracks;
    private TrackEntityCollection tracksEntities;
    private int frame;

    public Scene(TrackCollection tracks, Mesh pointMesh){
        this.tracks = tracks;
        this.frame = 0;

        TrackEntity.setPointMesh(pointMesh);
        tracksEntities = new TrackEntityCollection(tracks);
    }

    public void render(ShaderProgram shaderProgram) {
        if(showAxes) {
            renderAxes(shaderProgram);
        }
        tracksEntities.render(shaderProgram, frame);
    }

    public static void initAxes(){
        Mesh axesMesh = GenerateMesh.cylinder(0.2f, 30, 100000);
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

    public void dispose() {
        Prefs.set("TrackAnalysis.TrackPlot.showAxes", showAxes);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean showAxes = Prefs.get("TrackAnalysis.TrackPlot.showAxes",false);

    public boolean isAxesVisible(){
        return showAxes;
    }

    public void setAxesVisibility(boolean state){
        showAxes = state;
    }

    public void togglwAxesVisibility(){
        showAxes = !showAxes;
    }

}
