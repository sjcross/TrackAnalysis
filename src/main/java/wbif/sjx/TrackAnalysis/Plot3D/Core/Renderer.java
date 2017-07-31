package wbif.sjx.TrackAnalysis.Plot3D.Core;


import net.imglib2.ops.parse.token.Int;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Renderer {
    private ShaderProgram mainShader;

    public Renderer() throws Exception{
        mainShader = new ShaderProgram("main");

        mainShader.createVertexShader(DataTypeUtils.loadAsString("shaders/vertex.vs"));
        mainShader.createFragmentShader(DataTypeUtils.loadAsString("shaders/fragment.fs"));
        mainShader.link();

        mainShader.createUniform("projectionMatrix");
        mainShader.createUniform("cameraMatrix");
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
    }

    public void render(GLFW_Window window, Camera camera, Scene scene){
        preRender(window, camera);

        //Binds shader
        mainShader.bind();

        //Sets matrix uniforms for viewpoint
        mainShader.setMatrix4fUniform("projectionMatrix", getProjectionMatrix(window, camera));
        mainShader.setMatrix4fUniform("cameraMatrix", camera.getCameraMatrix());
//        mainShader.setMatrix4fUniform("");


       scene.render(mainShader);

        //Unbinds shader
        mainShader.unbind();
    }

    private Matrix4f getProjectionMatrix(GLFW_Window window, Camera camera){
        return Matrix4f.projection(camera.getFOV(), window.getAspectRatio(), camera.getViewDistanceNear(), camera.getViewDistanceFar());
    }

    public void dispose(){
        mainShader.dispose();
    }
}
