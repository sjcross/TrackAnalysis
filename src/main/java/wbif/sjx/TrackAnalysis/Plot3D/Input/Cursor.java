package wbif.sjx.TrackAnalysis.Plot3D.Input;


import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWVidMode;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2d;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;


public class Cursor extends GLFWCursorPosCallback{

    private static Vector2d currentPosition;
    private static Vector2d previousPosition;

    public Cursor(){
        currentPosition = new Vector2d();
        previousPosition = new Vector2d();
    }

    public void invoke(long window, double x, double y) {
        currentPosition.x = x;
        currentPosition.y = y;
    }

    public static void update(){
        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
    }

    public static Vector2d getCurrentPosition(){
        return currentPosition;
    }

    public static Vector2f getDeltaPosition() {
        Vector2d deltaPosition = Vector2d.Subtract(currentPosition, previousPosition);
        return new Vector2f(
                (float) deltaPosition.x,
                (float) deltaPosition.y
        );
    }
}
