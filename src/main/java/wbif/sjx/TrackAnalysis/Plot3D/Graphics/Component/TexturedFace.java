package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by sc13967 on 04/08/2017.
 */
public class TexturedFace extends Face{
    private Vector2f tA, tB, tC;

    public TexturedFace(
            Vector3f vA, Vector2f tA,
            Vector3f vB, Vector2f tB,
            Vector3f vC, Vector2f tC
    ){
        super(vA, vB, vC);
        this.tA = tA;
        this.tB = tB;
        this.tC = tC;
    }

    public Vector2f gettA() {
        return tA;
    }

    public Vector2f gettB() {
        return tB;
    }

    public Vector2f gettC() {
        return tC;
    }
}
