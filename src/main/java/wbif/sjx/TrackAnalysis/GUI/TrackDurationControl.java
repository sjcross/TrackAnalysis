package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.IntStream;

public class TrackDurationControl extends ModuleControl {
    public TrackDurationControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Track duration plot";
    }

    @Override
    public JPanel getCommonControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(20,5,20,5);

        // Button to run ensemble plotting
        JButton plotAllButton = new JButton(PLOT_ALL);
        plotAllButton.setPreferredSize(new Dimension(panelWidth,elementHeight));
        plotAllButton.addActionListener(this);
        panel.add(plotAllButton,c);

        return panel;
    }

    @Override
    public JPanel getExtraControls() {
        return null;

    }

    @Override
    public void run(int ID) {
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

    @Override
    public void extraActions(ActionEvent e) {


    }
}
