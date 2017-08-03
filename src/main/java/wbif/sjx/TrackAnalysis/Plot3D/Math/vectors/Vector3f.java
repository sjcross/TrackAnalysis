package wbif.sjx.TrackAnalysis.Plot3D.Math.vectors;

import org.apache.commons.math3.util.FastMath;

public class Vector3f {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(Vector3f vec){
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vector3f(float x, float y, float z) {
        set(x, y, z);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void set(Vector3f vec){
        this.set(vec.x, vec.y, vec.z);
    }

    public void set(float x, float y, float z){
        setX(x);
        setY(y);
        setZ(z);
    }

    public void change(Vector3f deltaVec){
        this.change(deltaVec.x, deltaVec.y, deltaVec.z);
    }

    public void change(float dx, float dy, float dz){
        changeX(dx);
        changeY(dy);
        changeZ(dz);
    }

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float x;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void changeX(float dx) {
		setX(getX() + dx);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float y;

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void changeY(float dy) {
		setY(getY() + dy);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float z;

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void changeZ(float dz) {
		setZ(getZ() + dz);
	}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public float getLength() {
		return (float) FastMath.sqrt(FastMath.pow(x, 2) + FastMath.pow(y, 2) + FastMath.pow(z, 2));
	}

    public float getTheta(){
        return (float)FastMath.toDegrees(FastMath.atan2(y, x));
    }

    public float getPhi(){
        float length = getLength();
        if(length != 0){
            return (float)FastMath.toDegrees(FastMath.acos(z / length));
        }else {
            return 0;
        }
    }

	public void scale(float scaler){
		setX(getX() * scaler);
		setY(getY() * scaler);
		setZ(getZ() * scaler);
	}

    public void normalize(){
        scale(1/getLength());
    }

    public float getLargestElement(){
        float result = 0;
        result = x > result ? x : result;
        result = y > result ? y : result;
        result = z > result ? z : result;
        return result;
    }

	@Override
	public String toString() {
		return String.format("Vec3f: [ %f, %f, %f ]", x, y, z);
	}

	///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

	public static Vector3f Add(Vector3f vec1, Vector3f vec2){
		return new Vector3f(
				vec1.x + vec2.x,
				vec1.y + vec2.y,
				vec1.z + vec2.z
		);
	}

	public static Vector3f Subtract(Vector3f vec1, Vector3f vec2){
		return new Vector3f(
				vec1.x - vec2.x,
				vec1.y - vec2.y,
				vec1.z - vec2.z
		);
	}

	public static Vector3f Scale(Vector3f vec1, float scaler){
		return new Vector3f(
				vec1.x * scaler,
				vec1.y * scaler,
				vec1.z * scaler
		);
	}

	public static float DotProduct(Vector3f vec1, Vector3f vec2){
		return (vec1.x * vec2.x) + (vec1.y * vec2.y) + (vec1.z * vec2.z);
	}

	public static Vector3f CrossProduct(Vector3f vec1, Vector3f vec2){
		return new Vector3f(
				vec1.y * vec2.z - vec1.z * vec2.y,
				vec1.z * vec2.x - vec1.x * vec2.z,
				vec1.x * vec2.y - vec1.y * vec2.x
		);
	}

	public static Vector3f Negative(Vector3f vec){
		return Scale(vec, -1);
	}

	public static float DistanceBetweenVectors(Vector3f vec1, Vector3f vec2){
		Vector3f deltaVec = Vector3f.Subtract(vec1, vec2);
		return deltaVec.getLength();
	}
}
