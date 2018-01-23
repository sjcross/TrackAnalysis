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

/**
 * Created by sc13967 on 25/06/2017.
 */
public class DirectionalPersistenceControl extends ModuleControl {

    public DirectionalPersistenceControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Directional persistence plot";
    }

    @Override
    public JPanel getExtraControls() {
        return null;
    }

    @Override
    public void run(int ID) {
        boolean pixelDistances = calibrationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.pixelDistances",pixelDistances);

        if (ID == -1) {
            double[][] directionalPersistence = tracks.getAverageDirectionalPersistence(pixelDistances);
            double[] errMin = new double[directionalPersistence[0].length];
            double[] errMax = new double[directionalPersistence[0].length];

            for (int i=0;i<errMin.length;i++) {
                errMin[i] = directionalPersistence[1][i] - directionalPersistence[2][i];
                errMax[i] = directionalPersistence[1][i] + directionalPersistence[2][i];
            }

            Plot plot = new Plot("Directional persistence (all tracks)","Interval (frames)","Directional persistence");
            plot.setColor(Color.BLACK);
            plot.addPoints(directionalPersistence[0],directionalPersistence[1],Plot.LINE);
            plot.setColor(Color.RED);
            plot.addPoints(directionalPersistence[0],errMin,Plot.LINE);
            plot.addPoints(directionalPersistence[0],errMax,Plot.LINE);
            plot.setLimitsToFit(true);
            plot.show();

        } else {
            Track track = tracks.get(ID);
            double[] f = track.getFAsDouble();
            TreeMap<Integer,Double> euclideanDistance = track.getRollingEuclideanDistance(pixelDistances);
            double[] vals = euclideanDistance.values().stream().mapToDouble(Double::doubleValue).toArray();

            String units = track.getUnits(pixelDistances);
            Plot plot = new Plot("Euclidean distance (track "+ID+")","Time relative to start of track (frames)",
                    "Euclidean distance ("+units+")",f,vals);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
