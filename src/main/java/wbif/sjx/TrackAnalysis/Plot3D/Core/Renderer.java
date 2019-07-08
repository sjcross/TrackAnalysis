package wbif.sjx.TrackAnalysis.Plot3D.Core;


import com.jogamp.opengl.util.awt.ImageUtil;
import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Texture.Texture;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Renderer {

    public static final float CONST = 20;

    private final ShaderProgram mainShader;
    private final ShaderProgram arrayTextureShader;
    private final ShaderProgram instancedShader;

    private boolean captureNextRender;

    private boolean useOrthoProjection;
    private Vector2f orthoBoundingConstraints;

    private Matrix4f projectedViewMatrix;

    public Renderer() throws Exception{
        mainShader = new ShaderProgram("main", "main", "texture2D");
        arrayTextureShader = new ShaderProgram("arrayTexture", "main", "texture2DArray");
        instancedShader = new ShaderProgram("instanced", "instanced", "instanced");

        Texture.SetActivateUnit(0);

        mainShader.bind();
        mainShader.setTextureSamplerUniform("textureSampler", 0);
        mainShader.unbind();

        useOrthoProjection = false;
        orthoBoundingConstraints = new Vector2f();
    }

    private void preRender(GLFWWindow window, Camera camera){
        camera.preRender();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapInterval(window.isVSyncEnabled() ? 1 : 0);

        if (useOrthoProjection) {
            float widthRatio = window.getWidth() / orthoBoundingConstraints.getX();
            float heightRatio = window.getHeight() / orthoBoundingConstraints.getY();
            float a, b;

            if(widthRatio < heightRatio){
                a = (orthoBoundingConstraints.getX() + CONST) / 2;
                b = (orthoBoundingConstraints.getX() / window.getAspectRatio() + CONST) / 2;
            }else if(heightRatio < widthRatio) {
                b = (orthoBoundingConstraints.getY() + CONST) / 2;
                a = (orthoBoundingConstraints.getY() * window.getAspectRatio() + CONST) / 2;
            }else {
                a = (orthoBoundingConstraints.getX() + CONST) / 2;
                b = (orthoBoundingConstraints.getY() + CONST) / 2;
            }

            projectedViewMatrix = Matrix4f.Multiply(Matrix4f.Orthographic(-a, a, b, -b, Camera.VIEW_DISTANCE_NEAR, Camera.VIEW_DISTANCE_FAR), camera.getViewMatrix());
        }else {
            projectedViewMatrix = Matrix4f.Multiply(Matrix4f.Perspective(camera.getFOV(), window.getAspectRatio(), Camera.VIEW_DISTANCE_NEAR, Camera.VIEW_DISTANCE_FAR), camera.getViewMatrix());
        }

        FrustumCuller.getInstance().setProjectedViewMatrix(projectedViewMatrix);
    }

    public void render(GLFWWindow window, Camera camera, Scene scene){
        preRender(window, camera);

        mainShader.bind();
        mainShader.setMatrix4fUniform("projectedViewMatrix", projectedViewMatrix);

        if(scene.isAxesVisible()) {
            for (Entity axis : scene.getAxes()) {
                axis.render(mainShader);
            }
        }

        if(scene.isBoundingBoxVisible() && !scene.getTracksEntities().ifMotilityPlot()) {
            scene.getBoundingFrame().render(mainShader);
        }

//        if(!scene.getTracksEntities().ifMotilityPlot()) {
//            scene.getPlaneXY().render(mainShader);
//            scene.getPlaneYZ().render(mainShader);
//        }

        mainShader.unbind();



        arrayTextureShader.bind();
        arrayTextureShader.setMatrix4fUniform("projectedViewMatrix", projectedViewMatrix);
        arrayTextureShader.setIntUniform("frame", scene.getFrame());

//        if(!scene.getTracksEntities().ifMotilityPlot()) {
//            scene.getPlaneXZ().render(arrayTextureShader);
//        }

        arrayTextureShader.unbind();



        instancedShader.bind();
        instancedShader.setMatrix4fUniform("projectedViewMatrix", projectedViewMatrix);
        scene.getTracksEntities().render(instancedShader, scene.getFrame());
        instancedShader.unbind();



        if(captureNextRender){
            doScreenshot(window);
            captureNextRender = false;
        }
    }

    public void takeScreenshot(){
        captureNextRender = true;
    }

    private void doScreenshot(GLFWWindow window){
        ByteBuffer pixels = ByteBuffer.allocateDirect(4 * window.getWidth() * window.getHeight());

        glReadBuffer(GL_BACK);
        glReadPixels(0, 0, window.getWidth(), window.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        BufferedImage image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        image.getWritableTile(0,0).setDataElements(0,0, window.getWidth(), window.getHeight(), DataUtils.toByteArray(pixels));

        ImageUtil.flipImageVertically(image);
        new ImagePlus("Screenshot", image).show();
    }

    public void dispose(){
        mainShader.dispose();
        arrayTextureShader.dispose();
        instancedShader.dispose();
    }

    public void setOrthoBoundingConstraints(float x, float y){
        orthoBoundingConstraints.set(x, y);
        useOrthoProjection = true;
    }

    public void disableOrthoProjection(){
        useOrthoProjection = false;
    }
}
