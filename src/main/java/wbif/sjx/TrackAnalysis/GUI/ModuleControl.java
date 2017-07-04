package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sc13967 on 24/06/2017.
 */
public abstract class ModuleControl implements ActionListener {
    private static final String PLOT_ALL = "All tracks";
    private static final String PLOT_SINGLE = "Single track";

    TrackCollection tracks;
    ImagePlus ipl;
    int panelWidth;
    int elementHeight;

    private JTextField plotSingleTextField;
    JCheckBox calibrationCheckbox;

    public ModuleControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        this.tracks = tracks;
        this.ipl = ipl;
        this.panelWidth = panelWidth;
        this.elementHeight = elementHeight;

    }

    JPanel getCommonControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(20,5,0,5);

        // Button to run ensemble plotting
        JButton plotAllButton = new JButton(PLOT_ALL);
        plotAllButton.setPreferredSize(new Dimension(panelWidth,elementHeight));
        plotAllButton.addActionListener(this);
        panel.add(plotAllButton,c);

        // Button and text field to specify a single track to plot
        JButton plotSingleButton = new JButton(PLOT_SINGLE);
        plotSingleButton.setPreferredSize(new Dimension(3*panelWidth/4,elementHeight));
        plotSingleButton.addActionListener(this);
        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(5,5,0,5);
        panel.add(plotSingleButton,c);

        plotSingleTextField = new JTextField();
        plotSingleTextField.setPreferredSize(new Dimension(panelWidth/4-5,elementHeight));
        c.gridx++;
        c.insets = new Insets(5,0,0,5);
        panel.add(plotSingleTextField,c);

        // Checkbox to turn pixelDistances on or off
        boolean pixelDistances = Prefs.get("TrackAnalysis.pixelDistances",true);
        calibrationCheckbox = new JCheckBox("Use pixel distances");
        calibrationCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        calibrationCheckbox.setSelected(pixelDistances);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(5,5,20,5);
        panel.add(calibrationCheckbox,c);

        return panel;

    }

    public abstract String getTitle();

    public abstract JPanel getExtraControls();

    public abstract void run(int ID);

    public abstract void extraActions(ActionEvent e);

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(PLOT_ALL)) {
            new Thread(() -> run(-1)).start();

        } else if (e.getActionCommand().equals(PLOT_SINGLE)) {
            try {
                int ID = Integer.parseInt(plotSingleTextField.getText());

                if (tracks.get(ID) == null) throw new NonExistentTrackException();

                new Thread(() -> run(ID)).start();

            } catch (NumberFormatException nfe) {
                System.out.println("Track ID not numeric");

            } catch (NonExistentTrackException nete) {
                System.out.println("Track doesn't exist");

            }
        } else {
            new Thread(() -> extraActions(e)).start();

        }
    }

}

class NonExistentTrackException extends Exception {

}
