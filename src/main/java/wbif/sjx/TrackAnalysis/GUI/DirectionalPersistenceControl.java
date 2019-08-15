package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.gui.Plot;
import wbif.sjx.TrackAnalysis.GUI.Control.PlotableModule;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;
import java.util.TreeMap;

/**
 * Created by sc13967 on 25/06/2017.
 */
public class DirectionalPersistenceControl extends PlotableModule
{
    public DirectionalPersistenceControl(TrackCollection tracks, ImagePlus ipl)
    {
        super(tracks, ipl);
    }

    @Override
    public void plotAll()
    {
        double[][] directionalPersistence = tracks.getAverageDirectionalPersistence();
        double[] errMin = new double[directionalPersistence[0].length];
        double[] errMax = new double[directionalPersistence[0].length];

        for (int i=0;i<errMin.length;i++) {
            errMin[i] = directionalPersistence[1][i] - directionalPersistence[2][i];
            errMax[i] = directionalPersistence[1][i] + directionalPersistence[2][i];
        }

        Plot plot = new Plot("Directional persistence (all tracks)","Interval (frames)","Directional persistence");
        plot.setColor(Color.BLACK);
        plot.addPoints(directionalPersistence[0],directionalPersistence[1],Plot.LINE);
        plot.setColor(Color.RED);
        plot.addPoints(directionalPersistence[0],errMin,Plot.LINE);
        plot.addPoints(directionalPersistence[0],errMax,Plot.LINE);
        plot.setLimitsToFit(true);
        plot.show();
    }

    @Override
    public void plotTrack(int ID)
    {
        Track track = tracks.get(ID);
        double[] f = track.getFAsDouble();
        TreeMap<Integer,Double> euclideanDistance = track.getRollingEuclideanDistance();
        double[] vals = euclideanDistance.values().stream().mapToDouble(Double::doubleValue).toArray();

        String units = track.getUnits();
        Plot plot = new Plot("Euclidean distance (track "+ID+")","Time relative to start of track (frames)",
                "Euclidean distance ("+units+")",f,vals);
        plot.show();
    }
}
