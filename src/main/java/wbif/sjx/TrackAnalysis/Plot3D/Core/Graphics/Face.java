package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics;

import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by Jordan Fisher on 06/06/2017.
 */
public class Face {
    private Vector3f vA;
    private Vector3f vB;
    private Vector3f vC;
    private Vector2f tA;
    private Vector2f tB;
    private Vector2f tC;
    private Vector3f normal;
    private Vector3f unitNormal;
    private float area;
    private float height;
    private float volume;
    private boolean normalFacesOrigin;

    public Face(
            Vector3f vA, Vector2f tA,
            Vector3f vB, Vector2f tB,
            Vector3f vC, Vector2f tC
    ){
        this.vA = vA; this.tA = tA;
        this.vB = vB; this.tB = tB;
        this.vC = vC; this.tC = tC;
        calculateNormal();
        calculateArea();
        calculateShortestDistanceFromOriginToFace();
        calcVolumeBoundToOrigin();
    }

    private void calculateNormal(){
        Vector3f vec1 = Vector3f.Subtract(vB, vA);
        Vector3f vec2 = Vector3f.Subtract(vC, vA);
        normal =  Vector3f.CrossProduct(vec1, vec2);
        unitNormal = new Vector3f(normal);
        unitNormal.normalize();
        normalFacesOrigin = Vector3f.DotProduct(vA, unitNormal) > 0;
    }

    private void calculateArea(){
        area = normal.getLength()/2;
    }

    private void calculateShortestDistanceFromOriginToFace(){
        Vector3f vec = vA;
        height = FastMath.abs(Vector3f.DotProduct(vec, unitNormal));
    }

    private void calcVolumeBoundToOrigin(){
        volume = (height * area)/3;
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

    public Vector2f gettA() {
        return tA;
    }

    public Vector2f gettB() {
        return tB;
    }

    public Vector2f gettC() {
        return tC;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector3f getUnitNormal() {
        return unitNormal;
    }

    public float getArea() {
        return area;
    }

    public float getHeight() {
        return height;
    }

    public float getVolume() {
        return volume;
    }

    public boolean facesOrigin() {
        return normalFacesOrigin;
    }
}
