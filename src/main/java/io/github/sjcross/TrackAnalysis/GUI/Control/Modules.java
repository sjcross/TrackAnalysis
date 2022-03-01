package io.github.sjcross.trackanalysis.GUI.Control;

import ij.ImagePlus;
import io.github.sjcross.common.object.tracks.TrackCollection;
import io.github.sjcross.trackanalysis.GUI.*;

import java.util.Arrays;

public enum Modules
{
    TRACK_SUMMARY(TrackSummary::new, false, "Track summary"),
//    DIRECTIONAL_PERSISTENCE(DirectionalPersistenceControl2::new, false, "Directional persistence plot"),
    DIRECTIONALITY_RATIO(DirectionalityRatioControl::new, false, "Directionality ratio plot"),
    EUCLIDEAN_DISTANCE(EuclideanDistanceControl::new, false, "Euclidean distance plot"),
    MEAN_SQUARED_DISPLACEMENT(MSDControl::new, false, "Mean squared displacement plot"),
    MOTILITY_PLOT(MotilityPlotControl::new, false, "Motility plot"),
//    NEAREST_NEIGHBOUR_DISTANCE(NearestNeighbourCalculator2::new, false, "Nearest neighbour distance plot"),
    SHOW_TRACK_ID(ShowTrackIDControl::new, true, "Show track paths with IDs"),
    TOTAL_PATH_LENGTH(TotalPathLengthControl::new, false, "Total path length plot"),
    TRACK_DURATION(TrackDurationControl::new, false, "Track duration plot"),
    TRACK_INTENSITY(TrackIntensityControl::new, true, "Track intensity plot"),
    TRACK_PLOT(TrackPlotControl::new, false, "Track Plot");

    private final ModuleFactory factory;
    private final String name;
    private final boolean requiresIpl;

    Modules(ModuleFactory factory, boolean requiresIpl, String name)
    {
        this.factory = factory;
        this.requiresIpl = requiresIpl;
        this.name = name;
    }

    public static Modules[] getAvailableModules(boolean iplProvided)
    {
        if(iplProvided) return values();

        return Arrays.stream(values()).filter(m -> !m.requiresIpl).toArray(Modules[]::new);
    }

    public BasicModule create(TrackCollection tracks, ImagePlus ipl)
    {
        return factory.create(tracks, ipl);
    }

    @Override
    public String toString()
    {
        return name;
    }

    private interface ModuleFactory
    {
        BasicModule create(TrackCollection trackCollection, ImagePlus ipl);
    }
}