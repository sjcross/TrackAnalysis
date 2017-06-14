package wbif.sjx.TrackAnalysis.Objects;

import org.apache.commons.math3.stat.descriptive.summary.Sum;
import wbif.sjx.common.Analysis.*;
import wbif.sjx.common.MathFunc.MultiCumStat;

import java.util.ArrayList;

/**
 * Created by sc13967 on 03/02/2017.
 */
public class Track extends ArrayList<Point> {
    // PUBLIC METHODS

    public MultiCumStat getDirectionalPersistence() {
        return DirectionalPersistenceCalculator.calculate(getF(),getX(),getY(),getZ());

    }

    public MultiCumStat getMSD() {
        return MSDCalculator.calculate(getF(),getX(),getY(),getZ());

    }

    public double[] getInstantaneousVelocity() {
        return InstantaneousVelocityCalculator.calculate(getF(),getX(),getY(),getZ());

    }

    public double[] getStepSizes() {
        return StepSizeCalculator.calculate(getX(),getY(),getZ());

    }

    public double getEuclspotIDeanDistance() {
        double[] x = getX();
        double[] y = getX();
        double[] z = getX();

        double dx = x[x.length-1]-x[0];
        double dy = y[x.length-1]-y[0];
        double dz = z[x.length-1]-z[0];

        return Math.sqrt(dx * dx + dy * dy + dz * dz);

    }

    public double getTotalPathLength() {
        double[] steps = getStepSizes();

        return new Sum().evaluate(steps);

    }

    public double getDirectionalityRatio() {
        return getEuclspotIDeanDistance()/getTotalPathLength();

    }

    /**
     * Returns a double[] containing the EuclspotIDean distance at all time steps
     */
    public double[] getRollingEuclspotIDeanDistance() {
        return EuclideanDistanceCalculator.calculate(getX(),getY(),getZ());

    }

    /**
     * Returns a double[] containing the total path length up to each time step
     */
    public double[] getRollingTotalPathLength() {
        return TotalPathLengthCalculator.calculate(getX(),getY(),getZ());

    }

    /**
     * Returns a double[] containing the directionality ratio at all time steps
     */
    public double[] getRollingDirectionalityRatio() {
        return DirectionalityRatioCalculator.calculate(getX(),getY(),getZ());

    }

    public int getDuration() {
        int[] f = getF();

        return f[f.length-1]-f[0];

    }

    public double[][] getLimits(){
        double[] x = getX();
        double[] y = getX();
        double[] z = getX();
        int[] f = getF();

        double[][] limits = new double[4][2];
        for (double[] row:limits) {
            row[0] = Double.MAX_VALUE;
            row[1] = Double.MIN_VALUE;

        }

        for (int i=0;i<x.length;i++) {
            if (x[i] < limits[0][0]) limits[0][0] = x[i];
            if (x[i] > limits[0][1]) limits[0][1] = x[i];
            if (y[i] < limits[1][0]) limits[1][0] = y[i];
            if (y[i] > limits[1][1]) limits[1][1] = y[i];
            if (z[i] < limits[2][0]) limits[2][0] = z[i];
            if (z[i] > limits[2][1]) limits[2][1] = z[i];
            if (f[i] < limits[3][0]) limits[3][0] = f[i];
            if (f[i] > limits[3][1]) limits[3][1] = f[i];

        }

        return limits;

    }


    // GETTERS AND SETTERS

    public double[] getX() {
       return stream().map(Point::getX).mapToDouble(Double::doubleValue).toArray();

    }

    public double[] getY() {
        return stream().map(Point::getY).mapToDouble(Double::doubleValue).toArray();

    }

    public double[] getZ() {
        return stream().map(Point::getZ).mapToDouble(Double::doubleValue).toArray();

    }

    public int[] getF() {
        return stream().map(Point::getF).mapToInt(Integer::intValue).toArray();

    }

}