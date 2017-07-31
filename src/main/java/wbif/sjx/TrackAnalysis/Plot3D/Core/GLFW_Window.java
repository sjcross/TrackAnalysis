package wbif.sjx.TrackAnalysis.Plot3D.Core;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseWheel;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class GLFW_Window {
    private GLFWVidMode primaryMonitor;
    private String title;
    private int width;
    private int height;
    private final boolean vSync;
    private long windowHandle;

    //Creates window object
    public GLFW_Window(String title, int width, int height, boolean vSync){
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        this.primaryMonitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, primaryMonitor.refreshRate());
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        centreWindow();

        glfwSetKeyCallback(windowHandle, new Keyboard());
        glfwSetMouseButtonCallback(windowHandle, new MouseButtons());
        glfwSetCursorPosCallback(windowHandle, new Cursor());
        glfwSetScrollCallback(windowHandle, new MouseWheel());

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        if (vSync) {
            glfwSwapInterval(1);
        }

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_MULTISAMPLE);

        glfwShowWindow(windowHandle);
    }

    //Centres the window on the primary monitor
    public void centreWindow(){
        glfwSetWindowPos(windowHandle, (primaryMonitor.width() - width) / 2, (primaryMonitor.height() - height) / 2);
    }

    public void dispose(){
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public boolean hasResized(){
        int[] w = new int[1]; int[] h = new int[1];
        glfwGetFramebufferSize(windowHandle, w, h);
        if(width != w[0] || height != h[0]) {
            width = w[0];
            height = h[0];
            return true;
        }else {
            return false;
        }
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(windowHandle);
    }

    public long getHandle(){
        return windowHandle;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth(){
        return width;
    }

    public  int getHeight(){
        return height;
    }

    public float getAspectRatio(){
        return getWidth() / (float)getHeight();
    }

    public GLFWVidMode getPrimaryMonitor(){
        return primaryMonitor;
    }

    public boolean isVSyncEnabled(){
        return vSync;
    }
}

