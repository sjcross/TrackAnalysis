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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL43.GL_MIPMAP;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class GLFWWindow {

    private GLFWVidMode primaryMonitor;
    private String title;
    private int width;
    private int height;
    private int refreshRate;
    private final boolean vSync;
    private long windowHandle;

    public GLFWWindow(String title, int width, int height, boolean vSync) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        this.primaryMonitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.title = title;
        this.width = width;
        this.height = height;
        this.refreshRate = primaryMonitor.refreshRate();
        this.vSync = vSync;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, refreshRate);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        centreWindow();
        changeWindowPosition(-500, 0);

        glfwSetFramebufferSizeCallback(windowHandle, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            glViewport(0, 0, newWidth, newHeight);
        });

        glfwSetKeyCallback(windowHandle, new Keyboard());
        glfwSetMouseButtonCallback(windowHandle, new MouseButtons());
        glfwSetCursorPosCallback(windowHandle, new Cursor());
        glfwSetScrollCallback(windowHandle, new MouseWheel());

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glFrontFace(GL_CCW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_MIPMAP);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnable(GL_DEPTH_TEST);
    }

    public void setVisibility(boolean state) {
        if (state) {
            glfwShowWindow(windowHandle);
        } else {
            glfwHideWindow(windowHandle);
        }
    }

    public void centreWindow() {
        glfwSetWindowPos(windowHandle, (primaryMonitor.width() - width) / 2, (primaryMonitor.height() - height) / 2);
    }

    public void changeWindowPosition(int dx, int dy) {
        int[] windowPosition = glfwGetWindowPosition();
        glfwSetWindowPos(windowHandle, windowPosition[0] + dx, windowPosition[1] + dy);
    }

    public int[] glfwGetWindowPosition() {
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowPos(windowHandle, x, y);
        return new int[]{x[0], y[0]};
    }

    public void dispose() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(windowHandle);
    }

    public long getHandle() {
        return windowHandle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowHandle, title);
    }

    public void appendToTitle(String string) {
        glfwSetWindowTitle(windowHandle, String.format("%s (%s)", title, string));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAspectRatio() {
        return getWidth() / (float) getHeight();
    }

    public GLFWVidMode getPrimaryMonitor() {
        return primaryMonitor;
    }

    public boolean isVSyncEnabled() {
        return vSync;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public boolean isMinimized() {
        return glfwGetWindowAttrib(windowHandle, GLFW_ICONIFIED) == 1; //Fix
    }

    public boolean isFocused() {
        return glfwGetWindowAttrib(windowHandle, GLFW_FOCUSED) == 1;
    }
}