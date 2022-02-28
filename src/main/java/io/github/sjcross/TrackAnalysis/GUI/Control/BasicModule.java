package io.github.sjcross.TrackAnalysis.GUI.Control;

import ij.ImagePlus;
import io.github.sjcross.common.object.tracks.TrackCollection;

import javax.swing.*;
import java.awt.*;

public abstract class BasicModule
{
    protected TrackCollection tracks;
    protected ImagePlus ipl;
    protected JPanel panel;

    public BasicModule(TrackCollection tracks, ImagePlus ipl)
    {
        this.tracks = tracks;
        this.ipl = ipl;
        this.panel = new JPanel(new GridBagLayout());
    }

    public JPanel getPanel()
    {
        return panel;
    }
}
