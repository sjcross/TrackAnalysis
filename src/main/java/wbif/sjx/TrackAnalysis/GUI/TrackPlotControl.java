package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Camera;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Engine;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Renderer;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntityCollection;
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
    private final static String PLOT = "Plot tracks";

    private JCheckBox showTrailCheckbox;
    private final static String SHOW_TRAILS = "Show trails";

    private JCheckBox showAxesCheckbox;
    private final static String SHOW_AXES = "Show axes";

    private JCheckBox showBoundingBoxCheckbox;
    private final static String SHOW_BOUNDING_BOX = "Show bounding box";

    private JCheckBox motilityPlotCheckbox;
    private final static String MOTILITY_PLOT = "Motility plot";

    private JCheckBox faceCentreCheckbox;
    private final static String FACE_CENTRE = "Face centre";

    private JSlider orbitVelocitySlider;
    private final static String ORBIT_VELOCITY = "Orbit velocity";

    private JSlider FOVSlider;
    private final static String FOV_SLIDER = "FOV";

    private JSlider trailLengthSlider;
    private final static String TRAIL_LENGTH_SLIDER = "Trail length";

    private JSlider frameSlider;
    private final static String FRAME_SLIDER = "Frame Slider";

    private JButton incrementFrameButton;
    private final static String ADD_FRAME = "Frame ++";

    private JButton decrementFrameButton;
    private final static String SUB_FRAME = "Frame --";

    private JToggleButton playFramesButton;
    private final static String PLAY_FRAMES = "Play";

    private JButton viewXZplaneButton;
    private final static String XZ_PLANE = "XZ plane";

    private JButton viewYZplaneButton;
    private final static String YZ_PLANE = "YZ plane";

    private JButton viewXYplaneButton;
    private final static String XY_PLANE = "XY plane";

    private JComboBox<TrackEntityCollection.displayColourOptions> displayColourComboBox;
    private final static String DISPLAY_COLOUR = "Display colour";

    private static Engine engine;
    private TrackCollection tracks;


    public TrackPlotControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
        this.tracks = tracks;
        if (engine == null) {
            engine = new Engine();
        }
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

        showTrailCheckbox = new JCheckBox(SHOW_TRAILS);
        showTrailCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showTrailCheckbox.setSelected(TrackEntityCollection.showTrail_DEFAULT);
        showTrailCheckbox.setEnabled(false);
        showTrailCheckbox.addActionListener(this);
        showTrailCheckbox.setName(SHOW_TRAILS);
        c.gridy++;
        panel.add(showTrailCheckbox,c);

        showAxesCheckbox = new JCheckBox(SHOW_AXES);
        showAxesCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showAxesCheckbox.setSelected(Scene.showAxes_DEFAULT);
        showAxesCheckbox.setEnabled(false);
        showAxesCheckbox.setName(SHOW_AXES);
        showAxesCheckbox.addActionListener(this);
        c.gridy++;
        panel.add(showAxesCheckbox,c);

        showBoundingBoxCheckbox = new JCheckBox(SHOW_BOUNDING_BOX);
        showBoundingBoxCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showBoundingBoxCheckbox.setSelected(Scene.showBoundingBox_DEFAULT);
        showBoundingBoxCheckbox.setEnabled(false);
        showBoundingBoxCheckbox.setName(SHOW_BOUNDING_BOX);
        showBoundingBoxCheckbox.addActionListener(this);
        c.gridy++;
        panel.add(showBoundingBoxCheckbox,c);

        motilityPlotCheckbox = new JCheckBox(MOTILITY_PLOT);
        motilityPlotCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        motilityPlotCheckbox.setSelected(TrackEntityCollection.motilityPlot_DEFAULT);
        motilityPlotCheckbox.setEnabled(false);
        motilityPlotCheckbox.setName(MOTILITY_PLOT);
        motilityPlotCheckbox.addActionListener(this);
        c.gridy++;
        panel.add(motilityPlotCheckbox,c);

        faceCentreCheckbox = new JCheckBox(FACE_CENTRE);
        faceCentreCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        faceCentreCheckbox.setSelected(Camera.faceCentre_DEFAULT);
        faceCentreCheckbox.setEnabled(false);
        faceCentreCheckbox.setName(FACE_CENTRE);
        faceCentreCheckbox.addActionListener(this);
        c.gridy++;
        panel.add(faceCentreCheckbox,c);

        orbitVelocitySlider = new JSlider(JSlider.HORIZONTAL, Camera.orbitVelocity_MINIMUM, Camera.orbitVelocity_MAXIMUM, Camera.orbitVelocity_DEFAULT);
        orbitVelocitySlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        orbitVelocitySlider.setEnabled(false);
        orbitVelocitySlider.addChangeListener(this);
        orbitVelocitySlider.setName(ORBIT_VELOCITY);
        c.gridy++;
        panel.add(new JLabel(ORBIT_VELOCITY),c);
        c.gridy++;
        panel.add(orbitVelocitySlider,c);

        FOVSlider = new JSlider(JSlider.HORIZONTAL, Camera.FOV_MINIMUM, Camera.FOV_MAXIMUM, Camera.FOV_DEFAULT);
        FOVSlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        FOVSlider.setEnabled(false);
        FOVSlider.addChangeListener(this);
        FOVSlider.setName(FOV_SLIDER);
        c.gridy++;
        panel.add(new JLabel(FOV_SLIDER),c);
        c.gridy++;
        panel.add(FOVSlider,c);

        final int highestFrame = tracks.getHighestFrame();
        final int trailLength_MAXIMUM = highestFrame < 1 ? 1 : highestFrame;
        trailLengthSlider = new JSlider(JSlider.HORIZONTAL, TrackEntityCollection.trailLength_MINIMUM, trailLength_MAXIMUM, trailLength_MAXIMUM);
        trailLengthSlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        trailLengthSlider.setEnabled(false);
        trailLengthSlider.addChangeListener(this);
        trailLengthSlider.setName(TRAIL_LENGTH_SLIDER);
        c.gridy++;
        panel.add(new JLabel(TRAIL_LENGTH_SLIDER),c);
        c.gridy++;
        panel.add(trailLengthSlider,c);

        frameSlider = new JSlider(JSlider.HORIZONTAL, 0, tracks.getHighestFrame(), Scene.frame_DEFAULT);
        frameSlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        frameSlider.setEnabled(false);
        frameSlider.addChangeListener(this);
        frameSlider.setName(FRAME_SLIDER);
        c.gridy++;
        panel.add(new JLabel(FRAME_SLIDER),c);
        c.gridy++;
        panel.add(frameSlider,c);

        incrementFrameButton = new JButton(ADD_FRAME);
        incrementFrameButton.setPreferredSize(new Dimension(panelWidth/3,elementHeight));
        incrementFrameButton.setEnabled(false);
        incrementFrameButton.addActionListener(this);
        incrementFrameButton.setName(ADD_FRAME);

        decrementFrameButton = new JButton(SUB_FRAME);
        decrementFrameButton.setPreferredSize(new Dimension(panelWidth/3,elementHeight));
        decrementFrameButton.setEnabled(false);
        decrementFrameButton.addActionListener(this);
        decrementFrameButton.setName(SUB_FRAME);

        playFramesButton = new JToggleButton(PLAY_FRAMES);
        playFramesButton.setPreferredSize(new Dimension(panelWidth/3,elementHeight));
        playFramesButton.setSelected(Scene.playFrames_DEFAULT);
        playFramesButton.setEnabled(false);
        playFramesButton.addActionListener(this);
        playFramesButton.setName(PLAY_FRAMES);

        c.gridwidth = 1;
        c.gridy++;
        panel.add(incrementFrameButton,c);
        c.gridx++;
        panel.add(decrementFrameButton,c);
        c.gridx++;
        panel.add(playFramesButton,c);

        viewXZplaneButton = new JButton(XZ_PLANE);
        viewXZplaneButton.setPreferredSize(new Dimension(panelWidth/3,elementHeight));
        viewXZplaneButton.setEnabled(false);
        viewXZplaneButton.addActionListener(this);
        viewXZplaneButton.setName(XZ_PLANE);

        viewYZplaneButton = new JButton(YZ_PLANE);
        viewYZplaneButton.setPreferredSize(new Dimension(panelWidth/3,elementHeight));
        viewYZplaneButton.setEnabled(false);
        viewYZplaneButton.addActionListener(this);
        viewYZplaneButton.setName(YZ_PLANE);

        viewXYplaneButton = new JButton(XY_PLANE);
        viewXYplaneButton.setPreferredSize(new Dimension(panelWidth/3,elementHeight));
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

        displayColourComboBox = new JComboBox<>(TrackEntityCollection.displayColourOptions.values());
        displayColourComboBox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        displayColourComboBox.setSelectedItem(TrackEntityCollection.displayColour_DEFAULT);
        displayColourComboBox.setName(DISPLAY_COLOUR);
        displayColourComboBox.setEnabled(false);
        displayColourComboBox.addActionListener(this);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel(DISPLAY_COLOUR),c);
        c.gridy++;
        c.insets = new Insets(0,5,20,5);
        panel.add(displayColourComboBox,c);

        return panel;

    }

    @Override
    public void run(int ID) {
        new Thread(() -> {
            engine.init(this);
            plotAllButton.setEnabled(true);
            showTrailCheckbox.setEnabled(false);
            showAxesCheckbox.setEnabled(false);
            showBoundingBoxCheckbox.setEnabled(false);
            motilityPlotCheckbox.setEnabled(false);
            faceCentreCheckbox.setEnabled(false);
            orbitVelocitySlider.setEnabled(false);
            FOVSlider.setEnabled(false);
            trailLengthSlider.setEnabled(false);
            frameSlider.setEnabled(false);
            incrementFrameButton.setEnabled(false);
            decrementFrameButton.setEnabled(false);
            playFramesButton.setEnabled(false);
            viewXZplaneButton.setEnabled(false);
            viewYZplaneButton.setEnabled(false);
            viewXYplaneButton.setEnabled(false);
            displayColourComboBox.setEnabled(false);
        }).start();

        plotAllButton.setEnabled(false);
        showTrailCheckbox.setEnabled(true);
        showAxesCheckbox.setEnabled(true);
        showBoundingBoxCheckbox.setEnabled(true);
        motilityPlotCheckbox.setEnabled(true);
        faceCentreCheckbox.setEnabled(true);
        orbitVelocitySlider.setEnabled(true);
        FOVSlider.setEnabled(true);
        trailLengthSlider.setEnabled(true);
        frameSlider.setEnabled(true);
        incrementFrameButton.setEnabled(true);
        decrementFrameButton.setEnabled(true);
        playFramesButton.setEnabled(true);
        viewXZplaneButton.setEnabled(true);
        viewYZplaneButton.setEnabled(true);
        viewXYplaneButton.setEnabled(true);
        displayColourComboBox.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = ((JComponent) e.getSource()).getName();

        switch (action){
            case PLOT:
                run(-1);
                break;
            case SHOW_TRAILS:
                engine.getScene().getTracksEntities().setTrailVisibility(((JCheckBox)e.getSource()).isSelected());
                break;
            case SHOW_AXES:
                Scene.setAxesVisibility(((JCheckBox)e.getSource()).isSelected());
                break;
            case SHOW_BOUNDING_BOX:
                Scene.setBoundingBoxVisibility(((JCheckBox)e.getSource()).isSelected());
                break;
            case MOTILITY_PLOT:
                engine.getScene().getTracksEntities().setMotilityPlot(((JCheckBox)e.getSource()).isSelected());
                break;
            case FACE_CENTRE:
                engine.getCamera().setFaceCentre(((JCheckBox)e.getSource()).isSelected());
                break;
            case ADD_FRAME:
                engine.getScene().incrementFrame();
                break;
            case SUB_FRAME:
                engine.getScene().decrementFrame();
                break;
            case PLAY_FRAMES:
                engine.getScene().setPlayFrames(((JToggleButton)e.getSource()).isSelected());
                break;
            case XZ_PLANE:
                engine.getCamera().viewXZplane(Scene.getBoundingBox());
                break;
            case YZ_PLANE:
                engine.getCamera().viewYZplane(Scene.getBoundingBox());
                break;
            case XY_PLANE:
                engine.getCamera().viewXYplane(Scene.getBoundingBox());
                break;
            case DISPLAY_COLOUR:
                engine.getScene().getTracksEntities().displayColour = (TrackEntityCollection.displayColourOptions)((JComboBox)e.getSource()).getSelectedItem();;
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
            case ORBIT_VELOCITY:
                engine.getCamera().setOrbitVelocity(((JSlider)e.getSource()).getValue());
                break;
            case FOV_SLIDER:
                engine.getCamera().setFOV(((JSlider)e.getSource()).getValue());
                break;
            case TRAIL_LENGTH_SLIDER:
                engine.getScene().getTracksEntities().setTrailLength(((JSlider)e.getSource()).getValue());
                break;
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
        faceCentreCheckbox.setSelected(engine.getCamera().isFacingCentre());
        orbitVelocitySlider.setEnabled(faceCentreCheckbox.isSelected());
        viewXZplaneButton.setEnabled(!faceCentreCheckbox.isSelected());
        viewYZplaneButton.setEnabled(!faceCentreCheckbox.isSelected());
        viewXYplaneButton.setEnabled(!faceCentreCheckbox.isSelected());
    }

    public TrackCollection getTracks() {
        return tracks;
    }
}
