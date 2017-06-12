package wbif.sjx.TrackAnalysis;

import ij.gui.Plot;

import java.util.ArrayList;

/**
 * Created by sc13967 on 12/06/2017.
 */
public class TrackAnalysis {
    private ArrayList<Track> tracks = new ArrayList<Track>();

    public TrackAnalysis(ArrayList<Track> tracks) {
        this.tracks = tracks;

        Track track = tracks.get(0);
        int[] f = track.getF();
        double[] y = track.getRollingTotalPathLength();

        float[] ff = new float[f.length];
        float[] yy = new float[y.length];
        for (int i=0;i<ff.length;i++) {
            ff[i] = (float) f[i];
            yy[i] = (float) y[i];

        }

        Plot plot = new Plot("Plot","x","y",ff,yy);
        plot.show();

    }
}
