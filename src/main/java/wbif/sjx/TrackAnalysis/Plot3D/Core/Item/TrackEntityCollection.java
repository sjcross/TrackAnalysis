package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataUtils;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import java.util.HashMap;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class TrackEntityCollection extends HashMap<Integer, TrackEntity> {

    public static final int minTrailLength = 3;

    private final Vector3f centreOfTracks;
    private final int highestFrame;
    public final int maxTrailLength;

    private DisplayColour displayColour;
    private RenderQuality renderQuality;
    private boolean bindColourBuffers = true;
    private boolean bindMeshBuffers = true;
    private boolean showTrail;
    private boolean motilityPlot;
    private int trailLength;

    public TrackEntityCollection(TrackCollection tracks) {
        final float maximumInstantaneousVelocity = (float) tracks.getMaximumInstantaneousSpeed();
        final float maximumTotalPathLength = (float) tracks.getMaximumTotalPathLength();

        for (int trackID : tracks.keySet()) {
            put(trackID, new TrackEntity(tracks.get(trackID), maximumInstantaneousVelocity, maximumTotalPathLength));
        }

        this.centreOfTracks = DataUtils.toVector3f(tracks.getMeanPoint(0));
        this.highestFrame = tracks.getHighestFrame();
        this.maxTrailLength = highestFrame < 1 ? 1 : highestFrame;
        this.trailLength = maxTrailLength;
        this.displayColour = DisplayColour.ID;
        this.renderQuality = RenderQuality.LOW;
        this.showTrail = true;
        this.motilityPlot = false;
    }

    public void render(ShaderProgram shaderProgram, int frame) {
        boolean useInstancedColour = displayColour != DisplayColour.ID;

        if (bindMeshBuffers) {
            bindMeshBuffers = false;
            for (TrackEntity trackEntity : values()) {
                trackEntity.updateMeshBuffer(renderQuality);
            }
        }
        if (bindColourBuffers) {
            bindColourBuffers = false;
            for (TrackEntity trackEntity : values()) {
                trackEntity.updateColourBuffer(displayColour);
            }
        }

        shaderProgram.setBooleanUniform("motilityPlot", motilityPlot);
        shaderProgram.setBooleanUniform("useInstancedColour", useInstancedColour);

        for (TrackEntity trackEntity : values()) {
            if (motilityPlot) {
                shaderProgram.setMatrix4fUniform("motilityPlotMatrix", trackEntity.getMotilityPlotMatrix());
            }

            if (!useInstancedColour) {
                shaderProgram.setColourUniformRGB("colour", trackEntity.getColour());
            }

            trackEntity.renderParticle(frame);

            if (showTrail) {
                trackEntity.renderTrail(frame, trailLength);
            }
        }
    }

    public void dispose() {
        for (TrackEntity trackEntity : values()) {
            trackEntity.dispose();
        }
    }

    public Vector3f getFocusOfPlot() {
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
        if (value < minTrailLength) {
            trailLength = minTrailLength;
        } else if (value > maxTrailLength) {
            trailLength = maxTrailLength;
        } else {
            trailLength = value;
        }
    }

    public void changeTrailLength(int deltaValue) {
        setTrailLength(getTrailLength() + deltaValue);
    }

    public DisplayColour getDisplayColour() {
        return displayColour;
    }

    public void setDisplayColour(DisplayColour displayColour) {
        this.displayColour = displayColour;
        this.bindColourBuffers = displayColour != DisplayColour.ID;
    }

    public RenderQuality getRenderQuality() {
        return renderQuality;
    }

    public void setDisplayQuality(RenderQuality renderQuality) {
        this.renderQuality = renderQuality;
        this.bindMeshBuffers = true;
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
