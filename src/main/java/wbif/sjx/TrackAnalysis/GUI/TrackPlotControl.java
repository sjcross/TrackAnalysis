package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Camera;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Engine;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Scene;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackPlotControl extends ModuleControl implements ChangeListener {

    private JButton plotAllButton;
    private final static String PLOT = "Show tracks";

    public interface PlotTypes{
        String NORMAL = "Normal";
        String MOTILITY = "Motility";

        String[] ALL = new String[]{NORMAL,MOTILITY};
    }
    private JTextField plotTypeTextField;
    private JComboBox<String> plotTypeComboBox;
    private final static String PLOT_TYPE = "Plot type";

    public interface AxesTypes {
        String NONE = "None";
        String AXES = "Axes only";
        String BOX = "Box only";
        String BOTH = "Both";

        String[] ALL = new String[]{NONE,AXES,BOX,BOTH};
    }
    private JTextField axesTypeTextField;
    private JComboBox<String> axesTypeComboBox;
    private final static String AXES_TYPE = "Axes type";

    private JToggleButton orbitButton;
    private JTextField orbitSpeedLabelTextField;
    private JTextField orbitSpeedTextEdit;
    private final static String ORBIT = "Orbit";
    private final static String ORBIT_SPEED = "degrees/second";

    private JToggleButton showTrailsButton;
    private JTextField trailLengthLabelTextField;
    private JTextField trailLengthTextEdit;
    private final static String SHOW_TRAILS = "Trails";
    private final static String TRAILS_LENGTH = "frames";

    private JToggleButton playFramesButton;
    private JTextField frameRateLabelTextField;
    private JTextField frameRateTextEdit;
    private final static String PLAY_FRAMES = "Play";
    private final static String FRAME_RATE = "frames/second";

    private JSlider frameSlider;
    private final static String FRAME_SLIDER = "Frame Slider";

    private JButton viewXZplaneButton;
    private final static String XZ_PLANE = "XZ plane";

    private JButton viewYZplaneButton;
    private final static String YZ_PLANE = "YZ plane";

    private JButton viewXYplaneButton;
    private final static String XY_PLANE = "XY plane";

    private JButton screenShotButton;
    private final static String SCREEN_SHOT = "Screen shot";

    private JTextField displayQualityTextField;
    private JComboBox<RenderQuality> displayQualityComboBox;
    private final static String DISPLAY_QUALITY = "Display quality";

    private JTextField displayColourTextField;
    private JComboBox<DisplayColour> displayColourComboBox;
    private final static String DISPLAY_COLOUR = "Display colour";

    private final Engine engine;
    private TrackCollection tracks;
    private ImagePlus ipl;

    public TrackPlotControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
        this.tracks = tracks;
        this.ipl = ipl;
        engine = new Engine(this);
    }

    @Override
    public String getTitle() {
        return "Track plot";
    }

    @Override
    public JPanel getCommonControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(20,5,20,5);

        // Button to run ensemble plotting
        plotAllButton = new JButton(PLOT);
        plotAllButton.setPreferredSize(new Dimension(panelWidth,elementHeight));
        plotAllButton.addActionListener(this);
        plotAllButton.setName(PLOT);
        panel.add(plotAllButton,c);

        return panel;
    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0,5,5,5);


        plotTypeTextField = new JTextField(PLOT_TYPE);
        plotTypeTextField.setEditable(false);
        plotTypeTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        plotTypeTextField.setBorder(null);
        plotTypeTextField.setEnabled(false);
        c.gridwidth = 1;
        c.gridy++;
        panel.add(plotTypeTextField,c);
        plotTypeComboBox = new JComboBox<>(PlotTypes.ALL);
        plotTypeComboBox.setPreferredSize(new Dimension(panelWidth*2/3-5,elementHeight));
        plotTypeComboBox.setSelectedItem(PlotTypes.NORMAL);
        plotTypeComboBox.setName(PLOT_TYPE);
        plotTypeComboBox.setEnabled(false);
        plotTypeComboBox.addActionListener(this);
        c.gridx++;
        c.gridwidth = 2;
        panel.add(plotTypeComboBox,c);

        axesTypeTextField = new JTextField(AXES_TYPE);
        axesTypeTextField.setEditable(false);
        axesTypeTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        axesTypeTextField.setBorder(null);
        axesTypeTextField.setEnabled(false);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(axesTypeTextField,c);
        axesTypeComboBox = new JComboBox<>(AxesTypes.ALL);
        axesTypeComboBox.setPreferredSize(new Dimension(panelWidth*2/3-5,elementHeight));
        axesTypeComboBox.setSelectedItem(AxesTypes.BOX);
        axesTypeComboBox.setName(AXES_TYPE);
        axesTypeComboBox.setEnabled(false);
        axesTypeComboBox.addActionListener(this);
        c.gridx++;
        c.gridwidth = 2;
        panel.add(axesTypeComboBox,c);

        displayQualityTextField = new JTextField(DISPLAY_QUALITY);
        displayQualityTextField.setEditable(false);
        displayQualityTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        displayQualityTextField.setBorder(null);
        displayQualityTextField.setEnabled(false);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(displayQualityTextField,c);
        displayQualityComboBox = new JComboBox<>(RenderQuality.values());
        displayQualityComboBox.setPreferredSize(new Dimension(panelWidth*2/3-5,elementHeight));
        displayQualityComboBox.setSelectedItem(RenderQuality.LOW);
        displayQualityComboBox.setName(DISPLAY_QUALITY);
        displayQualityComboBox.setEnabled(false);
        displayQualityComboBox.addActionListener(this);
        c.gridx++;
        c.gridwidth = 2;
        panel.add(displayQualityComboBox,c);

        displayColourTextField = new JTextField(DISPLAY_COLOUR);
        displayColourTextField.setEditable(false);
        displayColourTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        displayColourTextField.setBorder(null);
        displayColourTextField.setEnabled(false);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(displayColourTextField,c);
        displayColourComboBox = new JComboBox<>(DisplayColour.values());
        displayColourComboBox.setPreferredSize(new Dimension(panelWidth*2/3-5,elementHeight));
        displayColourComboBox.setSelectedItem(DisplayColour.ID);
        displayColourComboBox.setName(DISPLAY_COLOUR);
        displayColourComboBox.setEnabled(false);
        displayColourComboBox.addActionListener(this);
        c.gridx++;
        c.gridwidth = 2;
        panel.add(displayColourComboBox,c);

        showTrailsButton = new JToggleButton(SHOW_TRAILS);
        showTrailsButton.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        showTrailsButton.setSelected(true);
        showTrailsButton.setEnabled(false);
        showTrailsButton.setSelected(true);
        showTrailsButton.addActionListener(this);
        showTrailsButton.setName(SHOW_TRAILS);
        c.gridwidth = 1;
        c.gridx=0;
        c.gridy++;
        panel.add(showTrailsButton,c);
        final int highestFrame = tracks.getHighestFrame();
        final int trailLength_MAXIMUM = highestFrame < 1 ? 1 : highestFrame;
        trailLengthTextEdit = new JTextField(String.valueOf(trailLength_MAXIMUM));
        trailLengthTextEdit.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        trailLengthTextEdit.setEnabled(false);
        trailLengthTextEdit.addActionListener(this);
        trailLengthTextEdit.setName(TRAILS_LENGTH);
        trailLengthTextEdit.setEditable(true);
        c.gridx++;
        panel.add(trailLengthTextEdit,c);
        trailLengthLabelTextField = new JTextField(TRAILS_LENGTH);
        trailLengthLabelTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        trailLengthLabelTextField.setEnabled(false);
        trailLengthLabelTextField.setEditable(false);
        trailLengthLabelTextField.setBorder(null);
        c.gridx++;
        panel.add(trailLengthLabelTextField,c);

        orbitButton = new JToggleButton(ORBIT);
        orbitButton.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        orbitButton.setEnabled(false);
        orbitButton.addActionListener(this);
        orbitButton.setName(ORBIT);
        c.gridx=0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(orbitButton,c);
        orbitSpeedTextEdit = new JTextField(String.valueOf(Camera.DEF_ANGULAR_VELOCITY));
        orbitSpeedTextEdit.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        orbitSpeedTextEdit.setEnabled(false);
        orbitSpeedTextEdit.addActionListener(this);
        orbitSpeedTextEdit.setName(ORBIT_SPEED);
        orbitSpeedTextEdit.setEditable(true);
        c.gridx++;
        panel.add(orbitSpeedTextEdit,c);
        orbitSpeedLabelTextField = new JTextField(ORBIT_SPEED);
        orbitSpeedLabelTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        orbitSpeedLabelTextField.setEnabled(false);
        orbitSpeedLabelTextField.setEditable(false);
        orbitSpeedLabelTextField.setBorder(null);
        c.gridx++;
        panel.add(orbitSpeedLabelTextField,c);

        playFramesButton = new JToggleButton(PLAY_FRAMES);
        playFramesButton.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        playFramesButton.setSelected(false);
        playFramesButton.setEnabled(false);
        playFramesButton.addActionListener(this);
        playFramesButton.setName(PLAY_FRAMES);
        c.gridx=0;
        c.gridy++;
        panel.add(playFramesButton,c);
        frameRateTextEdit = new JTextField(String.valueOf(Scene.DEF_FRAME_PLAYBACK_RATE));
        frameRateTextEdit.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        frameRateTextEdit.setEnabled(false);
        frameRateTextEdit.addActionListener(this);
        frameRateTextEdit.setName(FRAME_RATE);
        frameRateTextEdit.setEditable(true);
        c.gridx++;
        panel.add(frameRateTextEdit,c);
        frameRateLabelTextField = new JTextField(FRAME_RATE);
        frameRateLabelTextField.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        frameRateLabelTextField.setEnabled(false);
        frameRateLabelTextField.setEditable(false);
        frameRateLabelTextField.setBorder(null);
        c.gridx++;
        panel.add(frameRateLabelTextField,c);

        frameSlider = new JSlider(JSlider.HORIZONTAL, 0, tracks.getHighestFrame(), 0);
        frameSlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        frameSlider.setEnabled(false);
        frameSlider.addChangeListener(this);
        frameSlider.setName(FRAME_SLIDER);
        c.gridx = 0;
        c.gridwidth=3;
        c.gridy++;
        panel.add(frameSlider,c);

        viewXZplaneButton = new JButton(XZ_PLANE);
        viewXZplaneButton.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        viewXZplaneButton.setEnabled(false);
        viewXZplaneButton.addActionListener(this);
        viewXZplaneButton.setName(XZ_PLANE);
        viewYZplaneButton = new JButton(YZ_PLANE);
        viewYZplaneButton.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        viewYZplaneButton.setEnabled(false);
        viewYZplaneButton.addActionListener(this);
        viewYZplaneButton.setName(YZ_PLANE);
        viewXYplaneButton = new JButton(XY_PLANE);
        viewXYplaneButton.setPreferredSize(new Dimension(panelWidth/3-5,elementHeight));
        viewXYplaneButton.setEnabled(false);
        viewXYplaneButton.addActionListener(this);
        viewXYplaneButton.setName(XY_PLANE);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(viewXZplaneButton,c);
        c.gridx++;
        panel.add(viewYZplaneButton,c);
        c.gridx++;
        panel.add(viewXYplaneButton,c);

        screenShotButton = new JButton(SCREEN_SHOT);
        screenShotButton.setPreferredSize(new Dimension(panelWidth,elementHeight));
        screenShotButton.setEnabled(false);
        screenShotButton.addActionListener(this);
        screenShotButton.setName(SCREEN_SHOT);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy++;
        panel.add(screenShotButton,c);

        return panel;
    }

    @Override
    public void run(int ID) {
        new Thread(() -> {
            try{
                engine.init();
                setControlMode(true);
                engine.start();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                setControlMode(false);
                engine.dispose();
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = ((JComponent) e.getSource()).getName();

        switch (action){
            case PLOT:
                plotAllButton.setEnabled(false);
                run(-1);
                break;
            case PLOT_TYPE:
                switch ((String) ((JComboBox) e.getSource()).getSelectedItem()) {
                    case PlotTypes.NORMAL:
                        engine.getScene().setMotilityPlot(false);
                        viewXYplaneButton.setEnabled(true);
                        viewXZplaneButton.setEnabled(true);
                        viewYZplaneButton.setEnabled(true);
                        break;
                    case PlotTypes.MOTILITY:
                        engine.getScene().setMotilityPlot(true);
                        viewXYplaneButton.setEnabled(false);
                        viewXZplaneButton.setEnabled(false);
                        viewYZplaneButton.setEnabled(false);
                        break;
                }
                break;
            case AXES_TYPE:
                switch ((String) ((JComboBox) e.getSource()).getSelectedItem()) {
                    case AxesTypes.NONE:
                        engine.getScene().setAxesVisibility(false);
                        engine.getScene().setBoundingBoxVisibility(false);
                        break;
                    case AxesTypes.AXES:
                        engine.getScene().setAxesVisibility(true);
                        engine.getScene().setBoundingBoxVisibility(false);
                        break;
                    case AxesTypes.BOX:
                        engine.getScene().setAxesVisibility(false);
                        engine.getScene().setBoundingBoxVisibility(true);
                        break;
                    case AxesTypes.BOTH:
                        engine.getScene().setAxesVisibility(true);
                        engine.getScene().setBoundingBoxVisibility(true);
                        break;
                }
                break;
            case PLAY_FRAMES:
                engine.getScene().setPlayFrames(((JToggleButton)e.getSource()).isSelected());
                int frameRate = Integer.parseInt(frameRateTextEdit.getText());
                engine.getScene().setFramePlaybackRate(frameRate);
                break;
            case FRAME_RATE:
                frameRate = Integer.parseInt(frameRateTextEdit.getText());
                engine.getScene().setFramePlaybackRate(frameRate);
                break;
            case ORBIT:
                engine.getScene().getCamera().setOrbit(((JToggleButton)e.getSource()).isSelected());
                engine.getRenderer().disableOrthoProj();
                int angularVelocity = Integer.parseInt(orbitSpeedTextEdit.getText());
                engine.getScene().getCamera().setAngularVelocity(angularVelocity);
                break;
            case ORBIT_SPEED:
                angularVelocity = Integer.parseInt(orbitSpeedTextEdit.getText());
                engine.getScene().getCamera().setAngularVelocity(angularVelocity);
                break;
            case SHOW_TRAILS:
                engine.getScene().setTrailVisibility(((JToggleButton)e.getSource()).isSelected());
                int trailLength = Integer.parseInt(trailLengthTextEdit.getText());
                engine.getScene().setTrailLength(trailLength);
                break;
            case TRAILS_LENGTH:
                trailLength = Integer.parseInt(trailLengthTextEdit.getText());
                engine.getScene().setTrailLength(trailLength);
                break;
            case XZ_PLANE:
                engine.viewXZplane();
                break;
            case YZ_PLANE:
                engine.viewYZplane();
                break;
            case XY_PLANE:
                engine.viewXYplane();
                break;
            case SCREEN_SHOT:
                engine.getRenderer().takeScreenshot();
                break;
            case DISPLAY_QUALITY:
                engine.getScene().setRenderQuality((RenderQuality)((JComboBox)e.getSource()).getSelectedItem());
                engine.getRenderer().setBindMeshBuffers(true);
                break;
            case DISPLAY_COLOUR:
                DisplayColour displayColour = (DisplayColour)((JComboBox)e.getSource()).getSelectedItem();
                engine.getScene().setDisplayColour(displayColour);
                engine.getRenderer().setBindColourBuffers(displayColour != DisplayColour.ID);
                break;
            default:
                break;
        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String action = ((Component) e.getSource()).getName();

        switch (action){
            case FRAME_SLIDER:
                engine.getScene().setFrame(((JSlider)e.getSource()).getValue());
                break;
            default:
                break;
        }
    }

    public void updateGui(){
        frameSlider.setValue(engine.getScene().getFrame());
        playFramesButton.setSelected(engine.getScene().isPlayingFrames());
    }

    public void setControlMode(boolean state){
        plotAllButton.setEnabled(!state);
        plotTypeTextField.setEnabled(state);
        plotTypeComboBox.setEnabled(state);
        axesTypeTextField.setEnabled(state);
        axesTypeComboBox.setEnabled(state);
        orbitButton.setEnabled(state);
        orbitSpeedTextEdit.setEnabled(state);
        orbitSpeedLabelTextField.setEnabled(state);
        showTrailsButton.setEnabled(state);
        trailLengthTextEdit.setEnabled(state);
        trailLengthLabelTextField.setEnabled(state);
        frameSlider.setEnabled(state);
        playFramesButton.setEnabled(state);
        frameRateTextEdit.setEnabled(state);
        frameRateLabelTextField.setEnabled(state);
        viewXZplaneButton.setEnabled(state);
        viewYZplaneButton.setEnabled(state);
        viewXYplaneButton.setEnabled(state);
        screenShotButton.setEnabled(state);
        displayQualityTextField.setEnabled(state);
        displayQualityComboBox.setEnabled(state);
        displayColourTextField.setEnabled(state);
        displayColourComboBox.setEnabled(state);
    }

    public enum DisplayColour
    {
        ID,
        TOTAL_PATH_LENGTH,
        VELOCITY
    }

    public enum RenderQuality
    {
        LOWEST,
        LOW,
        MEDIUM,
        HIGH
    }

    public TrackCollection getTracks() {
        return tracks;
    }

    public ImagePlus getIpl() {
        return ipl;
    }

    public boolean is2D() {
        double[][] limits = tracks.getSpatialLimits(true);

        return Math.abs(limits[2][0] - limits[2][1]) < 0.5;
    }
}
