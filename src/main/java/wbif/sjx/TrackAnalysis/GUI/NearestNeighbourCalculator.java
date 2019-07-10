package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.TreeMap;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class NearestNeighbourCalculator extends ModuleControl {
    private JComboBox<String> comboBox;

    public NearestNeighbourCalculator(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Nearest neighbour distance plot";
    }

    @Override
    public JPanel getExtraControls() {
        return null;

    }

    @Override
    public void run(int ID) {
        if (ID == -1) {
            double[][] nnDistance = tracks.getAverageNearestNeighbourDistance();
            double[] errMin = new double[nnDistance[0].length];
            double[] errMax = new double[nnDistance[0].length];

            for (int i=0;i<errMin.length;i++) {
                errMin[i] = nnDistance[1][i] - nnDistance[2][i];
                errMax[i] = nnDistance[1][i] + nnDistance[2][i];
            }

            String units = tracks.values().iterator().next().getUnits();

            Plot plot = new Plot("Nearest neighbour distance (all tracks)","Frame","Nearest neighbour distance ("+units+")");
            plot.setColor(Color.BLACK);
            plot.addPoints(nnDistance[0],nnDistance[1],Plot.LINE);
            plot.setColor(Color.RED);
            plot.addPoints(nnDistance[0],errMin,Plot.LINE);
            plot.addPoints(nnDistance[0],errMax,Plot.LINE);
            plot.setLimitsToFit(true);
            plot.show();

        } else {
            Track track = tracks.get(ID);
            double[] f = track.getFAsDouble();
            TreeMap<Integer,double[]> nnDistance = track.getNearestNeighbourDistance(tracks);
            double[] vals = new double[nnDistance.size()];
            for (int i=0;i<vals.length;i++) {
                vals[i] = nnDistance.get((int) Math.round(f[i]))[1];
            }

            String units = track.getUnits();
            Plot plot = new Plot("Nearest neighbour distance (track "+ID+")","Frame","Nearest neighbour distance ("+units+")");
            plot.setColor(Color.BLACK);
            plot.addPoints(f,vals,Plot.LINE);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {


    }
}
