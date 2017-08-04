package wbif.sjx.TrackAnalysis.Plot3D.Core;


import com.jogamp.opengl.util.awt.ImageUtil;
import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Renderer {
    private ShaderProgram mainShader;

    private FrustumCuller frustumCuller;

    private boolean takeScreenshotOnNextRender;

    private Matrix4f projectionMatrix;
    private Matrix4f cameraMatrix;

    //This is the product of the projection and camera Matrix
    private Matrix4f combinedViewMatrix;

    public Renderer() throws Exception{
        mainShader = new ShaderProgram("main");

        mainShader.createVertexShader(DataTypeUtils.loadAsString("shaders/vertex.vs"));
        mainShader.createFragmentShader(DataTypeUtils.loadAsString("shaders/fragment.fs"));
        mainShader.link();

        mainShader.createUniform("combinedViewMatrix");
        mainShader.createUniform("combinedTransformationMatrix");

        mainShader.createUniform("colour");

        frustumCuller = new FrustumCuller();
    }

    private void preRender(GLFWWindow window, Camera camera){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(window.hasResized()){
            glViewport(0, 0, window.getWidth(), window.getHeight());
        }

        if (window.isVSyncEnabled()) {
            glfwSwapInterval(1);
        }else {
            glfwSwapInterval(0);
        }

        glReadBuffer(0);

        calcCombinedViewMatrix(window, camera);
    }

    public void render(GLFWWindow window, Camera camera, Scene scene){
        preRender(window, camera);

        //Binds shader
        mainShader.bind();

        //Sets matrix uniforms for viewpoint
        mainShader.setMatrix4fUniform("combinedViewMatrix", combinedViewMatrix);
        frustumCuller.setCombinedViewMatrix(combinedViewMatrix);

        //Renders scene
        scene.render(mainShader, frustumCuller);

        //Unbinds shader
        mainShader.unbind();

        if(takeScreenshotOnNextRender){
            doScreenshot(window);
            takeScreenshotOnNextRender = false;
        }
    }

    private void calcProjectionMatrix(GLFWWindow window, Camera camera){
        projectionMatrix = Matrix4f.projection(camera.getFOV(), window.getAspectRatio(), Camera.VIEW_DISTANCE_NEAR, Camera.VIEW_DISTANCE_FAR);
    }

    private void calcCameraMatrix(Camera camera){
        cameraMatrix = camera.getCameraMatrix();
    }

    private void calcCombinedViewMatrix(GLFWWindow window, Camera camera){
        calcProjectionMatrix(window, camera);
        calcCameraMatrix(camera);
        combinedViewMatrix = Matrix4f.multiply(cameraMatrix, projectionMatrix);
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
