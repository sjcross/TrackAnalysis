package wbif.sjx.TrackAnalysis.Plot3D.Core;

import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.util.LinkedHashMap;

/**
 * Created by Jordan Fisher on 19/05/2017.
 */
public class Camera {
    public final static float VIEW_DISTANCE_NEAR = 0.01f;
    public final static int VIEW_DISTANCE_FAR = 100000;
    private static final int MAX_TILT = 90;

    private Vector3f position;
    private float tilt;
    private float pan;

    public Camera(){
        this.position = new Vector3f(0,0,0);
        this.tilt = 0;
        this.pan = 0;
        this.FOV = FOV_DEFAULT;
        this.cameraPositionStep = cameraPositionStep_DEFAULT;
        this.mouseSensitivity = mouseSensitivity_DEFAULT;
        this.faceCentre = faceCentre_DEFAULT;
        this.orbitVelocity = orbitVelocity_DEFAULT;
    }

    public Matrix4f getCameraMatrix(){
        Matrix4f result = Matrix4f.identity();
        result.apply(Matrix4f.rotation(tilt, pan, 0));
        result.apply(Matrix4f.translation(-position.getX(), -position.getZ(), -position.getY()));
        return result;
    }

    public void update(Scene scene){
        //Handles camera rotation
        if (faceCentre) {
            facePoint(scene.getTracksEntities().getCurrentCentreOfCollection());
            changePositionXRelativeToOrientation(orbitVelocity);
        }
    }

    public void returnToOrigin(){
        position = new Vector3f(0,0,0);
        tilt = 0;
        pan = 0;
    }

    public void facePoint(float x, float y, float z){
        facePoint(new Vector3f(x, y, z));
    }

    public void facePoint(Vector3f pointVector){
        Vector3f vec = Vector3f.Subtract(pointVector, position);
        tilt = vec.getPhi() - 90;
        pan = 90 + vec.getTheta();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public float calcOptimalBoundingBoxViewingDistance(float sideLength){
        return (float)((sideLength / 2) / FastMath.tan(FOV / 2));
    }

    public void viewXZplane(Entity boundingBox){
        setTilt(0);
        setPan(0);

        position.set(boundingBox.getPosition());

        float width = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getX());
        float length = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getY());
        float height = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getZ());

        float distance = FastMath.max(width, height);

        position.changeY(length / 2 + distance);
    }

    public void viewYZplane(Entity boundingBox){
        setTilt(0);
        setPan(-90);

        position.set(boundingBox.getPosition());

        float width = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getX());
        float length = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getY());
        float height = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getZ());

        float distance = FastMath.max(length, height);

        position.changeX(width / 2 + distance);
    }

    public void viewXYplane(Entity boundingBox){
        setTilt(90);
        setPan(0);

        position.set(boundingBox.getPosition());

        float width = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getX());
        float length = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getY());
        float height = calcOptimalBoundingBoxViewingDistance(boundingBox.getScale().getZ());

        float distance = FastMath.max(width, length);

        position.changeZ(height / 2 + distance);
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
            changePositionXRelativeToOrientation(dx);
        }
        if(dy != 0){
            changePositionYRelativeToOrientation(dy);
        }
        if(dz != 0){
            changePositionZRelativeToOrientation(dz);
        }
    }

    public void changePositionXRelativeToOrientation(float dx) {
        position.changeX(-dx * (float) FastMath.sin(FastMath.toRadians(pan - 90)));
        position.changeY(dx * (float) FastMath.cos(FastMath.toRadians(pan - 90)));
    }

    public void changePositionYRelativeToOrientation(float dy) {
        position.changeX(-dy * (float) FastMath.sin(FastMath.toRadians(pan)));
        position.changeY(dy * (float) FastMath.cos(FastMath.toRadians(pan)));
    }

    public void changePositionZRelativeToOrientation(float dz) {
        position.changeZ(dz);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public float getTilt(){
        return tilt;
    }

    public void setTilt(float value){
        if(value > MAX_TILT){
            tilt = MAX_TILT;
        }else if(value < -MAX_TILT){
            tilt = -MAX_TILT;
        }else {
            tilt = value;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void changeTilt(float deltaTilt){
        setTilt(getTilt() + deltaTilt);
    }

    public float getPan(){
        return pan;
    }

    public void setPan(float value){
        pan = Maths.floorMod(value, 360f);
    }

    public void changePan(float deltaValue){
        setPan(getPan() + deltaValue);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int FOV;
    public final static int FOV_DEFAULT = 70;
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

    private float cameraPositionStep;
    private final static float cameraPositionStep_DEFAULT = 0.5f;
    private final static float cameraPositionStep_MINIMUM = 0.01f;
    private final static float cameraPositionStep_MAXIMUM = 10f;

    public void setCameraPositionStep(float value){
        if(value < cameraPositionStep_MINIMUM){
            cameraPositionStep = cameraPositionStep_MINIMUM;
        }else if(value > cameraPositionStep_MAXIMUM){
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
    public final static float mouseSensitivity_DEFAULT = 0.3f;
    public final static float mouseSensitivity_MINIMUM = 0.01f;
    public final static float mouseSensitivity_MAXIMUM = 5f;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean faceCentre;
    public static final boolean faceCentre_DEFAULT = false;

    public boolean isFacingCentre() {
        return faceCentre;
    }

    public void setFaceCentre(boolean state){
        faceCentre = state;
    }

    public void toggleFaceCentre(){
        faceCentre = ! faceCentre;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int orbitVelocity;
    public final static int orbitVelocity_DEFAULT = 0;
    public final static int orbitVelocity_MAXIMUM = 20;
    public final static int orbitVelocity_MINIMUM = -orbitVelocity_MAXIMUM;

    public void setOrbitVelocity(int value){
        if(value < orbitVelocity_MINIMUM){
            orbitVelocity = orbitVelocity_MINIMUM;
        }else if(value > orbitVelocity_MAXIMUM){
            orbitVelocity = orbitVelocity_MAXIMUM;
        }else {
            orbitVelocity = value;
        }
    }

    public void changeOrbitVelocity(int value){
        setOrbitVelocity(getOrbitVelocity() + value);
    }

    public int getOrbitVelocity(){
        return orbitVelocity;
    }
}
