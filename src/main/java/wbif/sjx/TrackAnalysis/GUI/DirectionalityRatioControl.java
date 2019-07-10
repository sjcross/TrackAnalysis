package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by sc13967 on 25/06/2017.
 */
public class DirectionalityRatioControl extends ModuleControl {
    private static final String RELATIVE_TO_FIRST_FRAME = "Relative to first frame";
    private static final String RELATIVE_TO_TRACK_START = "Relative to track start";

    private JComboBox<String> comboBox;

    public DirectionalityRatioControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Directionality ratio plot";
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
            double[][] directionalityRatio = tracks.getAverageDirectionalityRatio(relativeToTrackStart);
            double[] errMin = new double[directionalityRatio[0].length];
            double[] errMax = new double[directionalityRatio[0].length];

            for (int i=0;i<errMin.length;i++) {
                errMin[i] = directionalityRatio[1][i] - directionalityRatio[2][i];
                errMax[i] = directionalityRatio[1][i] + directionalityRatio[2][i];
            }

            Plot plot = new Plot("Directionality ratio (all tracks)",xLabel,"Directionality ratio");
            plot.setColor(Color.BLACK);
            plot.addPoints(directionalityRatio[0],directionalityRatio[1],Plot.LINE);
            plot.setColor(Color.RED);
            plot.addPoints(directionalityRatio[0],errMin,Plot.LINE);
            plot.addPoints(directionalityRatio[0],errMax,Plot.LINE);
            plot.setLimitsToFit(true);
            plot.show();

        } else {
            Track track = tracks.get(ID);
            double[] f = track.getFAsDouble();
            TreeMap<Integer,Double> directionalityRatio = track.getRollingDirectionalityRatio();

            double[] vals = directionalityRatio.values().stream().mapToDouble(Double::doubleValue).toArray();
            Plot plot = new Plot("Directionality ratio (track "+ID+")",xLabel,"Directionality ratio",f,vals);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
