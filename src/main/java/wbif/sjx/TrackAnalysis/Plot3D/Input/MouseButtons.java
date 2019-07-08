package wbif.sjx.TrackAnalysis.Plot3D.Input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class MouseButtons extends GLFWMouseButtonCallback {

    private static final int BUTTON_TOTAL = 10;

    private static boolean[] curButtonState;
    private static boolean[] prevButtonState;

    public MouseButtons(){
        curButtonState = new boolean[BUTTON_TOTAL];
        prevButtonState = new boolean[BUTTON_TOTAL];
    }

    @Override
    public void invoke(long window, int button, int action, int mods) {
        curButtonState[button] = action != GLFW.GLFW_RELEASE;
    }

    public static void update(){
        prevButtonState = curButtonState.clone();
    }

    public static boolean isButtonDown(int buttonCode) {
        return curButtonState[buttonCode];
    }

    public static boolean isButtonUp(int buttonCode) {
        return !curButtonState[buttonCode];
    }

    public static boolean isButtonTapped(int buttonCode) {
        return isButtonUp(buttonCode) & prevButtonState[buttonCode];
    }
}
