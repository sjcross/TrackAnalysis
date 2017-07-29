package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.gui.TextRoi;
import ij.plugin.Duplicator;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by sc13967 on 04/07/2017.
 */
public class ShowTrackIDControl extends ModuleControl {
    private JTextField fontSizeTextField;

    public ShowTrackIDControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Show track IDs";
    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0,5,0,5);

        // Label and text field to get number of points to use for diffusion coefficient calculation
        JTextField label = new JTextField("Font size");
        label.setPreferredSize(new Dimension(3*panelWidth/4,elementHeight));
        label.setEditable(false);
        label.setBorder(null);
        c.gridwidth = 1;
        c.gridy++;
        c.insets = new Insets(5,5,20,5);
        panel.add(label,c);

        // Font size
        int fontSize = (int) Prefs.get("TrackAnalysis.ShowTrackID.fontSize",8);
        fontSizeTextField = new JTextField();
        fontSizeTextField.setPreferredSize(new Dimension(panelWidth/4-5,elementHeight));
        fontSizeTextField.setText(String.valueOf(fontSize));
        c.gridx++;
        c.insets = new Insets(5,0,20,5);
        panel.add(fontSizeTextField,c);

        return panel;

    }

    @Override
    public void run(int ID) {
        int fontSize = (int) Math.round(Double.parseDouble(fontSizeTextField.getText()));
        Prefs.set("TrackAnalysis.ShowTrackID.fontSize",fontSize);

        // Creating a duplicate of the image, so the original isn't altered
        ipl = new Duplicator().run(ipl);

        // Creating an overlay for the image
        Overlay ovl = new Overlay();
        ipl.setOverlay(ovl);

        if (ID == -1) {
            for (int key:tracks.keySet()) {
                Track track = tracks.get(key);

                Color col = Color.getHSBColor((float) Math.random(), (float) (Math.random()*0.2+0.8), (float) (Math.random()*0.2+0.8));

                double[] x = track.getX(true);
                double[] y = track.getY(true);
                double[] z = track.getZ(true);
                int[] f = track.getF();

                for (int i=0;i<f.length;i++) {
                    PointRoi roi = new PointRoi(x[i]+0.5,y[i]+0.5);
                    roi.setPointType(3);
                    roi.setPosition(1,(int) z[i]+1,f[i]+1);
                    roi.setStrokeColor(col);
                    ovl.addElement(roi);

                    TextRoi text = new TextRoi(x[i],y[i],String.valueOf(key));
                    text.setPosition(1,(int) z[i]+1,f[i]+1);
                    text.setCurrentFont(new Font(Font.SANS_SERIF,Font.PLAIN,fontSize));
                    text.setStrokeColor(col);
                    ovl.addElement(text);

                }
            }

        } else {
            Track track = tracks.get(ID);

            double[] x = track.getX(true);
            double[] y = track.getY(true);
            int[] f = track.getF();

            Color col = Color.getHSBColor((float) Math.random(), (float) (Math.random()*0.2+0.8), (float) (Math.random()*0.2+0.8));

            for (int i=0;i<f.length;i++) {
                PointRoi roi = new PointRoi(x[i]+0.5,y[i]+0.5);
                roi.setPointType(3);
                roi.setPosition(f[i]+1);
                roi.setStrokeColor(col);
                ovl.addElement(roi);

                TextRoi text = new TextRoi(x[i],y[i],String.valueOf(ID));
                text.setPosition(f[i]+1);
                text.setCurrentFont(new Font(Font.SANS_SERIF,Font.PLAIN,fontSize));
                text.setStrokeColor(col);
                ovl.addElement(text);

            }
        }

        ipl.show();

    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
