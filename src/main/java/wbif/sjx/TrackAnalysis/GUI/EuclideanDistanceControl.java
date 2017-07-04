package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
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
public class EuclideanDistanceControl extends ModuleControl {
    private static final String RELATIVE_TO_FIRST_FRAME = "Relative to first frame";
    private static final String RELATIVE_TO_TRACK_START = "Relative to track start";

    private JComboBox<String> comboBox;

    public EuclideanDistanceControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Euclidean distance plot";
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
        boolean pixelDistances = calibrationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.pixelDistances",pixelDistances);

        String relativePosition = (String) comboBox.getSelectedItem();
        Prefs.set("TrackAnalysis.relativePosition",relativePosition);
        boolean relativeToTrackStart = relativePosition.equals(RELATIVE_TO_TRACK_START);
        String xLabel = relativeToTrackStart ? "Time relative to start of track (frames)" : "Time relative to first frame (frames)";

        if (ID == -1) {
            double[][] euclideanDistance = tracks.getAverageRollingEuclideanDistance(pixelDistances,relativeToTrackStart);
            double[] errMin = new double[euclideanDistance[0].length];
            double[] errMax = new double[euclideanDistance[0].length];

            for (int i=0;i<errMin.length;i++) {
                errMin[i] = euclideanDistance[1][i] - euclideanDistance[2][i];
                errMax[i] = euclideanDistance[1][i] + euclideanDistance[2][i];
            }

            String units = tracks.values().iterator().next().getUnits(pixelDistances);

            Plot plot = new Plot("Euclidean distance (all tracks)",xLabel,"Euclidean distance ("+units+")");
            plot.setColor(Color.BLACK);
            plot.addPoints(euclideanDistance[0],euclideanDistance[1],Plot.LINE);
            plot.setColor(Color.RED);
            plot.addPoints(euclideanDistance[0],errMin,Plot.LINE);
            plot.addPoints(euclideanDistance[0],errMax,Plot.LINE);
            plot.setLimitsToFit(true);
            plot.show();

        } else {
            Track track = tracks.get(ID);
            double[] f = track.getFAsDouble();
            double[] euclideanDistance = track.getRollingEuclideanDistance(pixelDistances);

            String units = track.getUnits(pixelDistances);
            Plot plot = new Plot("Euclidean distance (track "+ID+")",xLabel,"Euclidean distance ("+units+")");
            plot.setColor(Color.BLACK);
            plot.addPoints(f,euclideanDistance,Plot.LINE);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {


    }
}
