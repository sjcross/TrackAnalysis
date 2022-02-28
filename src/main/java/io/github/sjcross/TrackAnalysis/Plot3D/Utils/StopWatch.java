package io.github.sjcross.TrackAnalysis.Plot3D.Utils;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class StopWatch {
    private double startTime;
    private double endTime;
    private boolean running;

    public StopWatch() {
        running = false;
    }

    public void start() {
        running = true;
        endTime = 0;
        startTime = getTime();
    }

    public void stop() {
        endTime = getTime();
        running = false;
    }

    public void restart() {
        start();
    }

    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    public double getElapsedTime() {
        return (running ? getTime() : endTime) - startTime;
    }

    public double getStartTime() {
        return startTime;
    }
}