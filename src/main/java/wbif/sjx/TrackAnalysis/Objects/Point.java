package wbif.sjx.TrackAnalysis.Objects;

public class Point {
    private double x;
    private double y;
    private double z;
    private int f;

    public Point(double x, double y, double z, int f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getF() {
        return f;
    }

}