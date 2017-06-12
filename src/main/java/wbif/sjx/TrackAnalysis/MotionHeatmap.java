package wbif.sjx.TrackAnalysis;

import ij.IJ;
import ij.ImagePlus;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;

import java.util.ArrayList;

/**
 * Created by sc13967 on 12/06/2017.
 */
public class MotionHeatmap {
    public static ImagePlus calculate(ArrayList<Track> tracks) {
        double[][] limits = new double[4][2];
        for (double[] row:limits) {
            row[0] = Double.MAX_VALUE;
            row[1] = Double.MIN_VALUE;

        }

        for (Track track:tracks) {
            double[][] currLimits = track.getLimits();

            if (currLimits[0][0] < limits[0][0]) limits[0][0] = currLimits[0][0];
            if (currLimits[0][1] > limits[0][1]) limits[0][1] = currLimits[0][1];
            if (currLimits[1][0] < limits[1][0]) limits[1][0] = currLimits[1][0];
            if (currLimits[1][1] > limits[1][1]) limits[1][1] = currLimits[1][1];
//            if (currLimits[2][0] < limits[2][0]) limits[2][0] = currLimits[2][0];
//            if (currLimits[2][1] > limits[2][1]) limits[2][1] = currLimits[2][1];
//            if (currLimits[3][0] < limits[3][0]) limits[3][0] = currLimits[3][0];
//            if (currLimits[3][1] > limits[3][1]) limits[3][1] = currLimits[3][1];

        }

        return calculate(tracks,(int) limits[0][1],(int) limits[1][1]);

    }

    public static ImagePlus calculate(ArrayList<Track> tracks, int width, int height) {
        // Creating new ImagePlus to display heatmap
        ImagePlus heatmap = IJ.createImage("Motion heatmap",width,height,1,32);

        // Average values are calculated using CumStat and 2D indexer
        Indexer indexer = new Indexer(width,height);
        CumStat cs = new CumStat(width*height);

        for (Track track:tracks) {
            for (int i=0;i<track.getX().length-1;i++) {
                // Getting step start locations
                double x1 = track.getX()[i];
                double y1 = track.getY()[i];

                // Getting step end locations
                double x2 = track.getX()[i+1];
                double y2 = track.getY()[i+1];

                // Getting start location index

            }
        }

        return null;

    }
}
