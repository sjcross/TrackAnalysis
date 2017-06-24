package wbif.sjx.TrackAnalysis.GUI;

import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class MotilityPlotControl extends ModuleControl {
    private JCheckBox blackPlotCheckbox;
    private JCheckBox showLabelsCheckbox;

    public MotilityPlotControl(TrackCollection tracks, int panelWidth, int elementHeight) {
        super(tracks, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Motility plot";
    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,5,0,5);

        boolean blackPlot = Prefs.get("TrackAnalysis.MotilityPlot.blackPlot",false);
        blackPlotCheckbox = new JCheckBox("Black lines");
        blackPlotCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        blackPlotCheckbox.setSelected(blackPlot);
        panel.add(blackPlotCheckbox,c);

        boolean showLabels = Prefs.get("TrackAnalysis.MotilityPlot.showLabels",false);
        showLabelsCheckbox = new JCheckBox("Show labels");
        showLabelsCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showLabelsCheckbox.setSelected(showLabels);
        c.gridy++;
        panel.add(showLabelsCheckbox,c);

        return panel;

    }

    @Override
    public void run(int ID) {
        boolean pixelDistances = calibrationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.pixelDistances",pixelDistances);

        boolean blackPlot = blackPlotCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.blackPlot",blackPlot);

        boolean showLabels = showLabelsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.showLabels",showLabels);

        Plot plot;
        if (ID == -1) {
            String units = tracks.values().iterator().next().getUnits(pixelDistances);
            plot = new Plot("Object motility (All tracks)","X-position ("+units+")","Y-position ("+units+")");

            for (int key:tracks.keySet()) {
                Track track = tracks.get(key);

                Color col = Color.BLACK;
                if (!blackPlot) {
                    col = Color.getHSBColor((float) Math.random(), (float) (Math.random()*0.2+0.8), (float) (Math.random()*0.2+0.8));
                }

                double[] x = track.getX(pixelDistances);
                double[] y = track.getY(pixelDistances);

                for (int i = 1; i < x.length; i++) {
                    x[i] = x[i] - x[0];
                    y[i] = y[i] - y[0];
                }
                x[0] = y[0] = 0;

                plot.setColor(col, col);
                plot.addPoints(x, y, Plot.LINE);

                if (showLabels) plot.addText(String.valueOf(key),x[x.length-1],y[y.length-1]);

            }

        } else {
            String units = tracks.values().iterator().next().getUnits(pixelDistances);
            plot = new Plot("Object motility (All tracks)", "X-position (" + units + ")", "Y-position (" + units + ")");

            Track track = tracks.get(ID);

            Color col = Color.BLACK;
            if (!blackPlot) {
                col = Color.getHSBColor((float) Math.random(), (float) (Math.random() * 0.2 + 0.8), (float) (Math.random() * 0.2 + 0.8));
            }

            double[] x = track.getX(pixelDistances);
            double[] y = track.getY(pixelDistances);

            for (int i = 1; i < x.length; i++) {
                x[i] = x[i] - x[0];
                y[i] = y[i] - y[0];
            }
            x[0] = y[0] = 0;

            plot.setColor(col, col);
            plot.addPoints(x, y, Plot.LINE);

            if (showLabels) plot.addText(String.valueOf(ID), x[x.length - 1], y[y.length - 1]);

        }

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

        plot.show();

    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
