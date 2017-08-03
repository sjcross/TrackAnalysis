package wbif.sjx.TrackAnalysis.Plot3D.Input;

import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class MouseWheel extends GLFWScrollCallback{
    private static final int WHEEL_SENSITIVITY = 2;
    private static int deltaScroll;

    public MouseWheel(){
        deltaScroll = 0;
    }

    public void invoke(long window, double dx, double dy) {
        deltaScroll = (int)dy;
    }

    public static void update(){
        deltaScroll = 0;
    }

    public static int getDeltaScroll(){
        return deltaScroll * WHEEL_SENSITIVITY;
    }

    public static boolean isScrolled(){
        return deltaScroll != 0;
    }
}
