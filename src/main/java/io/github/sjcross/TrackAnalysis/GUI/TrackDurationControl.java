package io.github.sjcross.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.gui.Plot;
import io.github.sjcross.TrackAnalysis.GUI.Control.BasicModule;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

public class TrackDurationControl extends BasicModule
{
    public TrackDurationControl(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20,5,20,5);

        // Button to plot all tracks
        JButton plotAllButton = new JButton("All tracks");
        plotAllButton.setPreferredSize(new Dimension(frameWidth, elementHeight));
        plotAllButton.addActionListener(e -> new Thread((this::plotAll)).start());
        panel.add(plotAllButton,c);
    }

    public void plotAll()
    {
        int highestFrame = tracks.getHighestFrame();
        double[] duration = IntStream.range(0,highestFrame).asDoubleStream().toArray();
        double[] number = new double[duration.length];

        for (Track track:tracks.values()) {
            int dur = track.getDuration();
            for (int i=0;i<dur;i++) number[i] = number[i] + 1;
        }

        Plot plot = new Plot("Duration of objects relative to track start","Duration (frames)","Number of objects");
        plot.setColor(Color.BLACK);
        plot.addPoints(duration,number,Plot.LINE);
        plot.setLimitsToFit(true);
        plot.show();

    }
}
