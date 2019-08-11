package wbif.sjx.TrackAnalysis.Plot3D.Core;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseWheel;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2i;

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

    private final GLFWVidMode primaryMonitor;
    private String title;
    private int width;
    private int height;
    private final int refreshRate;
    private final boolean vSync;
    private final long handle;

    public GLFWWindow(String title, int width, int height, boolean vSync) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        primaryMonitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
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

        handle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (handle == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glfwSetFramebufferSizeCallback(handle, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            glViewport(0, 0, newWidth, newHeight);
        });

        glfwSetKeyCallback(handle, new Keyboard());
        glfwSetMouseButtonCallback(handle, new MouseButtons());
        glfwSetCursorPosCallback(handle, new Cursor());
        glfwSetScrollCallback(handle, new MouseWheel());

        glfwMakeContextCurrent(handle);
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

        centre();
        move(-500, 0);
    }

    public void setVisibility(boolean state) {
        if (state) {
            glfwShowWindow(handle);
        } else {
            glfwHideWindow(handle);
        }
    }

    public void centre() {
        glfwSetWindowPos(handle, (primaryMonitor.width() - width) / 2, (primaryMonitor.height() - height) / 2);
    }

    public void move(int dx, int dy) {
        Vector2i position = getPosition();
        glfwSetWindowPos(handle, position.getX() + dx, position.getY() + dy);
    }

    public Vector2i getPosition() {
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowPos(handle, x, y);
        return new Vector2i(x[0], y[0]);
    }

    public void dispose() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void update() {
        glfwSwapBuffers(handle);
        glfwPollEvents();
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(handle);
    }

    public long getHandle() {
        return handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(handle, title);
    }

    public void appendToTitle(String string) {
        glfwSetWindowTitle(handle, String.format("%s (%s)", title, string));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAspectRatio() {
        return width / height;
    }

    public boolean isVSyncEnabled() {
        return vSync;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public boolean isMinimized() {
        return glfwGetWindowAttrib(handle, GLFW_ICONIFIED) == 1;
    }

    public boolean isFocused() {
        return glfwGetWindowAttrib(handle, GLFW_FOCUSED) == 1;
    }
}