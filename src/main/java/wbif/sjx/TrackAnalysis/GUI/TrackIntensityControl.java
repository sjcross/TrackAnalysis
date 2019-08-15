package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.TrackAnalysis.GUI.Control.BasicModule;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;

import static wbif.sjx.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static wbif.sjx.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

/**
 * Created by steph on 12/07/2017.
 */
public class TrackIntensityControl extends BasicModule
{
    private static final String RELATIVE_TO_FIRST_FRAME = "Relative to first frame";
    private static final String RELATIVE_TO_TRACK_START = "Relative to track start";

    private JComboBox<String> comboBox;

    public TrackIntensityControl(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        String relativePosition = Prefs.get("TrackAnalysis.relativePosition",RELATIVE_TO_TRACK_START);
        comboBox = new JComboBox<>(new String[]{RELATIVE_TO_TRACK_START,RELATIVE_TO_FIRST_FRAME});
        comboBox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        comboBox.setSelectedItem(relativePosition);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20,5,20,5);
        panel.add(comboBox,c);
    }

    private void plotSingle(int ID)
    {
        String relativePosition = (String) comboBox.getSelectedItem();
        Prefs.set("TrackAnalysis.relativePosition",relativePosition);
        boolean relativeToTrackStart = relativePosition.equals(RELATIVE_TO_TRACK_START);
        String xLabel = relativeToTrackStart ? "Time relative to start of track (frames)" : "Time relative to first frame (frames)";

        Track track = tracks.get(ID);
        double[] f = track.getFAsDouble();
        double[] trackIntensity = track.getRollingIntensity(ipl,2);

        Plot plot = new Plot("Total path length (track "+ID+")",xLabel,"Mean track intensity",f,trackIntensity);
        plot.show();
    }
}
