package wbif.sjx.TrackAnalysis.Plot3D.Math;

import org.apache.commons.math3.util.FastMath;
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

    public static float max(float f0, float f1, float... fs) {
        float result = FastMath.max(f0, f1);
        for(float f : fs){
            result = FastMath.max(result, f);
        }
        return result;
    }

    public static Vector3f midpointBetweenVectorPositions(Vector3f vec1, Vector3f vec2){
        Vector3f result  = Vector3f.Add(vec1, vec2);
        result.multiply(0.5f);
        return result;
    }

    public static float interpolateRangeLinearly(float initialRangeMin, float initialRangeMax, float newRangeMin, float newRangeMax, float value){
        if(value < initialRangeMin){
            return newRangeMin;
        }else if(value > initialRangeMax){
            return newRangeMax;
        }else {
            float initialRange = initialRangeMax - initialRangeMin;
            float newRange = newRangeMax - newRangeMin;
            return newRangeMin + ((value - initialRangeMin)/ initialRange) * newRange;
        }
    }
}
