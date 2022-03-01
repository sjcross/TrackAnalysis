package io.github.sjcross.trackanalysis.Plot3D.Input;


import org.lwjgl.glfw.GLFWCursorPosCallback;

import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector2f;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Cursor extends GLFWCursorPosCallback
{
    private static Vector2f curPosition;
    private static Vector2f prevPosition;

    public Cursor()
    {
        curPosition = new Vector2f();
        prevPosition = new Vector2f();
    }

    public void invoke(long window, double x, double y)
    {
        curPosition.setX((float) x);
        curPosition.setY((float) y);
    }

    public static void update()
    {
        prevPosition = new Vector2f(curPosition);
    }

    public static Vector2f getCurrentPosition()
    {
        return curPosition;
    }

    public static Vector2f getDeltaPosition()
    {
        return Vector2f.Subtract(curPosition, prevPosition);
    }
}
