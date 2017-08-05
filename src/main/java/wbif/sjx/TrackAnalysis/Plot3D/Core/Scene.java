package wbif.sjx.TrackAnalysis.Plot3D.Core;

import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.BoundingBox;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.*;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.TrackEntityCollection;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Texture;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;


/**
 * Created by sc13967 on 31/07/2017.
 */
public class Scene {
    private static Entity[] axes;
    private BoundingBox boundingBox;
    private TrackEntityCollection tracksEntities;
    private int frame;
    public static final int frame_DEFAULT = 0;
    public final static boolean playFrames_DEFAULT = false;
    private boolean playFrames;

    private Entity texturedPlane;

    public Scene(TrackCollection tracks, ImagePlus ipl){
        tracksEntities = new TrackEntityCollection(tracks);
        boundingBox = new BoundingBox(tracks);

        initAxes();

        texturedPlane = new Entity(GenerateMesh.texturedPlane(ipl.getHeight(),ipl.getWidth())); //not sure if these parameters are the right way around
        texturedPlane.getPosition().set(ipl.getHeight()/2,ipl.getWidth()/2,-boundingBox.getHeight()/2); // swap height with width of the other vertical images

        try {
            ipl.setPosition(1);
            texturedPlane.setTexture(new Texture(ipl.getBufferedImage()));
        }catch (Exception e){
            e.printStackTrace();
        }

        this.frame = frame_DEFAULT;
        this.playFrames = playFrames_DEFAULT;

        showAxes = showAxes_DEFAULT;
        showBoundingBox = showBoundingBox_DEFAULT;
    }

    public void update(){
        if(playFrames){
            if(frame == tracksEntities.getHighestFrame()){
                frame = 0;
            }else {
                incrementFrame();
            }
        }
    }

    public void render(ShaderProgram shaderProgram, FrustumCuller frustumCuller) {
        if(showAxes) {
            for (Entity axis : axes) {
                axis.render(shaderProgram, null);
            }
        }

        texturedPlane.render(shaderProgram, frustumCuller);

        tracksEntities.render(shaderProgram, frustumCuller, frame);

        if(showBoundingBox) {
            boundingBox.render(shaderProgram, frustumCuller);
        }
    }

    public void dispose(){
        tracksEntities = null;
    }

    public static void initAxes(){
        Mesh axesMesh = GenerateMesh.cylinder(1f, 20, 100000);
        Entity xAxis = new Entity(axesMesh, Color.RED);
        Entity yAxis = new Entity(axesMesh, Color.GREEN);
        Entity zAxis = new Entity(axesMesh, Color.BLUE);
        xAxis.getRotation().setY(90f);
        yAxis.getRotation().setX(90f);
        axes = new Entity[]{
                xAxis,
                yAxis,
                zAxis
        };
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame){
        if(frame < 0){
            this.frame = 0;
        }else if(frame > tracksEntities.getHighestFrame()){
            this.frame = tracksEntities.getHighestFrame();
        }else {
            this.frame = frame;
        }
    }

    public void changeFrame(int deltaFrame){
        setFrame(getFrame() + deltaFrame);
    }

    public void incrementFrame(){
        setFrame(getFrame() + 1);
    }

    public void decrementFrame(){
        setFrame(getFrame() - 1);
    }

    public boolean isPlayingFrames(){
        return playFrames;
    }

    public void setPlayFrames(boolean state){
        playFrames = state;
    }

    public void togglePlayFrames(){
        playFrames = !playFrames;
    }

    public TrackEntityCollection getTracksEntities() {
        return tracksEntities;
    }

    public BoundingBox getBoundingBox(){
        return boundingBox;
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
