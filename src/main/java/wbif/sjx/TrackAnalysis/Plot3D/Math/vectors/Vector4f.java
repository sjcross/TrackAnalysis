package wbif.sjx.TrackAnalysis.Plot3D.Math.vectors;

import org.apache.commons.math3.util.FastMath;

/**
 * Created by Jordan Fisher on 02/07/2017.
 */
public class Vector4f {
    public float x, y, z, w;

    public Vector4f() {
        x = 0;
        y = 0;
        z = 0;
        w = 0;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector3f vec, float w) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
        this.w = w;
    }

    public void set(Vector4f vec){
        this.set(vec.x, vec.y, vec.z, vec.w);
    }

    public void set(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void add(Vector4f vec){
        this.add(vec.x, vec.y, vec.z, vec.w);
    }

    public void add(float x, float y, float z, float w){
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
    }

    public void subtract(Vector4f vec){
        this.subtract(vec.x, vec.y, vec.z, vec.w);
    }

    public void subtract(float x, float y, float z, float w){
        this.add(-x, -y, -z, -w);
    }

    public void scale(float scaler){
        this.x *= scaler;
        this.y *= scaler;
        this.z *= scaler;
        this.w *= scaler;
    }

    public float getLength(){
        return (float) FastMath.sqrt(FastMath.pow(this.x, 2) + FastMath.pow(this.y, 2) + FastMath.pow(this.z, 2) + FastMath.pow(this.w, 2));
    }

    public void normalize(){
        scale(1/getLength());
    }

    public void normalize3(){
        scale(1f/(float) FastMath.sqrt(FastMath.pow(x, 2) + FastMath.pow(y, 2) + FastMath.pow(z, 2)));
    }

    @Override
    public String toString() {
        return String.format("Vec4f: [ %f, %f, %f, %f ]",x ,y, z, w);

    }
}
