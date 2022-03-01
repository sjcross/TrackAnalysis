package io.github.sjcross.trackanalysis.GUI;

import static io.github.sjcross.trackanalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.trackanalysis.GUI.Control.MainGUI.frameWidth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;
import io.github.sjcross.trackanalysis.GUI.Control.PlotableModule;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class MotilityPlotControl extends PlotableModule {
    private JTextField lineWidthTextField;
    private JCheckBox blackPlotCheckbox;
    private JCheckBox showLabelsCheckbox;

    public MotilityPlotControl(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 5);

        // Label and text field to specify line width
        JTextField label = new JTextField("Line width");
        label.setPreferredSize(new Dimension(3 * frameWidth / 4, elementHeight));
        label.setEditable(false);
        label.setBorder(null);
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 0, 5);
        panel.add(label, c);

        double lineWidth = Prefs.get("TrackAnalysis.MotilityPlot.lineWith", 1.0);
        lineWidthTextField = new JTextField();
        lineWidthTextField.setPreferredSize(new Dimension(frameWidth / 4 - 5, elementHeight));
        lineWidthTextField.setText(String.valueOf(lineWidth));
        c.gridx++;
        c.insets = new Insets(5, 0, 0, 5);
        panel.add(lineWidthTextField, c);

        boolean blackPlot = Prefs.get("TrackAnalysis.MotilityPlot.blackPlot", false);
        blackPlotCheckbox = new JCheckBox("Black lines");
        blackPlotCheckbox.setPreferredSize(new Dimension(frameWidth, elementHeight));
        blackPlotCheckbox.setSelected(blackPlot);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        panel.add(blackPlotCheckbox, c);

        boolean showLabels = Prefs.get("TrackAnalysis.MotilityPlot.showLabels", false);
        showLabelsCheckbox = new JCheckBox("Show labels");
        showLabelsCheckbox.setPreferredSize(new Dimension(frameWidth, elementHeight));
        showLabelsCheckbox.setSelected(showLabels);
        c.gridy++;
        c.insets = new Insets(5, 0, 20, 5);
        panel.add(showLabelsCheckbox, c);
    }

    @Override
    public void plotAll() {
        double lineWidth = Double.parseDouble(lineWidthTextField.getText());

        boolean blackPlot = blackPlotCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.blackPlot", blackPlot);

        boolean showLabels = showLabelsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.showLabels", showLabels);

        String units = tracks.values().iterator().next().getUnits();
        Plot plot = new Plot("Object motility (All tracks)", "X-position (" + units + ")",
                "Y-position (" + units + ")");
        plot.setLineWidth((float) lineWidth);

        double[][] lims = new double[2][2];
        for (int key : tracks.keySet()) {
            Track track = tracks.get(key);

            Color col = Color.BLACK;
            if (!blackPlot) {
                col = Color.getHSBColor((float) Math.random(), (float) (Math.random() * 0.2 + 0.8),
                        (float) (Math.random() * 0.2 + 0.8));
            }

            double[] x = track.getX();
            double[] y = track.getY();

            for (int i = 1; i < x.length; i++) {
                x[i] = x[i] - x[0];
                y[i] = y[i] - y[0];

                lims[0][0] = Math.min(lims[0][0],x[i]);
                lims[0][1] = Math.max(lims[0][1],x[i]);
                lims[1][0] = Math.min(lims[1][0],y[i]);
                lims[1][1] = Math.max(lims[1][1],y[i]);
                
            }
            x[0] = y[0] = 0;

            plot.setColor(col, col);
            plot.addPoints(x, y, Plot.LINE);

            if (showLabels)
                plot.addText(String.valueOf(key), x[x.length - 1], y[y.length - 1]);

        }

        plot.setLimitsToFit(true);
        plot.draw();        

        double mag = 1.2;
        plot.setLimits(lims[0][0] * mag, lims[0][1] * mag, lims[1][0] * mag, lims[1][1] * mag);
        int base_w = 600;
        double range_x = (lims[0][1] - lims[0][0]) * mag;
        double range_y = (lims[1][1] - lims[1][0]) * mag;

        if (range_x > range_y) {
            plot.setFrameSize(base_w, (int) (base_w * range_y / range_x));
        } else {
            plot.setFrameSize((int) (base_w * range_x / range_y), base_w);
        }

        plot.setLineWidth(1);
        plot.show();

    }

    @Override
    public void plotTrack(int ID) {
        double lineWidth = Double.parseDouble(lineWidthTextField.getText());

        boolean blackPlot = blackPlotCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.blackPlot", blackPlot);

        boolean showLabels = showLabelsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.showLabels", showLabels);

        String units = tracks.values().iterator().next().getUnits();
        Plot plot = new Plot("Object motility (All tracks)", "X-position (" + units + ")",
                "Y-position (" + units + ")");
        plot.setLineWidth((float) lineWidth);

        Track track = tracks.get(ID);

        Color col = Color.BLACK;
        if (!blackPlot) {
            col = Color.getHSBColor((float) Math.random(), (float) (Math.random() * 0.2 + 0.8),
                    (float) (Math.random() * 0.2 + 0.8));
        }

        double[] x = track.getX();
        double[] y = track.getY();

        for (int i = 1; i < x.length; i++) {
            x[i] = x[i] - x[0];
            y[i] = y[i] - y[0];
        }
        x[0] = y[0] = 0;

        plot.setColor(col, col);
        plot.addPoints(x, y, Plot.LINE);

        if (showLabels)
            plot.addText(String.valueOf(ID), x[x.length - 1], y[y.length - 1]);

        plot.setLimitsToFit(true);
        plot.draw();

        double mag = 1.2;
        double[] lim = plot.getLimits();
        plot.setLimits(lim[0] * mag, lim[1] * mag, lim[2] * mag, lim[3] * mag);
        int base_w = 600;
        double range_x = (lim[1] - lim[0]) * mag;
        double range_y = (lim[3] - lim[2]) * mag;

        if (range_x > range_y) {
            plot.setFrameSize(base_w, (int) (base_w * range_y / range_x));
        } else {
            plot.setFrameSize((int) (base_w * range_x / range_y), base_w);
        }

        plot.setLineWidth(1);
        plot.show();

    }
}
