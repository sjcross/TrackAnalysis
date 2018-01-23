package wbif.sjx.TrackAnalysis.Plot3D.Core;

import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.BoundingBox;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

public class Camera {
    public final static float VIEW_DISTANCE_NEAR = 0.01f;
    public final static int VIEW_DISTANCE_FAR = 100000;
    private double angularVelocityDegreesPerSec = 45 ; // 360 per sec for a revolution per sec


    public Camera(){
        this.position = new Vector3f();
        this.movementDirection = new Vector3f();
        this.tilt = 0;
        this.pan = 0;
        this.FOV = FOV_DEFAULT;
        this.cameraMovementSpeed = cameraMovementSpeed_DEFAULT;
        this.mouseSensitivity = mouseSensitivity_DEFAULT;
        this.faceCentre = faceCentre_DEFAULT;
//        this.orbitVelocity = orbitVelocity_DEFAULT;
    }

    public Matrix4f getViewMatrix(){
        Matrix4f result = Matrix4f.EulerRotation(-tilt, pan, 0);
        result.multiply(Matrix4f.Translation(Vector3f.Negative(position)));
        return result;
    }

    public void update(float interval, Vector3f centrePosition){
        changePositionRelativeToOrientation(Vector3f.Multiply(movementDirection, cameraMovementSpeed * interval));

        //Handles camera EulerRotation
        if (faceCentre) {
            Vector3f cameraToCentrePosition = Vector3f.Subtract(centrePosition, position);
            float cameraToCentrePositionDistance = cameraToCentrePosition.getLength();

            faceDirection(cameraToCentrePosition);
            double angularVelocityRadiansPerSec = FastMath.toRadians(angularVelocityDegreesPerSec);
            changePositionXRelativeToOrientation((float) angularVelocityRadiansPerSec * cameraToCentrePositionDistance * interval);

        }
    }

    public void returnToOrigin(){
        position = new Vector3f();
        tilt = 0;
        pan = 0;
    }

    public void facePoint(float x, float y, float z){
        facePoint(new Vector3f(x, y, z));
    }

    public void facePoint(Vector3f pointPositionVector){
        faceDirection(Vector3f.Subtract(pointPositionVector, position));
    }

    public void faceDirection(Vector3f direction){
        direction.normalize();
        pan = 90 + direction.getTheta();
        tilt = 90 - direction.getPhi();
    }

    @Override
    public String toString(){
        return String.format("Position  %s\nTilt: %f   Pan: %f\n", getPosition(), getTilt(), getPan());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float calcOptimalBoundingBoxViewingDistance(float sideLength){
        return (float)((sideLength / 2) / FastMath.tan(Math.toRadians(FOV / 2)));
    }

    public void viewXZplane(BoundingBox boundingBox){
        setTilt(-90);
        setPan(0);

        position.set(boundingBox.getCentrePosition());

        float width = calcOptimalBoundingBoxViewingDistance(boundingBox.getWidth());
        float length = calcOptimalBoundingBoxViewingDistance(boundingBox.getLength());
        float height = calcOptimalBoundingBoxViewingDistance(boundingBox.getHeight());

        float distance = FastMath.max(width, length);

        position.addY(height + distance);
    }

    public void viewYZplane(BoundingBox boundingBox){
        setTilt(0);
        setPan(-90);

        position.set(boundingBox.getCentrePosition());

        float width = calcOptimalBoundingBoxViewingDistance(boundingBox.getWidth());
        float length = calcOptimalBoundingBoxViewingDistance(boundingBox.getLength());
        float height = calcOptimalBoundingBoxViewingDistance(boundingBox.getHeight());

        float distance = FastMath.max(length, height);

        position.addX(width + distance);
    }

    public void viewXYplane(BoundingBox boundingBox){
        setTilt(0);
        setPan(0);

        position.set(boundingBox.getCentrePosition());

        float width = calcOptimalBoundingBoxViewingDistance(boundingBox.getWidth());
        float length = calcOptimalBoundingBoxViewingDistance(boundingBox.getLength());
        float height = calcOptimalBoundingBoxViewingDistance(boundingBox.getHeight());

        float distance = FastMath.max(width, height);

        position.addZ(length + distance);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Vector3f position;

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

    public void changePositionXRelativeToOrientation(float dx){
        position.addX(-dx * (float)FastMath.sin(FastMath.toRadians(pan - 90)));
        position.addZ(dx * (float) FastMath.cos(FastMath.toRadians(pan - 90)));
    }

    public void changePositionYRelativeToOrientation(float dy){
        position.addY(dy);
    }

    public void changePositionZRelativeToOrientation(float dz){
        position.addX(-dz * (float)FastMath.sin(FastMath.toRadians(pan)));
        position.addZ(dz * (float)FastMath.cos(FastMath.toRadians(pan)));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int MAX_TILT = 90;

    private float tilt; //Pitch

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

    public void changeTilt(float deltaTilt){
        setTilt(getTilt() + deltaTilt);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float pan; //Yaw

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

    private float cameraMovementSpeed;
    private final static float cameraMovementSpeed_DEFAULT = 500f;
    private final static float cameraMovementSpeed_MINIMUM = 10f;
    private final static float cameraMovementSpeed_MAXIMUM = 500f;

    public void setCameraMovementSpeed(float value){
        if(value < cameraMovementSpeed_MINIMUM){
            cameraMovementSpeed = cameraMovementSpeed_MINIMUM;
        }else if(value > cameraMovementSpeed_MAXIMUM){
            cameraMovementSpeed = cameraMovementSpeed_MAXIMUM;
        }else {
            cameraMovementSpeed = value;
        }
    }

    public void changeCameraMovementSpeed(float value){
        setCameraMovementSpeed(getCameraMovementSpeed() + value);
    }

    public float getCameraMovementSpeed(){
        return cameraMovementSpeed;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Vector3f movementDirection;

    public Vector3f getMovementDirection() {
        return movementDirection;
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

//    private int orbitVelocity;
//    public final static int orbitVelocity_DEFAULT = 0;
//    public final static int orbitVelocity_MAXIMUM = 100;
//    public final static int orbitVelocity_MINIMUM = -orbitVelocity_MAXIMUM;
//
//    public void setOrbitVelocity(int value){
//        if(value < orbitVelocity_MINIMUM){
//            orbitVelocity = orbitVelocity_MINIMUM;
//        }else if(value > orbitVelocity_MAXIMUM){
//            orbitVelocity = orbitVelocity_MAXIMUM;
//        }else {
//            orbitVelocity = value;
//        }
//    }
//
//    public void changeOrbitVelocity(int value){
//        setOrbitVelocity(getOrbitVelocity() + value);
//    }
//
//    public int getOrbitVelocity(){
//        return orbitVelocity;
//    }

    public final static int angularVelocityDegreesPerSec_DEFAULT = 90;

    public double getAngularVelocityDegreesPerSec() {
        return angularVelocityDegreesPerSec;
    }

    public void setAngularVelocityDegreesPerSec(double angularVelocityDegreesPerSec) {
        this.angularVelocityDegreesPerSec = angularVelocityDegreesPerSec;
    }
}
