package wbif.sjx.TrackAnalysis.Plot3D.Core;

import com.itextpdf.text.pdf.PRAcroForm;
import wbif.sjx.TrackAnalysis.GUI.TrackPlotControl;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntity;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseWheel;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Engine {
    private TrackPlotControl trackPlotControl;
    private boolean running;
    private GLFW_Window window;
    private Renderer renderer;
    private Camera camera;
    private Scene scene;

    public Engine(){
    }

    public void start(TrackPlotControl trackPlotControl){
        try {
            this.trackPlotControl = trackPlotControl;
            init();
            mainLoop();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            shutdown();
        }
    }

    private void init() throws Exception{
        window = new GLFW_Window("3D Track Plot", 600, 600, true);
        renderer = new Renderer();
        camera = new Camera(70);
//        camera.facePoint(DataTypeUtils.toVector3f(trackPlotControl.getTracks().getMeanPoint(0)));
//        camera.getGlobalPosition().set(500,500,-100);
        scene = new Scene(trackPlotControl.getTracks());
    }

    private void mainLoop() throws Exception{
        running = true;

        while (running && window.isOpen()){
            handleInput();
            update();
            renderFrame();
        }
    }

    public void stop(){
        running = false;
    }

    private void shutdown(){
        window.dispose();
        renderer.dispose();
    }

    private void handleInput(){

        //Handles switching between camera mode
        if(Cursor.inCameraMode()){
            if (Keyboard.isKeyTapped(GLFW_KEY_ESCAPE)) {
                Cursor.setCameraMode(window, false);
            }
        }else {
            if(MouseButtons.isButtonTapped(GLFW_MOUSE_BUTTON_1)){
                Cursor.setCameraMode(window, true);
            }
            if (Keyboard.isKeyTapped(GLFW_KEY_ESCAPE)) {
                stop();
            }
        }



        //Handles camera WASD movement
        Vector3f deltaCamPos = new Vector3f();
        float cameraPositionStep = camera.getCameraPositionStep();

        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            deltaCamPos.setX(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_D)){
            deltaCamPos.setX(cameraPositionStep);
        } else{
            deltaCamPos.setX(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            deltaCamPos.setY(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            deltaCamPos.setY(cameraPositionStep);
        } else{
            deltaCamPos.setY(0);
        }

        if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            deltaCamPos.setZ(cameraPositionStep);
        } else if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT)){
            deltaCamPos.setZ(-cameraPositionStep);
        } else{
            deltaCamPos.setZ(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            deltaCamPos.scale(5);
        }

        camera.changePositionRelativeToOrientation(deltaCamPos);

        //Handles camera rotation
        if(Cursor.inCameraMode()) {
            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
            deltaCursorPos.scale(camera.getMouseSensitivity());
            camera.changeTilt(deltaCursorPos.y);
            camera.changePan(deltaCursorPos.x);
        }

        //Handles frame switching
        if(Keyboard.isKeyDown(GLFW_KEY_UP)){
            scene.incrementFrame();
        }else if(Keyboard.isKeyDown(GLFW_KEY_DOWN)){
            scene.decrementFrame();
        }

        if(Keyboard.isKeyTapped(GLFW_KEY_PAGE_UP)){
            scene.incrementFrame();
        }else if(Keyboard.isKeyTapped(GLFW_KEY_PAGE_DOWN)){
            scene.decrementFrame();
        }

        if(Keyboard.isKeyTapped(GLFW_KEY_F)){
            scene.togglePlayFrames();
        }

        if(Keyboard.isKeyTapped(GLFW_KEY_T)){
            camera.changeTilt(10);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_P)) {
            camera.changePan(1);
        }


        //Essential static updates
        Keyboard.update();
        MouseButtons.update();
        Cursor.update(window.getPrimaryMonitor());
        MouseWheel.update();
    }

    private void update(){
        scene.update();
        trackPlotControl.updateGui();
    }


    private void renderFrame() {
        renderer.render(window, camera, scene);
        window.update();
    }

    public Camera getCamera() {
        return camera;
    }

    public Scene getScene() {
        return scene;
    }
}
