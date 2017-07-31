package wbif.sjx.TrackAnalysis.Plot3D.Core;

import com.sun.org.apache.bcel.internal.generic.FLOAD;
import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by Jordan Fisher on 19/05/2017.
 */
public class Camera {
    private Vector3f position;
    private Vector3f rotation;
    private int MAX_VERTICAL_ROTATION;

    public Camera(int MAX_VERTICAL_ROTATION){
        this.MAX_VERTICAL_ROTATION = MAX_VERTICAL_ROTATION;
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.FOV = FOV_DEFAULT;
        this.viewDistanceNear = viewDistanceNear_DEFAULT;
        this.viewDistanceFar = viewDistanceFar_DEFAULT;
        this.cameraPositionStep = cameraPositionStep_DEFAULT;
        this.mouseSensitivity = mouseSensitivity_DEFAULT;
    }

    public Matrix4f getCameraMatrix(){
        Matrix4f result = Matrix4f.identity();
        result.apply(Matrix4f.rotation(rotation));
        result.apply(Matrix4f.translation(Vector3f.Negative(position)));
        return result;
    }

    public void returnToOrigin(){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getPosition(){
        return position;
    }

    public void changePositionRelativeToOrientation(Vector3f deltaPosition){
        changePositionRelativeToOrientation(deltaPosition.getX(), deltaPosition.getY(), deltaPosition.getZ());
    }

    public void changePositionRelativeToOrientation(float dx, float dy, float dz) {
        if(dx != 0){
            position.changeX(-dx * (float) FastMath.sin(FastMath.toRadians(rotation.getY() - 90)));
            position.changeZ(dx * (float) FastMath.cos(FastMath.toRadians(rotation.getY() - 90)));
        }
        if(dy != 0){
            position.changeY(dy);
        }
        if(dz != 0){
            position.changeX(-dz * (float) FastMath.sin(FastMath.toRadians(rotation.getY())));
            position.changeZ(dz * (float) FastMath.cos(FastMath.toRadians(rotation.getY())));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getRotation(){
        return rotation;
    }

    public void changeRotation(Vector3f deltaRotation){
        changeRotation(deltaRotation.getX(), deltaRotation.getY(), deltaRotation.getZ());
    }

    public void changeRotation(float dx, float dy, float dz) {
        rotation.change(dx, dy, dz);
        Maths.normaliseRotationVector(rotation);
        limitVerticalRotation();
    }

    public void setRotation(Vector3f rotation) {
        setRotation(rotation.getX(), rotation.getY(), rotation.getZ());
    }

    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
        Maths.normaliseRotationVector(rotation);
        limitVerticalRotation();
    }

    public void limitVerticalRotation(){
        if(rotation.getX() > MAX_VERTICAL_ROTATION){
            rotation.setX(MAX_VERTICAL_ROTATION);
        }else if(rotation.getX() < -MAX_VERTICAL_ROTATION){
            rotation.setX(-MAX_VERTICAL_ROTATION);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int FOV;
    private final static int FOV_DEFAULT = 70;
    public final static int FOV_MINIMUM = 20;
    public final static int FOV_MAXIMUM = 160;

    public void setFOV(int value){
        if(value < FOV_MINIMUM){
            FOV = FOV_MINIMUM;
        }else if(value > FOV_MAXIMUM){
            FOV = FOV_MAXIMUM;
        }else {
            FOV = value;
        }
    }

    public void changeFOV(int value){
        setFOV(getFOV() + value);
    }

    public int getFOV(){
        return FOV;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float viewDistanceNear;
    private final static float viewDistanceNear_DEFAULT = 0.01f;
    private final static float viewDistanceNear_MINIMUM = 0.001f;
    private final static float viewDistanceNear_MAXIMUM = 10f;

    public void setViewDistanceNear(float value){
        if(value < viewDistanceNear_MINIMUM){
            viewDistanceNear = viewDistanceNear_MINIMUM;
        }else if(value > viewDistanceNear_MAXIMUM){
            viewDistanceNear = viewDistanceNear_MAXIMUM;
        }else {
            viewDistanceNear = value;
        }
    }

    public void changeViewDistanceNear(float value){
        setViewDistanceNear(getViewDistanceNear() + value);
    }

    public float getViewDistanceNear(){
        return viewDistanceNear;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int viewDistanceFar;
    private final static int viewDistanceFar_DEFAULT = 5000;
    public final static int viewDistanceFar_MINIMUM = 200;
    public final static int viewDistanceFar_MAXIMUM = 10200;

    public void setViewDistanceFar(int value){
        if(value < viewDistanceFar_MINIMUM){
            viewDistanceFar = viewDistanceFar_MINIMUM;
        }else if(value > viewDistanceFar_MAXIMUM){
            viewDistanceFar = viewDistanceFar_MAXIMUM;
        }else {
            viewDistanceFar = value;
        }
    }

    public void changeViewDistanceFar(int value){
        setViewDistanceFar(getViewDistanceFar() + value);
    }

    public int getViewDistanceFar(){
        return viewDistanceFar;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float cameraPositionStep;
    private final static float cameraPositionStep_DEFAULT = 0.3f;
    private final static float cameraPositionStep_MINIMUM = 0.01f;
    private final static float cameraPositionStep_MAXIMUM = 10f;

    public void setCameraPositionStep(float value){
        if(value < cameraPositionStep_MINIMUM){
            cameraPositionStep = cameraPositionStep_MINIMUM;
        }else if(value > viewDistanceNear_MAXIMUM){
            cameraPositionStep = cameraPositionStep_MAXIMUM;
        }else {
            cameraPositionStep = value;
        }
    }

    public void changeCameraPositionStep(float value){
        setCameraPositionStep(getCameraPositionStep() + value);
    }

    public float getCameraPositionStep(){
        return cameraPositionStep;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float mouseSensitivity;
    private final static float mouseSensitivity_DEFAULT = 0.3f;
    private final static float mouseSensitivity_MINIMUM = 0.01f;
    private final static float mouseSensitivity_MAXIMUM = 5f;

    public void setMouseSensitivity(float value){
        if(value < mouseSensitivity_MINIMUM){
            mouseSensitivity = mouseSensitivity_MINIMUM;
        }else if(value > mouseSensitivity_MAXIMUM){
            mouseSensitivity = mouseSensitivity_MAXIMUM;
        }else {
            mouseSensitivity = value;
        }
    }

    public void changeMouseSensitivity(float value){
        setMouseSensitivity(getMouseSensitivity() + value);
    }

    public float getMouseSensitivity(){
        return mouseSensitivity;
    }
}
