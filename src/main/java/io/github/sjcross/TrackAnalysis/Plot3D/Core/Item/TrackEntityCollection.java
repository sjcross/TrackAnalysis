package io.github.sjcross.TrackAnalysis.Plot3D.Core.Item;

import io.github.sjcross.TrackAnalysis.GUI.TrackPlotControl.*;
import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.MeshFactory;
import io.github.sjcross.TrackAnalysis.Plot3D.Utils.UniCallback;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class TrackEntityCollection {

    private static final float TRAIL_RADIUS = 0.5f;
    private static final float PARTICLE_RADIUS = 2.5f;

    private static final int PARTICLE_RESOLUTION = 20;
    private static final int LOWEST_RESOLUTION = 3;
    private static final int LOW_RESOLUTION = 6;
    private static final int MEDIUM_RESOLUTION = 10;
    private static final int HIGH_RESOLUTION = 20;

    Mesh particleMesh;
    Mesh pipeMeshLowest;
    Mesh hingePointMeshLowest;
    Mesh pipeMeshLow;
    Mesh hingePointMeshLow;
    Mesh pipeMeshMedium;
    Mesh hingePointMeshMedium;
    Mesh pipeMeshHigh;
    Mesh hingePointMeshHigh;

    private final TrackEntity[] trackEntities;

    public TrackEntityCollection(TrackCollection tracks) {
        particleMesh = MeshFactory.sphere(PARTICLE_RADIUS, PARTICLE_RESOLUTION);
        pipeMeshLowest = MeshFactory.pipe(TRAIL_RADIUS, LOWEST_RESOLUTION, 1);
        hingePointMeshLowest = MeshFactory.sphere(TRAIL_RADIUS, LOWEST_RESOLUTION);
        pipeMeshLow = MeshFactory.pipe(TRAIL_RADIUS, LOW_RESOLUTION, 1);
        hingePointMeshLow = MeshFactory.sphere(TRAIL_RADIUS, LOW_RESOLUTION);
        pipeMeshMedium = MeshFactory.pipe(TRAIL_RADIUS, MEDIUM_RESOLUTION, 1);
        hingePointMeshMedium = MeshFactory.sphere(TRAIL_RADIUS, MEDIUM_RESOLUTION);
        pipeMeshHigh = MeshFactory.pipe(TRAIL_RADIUS, HIGH_RESOLUTION, 1);
        hingePointMeshHigh = MeshFactory.sphere(TRAIL_RADIUS, HIGH_RESOLUTION);

        trackEntities = new TrackEntity[tracks.size()];

        final float maxInstSpeed = (float) tracks.getMaximumInstantaneousSpeed();
        final float maxPathLength = (float) tracks.getMaximumTotalPathLength();
        int i = 0;

        for (Track track : tracks.values()) {
            trackEntities[i++] = new TrackEntity(track, maxInstSpeed, maxPathLength, this);
        }
    }

    public void forEachTrackEntity(UniCallback<TrackEntity> x) {
        for(TrackEntity trackEntity : trackEntities) {
            x.invoke(trackEntity);
        }
    }

    public void updateColourBuffers(DisplayColour colour) {
        forEachTrackEntity(e -> e.updateColourBuffer(colour));
    }

    public void updateMeshBuffers(RenderQuality quality) {
        forEachTrackEntity(e -> e.updateMeshBuffer(quality, this));
    }

    public void dispose() {
        forEachTrackEntity(TrackEntity::dispose);

        particleMesh.dispose();
        pipeMeshLowest.dispose();
        hingePointMeshLowest.dispose();
        pipeMeshLow.dispose();
        hingePointMeshLow.dispose();
        pipeMeshMedium.dispose();
        hingePointMeshMedium.dispose();
        pipeMeshHigh.dispose();
        hingePointMeshHigh.dispose();
    }
}
