package io.github.sjcross.TrackAnalysis.Plot3D.Core;

import io.github.sjcross.TrackAnalysis.Plot3D.Input.Cursor;
import io.github.sjcross.TrackAnalysis.Plot3D.Input.Keyboard;
import io.github.sjcross.TrackAnalysis.Plot3D.Input.MouseButtons;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Matrix4f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Quaternion;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static io.github.sjcross.TrackAnalysis.Plot3D.Core.Scene.X_AXIS;
import static io.github.sjcross.TrackAnalysis.Plot3D.Core.Scene.Y_AXIS;

public class Camera {

    public static final float VIEW_DISTANCE_NEAR = 0.01f;
    public static final int VIEW_DISTANCE_FAR = 100000;
    private static float MOVE_STEP = 500f;
    private static float SENSITIVITY = 0.3f;
    private static final float MAX_TILT = 90;
    public static final int DEF_ANGULAR_VELOCITY = 90;
    public static final float DEF_FOV = 70;
    public static final float MIN_FOV = 20;
    public static final float MAX_FOV = 100;

    private Vector3f position;
    private Vector3f moveDirection;
    private float tilt; //Pitch
    private float pan;  //Yaw
    private float FOV;
    private boolean orbit;
    private double angularVelocity;
    private Quaternion rotation;
    private Matrix4f viewMatrix;

    public Camera()
    {
        position = new Vector3f();
        moveDirection = new Vector3f();
        tilt = 0;
        pan = 0;
        FOV = DEF_FOV;
        orbit = false;
        angularVelocity = DEF_ANGULAR_VELOCITY;
    }

    public void handleInput()
    {
        moveDirection = new Vector3f();

        if (Keyboard.isKeyDown(GLFW_KEY_A))
        {   // move left
            moveDirection.setX(-MOVE_STEP);
        }
        else if (Keyboard.isKeyDown(GLFW_KEY_D))
        {   // move right
            moveDirection.setX(MOVE_STEP);
        }

        if (Keyboard.isKeyDown(GLFW_KEY_W))
        {   // move forwards
            moveDirection.setZ(-MOVE_STEP);
        }
        else if (Keyboard.isKeyDown(GLFW_KEY_S))
        {   // move backwards
            moveDirection.setZ(MOVE_STEP);
        }

        if (Keyboard.isKeyDown(GLFW_KEY_E))
        {   // move up
            moveDirection.setY(-MOVE_STEP);
        }
        else if (Keyboard.isKeyDown(GLFW_KEY_Q))
        {   // move down
            moveDirection.setY(MOVE_STEP);
        }

        if (Keyboard.isKeyDown(GLFW_KEY_SPACE))
        {   // speed boost
            moveDirection.multiply(10f);
        }
        else if (Keyboard.isKeyDown(GLFW_KEY_LEFT_ALT))
        {   // slow movement
            moveDirection.multiply(0.1f);
        }

        if (MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {   // handle camera dragging
            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
            deltaCursorPos.multiply(SENSITIVITY);
            changeTilt(-deltaCursorPos.getY());
            changePan(deltaCursorPos.getX());
        }
    }

    public void update(float interval, Vector3f centrePos)
    {
        changePosRelativeToOrientation(Vector3f.Multiply(moveDirection, interval));

        if (orbit)
        {
            Vector3f cameToCentrePos;

            //First displace the camera horizontally along the tangent of current position in orbit
            cameToCentrePos = Vector3f.Subtract(centrePos, position);
            final float camToCentreDistBeforeDisplacement = (float) Math.sqrt(cameToCentrePos.getX() * cameToCentrePos.getX() + cameToCentrePos.getZ() * cameToCentrePos.getZ());

            faceDirection(cameToCentrePos);
            changePosXRelativeToOrientation((float) Math.toRadians(angularVelocity) * camToCentreDistBeforeDisplacement * interval);

            //Then displace the camera horizontally along the radius of the orbit motion to correct orbit radius
            cameToCentrePos = Vector3f.Subtract(centrePos, position);
            final float camToCentreDistAfterDisplacement = (float) Math.sqrt(cameToCentrePos.getX() * cameToCentrePos.getX() + cameToCentrePos.getZ() * cameToCentrePos.getZ());

            faceDirection(cameToCentrePos);
            changePositionZRelativeToOrientation(camToCentreDistBeforeDisplacement - camToCentreDistAfterDisplacement);
        }
    }

    public void calcViewMatrix()
    {
        rotation = new Quaternion(-tilt, X_AXIS);
        rotation.multiply(pan, Y_AXIS);

        viewMatrix = Matrix4f.QuaternionRotation(Quaternion.Conjugate(rotation));
        viewMatrix.multiply(Matrix4f.Translation(Vector3f.Negative(position)));
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public Matrix4f getViewMatrix()
    {
        return viewMatrix;
    }

    public Vector3f getDirection()
    {
        return Vector3f.Multiply(new Vector3f(0, 0, -1), getRotation());
    }

    public void returnToOrigin()
    {
        position = new Vector3f();
        tilt = 0;
        pan = 0;
    }

    public void facePoint(float x, float y, float z)
    {
        facePoint(new Vector3f(x, y, z));
    }

    public void facePoint(Vector3f pointPositionVector)
    {
        faceDirection(Vector3f.Subtract(pointPositionVector, position));
    }

    public void faceDirection(Vector3f direction)
    {
        direction.normalize();
        setPan(90 + direction.getTheta());
        setTilt(90 - direction.getPhi());
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void changePosRelativeToOrientation(Vector3f deltaPosition)
    {
        changePosRelativeToOrientation(deltaPosition.getX(), deltaPosition.getY(), deltaPosition.getZ());
    }

    public void changePosRelativeToOrientation(float dx, float dy, float dz)
    {
        if (dx != 0)
        {
            changePosXRelativeToOrientation(dx);
        }
        if (dy != 0)
        {
            changePositionYRelativeToOrientation(dy);
        }
        if (dz != 0)
        {
            changePositionZRelativeToOrientation(dz);
        }
    }

    public void changePosXRelativeToOrientation(float dx)
    {
        position.addX(-dx * (float) Math.sin(Math.toRadians(pan - 90)));
        position.addZ( dx * (float) Math.cos(Math.toRadians(pan - 90)));
    }

    public void changePositionYRelativeToOrientation(float dy)
    {
        position.addY(dy);
    }

    public void changePositionZRelativeToOrientation(float dz)
    {
        position.addX(-dz * (float) Math.sin(Math.toRadians(pan)));
        position.addZ( dz * (float) Math.cos(Math.toRadians(pan)));
    }

    public float getTilt()
    {
        return tilt;
    }

    public void setTilt(float value)
    {
        tilt = Math.min(Math.max(value, -MAX_TILT), MAX_TILT);
    }

    public void changeTilt(float deltaTilt)
    {
        setTilt(tilt + deltaTilt);
    }

    public float getPan()
    {
        return pan;
    }

    public void setPan(float value)
    {
        pan = value % 360f;
    }

    public void changePan(float deltaValue)
    {
        setPan(pan + deltaValue);
    }

    public void setFOV(float value)
    {
        FOV = Math.min(Math.max(value, MIN_FOV), MAX_FOV);
    }

    public void changeFOV(float value)
    {
        setFOV(FOV + value);
    }

    public float getFOV()
    {
        return FOV;
    }

    public boolean isOrbiting()
    {
        return orbit;
    }

    public void setOrbit(boolean state)
    {
        orbit = state;
    }

    public double getAngularVelocity()
    {
        return angularVelocity;
    }

    public void setAngularVelocity(double value)
    {
        angularVelocity = value;
    }

    @Override
    public String toString()
    {
        return String.format("Position  %s\nTilt: %f   Pan: %f\n", getPosition(), getTilt(), getPan());
    }
}
