package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by Jordan Fisher on 06/06/2017.
 */
public class Face {
    private Vector3f vA;
    private Vector3f vB;
    private Vector3f vC;

    public Face(
            Vector3f vA,
            Vector3f vB,
            Vector3f vC
    ){
        this.vA = vA;
        this.vB = vB;
        this.vC = vC;
    }

    public Vector3f getvA() {
        return vA;
    }

    public Vector3f getvB() {
        return vB;
    }

    public Vector3f getvC() {
        return vC;
    }

}
