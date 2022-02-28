package io.github.sjcross.trackanalysis.Plot3D.Core;

import ij.ImagePlus;
import io.github.sjcross.common.object.tracks.TrackCollection;
import io.github.sjcross.trackanalysis.GUI.TrackPlotControl.*;
import io.github.sjcross.trackanalysis.Plot3D.Core.Item.CollectionBounds;
import io.github.sjcross.trackanalysis.Plot3D.Core.Item.Entity;
import io.github.sjcross.trackanalysis.Plot3D.Core.Item.TrackEntityCollection;
import io.github.sjcross.trackanalysis.Plot3D.Graphics.MeshFactory;
import io.github.sjcross.trackanalysis.Plot3D.Graphics.Component.Mesh;
import io.github.sjcross.trackanalysis.Plot3D.Graphics.Texture.Texture;
import io.github.sjcross.trackanalysis.Plot3D.Graphics.Texture.Texture2D;
import io.github.sjcross.trackanalysis.Plot3D.Input.Keyboard;
import io.github.sjcross.trackanalysis.Plot3D.Input.MouseButtons;
import io.github.sjcross.trackanalysis.Plot3D.Input.MouseWheel;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector3f;
import io.github.sjcross.trackanalysis.Plot3D.Utils.DataUtils;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Scene
{
    public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    private static final float BOUNDING_FRAME_THICKNESS = 1.5f;
    public static final int DEF_FRAME_PLAYBACK_RATE = 50;

    private final TrackEntityCollection tracksEntities;
    private final CollectionBounds bounds;
    private final Camera camera;
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

    public Scene(TrackCollection tracks, ImagePlus ipl) throws Exception
    {
        tracksEntities = new TrackEntityCollection(tracks);
        bounds = new CollectionBounds(tracks.getSpatialLimits(true));
        camera = new Camera();

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
        framePlaybackRate = DEF_FRAME_PLAYBACK_RATE;
        secsPerFrame = 1f / (float) framePlaybackRate;


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
    }

    public void handleInput()
    {
        camera.handleInput();

        if(Keyboard.isKeyDown(GLFW_KEY_UP) || Keyboard.isKeyTapped(GLFW_KEY_PAGE_UP))
        {
            nextFrame();
        }
        else if(Keyboard.isKeyDown(GLFW_KEY_DOWN) || Keyboard.isKeyTapped(GLFW_KEY_PAGE_DOWN))
        {
            previousFrame();
        }

        if(MouseButtons.isButtonTapped(GLFW_MOUSE_BUTTON_MIDDLE)){
            camera.setFOV(Camera.DEF_FOV);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_LEFT_CONTROL) || Keyboard.isKeyDown(GLFW_KEY_RIGHT_CONTROL))
        {
            camera.changeFOV(-MouseWheel.getDeltaScroll());
        }
        else
        {
            changeFrame(MouseWheel.getDeltaScroll());
        }

        if(Keyboard.isKeyTapped(GLFW_KEY_F))
        {
            togglePlayFrames();
        }
    }

    public void update(float interval)
    {
        camera.update(interval, motilityPlot ? new Vector3f() : centreOfTracks);

        if (playFrames)
        {
            cumulativeTime += interval;

            if (cumulativeTime >= secsPerFrame)
            {
                cumulativeTime -= secsPerFrame;

                if (frame >= highestFrame)
                {
                    frame = 0;
                }
                else
                {
                    nextFrame();
                }
            }
        }
    }

    public TrackEntityCollection getTracksEntities()
    {
        return tracksEntities;
    }

    public CollectionBounds getBounds()
    {
        return bounds;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public Entity getBoundingFrame()
    {
        return boundingFrame;
    }

    public Entity getPlaneXY()
    {
        return planeXY;
    }

    public Entity getPlaneXZ()
    {
        return planeXZ;
    }

    public Entity getPlaneYZ()
    {
        return planeYZ;
    }

    public Entity[] getAxes()
    {
        return axes;
    }

    public void dispose()
    {
        tracksEntities.dispose();
        debugTexture.dispose();
//        iplXZTexture.dispose();
    }

    public int getFrame()
    {
        return frame;
    }

    public void setFrame(int value)
    {
        frame = Math.max(0, Math.min(value, highestFrame));
    }

    public void changeFrame(int deltaFrame)
    {
        setFrame(frame + deltaFrame);
    }

    public void nextFrame()
    {
        setFrame(frame + 1);
    }

    public void previousFrame()
    {
        setFrame(frame - 1);
    }

    public boolean isPlayingFrames()
    {
        return playFrames;
    }

    public void setPlayFrames(boolean state)
    {
        playFrames = state;
    }

    public void togglePlayFrames()
    {
        playFrames = !playFrames;
    }

    public boolean isAxesVisible()
    {
        return showAxes;
    }

    public void setAxesVisibility(boolean state)
    {
        showAxes = state;
    }

    public boolean isBoundingBoxVisible()
    {
        return showBoundingBox;
    }

    public void setBoundingBoxVisibility(boolean state)
    {
        showBoundingBox = state;
    }

    public int getFramePlaybackRate()
    {
        return framePlaybackRate;
    }

    public void setFramePlaybackRate(int value)
    {
        framePlaybackRate = Math.max(0, value);

        secsPerFrame = 1f / (float) (framePlaybackRate);
    }

    public int getHighestFrame()
    {
        return highestFrame;
    }

    public boolean isTrailVisibile()
    {
        return showTrail;
    }

    public void setTrailVisibility(boolean state)
    {
        showTrail = state;
    }

    public boolean ifMotilityPlot()
    {
        return motilityPlot;
    }

    public void setMotilityPlot(boolean state)
    {
        motilityPlot = state;
    }

    public int getTrailLength()
    {
        return trailLength;
    }

    public void setTrailLength(int value)
    {
        trailLength = Math.max(1, Math.min(value, highestFrame));
    }

    public DisplayColour getDisplayColour()
    {
        return displayColour;
    }

    public void setDisplayColour(DisplayColour displayColour)
    {
        this.displayColour = displayColour;
    }

    public RenderQuality getRenderQuality()
    {
        return renderQuality;
    }

    public void setRenderQuality(RenderQuality renderQuality)
    {
        this.renderQuality = renderQuality;
    }
}
