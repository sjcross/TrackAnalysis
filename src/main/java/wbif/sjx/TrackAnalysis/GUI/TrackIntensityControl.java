package wbif.sjx.TrackAnalysis.GUI;

import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by steph on 12/07/2017.
 */
public class TrackIntensityControl extends ModuleControl {
    private static final String RELATIVE_TO_FIRST_FRAME = "Relative to first frame";
    private static final String RELATIVE_TO_TRACK_START = "Relative to track start";

    private JComboBox<String> comboBox;

    public TrackIntensityControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Track intensity plot";
    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,5,0,5);

        String relativePosition = Prefs.get("TrackAnalysis.relativePosition",RELATIVE_TO_TRACK_START);
        comboBox = new JComboBox<>(new String[]{RELATIVE_TO_TRACK_START,RELATIVE_TO_FIRST_FRAME});
        comboBox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        comboBox.setSelectedItem(relativePosition);
        c.insets = new Insets(0,5,20,5);
        panel.add(comboBox,c);

        return panel;

    }

    @Override
    public void run(int ID) {
        String relativePosition = (String) comboBox.getSelectedItem();
        Prefs.set("TrackAnalysis.relativePosition",relativePosition);
        boolean relativeToTrackStart = relativePosition.equals(RELATIVE_TO_TRACK_START);
        String xLabel = relativeToTrackStart ? "Time relative to start of track (frames)" : "Time relative to first frame (frames)";

        if (ID == -1) {
            IJ.showMessage("Feature coming soon (12-07-2017)");

        } else {
            System.out.println(ipl.getNChannels()+"_"+ipl.getNSlices()+"_"+ipl.getNFrames());
            Track track = tracks.get(ID);
            double[] f = track.getFAsDouble();
            double[] trackIntensity = track.getRollingIntensity(ipl,2);

            Plot plot = new Plot("Total path length (track "+ID+")",xLabel,"Mean track intensity",f,trackIntensity);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
