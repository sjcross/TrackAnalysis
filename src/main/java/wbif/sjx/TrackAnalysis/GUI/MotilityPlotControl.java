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
    private JTextField lineWidthTextField;
    private JCheckBox blackPlotCheckbox;
    private JCheckBox showLabelsCheckbox;

    MotilityPlotControl(TrackCollection tracks, int panelWidth, int elementHeight) {
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

        // Label and text field to specify line width
        JTextField label = new JTextField("Line width");
        label.setPreferredSize(new Dimension(3*panelWidth/4,elementHeight));
        label.setEditable(false);
        label.setBorder(null);
        label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
        c.gridwidth = 1;
        c.insets = new Insets(5,5,0,5);
        panel.add(label,c);

        double lineWidth = Prefs.get("TrackAnalysis.MotilityPlot.lineWith",1.0);
        lineWidthTextField = new JTextField();
        lineWidthTextField.setPreferredSize(new Dimension(panelWidth/4-5,elementHeight));
        lineWidthTextField.setText(String.valueOf(lineWidth));
        c.gridx++;
        c.insets = new Insets(5,0,0,5);
        panel.add(lineWidthTextField,c);

        boolean blackPlot = Prefs.get("TrackAnalysis.MotilityPlot.blackPlot",false);
        blackPlotCheckbox = new JCheckBox("Black lines");
        blackPlotCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        blackPlotCheckbox.setSelected(blackPlot);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        panel.add(blackPlotCheckbox,c);

        boolean showLabels = Prefs.get("TrackAnalysis.MotilityPlot.showLabels",false);
        showLabelsCheckbox = new JCheckBox("Show labels");
        showLabelsCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showLabelsCheckbox.setSelected(showLabels);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(showLabelsCheckbox,c);

        return panel;

    }

    @Override
    public void run(int ID) {
        boolean pixelDistances = calibrationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.pixelDistances",pixelDistances);

        double lineWidth = Double.parseDouble(lineWidthTextField.getText());

        boolean blackPlot = blackPlotCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.blackPlot",blackPlot);

        boolean showLabels = showLabelsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MotilityPlot.showLabels",showLabels);

        Plot plot;
        if (ID == -1) {
            String units = tracks.values().iterator().next().getUnits(pixelDistances);
            plot = new Plot("Object motility (All tracks)","X-position ("+units+")","Y-position ("+units+")");
            plot.setLineWidth((float) lineWidth);

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
            plot.setLineWidth((float) lineWidth);

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

        plot.setLineWidth(1);
        plot.show();

    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
