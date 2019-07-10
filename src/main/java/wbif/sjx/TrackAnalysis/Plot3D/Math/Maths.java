package wbif.sjx.TrackAnalysis.Plot3D.Math;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Maths {

    private Maths() {
    }

    public static float max(float f0, float f1, float... fs) {
        float result = Math.max(f0, f1);
        for (float f : fs) {
            result = Math.max(result, f);
        }
        return result;
    }

    public static float scaleRange(float initRangeMin, float initRangeMax, float newRangeMin, float newRangeMax, float value) {
        if (value < initRangeMin) {
            return newRangeMin;
        } else if (value > initRangeMax) {
            return newRangeMax;
        } else {
            float initialRange = initRangeMax - initRangeMin;
            float newRange = newRangeMax - newRangeMin;
            return newRangeMin + ((value - initRangeMin) / initialRange) * newRange;
        }
    }
}
