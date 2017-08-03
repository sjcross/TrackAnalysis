package wbif.sjx.TrackAnalysis.Plot3D.Math;



import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import java.nio.FloatBuffer;

public class Matrix4f {

    public static final int order = 4;
    public float[][] elements;

    public Matrix4f() {
        elements = new float[order][order];
    }

    public void apply(Matrix4f M){
        elements = multiply(M, this).elements;
    }

    public static Matrix4f identity() {
        Matrix4f I = new Matrix4f();
        for (int row = 0; row < order; row++) {
            for (int col = 0; col < order; col++) {
                if (row == col) {
                    I.elements[row][col] = 1f;
                } else {
                    I.elements[row][col] = 0f;
                }
            }
        }
        return I;
    }

    public static Matrix4f multiply(Matrix4f A, Matrix4f B) {
        Matrix4f result = new Matrix4f();
        for (int row = 0; row < order; row++) {
            for (int col = 0; col < order; col++) {
                float sum = 0;
                for (int i = 0; i < order; i++) {
                    sum += (A.elements[i][col] * B.elements[row][i]);
                }
                result.elements[row][col] = sum;
            }
        }
        return result;
    }

    public static Vector4f multiply(Matrix4f M, Vector4f V) {
        Vector4f result;
        Float[] component = new Float[order];
        for (int row = 0; row < order; row++) {
            component[row] = M.elements[row][0] * V.getX() +  M.elements[row][1] * V.getY() +  M.elements[row][2] * V.getZ() +  M.elements[row][3] * V.getW();
        }
        result = new Vector4f(
                component[0],
                component[1],
                component[2],
                component[3]
        );
        return result;
    }

    public static Vector4f[] multiply(Matrix4f M, Vector4f[] Vs) {
        Vector4f[] result = new Vector4f[Vs.length];
        for(int i = 0; i < result.length; i++){
            result[i] = multiply(M, Vs[i]);
        }
        return result;
    }

    public static Vector3f multiply(Matrix4f M, Vector3f V) {
        Vector3f result;
        Float[] component = new Float[order];
        for (int row = 0; row < order; row++) {
            component[row] = M.elements[row][0] * V.getX() +  M.elements[row][1] * V.getY() +  M.elements[row][2] * V.getZ() +  M.elements[row][3] * 1;
        }
        result = new Vector3f(
                component[0],
                component[1],
                component[2]
        );
        return result;
    }

    public static Vector3f[] multiply(Matrix4f M, Vector3f[] Vs) {
        Vector3f[] result = new Vector3f[Vs.length];
        for(int i = 0; i < result.length; i++){
            result[i] = multiply(M, Vs[i]);
        }
        return result;
    }

    public static Matrix4f projection(float FOV, float aspectRatio, float zNear, float zFar){
        Matrix4f result = new Matrix4f();
        float fov = (float) FastMath.toRadians(FOV);
        float a = aspectRatio;
        float zm = zFar - zNear;
        float zp = zFar + zNear;
        result.elements[0][0] = (float) ((1/ FastMath.tan(fov/2))/a);
        result.elements[1][1] = (float) (1/ FastMath.tan(fov/2));
        result.elements[2][2] = (-zp/zm);
        result.elements[2][3] = (-(2f*zFar*zNear)/zm);
        result.elements[3][2] = -1f;
        return result;
    }

    public static Matrix4f orthographic(int left, int right, int top, int bottom){
        Matrix4f result = identity();
        result.elements[0][0] = 2f/(right - left);
        result.elements[0][3] = right == left ? 0 : (-(right + left)) / (right - left);
        result.elements[1][1] = 2f/(top - bottom);
        result.elements[1][3] = top == bottom ? 0 : (-(top + bottom)) / (top - bottom);
        result.elements[2][2] = -1f;
        return result;
    }

    public static Matrix4f translation(Vector3f translation) {
        return translation(translation.getX(), translation.getY(), translation.getZ());
    }

    public static Matrix4f translation(float x, float y, float z) {
        Matrix4f result = identity();
        result.elements[0][order - 1] = x;
        result.elements[1][order - 1] = y;
        result.elements[2][order - 1] = z;
        return result;
    }

    public static Matrix4f rotation(Vector3f rotation){
        return rotation(rotation.getX(), rotation.getY(), rotation.getZ());
    }

    public static Matrix4f rotation(float x, float y, float z){
        Matrix4f result = identity();
        if(x != 0) {
            result.apply(xRotation(x));
        }
        if(y != 0) {
            result.apply(yRotation(y));
        }
        if(z != 0) {
            result.apply(zRotation(z));
        }
        return result;
    }

    public static Matrix4f xRotation(float angleX) {
        Matrix4f result = identity();
        double theta = FastMath.toRadians(angleX);
        result.elements[1][1] = (float) FastMath.cos(theta);
        result.elements[1][2] = (float) -FastMath.sin(theta);
        result.elements[2][1] = (float) FastMath.sin(theta);
        result.elements[2][2] = (float) FastMath.cos(theta);
        return result;
    }

    public static Matrix4f yRotation(float angleY) {
        Matrix4f result = identity();
        double theta = FastMath.toRadians(angleY);
        result.elements[0][0] = (float) FastMath.cos(theta);
        result.elements[0][2] = (float) FastMath.sin(theta);
        result.elements[2][0] = (float) -FastMath.sin(theta);
        result.elements[2][2] = (float) FastMath.cos(theta);
        return result;
    }

    public static Matrix4f zRotation(float angleZ) {
        Matrix4f result = identity();
        double theta = FastMath.toRadians(angleZ);
        result.elements[0][0] = (float) FastMath.cos(theta);
        result.elements[0][1] = (float) -FastMath.sin(theta);
        result.elements[1][0] = (float) FastMath.sin(theta);
        result.elements[1][1] = (float) FastMath.cos(theta);
        return result;
    }

    public static Matrix4f enlargement(float sf) {
        Matrix4f result = identity();
        for (int i = 0; i < 3; i++) {
            result.elements[i][i] = sf;
        }
        return result;
    }

    public static Matrix4f stretchX(float sf) {
        Matrix4f result = identity();
        result.elements[0][0] = sf;
        return result;
    }

    public static Matrix4f stretchY(float sf) {
        Matrix4f result = identity();
        result.elements[1][1] = sf;
        return result;
    }

    public static Matrix4f stretchZ(float sf) {
        Matrix4f result = identity();
        result.elements[2][2] = sf;
        return result;
    }

    public static Matrix4f stretch(float xSf, float ySf, float zSf) {
        Matrix4f result = identity();
        result.apply(stretchX(xSf));
        result.apply(stretchY(ySf));
        result.apply(stretchZ(zSf));
        return result;
    }

    public static Matrix4f stretch(Vector3f vec) {
        return stretch(vec.getX(), vec.getY(), vec.getZ());
    }





    @Override
    public String toString() {

        String result = "";

        final int spacing = 5;
        int longestelement = 1;

        for (int row = 0; row < order; row++) {
            for (int col = 0; col < order; col++) {
                if (Float.toString(elements[row][col]).length() > longestelement) {
                    longestelement = Float.toString(elements[row][col]).length();
                }
            }
        }

        for (int row = 0; row < order; row++) {
            result += "\n|";
            for (int i = 0; i < spacing; i++) {
                result += " ";
            }

            for (int col = 0; col < order; col++) {
                result += elements[row][col];
                for (int i = Float.toString(elements[row][col]).length(); i < longestelement; i++) {
                    result += " ";
                }

                for (int i = 0; i < spacing; i++) {
                    result += " ";
                }
            }
            result += "|";
        }

        result += "\n";

        return result;
    }

    public FloatBuffer toFloatBuffer() {
        return DataTypeUtils.toFloatBuffer(DataTypeUtils.toFloatArray(elements));
    }
}
