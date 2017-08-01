package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Engine;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackPlotControl extends ModuleControl implements ChangeListener {

    private final static String PLOT = "Plot tracks";

    private Engine engine;

    private JCheckBox motilityPlotCheckbox;
    private JSlider FOVSlider;

    public TrackPlotControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Track plot";
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
        JButton plotAllButton = new JButton(PLOT);
        plotAllButton.setPreferredSize(new Dimension(panelWidth,elementHeight));
        plotAllButton.addActionListener(this);
        panel.add(plotAllButton,c);

        return panel;

    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,5,0,5);

        int FOV = (int)Prefs.get("TrackAnalysis.TrackPlot.motilityPlotCheckbox", 60);
        FOVSlider = new JSlider(JSlider.HORIZONTAL, 30, 160, FOV);
        FOVSlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        FOVSlider.addChangeListener(engine);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(FOVSlider,c);

        boolean motilityPlot = Prefs.get("TrackAnalysis.TrackPlot.motilityPlotCheckbox",false);
        motilityPlotCheckbox = new JCheckBox("Show motility plot");
        motilityPlotCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        motilityPlotCheckbox.setSelected(motilityPlot);
        motilityPlotCheckbox.addActionListener(engine);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(motilityPlotCheckbox,c);

        return panel;
    }

    @Override
    public void run(int ID) {
        new Thread(() -> engine = new Engine(tracks)).start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(PLOT)){
            run(-1);
        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }


    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
