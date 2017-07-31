package wbif.sjx.TrackAnalysis.Plot3D.Input;


import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWVidMode;
import wbif.sjx.TrackAnalysis.Plot3D.Core.GLFW_Window;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2d;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class Cursor extends GLFWCursorPosCallback{

    private static Vector2d currentPosition;
    private static Vector2d previousPosition;

    private static boolean cameraMode;
    private static boolean onScreen;

    public Cursor(){
        currentPosition = new Vector2d();
        previousPosition = new Vector2d();
        cameraMode = false;
    }

    public void invoke(long window, double x, double y) {
        currentPosition.x = x;
        currentPosition.y = y;
    }

    public static void update(GLFWVidMode primaryMonitor){
        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
        if(!cameraMode){
            onScreen = (currentPosition.x > 0 && currentPosition.y > 0 && currentPosition.x < primaryMonitor.width() && currentPosition.y < primaryMonitor.height());
        }
    }

    public static Vector2d getCurrentPosition(){
        return currentPosition;
    }

    public static Vector2f getDeltaPosition(){
        Vector2d deltaPosition = Vector2d.Subtract(currentPosition, previousPosition);
        return new Vector2f(
                (float)deltaPosition.x,
                (float)deltaPosition.y
        );
    }

    public static void toggleCameraMode(GLFW_Window window){
        setCameraMode(window, !cameraMode);
    }

    public static void setCameraMode(GLFW_Window window, boolean state){
        cameraMode = state;
        if(state){
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }else{
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            glfwSetCursorPos(window.getHandle(), window.getWidth()/2, window.getHeight()/2);
        }

    }

    public static boolean inCameraMode(){
        return cameraMode;
    }

    public static boolean onScreen(){
        return onScreen;
    }
}
