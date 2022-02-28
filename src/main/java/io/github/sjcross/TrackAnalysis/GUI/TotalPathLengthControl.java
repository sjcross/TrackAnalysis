package io.github.sjcross.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import io.github.sjcross.TrackAnalysis.GUI.Control.PlotableModule;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class TotalPathLengthControl extends PlotableModule
{
    private static final String RELATIVE_TO_FIRST_FRAME = "Relative to first frame";
    private static final String RELATIVE_TO_TRACK_START = "Relative to track start";

    private JComboBox<String> comboBox;

    public TotalPathLengthControl(TrackCollection tracks, ImagePlus ipl) {
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

        double[][] totalPathLength = tracks.getAverageTotalPathLength(relativeToTrackStart);
        double[] errMin = new double[totalPathLength[0].length];
        double[] errMax = new double[totalPathLength[0].length];

        for (int i=0;i<errMin.length;i++) {
            errMin[i] = totalPathLength[1][i] - totalPathLength[2][i];
            errMax[i] = totalPathLength[1][i] + totalPathLength[2][i];
        }

        String units = tracks.values().iterator().next().getUnits();
        Plot plot = new Plot("Total path length (all tracks)",xLabel,"Total path length ("+units+")");
        plot.setColor(Color.BLACK);
        plot.addPoints(totalPathLength[0],totalPathLength[1],Plot.LINE);
        plot.setColor(Color.RED);
        plot.addPoints(totalPathLength[0],errMin,Plot.LINE);
        plot.addPoints(totalPathLength[0],errMax,Plot.LINE);
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
        TreeMap<Integer,Double> totalPathLength = track.getRollingTotalPathLength();
        double[] vals = totalPathLength.values().stream().mapToDouble(Double::doubleValue).toArray();

        String units = track.getUnits();
        Plot plot = new Plot("Total path length (track "+ID+")",xLabel,"Total path length ("+units+")",f,vals);
        plot.show();
    }
}
