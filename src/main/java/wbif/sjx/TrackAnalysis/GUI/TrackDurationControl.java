package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.TreeMap;

public class TrackDurationControl extends ModuleControl {
    public TrackDurationControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Track duration plot";
    }

    @Override
    public JPanel getExtraControls() {
        return null;

    }

    @Override
    public void run(int ID) {
        int[][] numberOfObjects = tracks.getNumberOfObjects(true);
        double[] duration = Arrays.stream(numberOfObjects[0]).asDoubleStream().toArray();
        double[] number = Arrays.stream(numberOfObjects[1]).asDoubleStream().toArray();

        Plot plot = new Plot("Duration of objects relative to track start","Duration (frames)","Number of objects");
        plot.setColor(Color.BLACK);
        plot.addPoints(duration,number,Plot.LINE);
        plot.setLimitsToFit(true);
        plot.show();

    }

    @Override
    public void extraActions(ActionEvent e) {


    }
}
