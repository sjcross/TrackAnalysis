package wbif.sjx.TrackAnalysis.Plot3D.Core;

import wbif.sjx.TrackAnalysis.GUI.TrackPlotControl;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseWheel;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Engine {
    private TrackPlotControl trackPlotControl;
    private boolean running;
    private GLFWWindow window;
    private Renderer renderer;
    private Camera camera;
    private Scene scene;

    public Engine(TrackPlotControl trackPlotControl){
        this.trackPlotControl = trackPlotControl;
    }

    public void init() throws Exception{

        window = new GLFWWindow("3D Track Plot", 600, 600, true);
        renderer = new Renderer();
        camera = new Camera();
        scene = new Scene(trackPlotControl.getTracks(), trackPlotControl.getIpl());

        camera.getPosition().set(400,400,100);
        camera.facePoint(scene.getTracksEntities().getCurrentCentreOfCollection());
    }

    public void start() throws Exception{
        mainLoop();
    }

    public void stop() {
        running = false;
    }

    private void mainLoop() {
        window.setVisibility(true);
        running = true;

        while (running && window.isOpen()){
            handleInput();
            update();
            renderFrame();
        }

        window.setVisibility(false);
        running = false;
    }

    public void dispose(){
        renderer.dispose();
        scene.dispose();
        window.dispose();
    }

    private void handleInput(){
        //Handles camera movement
        Vector3f deltaCamPos = new Vector3f();
        float cameraPositionStep = camera.getCameraPositionStep();

        //left right
        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            deltaCamPos.setX(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_D)){
            deltaCamPos.setX(cameraPositionStep);
        }

        //forwards backwards
        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            deltaCamPos.setY(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            deltaCamPos.setY(cameraPositionStep);
        }

        //up down
        if(Keyboard.isKeyDown(GLFW_KEY_E)){
            deltaCamPos.setZ(-cameraPositionStep);
        } else if(Keyboard.isKeyDown(GLFW_KEY_Q)){
            deltaCamPos.setZ(cameraPositionStep);
        }

        //movement speed boost
        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            deltaCamPos.scale(10);
        }

        camera.changePositionRelativeToOrientation(deltaCamPos);

        //Handles camera rotation
        if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT) || MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
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

        scene.changeFrame(MouseWheel.getDeltaScroll());

        if(Keyboard.isKeyTapped(GLFW_KEY_F)) {
            scene.togglePlayFrames();
        }

        if(Keyboard.isKeyTapped(GLFW_KEY_P)) {
            camera.toggleFaceCentre();
        }

        if(Keyboard.isKeyDown(GLFW_KEY_M)) {
            camera.facePoint(DataTypeUtils.toVector3f(trackPlotControl.getTracks().getMeanPoint(0)));
        }else if(Keyboard.isKeyDown(GLFW_KEY_O)){
            camera.facePoint(0,0,0);
        }

        //Essential static updates
        Keyboard.update();
        Cursor.update(window.getPrimaryMonitor());
        MouseButtons.update();
        MouseWheel.update();
    }

    private void update(){
        scene.update();
        camera.update(scene);
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

    public Renderer getRenderer() {
        return renderer;
    }

    public GLFWWindow getWindow() {
        return window;
    }
}
