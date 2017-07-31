package wbif.sjx.TrackAnalysis.Plot3D.Math;

import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Maths {
    private Maths(){}

    public static float floorMod(float x, float y){
        int quo = (int)(x/y);
        float rem = x - quo * y;
        return rem;
    }

    public static void normaliseRotationVector(Vector3f rotation){
        if(!(0 <= rotation.getX() && rotation.getX() < 360)) {
            rotation.setX(Maths.floorMod(rotation.getX(), 360));
        }
        if(!(0 <= rotation.getY() && rotation.getY() < 360)) {
            rotation.setY(Maths.floorMod(rotation.getY(), 360));
        }
        if(!(0 <= rotation.getZ() && rotation.getZ() < 360)) {
            rotation.setZ(Maths.floorMod(rotation.getZ(), 360));
        }
    }
}
