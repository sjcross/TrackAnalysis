package wbif.sjx.TrackAnalysis.Plot3D.Core;


import com.jogamp.opengl.util.awt.ImageUtil;
import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Item.TrackEntity;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Texture.Texture;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Renderer {

    public static final float BIAS = 20;

    private final ShaderProgram mainShader;
    private final ShaderProgram arrayTextureShader;
    private final ShaderProgram instancedShader;

    private boolean captureNextRender;

    private boolean useOrthoProj;
    private boolean bindColourBuffers = true;
    private boolean bindMeshBuffers = true;
    private Vector2f orthoBoundingConstraints;

    public Renderer() throws Exception {
        mainShader = new ShaderProgram("main", "main", "texture2D");
        arrayTextureShader = new ShaderProgram("arrayTexture", "main", "texture2DArray");
        instancedShader = new ShaderProgram("instanced", "instanced", "instanced");

        Texture.SetActivateUnit(0);

        mainShader.bind();
        mainShader.setTextureSamplerUniform("textureSampler", 0);
        mainShader.unbind();

        useOrthoProj = false;
        orthoBoundingConstraints = new Vector2f();
    }

    public void render(GLFWWindow window, Camera camera, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapInterval(window.isVSyncEnabled() ? 1 : 0);

        Matrix4f projViewMatrix = calcProjViewMatrix(window, camera);

        mainShader.bind();
        mainShader.setMatrix4fUniform("projectedViewMatrix", projViewMatrix);

        if (scene.isAxesVisible()) {
            for (Entity axis : scene.getAxes()) {
                axis.render(mainShader);
            }
        }

        if (scene.isBoundingBoxVisible() && !scene.ifMotilityPlot()) {
            scene.getBoundingFrame().render(mainShader);
        }

//        if(!scene.getTracksEntities().ifMotilityPlot()) {
//            scene.getPlaneXY().render(mainShader);
//            scene.getPlaneYZ().render(mainShader);
//        }

        mainShader.unbind();


        arrayTextureShader.bind();
        arrayTextureShader.setMatrix4fUniform("projectedViewMatrix", projViewMatrix);
        arrayTextureShader.setIntUniform("frame", scene.getFrame());

        if(!scene.ifMotilityPlot()) {
            scene.getPlaneXZ().render(arrayTextureShader);
        }

        arrayTextureShader.unbind();


        instancedShader.bind();
        instancedShader.setMatrix4fUniform("projectedViewMatrix", projViewMatrix);

        boolean useInstancedColour = scene.getDisplayColour() != Scene.DisplayColour.ID;

        if (bindMeshBuffers) {
            bindMeshBuffers = false;
            scene.getTracksEntities().updateMeshBuffers(scene.getRenderQuality());
        }
        if (bindColourBuffers) {
            bindColourBuffers = false;
            scene.getTracksEntities().updateColourBuffers(scene.getDisplayColour());
        }

        instancedShader.setBooleanUniform("motilityPlot", scene.ifMotilityPlot());
        instancedShader.setBooleanUniform("useInstancedColour", useInstancedColour);

        scene.getTracksEntities().forEachTrackEntity(trackEntity -> {
            if (scene.ifMotilityPlot()) {
                instancedShader.setMatrix4fUniform("motilityPlotMatrix", trackEntity.getMotilityPlotMatrix());
            }

            if (!useInstancedColour) {
                instancedShader.setColourUniformRGB("colour", trackEntity.getColour());
            }

            trackEntity.renderParticle(scene.getFrame());

            if (scene.isTrailVisibile()) {
                trackEntity.renderTrail(scene.getFrame(), scene.getTrailLength(), scene.getRenderQuality() != Scene.RenderQuality.LOWEST);
            }
        });

        instancedShader.unbind();


        if (captureNextRender) {
            doScreenshot(window);
            captureNextRender = false;
        }
    }

    private Matrix4f calcProjViewMatrix(GLFWWindow window, Camera camera) {
        Matrix4f projMatrix;

        if (useOrthoProj && !camera.isOrbiting()) {
            float widthRatio = window.getWidth() / orthoBoundingConstraints.getX();
            float heightRatio = window.getHeight() / orthoBoundingConstraints.getY();

            float a = (orthoBoundingConstraints.getX() + BIAS) / 2;
            float b = (orthoBoundingConstraints.getY() + BIAS) / 2;

            if (widthRatio < heightRatio) {
                b = (orthoBoundingConstraints.getX() / window.getAspectRatio() + BIAS) / 2;
            } else if (heightRatio < widthRatio) {
                a = (orthoBoundingConstraints.getY() * window.getAspectRatio() + BIAS) / 2;
            }

            projMatrix = Matrix4f.Orthographic(-a, a, b, -b, Camera.VIEW_DISTANCE_NEAR, Camera.VIEW_DISTANCE_FAR);
        } else {
            projMatrix = Matrix4f.Perspective(camera.getFOV(), window.getAspectRatio(), Camera.VIEW_DISTANCE_NEAR, Camera.VIEW_DISTANCE_FAR);
        }

        camera.calcViewMatrix();

        return Matrix4f.Multiply(projMatrix, camera.getViewMatrix());
    }

    public void takeScreenshot() {
        captureNextRender = true;
    }

    private void doScreenshot(GLFWWindow window) {
        ByteBuffer pixels = ByteBuffer.allocateDirect(4 * window.getWidth() * window.getHeight());

        glReadBuffer(GL_BACK);
        glReadPixels(0, 0, window.getWidth(), window.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        BufferedImage image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        image.getWritableTile(0, 0).setDataElements(0, 0, window.getWidth(), window.getHeight(), DataUtils.toByteArray(pixels));

        ImageUtil.flipImageVertically(image);
        new ImagePlus("Screenshot", image).show();
    }

    public void dispose() {
        mainShader.dispose();
        arrayTextureShader.dispose();
        instancedShader.dispose();
    }

    public void setOrthoBoundingConstraints(float x, float y) {
        orthoBoundingConstraints.set(x, y);
        useOrthoProj = true;
    }

    public void disableOrthoProj() {
        useOrthoProj = false;
    }

    public void setBindColourBuffers(boolean bindColourBuffers) {
        this.bindColourBuffers = bindColourBuffers;
    }

    public void setBindMeshBuffers(boolean bindMeshBuffers) {
        this.bindMeshBuffers = bindMeshBuffers;
    }
}
