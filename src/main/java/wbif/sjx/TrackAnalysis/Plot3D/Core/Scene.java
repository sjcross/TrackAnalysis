package wbif.sjx.TrackAnalysis.Plot3D.Core;

import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.CollectionBounds;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.TrackEntity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.TrackEntityCollection;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.MeshFactory;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Texture.Texture;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Texture.Texture2D;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataUtils;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Scene {

    public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    private static final float BOUNDING_FRAME_THICKNESS = 1.5f;

    private final TrackEntityCollection tracksEntities;
    private final CollectionBounds bounds;
    private final Entity[] axes;
    private final Entity boundingFrame;
    private final Entity planeXY;
    private final Entity planeXZ;
    private final Entity planeYZ;

    private final Vector3f centreOfTracks;
    private DisplayColour displayColour;
    private RenderQuality renderQuality;
    private final int highestFrame;
    private boolean showBoundingBox;
    private boolean motilityPlot;
    private boolean playFrames;
    private boolean showTrail;
    private boolean showAxes;
    private float cumulativeTime;
    private float secsPerFrame;
    private int framePlaybackRate;
    private int trailLength;
    private int frame;

    private final Texture debugTexture;
//    private final Texture iplXZTexture;

    public Scene(TrackCollection tracks, ImagePlus ipl) throws Exception {
        tracksEntities = new TrackEntityCollection(tracks);
        System.out.println("tracks done");
        bounds = new CollectionBounds(tracks.getSpatialLimits(true));

        centreOfTracks = DataUtils.toVector3f(tracks.getMeanPoint(0));
        displayColour = DisplayColour.ID;
        renderQuality = RenderQuality.LOW;

        showBoundingBox = true;
        playFrames = false;
        showAxes = false;
        showTrail = true;

        highestFrame = tracks.getHighestFrame();
        trailLength = highestFrame;
        cumulativeTime = 0;
        frame = 0;


        final float width = bounds.getWidth();
        final float height = bounds.getHeight();
        final float length = bounds.getLength();
        final Vector3f minPosition = bounds.getMinPosition();
        final Vector3f centrePosition = bounds.getCentrePosition();

        boundingFrame = new Entity(MeshFactory.cuboidFrame(width, height, length, BOUNDING_FRAME_THICKNESS), Color.white);
        boundingFrame.getPosition().set(centrePosition);

//        iplXZTexture = new Texture2DArray(ipl, (int)minPosition.getX(), (int)minPosition.getZ(), (int)width, (int)length);
        debugTexture = Texture2D.loadFromImage("/Textures/debugTexture.png");

        // note that both planes and axes are named according the the z-up orientation

        planeXZ = new Entity(MeshFactory.plane(width, length), debugTexture);
        planeXZ.getPosition().setX(centrePosition.getX());
        planeXZ.getPosition().setY(minPosition.getY() - BOUNDING_FRAME_THICKNESS / 2);
        planeXZ.getPosition().setZ(centrePosition.getZ());

        planeXY = new Entity(MeshFactory.plane(width, height), debugTexture);
        planeXY.getPosition().setX(centrePosition.getX());
        planeXY.getPosition().setY(centrePosition.getY());
        planeXY.getPosition().setZ(minPosition.getZ() - BOUNDING_FRAME_THICKNESS / 2);
        planeXY.getRotation().set(-90, X_AXIS);

        planeYZ = new Entity(MeshFactory.plane(length, height), debugTexture);
        planeYZ.getPosition().setX(minPosition.getX() - BOUNDING_FRAME_THICKNESS / 2);
        planeYZ.getPosition().setY(centrePosition.getY());
        planeYZ.getPosition().setZ(centrePosition.getZ());
        planeYZ.getRotation().set(-90, Y_AXIS);
        planeYZ.getRotation().multiply(90, Z_AXIS);


        Mesh axesMesh = MeshFactory.pipe(1f, 20, 10000);

        Entity origin = new Entity(MeshFactory.cube(2), Color.WHITE);
        Entity xAxis = new Entity(axesMesh, Color.RED);
        Entity yAxis = new Entity(axesMesh, Color.GREEN);
        Entity zAxis = new Entity(axesMesh, Color.BLUE);

        yAxis.getRotation().multiply(-90, X_AXIS);
        xAxis.getRotation().multiply(90, Z_AXIS);

        axes = new Entity[]{origin, xAxis, yAxis, zAxis};

        System.out.println("scene done");
    }

    public void update(float interval) {
        if (playFrames) {
            cumulativeTime += interval;

            if (cumulativeTime >= secsPerFrame) {
                cumulativeTime -= secsPerFrame;

                if (frame >= highestFrame) {
                    frame = 0;
                } else {
                    incrementFrame();
                }
            }
        }
    }

    public TrackEntityCollection getTracksEntities() {
        return tracksEntities;
    }

    public CollectionBounds getBounds() {
        return bounds;
    }

    public Entity getBoundingFrame() {
        return boundingFrame;
    }

    public Entity getPlaneXY() {
        return planeXY;
    }

    public Entity getPlaneXZ() {
        return planeXZ;
    }

    public Entity getPlaneYZ() {
        return planeYZ;
    }

    public void dispose()
    {
        tracksEntities.dispose();
        debugTexture.dispose();
//        iplXZTexture.dispose();
    }

    public Entity[] getAxes() {
        return axes;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        if (frame < 0) {
            this.frame = 0;
        } else if (frame > highestFrame) {
            this.frame = highestFrame;
        } else {
            this.frame = frame;
        }
    }

    public void changeFrame(int deltaFrame) {
        setFrame(getFrame() + deltaFrame);
    }

    public void incrementFrame() {
        setFrame(getFrame() + 1);
    }

    public void decrementFrame() {
        setFrame(getFrame() - 1);
    }

    public boolean isPlayingFrames() {
        return playFrames;
    }

    public void setPlayFrames(boolean state) {
        playFrames = state;
    }

    public void togglePlayFrames() {
        playFrames = !playFrames;
    }

    public boolean isAxesVisible() {
        return showAxes;
    }

    public void setAxesVisibility(boolean state) {
        showAxes = state;
    }

    public void toggleAxesVisibility() {
        showAxes = !showAxes;
    }

    public boolean isBoundingBoxVisible() {
        return showBoundingBox;
    }

    public void setBoundingBoxVisibility(boolean state) {
        showBoundingBox = state;
    }

    public void toggleBoundingBoxVisibility() {
        showBoundingBox = !showBoundingBox;
    }

    public int getFramePlaybackRate() {
        return framePlaybackRate;
    }

    public void setFramePlaybackRate(int value) {
        if (value < 0) {
            framePlaybackRate = 0;
        } else {
            framePlaybackRate = value;
        }

        secsPerFrame = 1f / (float) (framePlaybackRate);
    }

    public Vector3f getPlotFocus() {
        if (motilityPlot) {
            return new Vector3f();
        } else {
            return centreOfTracks;
        }
    }

    public int getHighestFrame() {
        return highestFrame;
    }

    public boolean isTrailVisibile() {
        return showTrail;
    }

    public void setTrailVisibility(boolean state) {
        showTrail = state;
    }

    public void toggleTrailVisibility() {
        showTrail = !showTrail;
    }

    public boolean ifMotilityPlot() {
        return motilityPlot;
    }

    public void setMotilityPlot(boolean state) {
        motilityPlot = state;
    }

    public void toggleMotilityPlot() {
        motilityPlot = !motilityPlot;
    }

    public int getTrailLength() {
        return trailLength;
    }

    public void setTrailLength(int value) {
        if (value < 1) {
            trailLength = 1;
        } else if (value > highestFrame) {
            trailLength = highestFrame;
        } else {
            trailLength = value;
        }
    }

    public DisplayColour getDisplayColour() {
        return displayColour;
    }

    public void setDisplayColour(DisplayColour displayColour) {
        this.displayColour = displayColour;
    }

    public RenderQuality getRenderQuality() {
        return renderQuality;
    }

    public void setRenderQuality(RenderQuality renderQuality) {
        this.renderQuality = renderQuality;
    }

    public enum DisplayColour {
        ID,
        TOTAL_PATH_LENGTH,
        VELOCITY
    }

    public enum RenderQuality {
        LOWEST,
        LOW,
        MEDIUM,
        HIGH
    }
}
