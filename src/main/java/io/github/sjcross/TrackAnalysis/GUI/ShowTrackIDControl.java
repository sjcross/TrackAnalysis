package io.github.sjcross.trackanalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.gui.TextRoi;
import ij.plugin.Duplicator;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;
import io.github.sjcross.trackanalysis.GUI.Control.PlotableModule;

import javax.swing.*;

import static io.github.sjcross.trackanalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.trackanalysis.GUI.Control.MainGUI.frameWidth;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by sc13967 on 04/07/2017.
 */
public class ShowTrackIDControl extends PlotableModule {
    private JTextField spotSizeTextField;
    private JTextField fontSizeTextField;
    private JCheckBox showTrailsCheckbox;
    private JTextField trailWidthTextField;

    public ShowTrackIDControl(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 5, 0, 5);

        JTextField spotSizeLabel = new JTextField("Spot size (0-4)");
        spotSizeLabel.setPreferredSize(new Dimension(3 * frameWidth / 4, elementHeight));
        spotSizeLabel.setEditable(false);
        spotSizeLabel.setBorder(null);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(spotSizeLabel, c);

        // Font size
        int spotSize = (int) Prefs.get("TrackAnalysis.ShowTrackID.spotSize", 2);
        spotSizeTextField = new JTextField();
        spotSizeTextField.setPreferredSize(new Dimension(frameWidth / 4 - 5, elementHeight));
        spotSizeTextField.setText(String.valueOf(spotSize));
        c.gridx++;
        panel.add(spotSizeTextField, c);

        JTextField fontSizeLabel = new JTextField("Font size");
        fontSizeLabel.setPreferredSize(new Dimension(3 * frameWidth / 4, elementHeight));
        fontSizeLabel.setEditable(false);
        fontSizeLabel.setBorder(null);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(fontSizeLabel, c);

        // Font size
        int fontSize = (int) Prefs.get("TrackAnalysis.ShowTrackID.fontSize", 8);
        fontSizeTextField = new JTextField();
        fontSizeTextField.setPreferredSize(new Dimension(frameWidth / 4 - 5, elementHeight));
        fontSizeTextField.setText(String.valueOf(fontSize));
        c.gridx++;
        panel.add(fontSizeTextField, c);

        JTextField showLabelLabel = new JTextField("Show trails");
        showLabelLabel.setPreferredSize(new Dimension(3 * frameWidth / 4, elementHeight));
        showLabelLabel.setEditable(false);
        showLabelLabel.setBorder(null);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(showLabelLabel, c);

        // Font size
        boolean showTrails = Prefs.get("TrackAnalysis.ShowTrackID.showTrails", true);
        showTrailsCheckbox = new JCheckBox();
        showTrailsCheckbox.setPreferredSize(new Dimension(frameWidth / 4 - 5, elementHeight));
        showTrailsCheckbox.setSelected(showTrails);
        c.gridx++;
        panel.add(showTrailsCheckbox, c);

        JTextField trailWidthLabel = new JTextField("Trail width");
        trailWidthLabel.setPreferredSize(new Dimension(3 * frameWidth / 4, elementHeight));
        trailWidthLabel.setEditable(false);
        trailWidthLabel.setBorder(null);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        c.insets = new Insets(5, 5, 20, 5);
        panel.add(trailWidthLabel, c);

        // Font size
        int trailWidth = (int) Prefs.get("TrackAnalysis.ShowTrackID.trailWidth", 1);
        trailWidthTextField = new JTextField();
        trailWidthTextField.setPreferredSize(new Dimension(frameWidth / 4 - 5, elementHeight));
        trailWidthTextField.setText(String.valueOf(trailWidth));
        c.gridx++;
        c.insets = new Insets(5, 0, 20, 5);
        panel.add(fontSizeTextField, c);
    }

    @Override
    public void plotAll() {
        int fontSize = (int) Math.round(Double.parseDouble(fontSizeTextField.getText()));
        Prefs.set("TrackAnalysis.ShowTrackID.fontSize", fontSize);

        int spotSize = (int) Math.round(Double.parseDouble(spotSizeTextField.getText()));
        Prefs.set("TrackAnalysis.ShowTrackID.spotSize", spotSize);

        boolean showTrails = showTrailsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.ShowTrackID.showTrails", showTrails);

        int trailWidth = (int) Math.round(Double.parseDouble(trailWidthTextField.getText()));
        Prefs.set("TrackAnalysis.ShowTrackID.trailWidth", trailWidth);

        // Creating a duplicate of the image, so the original isn't altered
        ImagePlus dupIpl = ipl.duplicate();

        // Creating an overlay for the image
        Overlay ovl = new Overlay();
        dupIpl.setOverlay(ovl);

        for (int key : tracks.keySet()) {
            Track track = tracks.get(key);

            Color col = Color.getHSBColor((float) Math.random(), (float) (Math.random() * 0.2 + 0.8),
                    (float) (Math.random() * 0.2 + 0.8));

            double[] x = track.getX();
            double[] y = track.getY();
            double[] z = track.getZ();
            int[] f = track.getF();

            for (int i = 0; i < f.length; i++) {
                PointRoi roi = new PointRoi(x[i] + 0.5, y[i] + 0.5);
                roi.setPointType(3);
                roi.setSize(spotSize);
                if (dupIpl.isHyperStack())
                    roi.setPosition(1, (int) (z[i] + 1), f[i] + 1);
                else
                    roi.setPosition(f[i] + 1);
                roi.setStrokeColor(col);
                ovl.addElement(roi);

                // Add labels if the font size is not equal to zero
                if (fontSize != 0) {
                    TextRoi text = new TextRoi(x[i], y[i], String.valueOf(key));
                    if (dupIpl.isHyperStack())
                        text.setPosition(1, (int) (z[i] + 1), f[i] + 1);
                    else
                        text.setPosition(f[i] + 1);
                    text.setCurrentFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
                    text.setStrokeColor(col);
                    ovl.addElement(text);
                }

                // Adding trail if selected
                if (showTrails) {
                    // Adding trail to all subsequent sections
                    for (int ff = f[i] + 1; ff < ipl.getNFrames(); ff++) {
                        if ((i + 1) == f.length)
                            continue;
                        Line line = new Line(x[i], y[i], x[i + 1], y[i + 1]);
                        if (dupIpl.isHyperStack())
                            line.setPosition(1, (int) (z[i] + 1), ff + 1);
                        else
                            line.setPosition(ff + 1);
                        line.setStrokeColor(col);
                        line.setStrokeWidth(trailWidth);
                        ovl.addElement(line);
                    }
                }
            }
        }

        ipl.show();
    }

    @Override
    public void plotTrack(int ID) {
        int fontSize = (int) Math.round(Double.parseDouble(fontSizeTextField.getText()));
        Prefs.set("TrackAnalysis.ShowTrackID.fontSize", fontSize);

        // Creating a duplicate of the image, so the original isn't altered
        ipl = new Duplicator().run(ipl);

        // Creating an overlay for the image
        Overlay ovl = new Overlay();
        ipl.setOverlay(ovl);

        Track track = tracks.get(ID);

        double[] x = track.getX();
        double[] y = track.getY();
        int[] f = track.getF();

        Color col = Color.getHSBColor((float) Math.random(), (float) (Math.random() * 0.2 + 0.8),
                (float) (Math.random() * 0.2 + 0.8));

        for (int i = 0; i < f.length; i++) {
            PointRoi roi = new PointRoi(x[i] + 0.5, y[i] + 0.5);
            roi.setPointType(3);
            roi.setPosition(f[i] + 1);
            roi.setStrokeColor(col);
            ovl.addElement(roi);

            TextRoi text = new TextRoi(x[i], y[i], String.valueOf(ID));
            text.setPosition(f[i] + 1);
            text.setCurrentFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
            text.setStrokeColor(col);
            ovl.addElement(text);
        }

        ipl.show();
    }
}
