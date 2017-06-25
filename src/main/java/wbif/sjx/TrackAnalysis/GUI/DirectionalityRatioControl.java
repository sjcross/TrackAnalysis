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
public class DirectionalityRatioControl extends ModuleControl {
    DirectionalityRatioControl(TrackCollection tracks, int panelWidth, int elementHeight) {
        super(tracks, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Directionality ratio plot";
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
            double[][] directionalityRatio = tracks.getAverageDirectionalityRatio(pixelDistances);
            double[] errMin = new double[directionalityRatio[0].length];
            double[] errMax = new double[directionalityRatio[0].length];

            for (int i=0;i<errMin.length;i++) {
                errMin[i] = directionalityRatio[1][i] - directionalityRatio[2][i];
                errMax[i] = directionalityRatio[1][i] + directionalityRatio[2][i];
            }

            Plot plot = new Plot("Directionality ratio (all tracks)","Time relative to start of track (frames)","Directionality ratio");
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
            double[] directionalityRatio = track.getRollingDirectionalityRatio(pixelDistances);

            Plot plot = new Plot("Directionality ratio (track "+ID+")","Time relative to start of track (frames)","Directionality ratio",f,directionalityRatio);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
