package wbif.sjx.TrackAnalysis.Objects;

import java.util.HashMap;

/**
 * Created by sc13967 on 13/06/2017.
 */
public class TrackCollection extends HashMap<Integer,Track> {
    private double distXY = 1;
    private double distZ = 1;


    // CONSTRUCTORS

    public TrackCollection(double distXY, double distZ) {
        this.distXY = distXY;
        this.distZ = distZ;

    }


    // GETTERS AND SETTERS

    public double getDistXY() {
        return distXY;
    }

    public void setDistXY(double distXY) {
        this.distXY = distXY;
    }

    public double getDistZ() {
        return distZ;
    }

    public void setDistZ(double distZ) {
        this.distZ = distZ;
    }

}
