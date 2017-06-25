package wbif.sjx.TrackAnalysis.GUI;

import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by sc13967 on 25/06/2017.
 */
public class DirectionalPersistenceControl extends ModuleControl {
    DirectionalPersistenceControl(TrackCollection tracks, int panelWidth, int elementHeight) {
        super(tracks, panelWidth, elementHeight);
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
            double[] euclideanDistance = track.getRollingEuclideanDistance(pixelDistances);

            String units = track.getUnits(pixelDistances);
            Plot plot = new Plot("Euclidean distance (track "+ID+")","Time relative to start of track (frames)","Euclidean distance ("+units+")",f,euclideanDistance);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}