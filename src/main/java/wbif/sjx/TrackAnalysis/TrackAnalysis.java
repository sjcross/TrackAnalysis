package wbif.sjx.TrackAnalysis;

import ij.Prefs;
import ij.gui.GenericDialog;
import wbif.sjx.TrackAnalysis.Objects.TrackCollection;
import wbif.sjx.TrackAnalysis.Visualisation.MotionHeatmap;

/**
 * Created by sc13967 on 12/06/2017.
 */
public class TrackAnalysis {
    private TrackCollection tracks;

    public TrackAnalysis(TrackCollection tracks) {
        this.tracks = tracks;

        displayMotionHeatmap();

    }

    private void displayMotionHeatmap() {
        String[] modes = MotionHeatmap.modes;
        String[] statOpts = MotionHeatmap.stats;

        int modeIdx = (int) Prefs.get("TrackAnalysis.heatmapModeIdx",0);
        int statIdx = (int) Prefs.get("TrackAnalysis.heatmapStatIdx",0);
        int binning = (int) Prefs.get("TrackAnalysis.heatmapBinning",1);
        boolean smoothBinning = Prefs.get("TrackAnalysis.heatmapSmoothBinning",true);

        GenericDialog gd = new GenericDialog("Settings");
        gd.addChoice("Metric to display",modes,modes[modeIdx]);
        gd.addChoice("Statistic",statOpts,statOpts[statIdx]);
        gd.addNumericField("Binning",binning,0);
        gd.addCheckbox("Smooth binning",smoothBinning);
        gd.showDialog();

        modeIdx = gd.getNextChoiceIndex();
        statIdx = gd.getNextChoiceIndex();
        binning = (int) gd.getNextNumber();
        smoothBinning = gd.getNextBoolean();

        Prefs.set("TrackAnalysis.heatmapModeIdx",modeIdx);
        Prefs.set("TrackAnalysis.heatmapStatIdx",statIdx);
        Prefs.set("TrackAnalysis.heatmapBinning",binning);
        Prefs.set("TrackAnalysis.heatmapSmoothBinning",smoothBinning);

        MotionHeatmap motionHeatmap = new MotionHeatmap(tracks);
        motionHeatmap.setBinning(binning);
        motionHeatmap.setSmoothBinning(smoothBinning);
        motionHeatmap.calculate(modes[modeIdx],statOpts[statIdx]);

    }
}
