package wbif.sjx.TrackAnalysis;

import ij.IJ;
import ij.ImageJ;
import ij.plugin.PlugIn;

import java.util.ArrayList;

/**
 * Loads tracks from results table.
 */
public class TrackAnalysisPlugin implements PlugIn {
    public static void main(String[] args) {
        new ImageJ();
        IJ.runMacro("waitForUser");

        new TrackAnalysisPlugin().run("");

    }

    public void run(String s) {


    }

    private void importTracksFromResults() {

    }
}
