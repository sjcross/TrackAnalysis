package io.github.sjcross.trackanalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;
import io.github.sjcross.trackanalysis.GUI.Control.PlotableModule;

import javax.swing.*;

import static io.github.sjcross.trackanalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.trackanalysis.GUI.Control.MainGUI.frameWidth;

import java.awt.*;
import java.util.TreeMap;

/**
 * Created by sc13967 on 25/06/2017.
 */
public class DirectionalityRatioControl extends PlotableModule
{
    private static final String RELATIVE_TO_FIRST_FRAME = "Relative to first frame";
    private static final String RELATIVE_TO_TRACK_START = "Relative to track start";

    private JComboBox<String> comboBox;

    public DirectionalityRatioControl(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,5,0,5);

        String relativePosition = Prefs.get("TrackAnalysis.relativePosition",RELATIVE_TO_TRACK_START);
        comboBox = new JComboBox<>(new String[]{RELATIVE_TO_TRACK_START,RELATIVE_TO_FIRST_FRAME});
        comboBox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        comboBox.setSelectedItem(relativePosition);
        c.insets = new Insets(0,5,20,5);
        panel.add(comboBox,c);
    }

    @Override
    public void plotAll()
    {
        String relativePosition = (String) comboBox.getSelectedItem();
        Prefs.set("TrackAnalysis.relativePosition",relativePosition);
        boolean relativeToTrackStart = relativePosition.equals(RELATIVE_TO_TRACK_START);
        String xLabel = relativeToTrackStart ? "Time relative to start of track (frames)" : "Time relative to first frame (frames)";

        double[][] directionalityRatio = tracks.getAverageDirectionalityRatio(relativeToTrackStart);
        double[] errMin = new double[directionalityRatio[0].length];
        double[] errMax = new double[directionalityRatio[0].length];

        for (int i=0;i<errMin.length;i++) {
            errMin[i] = directionalityRatio[1][i] - directionalityRatio[2][i];
            errMax[i] = directionalityRatio[1][i] + directionalityRatio[2][i];
        }

        Plot plot = new Plot("Directionality ratio (all tracks)",xLabel,"Directionality ratio");
        plot.setColor(Color.BLACK);
        plot.addPoints(directionalityRatio[0],directionalityRatio[1],Plot.LINE);
        plot.setColor(Color.RED);
        plot.addPoints(directionalityRatio[0],errMin,Plot.LINE);
        plot.addPoints(directionalityRatio[0],errMax,Plot.LINE);
        plot.setLimitsToFit(true);
        plot.show();
    }

    @Override
    public void plotTrack(int ID)
    {
        String relativePosition = (String) comboBox.getSelectedItem();
        Prefs.set("TrackAnalysis.relativePosition",relativePosition);
        boolean relativeToTrackStart = relativePosition.equals(RELATIVE_TO_TRACK_START);
        String xLabel = relativeToTrackStart ? "Time relative to start of track (frames)" : "Time relative to first frame (frames)";

        Track track = tracks.get(ID);
        double[] f = track.getFAsDouble();
        TreeMap<Integer,Double> directionalityRatio = track.getRollingDirectionalityRatio();

        double[] vals = directionalityRatio.values().stream().mapToDouble(Double::doubleValue).toArray();
        Plot plot = new Plot("Directionality ratio (track "+ID+")",xLabel,"Directionality ratio",f,vals);
        plot.show();
    }
}
