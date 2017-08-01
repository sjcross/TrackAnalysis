package wbif.sjx.TrackAnalysis.Plot3D.Core;


import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Renderer {
    private ShaderProgram mainShader;
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
    }

    private void preRender(GLFW_Window window, Camera camera){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(window.hasResized()){
            glViewport(0, 0, window.getWidth(), window.getHeight());
        }

        if (window.isVSyncEnabled()) {
            glfwSwapInterval(1);
        }else {
            glfwSwapInterval(0);
        }

        calcCombinedViewMatrix(window, camera);
    }

    public void render(GLFW_Window window, Camera camera, Scene scene){
        preRender(window, camera);

        //Binds shader
        mainShader.bind();

        //Sets matrix uniforms for viewpoint
        mainShader.setMatrix4fUniform("combinedViewMatrix", combinedViewMatrix);


        scene.render(mainShader);

        //Unbinds shader
        mainShader.unbind();
    }

    private void calcProjectionMatrix(GLFW_Window window, Camera camera){
        projectionMatrix = Matrix4f.projection(camera.getFOV(), window.getAspectRatio(), camera.getViewDistanceNear(), camera.getViewDistanceFar());
    }

    private void calcCameraMatrix(Camera camera){
        cameraMatrix = camera.getCameraMatrix();
    }

    private void calcCombinedViewMatrix(GLFW_Window window, Camera camera){
        calcProjectionMatrix(window, camera);
        calcCameraMatrix(camera);
        combinedViewMatrix = Matrix4f.multiply(cameraMatrix, projectionMatrix);
    }

    public void dispose(){
        mainShader.dispose();
    }
}
