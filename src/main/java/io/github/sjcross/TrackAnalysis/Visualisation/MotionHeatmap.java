package io.github.sjcross.trackanalysis.Visualisation;
//package io.github.sjcross.TrackAnalysis.Visualisation;
//
//import ij.ImagePlus;
//import ij.process.FloatProcessor;
//import ij.process.ImageProcessor;
//import io.github.sjcross.common.MathFunc.CumStat;
//import io.github.sjcross.common.MathFunc.Indexer;
//import io.github.sjcross.common.Object.LUTs;
//import io.github.sjcross.common.object.tracks.Track;
//import io.github.sjcross.common.object.tracks.TrackCollection;
//import io.github.sjcross.common.Process.IntensityMinMax;
//import io.github.sjcross.common.Process.NanBinner;
//
//import java.util.HashMap;
//
///**
// * Created by sc13967 on 12/06/2017.
// */
//public class MotionHeatmap {
//    private static final String ABSOLUTE_VELOCITY = "Absolute velocity";
//    private static final String X_VELOCITY = "X-axis velocity";
//    private static final String Y_VELOCITY = "Y-axis velocity";
//    private static final String Z_VELOCITY = "Z-axis velocity";
//
//    private static final String MEAN = "Mean";
//    private static final String STDEV = "Standard deviation";
//    private static final String MIN = "Minimum";
//    private static final String MAX = "Maximum";
//    private static final String SUM = "Sum";
//    private static final String N_MEAS = "Number of measurements";
//
//    public static final String[] modes = new String[]{ABSOLUTE_VELOCITY,X_VELOCITY,Y_VELOCITY,Z_VELOCITY};
//    public static final String[] stats = new String[]{MEAN,STDEV,MIN,MAX,SUM,N_MEAS};
//
//    private TrackCollection tracks;
//    private int binning = 1;
//    private boolean smoothBinning = true;
//
//
//    // CONSTRUCTORS
//
//    public MotionHeatmap(TrackCollection tracks) {
//        this.tracks = tracks;
//
//    }
//
//
//    // PUBLIC METHODS
//
//    public void calculate(String mode, String stat) {
//        double[][] limits = new double[2][2];
//        for (double[] row:limits) {
//            row[0] = Double.MAX_VALUE;
//            row[1] = Double.MIN_VALUE;
//
//        }
//
//        for (Track track:tracks.values()) {
//            double[][] currLimits = track.getLimits();
//
//            if (currLimits[0][0] < limits[0][0]) limits[0][0] = currLimits[0][0];
//            if (currLimits[0][1] > limits[0][1]) limits[0][1] = currLimits[0][1];
//            if (currLimits[1][0] < limits[1][0]) limits[1][0] = currLimits[1][0];
//            if (currLimits[1][1] > limits[1][1]) limits[1][1] = currLimits[1][1];
//
//        }
//
//        calculate(mode,stat,(int) limits[0][1],(int) limits[1][1]);
//
//    }
//
//    public void calculate(String mode, String stat,int width, int height) {
//        // Creating new ImagePlus to display heatmap
//        ImageProcessor ipr = new FloatProcessor(width,height);
//
//        // Average values are calculated using CumStat and 2D indexer
//        HashMap<Integer,CumStat> measurements = new HashMap<>();
//        Indexer indexer = new Indexer(width,height);
//
//        switch (mode) {
//            case ABSOLUTE_VELOCITY:
//                getAbsoluteVelocity(measurements,indexer);
//                break;
//
//            case X_VELOCITY:
//                getXVelocity(measurements,indexer);
//                break;
//
//            case Y_VELOCITY:
//                getYVelocity(measurements,indexer);
//                break;
//
//            case Z_VELOCITY:
//                getZVelocity(measurements,indexer);
//                break;
//
//        }
//
//        // Assigning values to the heatmaps
//        for (int idx:measurements.keySet()) {
//            if (idx != -1) {
//                int[] coords = indexer.getCoord(idx);
//
//                switch (stat) {
//                    case MEAN:
//                        ipr.putPixelValue(coords[0], coords[1], measurements.get(idx).getMean());
//                        break;
//
//                    case STDEV:
//                        ipr.putPixelValue(coords[0], coords[1], measurements.get(idx).getStd());
//                        break;
//
//                    case MIN:
//                        ipr.putPixelValue(coords[0], coords[1], measurements.get(idx).getMin());
//                        break;
//
//                    case MAX:
//                        ipr.putPixelValue(coords[0], coords[1], measurements.get(idx).getMax());
//                        break;
//
//                    case SUM:
//                        ipr.putPixelValue(coords[0], coords[1], measurements.get(idx).getSum());
//                        break;
//
//                    case N_MEAS:
//                        ipr.putPixelValue(coords[0], coords[1], measurements.get(idx).getN());
//                        break;
//
//                }
//            }
//        }
//
//        // Setting any pixels without a measurement to zero
//        for (int x=0;x<width;x++) {
//            for (int y=0;y<width;y++) {
//                int idx = indexer.getIndex(new int[]{x,y});
//                if (!measurements.containsKey(idx)) ipr.putPixelValue(x, y,Double.NaN);
//
//            }
//        }
//
//        // Creating the image
//        ImagePlus ipl = new ImagePlus(mode+"_"+stat,ipr);
//
//        // Binning the image
//        if (binning != 1) {
//            ipl = NanBinner.shrink(ipl, binning, binning, 1, NanBinner.AVERAGE);
//
//            // Scaling the image back to it's original size
//            if (smoothBinning) {
//                ipl.getProcessor().setInterpolationMethod(ImageProcessor.BICUBIC);
//                ipl.setProcessor(ipl.getProcessor().resize(width, height));
//
//            }
//        }
//
//        // Displaying
//        ipl.setLut(LUTs.BlackFire());
//        IntensityMinMax.run(ipl,false);
//        ipl.show();
//
//    }
//
//
//    // PRIVATE METHODS
//
//    private void getAbsoluteVelocity(HashMap<Integer,CumStat> stats, Indexer indexer) {
//        double distXY = tracks.getDistXY();
//        double distZ = tracks.getDistZ();
//
//        for (Track track:tracks.values()) {
//            for (int i=0;i<track.getX().length-1;i++) {
//
//                // Getting step start locations
//                double x1 = track.getX()[i];
//                double y1 = track.getY()[i];
//                double z1 = track.getZ()[i];
//                double f1 = track.getF()[i];
//
//                // Getting step end locations
//                double x2 = track.getX()[i+1];
//                double y2 = track.getY()[i+1];
//                double z2 = track.getZ()[i+1];
//                double f2 = track.getF()[i+1];
//
//                // Getting start location index (the locations must be pixel coordinates - if not, they'll need
//                // converting).  Creating a CumStat object at this location if one doesn't already exist.
//                int idx = indexer.getIndex(new int[]{(int) x1,(int) y1});
//                stats.putIfAbsent(idx,new CumStat());
//
//                // Calculating the speed
//                double velocity = Math.sqrt(distXY*(x2-x1)*distXY*(x2-x1)+distXY*(y2-y1)*distXY*(y2-y1)+distZ*(z2-z1)*distZ*(z2-z1))/(f2-f1);
//                stats.get(idx).addMeasure(velocity);
//
//            }
//        }
//    }
//
//    private  void getXVelocity(HashMap<Integer,CumStat> stats, Indexer indexer) {
//        double distXY = tracks.getDistXY();
//        for (Track track:tracks.values()) {
//            for (int i=0;i<track.getX().length-1;i++) {
//                // Getting step start locations
//                double x1 = track.getX()[i];
//                double y1 = track.getY()[i];
//                double f1 = track.getF()[i];
//
//                // Getting step end locations
//                double x2 = track.getX()[i+1];
//                double f2 = track.getF()[i+1];
//
//                // Getting start location index (the locations must be pixel coordinates - if not, they'll need
//                // converting).  Creating a CumStat object at this location if one doesn't already exist.
//                int idx = indexer.getIndex(new int[]{(int) x1,(int) y1});
//                stats.putIfAbsent(idx,new CumStat());
//
//                // Calculating the x-velocity
//                double velocity = distXY*(x2-x1)/(f2-f1);
//                stats.get(idx).addMeasure(velocity);
//
//            }
//        }
//    }
//
//    private void getYVelocity(HashMap<Integer,CumStat> stats, Indexer indexer) {
//        double distXY = tracks.getDistXY();
//
//        for (Track track:tracks.values()) {
//            for (int i=0;i<track.getX().length-1;i++) {
//                // Getting step start locations
//                double x1 = track.getX()[i];
//                double y1 = track.getY()[i];
//                double f1 = track.getF()[i];
//
//                // Getting step end locations
//                double y2 = track.getY()[i+1];
//                double f2 = track.getF()[i+1];
//
//                // Getting start location index (the locations must be pixel coordinates - if not, they'll need
//                // converting).  Creating a CumStat object at this location if one doesn't already exist.
//                int idx = indexer.getIndex(new int[]{(int) x1,(int) y1});
//                stats.putIfAbsent(idx,new CumStat());
//
//                // Calculating the x-velocity
//                double velocity = distXY*(y2-y1)/(f2-f1);
//                stats.get(idx).addMeasure(velocity);
//
//            }
//        }
//    }
//
//    private void getZVelocity(HashMap<Integer,CumStat> stats, Indexer indexer) {
//        double distZ = tracks.getDistZ();
//
//        for (Track track:tracks.values()) {
//            for (int i=0;i<track.getX().length-1;i++) {
//                // Getting step start locations
//                double x1 = track.getX()[i];
//                double y1 = track.getY()[i];
//                double z1 = track.getZ()[i];
//                double f1 = track.getF()[i];
//
//                // Getting step end locations
//                double z2 = track.getZ()[i+1];
//                double f2 = track.getF()[i+1];
//
//                // Getting start location index (the locations must be pixel coordinates - if not, they'll need
//                // converting).  Creating a CumStat object at this location if one doesn't already exist.
//                int idx = indexer.getIndex(new int[]{(int) x1,(int) y1});
//                stats.putIfAbsent(idx,new CumStat());
//
//                // Calculating the x-velocity
//                double velocity = distZ*(z2-z1)/(f2-f1);
//                stats.get(idx).addMeasure(velocity);
//
//            }
//        }
//    }
//
//
//    // GETTERS AND SETTERS
//
//    public int getBinning() {
//        return binning;
//    }
//
//    public void setBinning(int binning) {
//        this.binning = binning;
//    }
//
//    public boolean isSmoothBinning() {
//        return smoothBinning;
//    }
//
//    public void setSmoothBinning(boolean smoothBinning) {
//        this.smoothBinning = smoothBinning;
//    }
//}
