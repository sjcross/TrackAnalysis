package io.github.sjcross.trackanalysis.Plot3D.Utils;

import java.awt.Color;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import io.github.sjcross.common.object.Point;
import io.github.sjcross.common.object.tracks.Timepoint;
import io.github.sjcross.trackanalysis.Plot3D.Math.Matrix4f;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector2f;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector3f;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector3i;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector4f;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class DataUtils {

    private DataUtils() {
    }

    public static void putVector2f(FloatBuffer floatBuffer, Vector2f vector2f) {
        floatBuffer.put(vector2f.getX());
        floatBuffer.put(vector2f.getY());
    }

    public static void putVector3f(FloatBuffer floatBuffer, Vector3f vector3f) {
        floatBuffer.put(vector3f.getX());
        floatBuffer.put(vector3f.getY());
        floatBuffer.put(vector3f.getZ());
    }

    public static void putMatrix4f(FloatBuffer floatBuffer, Matrix4f matrix4f) {
        for (float f : toFloatArray(matrix4f.elements)) {
            floatBuffer.put(f);
        }
    }

    public static ByteBuffer toByteBuffer(byte[] array) {
        ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
        result.put(array).flip();
        return result;
    }

    public static byte[] toByteArray(ByteBuffer byteBuffer) {
        byte[] byteArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byteArray);
        return byteArray;
    }

    public static FloatBuffer toFloatBuffer(float[] array) {
        FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(array).flip();
        return result;
    }

    public static IntBuffer toIntBuffer(int[] array) {
        IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(array).flip();
        return result;
    }

    public static int[] toIntArray(IntBuffer intBuffer) {
        int[] intArray = new int[intBuffer.remaining()];
        intBuffer.get(intArray);
        return intArray;
    }

    public static float[] toFloatArray(float[][] array) {
        float[] result = new float[array.length * array[0].length];
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[0].length; col++) {
                result[row + col * array[0].length] = array[row][col];
            }
        }
        return result;
    }

    public static float[] toFloatArray(Vector4f[] array) {
        float[] result = new float[array.length * 4];
        for (int i = 0; i < array.length; i++) {
            result[i * 4] = array[i].getX();
            result[i * 4 + 1] = array[i].getY();
            result[i * 4 + 2] = array[i].getZ();
            result[i * 4 + 3] = array[i].getW();
        }
        return result;
    }

    public static float[] toFloatArray(Vector3f[] array) {
        float[] result = new float[array.length * 3];
        for (int i = 0; i < array.length; i++) {
            result[i * 3] = array[i].getX();
            result[i * 3 + 1] = array[i].getY();
            result[i * 3 + 2] = array[i].getZ();
        }
        return result;
    }

    public static float[] toFloatArray(Vector2f[] array) {
        float[] result = new float[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            result[i * 2] = array[i].getX();
            result[i * 2 + 1] = array[i].getY();
        }
        return result;
    }

    public static int[] toIntArray(Vector3i[] array) {
        int[] result = new int[array.length * 3];
        for (int i = 0; i < array.length; i++) {
            result[i * 3] = array[i].getX();
            result[i * 3 + 1] = array[i].getY();
            result[i * 3 + 2] = array[i].getZ();
        }
        return result;
    }

    public static char[] toCharArray(String string) {
        char[] result = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            result[i] = string.charAt(i);
        }
        return result;
    }

    public static Vector3f[] flipArray(Vector3f[] vecs) {
        int length = vecs.length;
        Vector3f[] result = new Vector3f[length];

        for (int i = 0; i < length; i++) {
            result[i] = vecs[length - i - 1];
        }

        return result;
    }

    public static Vector3f toVector3f(Color colour) {
        float[] rgb = new float[3];
        colour.getColorComponents(rgb);
        return new Vector3f(rgb[0], rgb[1], rgb[2]);
    }

    public static Vector4f toVector4f(Color colour) {
        float[] rgba = new float[4];
        colour.getComponents(rgba);
        return new Vector4f(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public static Vector3f toVector3f(Point point) {
        if (point == null)
            return new Vector3f();

        return new Vector3f(point.getX().floatValue(), point.getZ().floatValue(), point.getY().floatValue());
    }

    public static String loadAsString(String fileName) throws Exception {
        String result;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream in = classloader.getResourceAsStream(fileName);
            Scanner scanner = new Scanner(in, "UTF-8");
            result = scanner.useDelimiter("\\A").next();
        }
        catch (Exception e) {
            return null;
        }
        return result;
    }
}
