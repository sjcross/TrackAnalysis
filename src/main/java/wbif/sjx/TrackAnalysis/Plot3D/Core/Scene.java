package wbif.sjx.TrackAnalysis.Plot3D.Core;

import ij.Prefs;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.*;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntityCollection;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;


/**
 * Created by sc13967 on 31/07/2017.
 */
public class Scene {
    private static Entity[] axes;
    private static final Color AXIS_COLOUR = Color.white;
    private static Entity boundingBox;
    private TrackCollection tracks;
    private TrackEntityCollection tracksEntities;
    private int frame;

    public Scene(TrackCollection tracks){
        this.tracks = tracks;
        this.frame = 0;

        initAxes();
        initBoundingBox(tracks);

        tracksEntities = new TrackEntityCollection(tracks);

        showAxes = showAxes_DEFAULT;
        showBoundingBox = showBoundingBox_DEFAULT;
    }

    public void render(ShaderProgram shaderProgram, FrustumCuller frustumCuller) {
        if(showAxes) {
            renderAxes(shaderProgram);
        }

        tracksEntities.render(shaderProgram, frustumCuller, frame);

        if(showBoundingBox) {
            renderBoundingBox(shaderProgram, frustumCuller);
        }
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
        for(Entity axis: axes){
            axis.render(shaderProgram);
        }
    }

    private static void renderBoundingBox(ShaderProgram shaderProgram, FrustumCuller frustumCuller){
        if(frustumCuller.isInsideFrustum(boundingBox)) {
            boundingBox.render(shaderProgram);
        }
    }

    private static void initBoundingBox(TrackCollection tracks){
        double[][] spacialLimits  = tracks.getSpatialLimits(false);
        float width = (float)(spacialLimits[0][1] - spacialLimits[0][0]);
        float height = (float)(spacialLimits[1][1] - spacialLimits[1][0]);
        float length = (float)(spacialLimits[2][1] - spacialLimits[2][0]);
        boundingBox = new Entity(GenerateMesh.cuboid(width, height, length), new Color(255,0,0, 60));
        boundingBox.getPosition().set(
                (float)spacialLimits[0][0] + width/2,
                (float)spacialLimits[1][0] + height/2,
                (float)spacialLimits[2][0] + length/2
        );
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
    }

    public TrackEntityCollection getTracksEntities() {
        return tracksEntities;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final boolean showAxes_DEFAULT = false;

    private static boolean showAxes;

    public static boolean isAxesVisible(){
        return showAxes;
    }

    public static void setAxesVisibility(boolean state){
        showAxes = state;
    }

    public static void toggleAxesVisibility(){
        showAxes = !showAxes;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final boolean showBoundingBox_DEFAULT = false;

    private static boolean showBoundingBox;

    public static boolean isBoundingBoxVisible(){
        return showBoundingBox;
    }

    public static void setBoundingBoxVisibility(boolean state){
        showBoundingBox = state;
    }

    public static void toggleBoundingBoxVisibility(){
        showBoundingBox = !showBoundingBox;
    }

}
