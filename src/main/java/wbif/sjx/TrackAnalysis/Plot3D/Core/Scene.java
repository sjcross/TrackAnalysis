package wbif.sjx.TrackAnalysis.Plot3D.Core;

import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.BoundingBox;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.*;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.TrackEntityCollection;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Scene {
    public static final Vector3f X_AXIS = new Vector3f(1,0,0);
    public static final Vector3f Y_AXIS = new Vector3f(0,1,0);
    public static final Vector3f Z_AXIS = new Vector3f(0,0,1);
    public static final Vector3f WORLD_FORWARD_VECTOR = Z_AXIS;

    private static Entity[] axes;
    private BoundingBox boundingBox;
    private TrackEntityCollection tracksEntities;
    private boolean playFrames;
    private float cumulativetime = 0;

    public Scene(TrackCollection tracks, ImagePlus ipl){
        tracksEntities = new TrackEntityCollection(tracks);
        boundingBox = new BoundingBox(tracks);

        initAxes();

        this.frame = frame_DEFAULT;
        this.playFrames = playFrames_DEFAULT;

        showAxes = showAxes_DEFAULT;
        showBoundingBox = showBoundingBox_DEFAULT;
        framePlaybackRate = framePlaybackRate_DEFAULT;
        updateSecondsPerFramePlayback();
    }

    public void update(float interval){
        if(playFrames){
            cumulativetime += interval;    //TODO: allow varying playback speed

            if(cumulativetime >= secondsPerFramePlayback){
                cumulativetime -= secondsPerFramePlayback;

                if (frame == tracksEntities.getHighestFrame()) {
                    frame = 0;
                } else {
                    incrementFrame();
                }
            }
        }
    }

    public void render(ShaderProgram shaderProgram) {
        if(showAxes) {
            for (Entity axis : axes) {
                axis.render(shaderProgram);
            }
        }

        tracksEntities.render(shaderProgram, frame);

        if(showBoundingBox && !tracksEntities.ifMotilityPlot()) {
            boundingBox.render(shaderProgram);
        }
    }

    public TrackEntityCollection getTracksEntities() {
        return tracksEntities;
    }

    public BoundingBox getBoundingBox(){
        return boundingBox;
    }

    public void dispose(){
        tracksEntities = null;
    }

    public static void initAxes(){ //Axis are presented in Z-up form
        Mesh axesMesh = GenerateMesh.pipe(1f, 20, 10000);

        Entity origin = new Entity(GenerateMesh.cube(2), Color.WHITE);

        Entity xAxis = new Entity(axesMesh, Color.RED);
        xAxis.getRotation().setZ(-90);

        Entity yAxis = new Entity(axesMesh, Color.GREEN);
        yAxis.getRotation().setX(90);

        Entity zAxis = new Entity(axesMesh, Color.BLUE);
        zAxis.getRotation().setX(0);

        axes = new Entity[]{
                origin,
                xAxis,
                yAxis,
                zAxis
        };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int frame_DEFAULT = 0;
    private int frame;

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

    public final static boolean playFrames_DEFAULT = false;

    public boolean isPlayingFrames(){
        return playFrames;
    }

    public void setPlayFrames(boolean state){
        playFrames = state;
    }

    public void togglePlayFrames(){
        playFrames = !playFrames;
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

    public static final boolean showBoundingBox_DEFAULT = true;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int framePlaybackRate_DEFAULT = 5;

    private int framePlaybackRate;
    private float secondsPerFramePlayback;

    public int getFramePlaybackRate() {
        return framePlaybackRate;
    }

    public void setFramePlaybackRate(int value) {
        if (value < 0) {
            framePlaybackRate = 0;
        } else {
            framePlaybackRate = value;
        }

        updateSecondsPerFramePlayback();

    }

    private void updateSecondsPerFramePlayback(){
        secondsPerFramePlayback  = 1f / (float)(framePlaybackRate);
    }
}
