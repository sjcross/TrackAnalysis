package wbif.sjx.TrackAnalysis.Plot3D.Core;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.GenerateMesh;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseWheel;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Engine implements ActionListener, ChangeListener{
    private TrackCollection tracks;
    private boolean running;
    private GLFW_Window window;
    private Renderer renderer;
    private Camera camera;
    private Scene scene;

    public Engine(TrackCollection tracks){
        try {
            this.tracks = tracks;
            init();
            mainLoop();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            shutdown();
        }
    }

    private void init() throws Exception{
        window = new GLFW_Window("window", 600, 600, true);
        renderer = new Renderer();
        camera = new Camera(70);
        scene = new Scene(tracks, GenerateMesh.sphere(1f, 50));
    }

    private void mainLoop() throws Exception{
        running = true;

        while (running && window.isOpen()){
//            System.out.println(camera.getPosition());
            handleInput();
            renderFrame();
        }
    }

    public void stop(){
        running = false;
    }

    private void shutdown(){
        window.dispose();
        renderer.dispose();
        scene.dispose();
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

        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            deltaCamPos.setZ(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            deltaCamPos.setZ(cameraPositionStep);
        } else{
            deltaCamPos.setZ(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            deltaCamPos.setX(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_D)){
            deltaCamPos.setX(cameraPositionStep);
        } else{
            deltaCamPos.setX(0);
        }

        if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            deltaCamPos.setY(cameraPositionStep);
        } else if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT)){
            deltaCamPos.setY(-cameraPositionStep);
        } else{
            deltaCamPos.setY(0);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            deltaCamPos.scale(5);
        }

        camera.changePositionRelativeToOrientation(deltaCamPos);

        //Handles camera rotation
        Vector3f deltaCamRot;

        if(Cursor.inCameraMode()) {
            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
            deltaCursorPos.scale(camera.getMouseSensitivity());
            deltaCamRot = new Vector3f(deltaCursorPos.y, deltaCursorPos.x, 0);
            camera.changeRotation(deltaCamRot);
        }

        //Handles frame switching
        if(Keyboard.isKeyDown(GLFW_KEY_UP)){
            scene.incrementFrame();
        }else if(Keyboard.isKeyDown(GLFW_KEY_DOWN)){
            scene.decrementFrame();
        }

        if(Keyboard.isKeyTapped(GLFW_KEY_T)){
            scene.togglwAxesVisibility();
        }

        //Essential static updates
        Keyboard.update();
        MouseButtons.update();
        Cursor.update(window.getPrimaryMonitor());
        MouseWheel.update();
    }

    private void renderFrame(){
        renderer.render(window, camera, scene);
        window.update();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("jeff");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("jeff");
    }
}
