package io.github.sjcross.TrackAnalysis.GUI.Control;

import ij.ImagePlus;
import io.github.sjcross.common.object.tracks.TrackCollection;

import javax.swing.*;
import java.awt.*;

import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

public abstract class PlotableModule extends BasicModule
{
    private final JPanel superPanel;

    public PlotableModule(TrackCollection tracks, ImagePlus ipl)
    {
        super(tracks, ipl);

        superPanel = new JPanel(new GridBagLayout());

        // Button to plot all tracks
        JButton plotAllButton = new JButton("All tracks");
        plotAllButton.setPreferredSize(new Dimension(frameWidth, elementHeight));
        plotAllButton.addActionListener(e -> new Thread((this::plotAll)).start());

        // Button and text field to specify a single track to plot
        JTextField plotSingleTextField = new JTextField();
        plotSingleTextField.setPreferredSize(new Dimension(frameWidth/4-5,elementHeight));

        JButton plotSingleButton = new JButton("Single track");
        plotSingleButton.setPreferredSize(new Dimension(3*frameWidth/4,elementHeight));
        plotSingleButton.addActionListener(e -> tryPlotTrack(plotSingleTextField.getText()));



        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(20,5,0,5);
        superPanel.add(plotAllButton,c);

        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(5,5,0,0);
        superPanel.add(plotSingleButton,c);

        c.gridx++;
        c.insets = new Insets(5,0,0,5);
        superPanel.add(plotSingleTextField,c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(5,0,20,0);
        superPanel.add(panel, c);
    }

    @Override
    public JPanel getPanel()
    {
        return superPanel;
    }

    private void tryPlotTrack(String s)
    {
        try
        {
            int ID = Integer.parseInt(s);

            if (tracks.get(ID) == null) throw new NonExistentTrackException("Track doesn't exist");

            new Thread(() -> plotTrack(ID)).start();
        }
        catch (NumberFormatException nfe)
        {
            new Exception("Track ID not numeric", nfe).printStackTrace();
        }
        catch (NonExistentTrackException nete)
        {
            nete.printStackTrace();
        }
    }

    public abstract void plotAll();

    public abstract void plotTrack(int ID);
}
