package wbif.sjx.TrackAnalysis.Plot3D.Core;


import com.jogamp.opengl.util.awt.ImageUtil;
import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.GenerateMesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Renderer {
    private ShaderProgram mainShader;

    private boolean takeScreenshotOnNextRender;

    private Matrix4f projectedViewMatrix;

    public Renderer() throws Exception{
        mainShader = new ShaderProgram("main");

        mainShader.createVertexShader(DataTypeUtils.loadAsString("shaders/main/vertex.vs"));
        mainShader.createFragmentShader(DataTypeUtils.loadAsString("shaders/main/fragment.fs"));
        mainShader.link();

        mainShader.createUniform("projectedViewMatrix");
        mainShader.createUniform("globalMatrix");

        mainShader.createUniform("colour");
    }

    final int j = 500;

    private void preRender(GLFWWindow window, Camera camera){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (window.isVSyncEnabled()) {
            glfwSwapInterval(1);
        }else {
            glfwSwapInterval(0);
        }

        Matrix4f perspective = Matrix4f.Perspective(camera.getFOV(), window.getAspectRatio(), Camera.VIEW_DISTANCE_NEAR, Camera.VIEW_DISTANCE_FAR);

        projectedViewMatrix = Matrix4f.Multiply(perspective, camera.getViewMatrix());
    }

    public void render(GLFWWindow window, Camera camera, Scene scene){
        preRender(window, camera);

        //Binds shader
        mainShader.bind();

        //Sets matrix uniform for viewpoint
        mainShader.setMatrix4fUniform("projectedViewMatrix", projectedViewMatrix);
        FrustumCuller.getInstance().setProjectedViewMatrix(projectedViewMatrix);

        //Renders scene
        scene.render(mainShader);

        //Unbinds shader
        mainShader.unbind();

        //Handles screenshot
        if(takeScreenshotOnNextRender){
            doScreenshot(window);
            takeScreenshotOnNextRender = false;
        }
    }

    public void takeScreenshot(){
        takeScreenshotOnNextRender = true;
    }

    private void doScreenshot(GLFWWindow window){
        ByteBuffer pixels = ByteBuffer.allocateDirect(4 * window.getWidth() * window.getHeight());

        glReadBuffer(GL_BACK);
        glReadPixels(0, 0, window.getWidth(), window.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        BufferedImage image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        image.getWritableTile(0,0).setDataElements(0,0,window.getWidth(), window.getHeight(), DataTypeUtils.toByteArray(pixels));

        ImageUtil.flipImageVertically(image);
        new ImagePlus("Screenshot", image).show();
    }

    public void dispose(){
        mainShader.dispose();
    }
}
