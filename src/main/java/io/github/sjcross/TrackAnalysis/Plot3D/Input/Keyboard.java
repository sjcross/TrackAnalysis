package io.github.sjcross.TrackAnalysis.Plot3D.Input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Keyboard extends GLFWKeyCallback
{
    private static final int KEY_TOTAL = 350;

    private static boolean[] curKeyState;
    private static boolean[] prevKeyState;

    public Keyboard()
    {
        curKeyState = new boolean[KEY_TOTAL];
        prevKeyState = new boolean[KEY_TOTAL];
    }

    public void invoke(long window, int key, int scanCode, int action, int mods)
    {
        if (key >= 0) curKeyState[key] = action != GLFW.GLFW_RELEASE;
    }

    public static void update()
    {
        prevKeyState = curKeyState.clone();
    }

    public static boolean isKeyDown(int keyCode)
    {
        return curKeyState[keyCode];
    }

    public static boolean isKeyUp(int keyCode)
    {
        return !curKeyState[keyCode];
    }

    public static boolean isKeyTapped(int keyCode)
    {
        return isKeyUp(keyCode) & prevKeyState[keyCode];
    }
}
