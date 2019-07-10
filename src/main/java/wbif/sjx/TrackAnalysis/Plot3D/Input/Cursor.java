package wbif.sjx.TrackAnalysis.Plot3D.Input;


import org.lwjgl.glfw.GLFWCursorPosCallback;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2d;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Cursor extends GLFWCursorPosCallback{

    private static Vector2d curPosition;
    private static Vector2d prevPosition;

    public Cursor(){
        curPosition = new Vector2d();
        prevPosition = new Vector2d();
    }

    public void invoke(long window, double x, double y) {
        curPosition.setX(x);
        curPosition.setY(y);
    }

    public static void update(){
        prevPosition = new Vector2d(curPosition);
    }

    public static Vector2d getCurrentPosition(){
        return curPosition;
    }

    public static Vector2f getDeltaPosition() {
        Vector2d deltaPosition = Vector2d.Subtract(curPosition, prevPosition);
        return new Vector2f(
                (float) deltaPosition.getX(),
                (float) deltaPosition.getY()
        );
    }
}
