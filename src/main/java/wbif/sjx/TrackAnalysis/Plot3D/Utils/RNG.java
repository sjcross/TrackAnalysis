package wbif.sjx.TrackAnalysis.Plot3D.Utils;

import java.awt.*;
import java.util.Random;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class RNG {

    private static Random RNG = new Random();

    private RNG() {
    }

    public static int Int(int min, int max) {
        return RNG.nextInt(max - min + 1) + min;
    }

    public static double Double(double min, double max) {
        return RNG.nextDouble() * (max - min) + min;
    }

    public static float Float(float min, float max) {
        return RNG.nextFloat() * (max - min) + min;
    }

    public static boolean Boolean() {
        return RNG.nextBoolean();
    }

    public static Color Colour() {
        return Color.getHSBColor(Float(0, 1), 1f, 1f);
    }
}
