package wbif.sjx.TrackAnalysis.Plot3D.Core;

import wbif.sjx.TrackAnalysis.GUI.TrackPlotControl;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Cursor;
import wbif.sjx.TrackAnalysis.Plot3D.Input.Keyboard;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseButtons;
import wbif.sjx.TrackAnalysis.Plot3D.Input.MouseWheel;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.StopWatch;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Engine {

    private static final int TARGET_UPDATES_PER_SECOND = 600;

    private TrackPlotControl trackPlotControl;
    private boolean running;
    private GLFWWindow window;
    private Renderer renderer;
    private Camera camera;
    private Scene scene;
    private StopWatch fpsTimer;
    private StopWatch loopTimer;

    private int frameCount;

    public Engine(TrackPlotControl trackPlotControl){
        this.trackPlotControl = trackPlotControl;
    }

    public void init() throws Exception{
        window = new GLFWWindow("3D Track Plot", 600, 600, false);
        renderer = new Renderer();
        camera = new Camera();
        scene = new Scene(trackPlotControl.getTracks(), trackPlotControl.getIpl());
        fpsTimer = new StopWatch();
        loopTimer = new StopWatch();

        camera.getPosition().set(-200,100,-200);
        camera.facePoint(scene.getTracksEntities().getCurrentCentreOfCollection());
    }

    public void start() throws Exception{
        window.setVisibility(true);
        running = true;
        mainLoop();
    }

    public void stop() {
        running = false;
    }

    private void mainLoop() {
        float timeElapsedDuringLastLoop;
        float cumulativeTime = 0f;
        final float TARGET_SECONDS_PER_UPDATE = 1f / (float)TARGET_UPDATES_PER_SECOND;
        final float TARGET_SECONDS_PER_FRAME = 1f / window.getRefreshRate();
        loopTimer.start();
        fpsTimer.start();

        while (running && window.isOpen()){
            timeElapsedDuringLastLoop = loopTimer.getElapsedTime();
            cumulativeTime += timeElapsedDuringLastLoop;
            loopTimer.start();

            handleInput();

            while (cumulativeTime >= TARGET_SECONDS_PER_UPDATE) {
                update(TARGET_SECONDS_PER_UPDATE);
                cumulativeTime -= TARGET_SECONDS_PER_UPDATE;
            }

            renderFrame();

            if (window.isVSyncEnabled()) {
                sync(TARGET_SECONDS_PER_FRAME);
            }

            loopTimer.stop();
        }

        window.setVisibility(false);
        running = false;
    }

    private void sync(float TARGET_SECONDS_PER_FRAME) {
        double expectedEndTime = loopTimer.getLoopStartTime() + TARGET_SECONDS_PER_FRAME;
        while (loopTimer.getTime() < expectedEndTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void dispose(){
        renderer.dispose();
        scene.dispose();
        window.dispose();
    }

    private void handleInput(){
        //Handles camera movement

        Vector3f cameraMovementDirection = new Vector3f();

        //left right
        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            cameraMovementDirection.setX(-1);
        } else if(Keyboard.isKeyDown(GLFW_KEY_D)){
            cameraMovementDirection.setX(1);
        }

        //forwards backwards
        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            cameraMovementDirection.setZ(-1);
        } else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            cameraMovementDirection.setZ(1);
        }

        //up down
        if(Keyboard.isKeyDown(GLFW_KEY_E)){
            cameraMovementDirection.setY(-1);
        } else if(Keyboard.isKeyDown(GLFW_KEY_Q)){
            cameraMovementDirection.setY(1);
        }

        //movement speed boost
        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            cameraMovementDirection.multiply(10);
        }else if(Keyboard.isKeyDown(GLFW_KEY_LEFT_ALT)){
            cameraMovementDirection.multiply(0.1f);
        }

        camera.getMovementDirection().set(cameraMovementDirection);

        //Handles camera EulerRotation
        if(MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_LEFT) || MouseButtons.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f deltaCursorPos = Cursor.getDeltaPosition();
            deltaCursorPos.scale(camera.getMouseSensitivity());
            camera.changeTilt(-deltaCursorPos.y);
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
        Cursor.update();
        MouseButtons.update();
        MouseWheel.update();
    }

    private void update(float interval){
        scene.update(interval);
        camera.update(interval, scene.getTracksEntities().getCurrentCentreOfCollection());
        trackPlotControl.updateGui();
    }

    private void renderFrame() {
        if (fpsTimer.getElapsedTime() > 1) {
            fpsTimer.restart();
            window.appendToTitle(String.format("FPS: %d", frameCount));
            frameCount = 0;
        }
        frameCount++;

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
