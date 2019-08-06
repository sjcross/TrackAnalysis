package wbif.sjx.TrackAnalysis.Plot3D.Core;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.CollectionBounds;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Quaternion;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static wbif.sjx.TrackAnalysis.Plot3D.Core.Renderer.BIAS;
import static wbif.sjx.TrackAnalysis.Plot3D.Core.Scene.X_AXIS;
import static wbif.sjx.TrackAnalysis.Plot3D.Core.Scene.Y_AXIS;

public class Camera {

    public static final float VIEW_DISTANCE_NEAR = 0.01f;
    public static final int VIEW_DISTANCE_FAR = 100000;
    private static final int MAX_TILT = 90;
    public static final int angularVelocityDPS_DEF = 90;
    public static final int fov_DEF = 70;
    public static final int fov_MIN = 20;
    public static final int fov_MAX = 100;
    public static final float sensitivity_DEF = 0.3f;
    public static final float sensitivity_MIN = 0.01f;
    public static final float sensitivity_MAX = 5f;
    private static final float moveSpeed_DEF = 500f;
    private static final float moveSpeed_MIN = 10f;
    private static final float moveSpeed_MAX = 500f;

    private Vector3f position;
    private Vector3f moveDirection;
    private float tilt; //Pitch
    private float pan;  //Yaw
    private int fov;
    private float moveSpeed;
    private float sensitivity;
    private boolean orbit;
    private float angularVelocityRPS;
    private double angularVelocityDPS;
    private Quaternion rotation;
    private Matrix4f viewMatrix;

    public Camera() {
        this.position = new Vector3f();
        this.moveDirection = new Vector3f();
        this.tilt = 0;
        this.pan = 0;
        this.fov = fov_DEF;
        this.moveSpeed = moveSpeed_DEF;
        this.sensitivity = sensitivity_DEF;
        this.orbit = orbit_DEF;
        this.angularVelocityDPS = angularVelocityDPS_DEF;

        updateAngularVelocityRPS();
    }

    public void handleInput() {
        if (Keyboard.isKeyDown(GLFW_KEY_A)) { // move left
            moveDirection.setX(-moveSpeed);
        } else if (Keyboard.isKeyDown(GLFW_KEY_D)) { // move right
            moveDirection.setX(moveSpeed);
        } else {
            moveDirection.setX(0f);
        }

        if (Keyboard.isKeyDown(GLFW_KEY_W)) { // move forwards
            moveDirection.setZ(-moveSpeed);
        } else if (Keyboard.isKeyDown(GLFW_KEY_S)) { // move backwards
            moveDirection.setZ(moveSpeed);
        } else {
            moveDirection.setZ(0f);
        }

        if (Keyboard.isKeyDown(GLFW_KEY_E)) { // move up
            moveDirection.setY(-moveSpeed);
        } else if (Keyboard.isKeyDown(GLFW_KEY_Q)) { // move down
            moveDirection.setY(moveSpeed);
        } else {
            moveDirection.setY(0f);
        }

        if (Keyboard.isKeyDown(GLFW_KEY_SPACE)) { // speed boost
            moveDirection.multiply(10f);
        } else if (Keyboard.isKeyDown(GLFW_KEY_LEFT_ALT)) { // slow movement
            moveDirection.multiply(0.1f);
        }

        if (MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) { // handle camera dragging
            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
            deltaCursorPos.multiply(getSensitivity());
            changeTilt(-deltaCursorPos.getY());
            changePan(deltaCursorPos.getX());
        }
    }

    public void update(float interval, Vector3f centrePosition) {
        changePositionRelativeToOrientation(Vector3f.Multiply(moveDirection, interval));

        if (orbit) {
            Vector3f cameraToCentrePosition;

            //First displace the camera horizontally along the tangent of current position in orbit
            cameraToCentrePosition = Vector3f.Subtract(centrePosition, position);
            final float cameraToRotationCentreDistanceBeforeDisplacement = (float) Math.sqrt(cameraToCentrePosition.getX() * cameraToCentrePosition.getX() + cameraToCentrePosition.getZ() * cameraToCentrePosition.getZ());

            faceDirection(cameraToCentrePosition);
            changePositionXRelativeToOrientation(angularVelocityRPS * cameraToRotationCentreDistanceBeforeDisplacement * interval);

            //Then displace the camera horizontally along the radius of the orbit motion to correct orbit radius
            cameraToCentrePosition = Vector3f.Subtract(centrePosition, position);
            final float cameraToRotationCentreDistanceAfterDisplacement = (float) Math.sqrt(cameraToCentrePosition.getX() * cameraToCentrePosition.getX() + cameraToCentrePosition.getZ() * cameraToCentrePosition.getZ());

            faceDirection(cameraToCentrePosition);
            changePositionZRelativeToOrientation(cameraToRotationCentreDistanceBeforeDisplacement - cameraToRotationCentreDistanceAfterDisplacement);
        }
    }

    public void calcViewMatrix() {
        rotation = new Quaternion(-tilt, X_AXIS);
        rotation.multiply(pan, Y_AXIS);

        viewMatrix = Matrix4f.QuaternionRotation(Quaternion.Conjugate(rotation));
        viewMatrix.multiply(Matrix4f.Translation(Vector3f.Negative(position)));
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Vector3f getDirection() {
        return Vector3f.Multiply(new Vector3f(0, 0, -1), getRotation());
    }

    public void returnToOrigin() {
        position = new Vector3f();
        setTilt(0f);
        setPan(0f);
    }

    public void facePoint(float x, float y, float z) {
        facePoint(new Vector3f(x, y, z));
    }

    public void facePoint(Vector3f pointPositionVector) {
        faceDirection(Vector3f.Subtract(pointPositionVector, position));
    }

    public void faceDirection(Vector3f direction) {
        direction.normalize();
        setPan(90 + direction.getTheta());
        setTilt(90 - direction.getPhi());
    }

    @Override
    public String toString() {
        return String.format("Position  %s\nTilt: %f   Pan: %f\n", getPosition(), getTilt(), getPan());
    }

    public Vector3f getPosition() {
        return position;
    }

    public void changePositionRelativeToOrientation(Vector3f deltaPosition) {
        changePositionRelativeToOrientation(deltaPosition.getX(), deltaPosition.getY(), deltaPosition.getZ());
    }

    public void changePositionRelativeToOrientation(float dx, float dy, float dz) {
        if (dx != 0) {
            changePositionXRelativeToOrientation(dx);
        }
        if (dy != 0) {
            changePositionYRelativeToOrientation(dy);
        }
        if (dz != 0) {
            changePositionZRelativeToOrientation(dz);
        }
    }

    public void changePositionXRelativeToOrientation(float dx) {
        position.addX(-dx * (float) Math.sin(Math.toRadians(pan - 90)));
        position.addZ(dx * (float) Math.cos(Math.toRadians(pan - 90)));
    }

    public void changePositionYRelativeToOrientation(float dy) {
        position.addY(dy);
    }

    public void changePositionZRelativeToOrientation(float dz) {
        position.addX(-dz * (float) Math.sin(Math.toRadians(pan)));
        position.addZ(dz * (float) Math.cos(Math.toRadians(pan)));
    }

    public float getTilt() {
        return tilt;
    }

    public void setTilt(float value) {
        if (value > MAX_TILT) {
            tilt = MAX_TILT;
        } else if (value < -MAX_TILT) {
            tilt = -MAX_TILT;
        } else {
            tilt = value;
        }
    }

    public void changeTilt(float deltaTilt) {
        setTilt(getTilt() + deltaTilt);
    }

    public float getPan() {
        return pan;
    }

    public void setPan(float value) {
        pan = value % 360f;
    }

    public void changePan(float deltaValue) {
        setPan(getPan() + deltaValue);
    }

    public void setFOV(int value) {
        if (value < fov_MIN) {
            fov = fov_MIN;
        } else if (value > fov_MAX) {
            fov = fov_MAX;
        } else {
            fov = value;
        }
    }

    public void changeFOV(int value) {
        setFOV(getFOV() + value);
    }

    public int getFOV() {
        return fov;
    }

    public void setMovementSpeed(float value) {
        if (value < moveSpeed_MIN) {
            moveSpeed = moveSpeed_MIN;
        } else if (value > moveSpeed_MAX) {
            moveSpeed = moveSpeed_MAX;
        } else {
            moveSpeed = value;
        }
    }

    public void changeMovementSpeed(float deltaValue) {
        setMovementSpeed(getMovementSpeed() + deltaValue);
    }

    public float getMovementSpeed() {
        return moveSpeed;
    }

    public void setSensitivity(float value) {
        if (value < sensitivity_MIN) {
            sensitivity = sensitivity_MIN;
        } else if (value > sensitivity_MAX) {
            sensitivity = sensitivity_MAX;
        } else {
            sensitivity = value;
        }
    }

    public void changeSensitivity(float deltaValue) {
        setSensitivity(getSensitivity() + deltaValue);
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public static final boolean orbit_DEF = false;

    public boolean isOrbiting() {
        return orbit;
    }

    public void setOrbit(boolean state) {
        orbit = state;
    }

    public void toggleOrbit() {
        orbit = !orbit;
    }

    public double getAngularVelocityDPS() {
        return angularVelocityDPS;
    }

    public void setAngularVelocityDPS(double value) {
        angularVelocityDPS = value;
        updateAngularVelocityRPS();
    }

    public void changeAngularVelocityDPS(double deltaValue) {
        setAngularVelocityDPS(getAngularVelocityDPS() + deltaValue);
    }

    private void updateAngularVelocityRPS() {
        angularVelocityRPS = (float) Math.toRadians(angularVelocityDPS);
    }
}
