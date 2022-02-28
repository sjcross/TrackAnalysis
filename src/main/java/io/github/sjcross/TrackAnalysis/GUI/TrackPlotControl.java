package io.github.sjcross.TrackAnalysis.GUI;

import ij.ImagePlus;
import io.github.sjcross.TrackAnalysis.GUI.Control.BasicModule;
import io.github.sjcross.TrackAnalysis.Plot3D.Core.Camera;
import io.github.sjcross.TrackAnalysis.Plot3D.Core.Engine;
import io.github.sjcross.TrackAnalysis.Plot3D.Core.Scene;
import io.github.sjcross.common.object.tracks.TrackCollection;

import javax.swing.*;
import java.awt.*;

import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class TrackPlotControl extends BasicModule
{
    private JButton showTracksButton;
    private JTextField axesTypeTextField;
    private JComboBox<AxesType> axesTypeComboBox;
    private JToggleButton orbitButton;
    private JTextField orbitSpeedLabelTextField;
    private JTextField orbitSpeedTextEdit;
    private JToggleButton showTrailsButton;
    private JTextField trailLengthLabelTextField;
    private JTextField trailLengthTextEdit;
    private JToggleButton playFramesButton;
    private JTextField framePlaybackLabelTextField;
    private JTextField framePlaybackTextEdit;
    private JTextField plotTypeTextField;
    private JComboBox<PlotType> plotTypeComboBox;
    private JSlider frameSlider;
    private JButton viewXZplaneButton;
    private JButton viewYZplaneButton;
    private JButton viewXYplaneButton;
    private JButton screenShotButton;
    private JTextField displayQualityTextField;
    private JComboBox<RenderQuality> displayQualityComboBox;
    private JTextField displayColourTextField;
    private JComboBox<DisplayColour> displayColourComboBox;

    private final Engine engine;

    public TrackPlotControl(TrackCollection tracks, ImagePlus ipl)
    {
        super(tracks, ipl);
        engine = new Engine(this);

        createComponents();
        addListeners();
        makePanel();

        setControlMode(false);
    }

    private void createComponents()
    {
        showTracksButton = new JButton("Show tracks");
        showTracksButton.setPreferredSize(new Dimension(frameWidth, elementHeight));

        plotTypeTextField = new JTextField("Plot type");
        plotTypeTextField.setEditable(false);
        plotTypeTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        plotTypeTextField.setBorder(null);

        plotTypeComboBox = new JComboBox<>(PlotType.values());
        plotTypeComboBox.setPreferredSize(new Dimension(frameWidth*2/3-5, elementHeight));
        plotTypeComboBox.setSelectedItem(PlotType.NORMAL);

        axesTypeTextField = new JTextField("Axes type");
        axesTypeTextField.setEditable(false);
        axesTypeTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        axesTypeTextField.setBorder(null);

        axesTypeComboBox = new JComboBox<>(AxesType.values());
        axesTypeComboBox.setPreferredSize(new Dimension(frameWidth*2/3-5, elementHeight));
        axesTypeComboBox.setSelectedItem(AxesType.BOX_ONLY);

        displayQualityTextField = new JTextField("Display quality");
        displayQualityTextField.setEditable(false);
        displayQualityTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        displayQualityTextField.setBorder(null);

        displayQualityComboBox = new JComboBox<>(RenderQuality.values());
        displayQualityComboBox.setPreferredSize(new Dimension(frameWidth*2/3- 5, elementHeight));
        displayQualityComboBox.setSelectedItem(RenderQuality.LOW);

        displayColourTextField = new JTextField("Display colour");
        displayColourTextField.setEditable(false);
        displayColourTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        displayColourTextField.setBorder(null);

        displayColourComboBox = new JComboBox<>(DisplayColour.values());
        displayColourComboBox.setPreferredSize(new Dimension(frameWidth*2/3-5, elementHeight));
        displayColourComboBox.setSelectedItem(DisplayColour.ID);

        showTrailsButton = new JToggleButton("Trails");
        showTrailsButton.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        showTrailsButton.setSelected(true);
        showTrailsButton.setSelected(true);


        trailLengthTextEdit = new JTextField(String.valueOf(tracks.getHighestFrame()));
        trailLengthTextEdit.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        trailLengthTextEdit.setEditable(true);

        trailLengthLabelTextField = new JTextField("frames");
        trailLengthLabelTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        trailLengthLabelTextField.setEditable(false);
        trailLengthLabelTextField.setBorder(null);

        orbitButton = new JToggleButton("Orbit");
        orbitButton.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));

        orbitSpeedTextEdit = new JTextField(String.valueOf(Camera.DEF_ANGULAR_VELOCITY));
        orbitSpeedTextEdit.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        orbitSpeedTextEdit.setEditable(true);

        orbitSpeedLabelTextField = new JTextField("degrees/second");
        orbitSpeedLabelTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        orbitSpeedLabelTextField.setEditable(false);
        orbitSpeedLabelTextField.setBorder(null);

        playFramesButton = new JToggleButton("Play");
        playFramesButton.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        playFramesButton.setSelected(false);

        framePlaybackTextEdit = new JTextField(String.valueOf(Scene.DEF_FRAME_PLAYBACK_RATE));
        framePlaybackTextEdit.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        framePlaybackTextEdit.setEditable(true);

        framePlaybackLabelTextField = new JTextField("frames/second");
        framePlaybackLabelTextField.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        framePlaybackLabelTextField.setEditable(false);
        framePlaybackLabelTextField.setBorder(null);

        frameSlider = new JSlider(JSlider.HORIZONTAL, 0, tracks.getHighestFrame(), 0);
        frameSlider.setPreferredSize(new Dimension(frameWidth, elementHeight));

        viewXZplaneButton = new JButton("XZ plane");
        viewXZplaneButton.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        viewYZplaneButton = new JButton("YZ plane");
        viewYZplaneButton.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));
        viewXYplaneButton = new JButton("XY plane");
        viewXYplaneButton.setPreferredSize(new Dimension(frameWidth/3-5, elementHeight));

        screenShotButton = new JButton("Screen shot");
        screenShotButton.setPreferredSize(new Dimension(frameWidth, elementHeight));
    }

    private void addListeners()
    {
        showTracksButton.addActionListener(e ->
        {
            new Thread(() ->
            {
                try
                {
                    engine.init();
                    setControlMode(true);
                    engine.start();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    setControlMode(false);
                    engine.dispose();
                }
            }).start();
        });

        plotTypeComboBox.addActionListener(e ->
        {
            PlotType plotType = (PlotType) plotTypeComboBox.getSelectedItem();
            if (plotType == null) return;

            switch (plotType) {
                case NORMAL:
                    engine.getScene().setMotilityPlot(false);
                    viewXYplaneButton.setEnabled(true);
                    viewXZplaneButton.setEnabled(true);
                    viewYZplaneButton.setEnabled(true);
                    break;
                case MOTILITY:
                    engine.getScene().setMotilityPlot(true);
                    viewXYplaneButton.setEnabled(false);
                    viewXZplaneButton.setEnabled(false);
                    viewYZplaneButton.setEnabled(false);
                    break;
            }
        });

        axesTypeComboBox.addActionListener(e ->
        {
            AxesType axesTypes = (AxesType) axesTypeComboBox.getSelectedItem();
            if (axesTypes == null) return;

            switch (axesTypes) {
                case NONE:
                    engine.getScene().setAxesVisibility(false);
                    engine.getScene().setBoundingBoxVisibility(false);
                    break;
                case AXES_ONLY:
                    engine.getScene().setAxesVisibility(true);
                    engine.getScene().setBoundingBoxVisibility(false);
                    break;
                case BOX_ONLY:
                    engine.getScene().setAxesVisibility(false);
                    engine.getScene().setBoundingBoxVisibility(true);
                    break;
                case BOTH:
                    engine.getScene().setAxesVisibility(true);
                    engine.getScene().setBoundingBoxVisibility(true);
                    break;
            }
        });

        displayQualityComboBox.addActionListener(e ->
        {
            engine.getScene().setRenderQuality((RenderQuality) displayQualityComboBox.getSelectedItem());
            engine.getRenderer().setBindMeshBuffers(true);
        });

        displayColourComboBox.addActionListener(e ->
        {
            DisplayColour displayColour = (DisplayColour) displayColourComboBox.getSelectedItem();
            engine.getScene().setDisplayColour(displayColour);
            engine.getRenderer().setBindColourBuffers(displayColour != DisplayColour.ID);
        });

        showTrailsButton.addActionListener(e -> engine.getScene().setTrailVisibility(showTrailsButton.isSelected()));
        trailLengthTextEdit.addActionListener(e ->
        {
            int trailLength = Integer.parseInt(trailLengthTextEdit.getText());
            engine.getScene().setTrailLength(trailLength);
        });

        orbitButton.addActionListener(e ->
        {
            engine.getScene().getCamera().setOrbit(orbitButton.isSelected());
            engine.getRenderer().disableOrthoProj();
        });
        orbitSpeedTextEdit.addActionListener(e ->
        {
            int angularVelocityDPS = Integer.parseInt(orbitSpeedTextEdit.getText());
            engine.getScene().getCamera().setAngularVelocity(angularVelocityDPS);
        });

        playFramesButton.addActionListener(e ->  engine.getScene().setPlayFrames(playFramesButton.isSelected()));
        framePlaybackTextEdit.addActionListener(e ->
        {
            int framePlayback = Integer.parseInt(framePlaybackTextEdit.getText());
            engine.getScene().setFramePlaybackRate(framePlayback);
        });

        frameSlider.addChangeListener(e -> engine.getScene().setFrame(frameSlider.getValue()));

        viewXZplaneButton.addActionListener(e -> engine.viewXZplane());
        viewYZplaneButton.addActionListener(e -> engine.viewYZplane());
        viewXYplaneButton.addActionListener(e -> engine.viewXYplane());

        screenShotButton.addActionListener(e -> engine.getRenderer().takeScreenshot());
    }

    private void makePanel()
    {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(20,5,20,5);
        panel.add(showTracksButton,c);

        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(0,5,5,5);
        panel.add(plotTypeTextField,c);

        c.gridx++;
        c.gridwidth = 2;
        panel.add(plotTypeComboBox,c);

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(axesTypeTextField,c);

        c.gridx++;
        c.gridwidth = 2;
        panel.add(axesTypeComboBox,c);

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(displayQualityTextField,c);

        c.gridx++;
        c.gridwidth = 2;
        panel.add(displayQualityComboBox,c);

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(displayColourTextField,c);

        c.gridx++;
        c.gridwidth = 2;
        panel.add(displayColourComboBox,c);

        c.gridwidth = 1;
        c.gridx=0;
        c.gridy++;
        panel.add(showTrailsButton,c);

        c.gridx++;
        panel.add(trailLengthTextEdit,c);

        c.gridx++;
        panel.add(trailLengthLabelTextField,c);

        c.gridx=0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(orbitButton,c);

        c.gridx++;
        panel.add(orbitSpeedTextEdit,c);

        c.gridx++;
        panel.add(orbitSpeedLabelTextField,c);

        c.gridx=0;
        c.gridy++;
        panel.add(playFramesButton,c);

        c.gridx++;
        panel.add(framePlaybackTextEdit,c);

        c.gridx++;
        panel.add(framePlaybackLabelTextField,c);

        c.gridx = 0;
        c.gridwidth=3;
        c.gridy++;
        panel.add(frameSlider,c);

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy++;
        panel.add(viewXZplaneButton,c);
        c.gridx++;
        panel.add(viewYZplaneButton,c);
        c.gridx++;
        panel.add(viewXYplaneButton,c);

        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy++;
        panel.add(screenShotButton,c);
    }

    public void updateGui(){
        frameSlider.setValue(engine.getScene().getFrame());
        playFramesButton.setSelected(engine.getScene().isPlayingFrames());
    }

    public void setControlMode(boolean state){
        showTracksButton.setEnabled(!state);
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
        framePlaybackTextEdit.setEnabled(state);
        framePlaybackLabelTextField.setEnabled(state);
        viewXZplaneButton.setEnabled(state);
        viewYZplaneButton.setEnabled(state);
        viewXYplaneButton.setEnabled(state);
        screenShotButton.setEnabled(state);
        displayQualityTextField.setEnabled(state);
        displayQualityComboBox.setEnabled(state);
        displayColourTextField.setEnabled(state);
        displayColourComboBox.setEnabled(state);
    }

    public TrackCollection getTracks() {
        return tracks;
    }

    public ImagePlus getIpl() {
        return ipl;
    }

    public enum PlotType
    {
        NORMAL,
        MOTILITY;

        @Override
        public String toString()
        {
            String s = super.toString();
            return s.charAt(0) + s.replace('_', ' ').toLowerCase().substring(1);
        }
    }

    public enum AxesType
    {
        NONE,
        AXES_ONLY,
        BOX_ONLY,
        BOTH;

        @Override
        public String toString()
        {
            return super.toString().charAt(0) + super.toString().replace('_', ' ').toLowerCase().substring(1);
        }
    }

    public enum DisplayColour
    {
        ID,
        TOTAL_PATH_LENGTH,
        VELOCITY;

        @Override
        public String toString()
        {
            return super.toString().charAt(0) + super.toString().replace('_', ' ').toLowerCase().substring(1);
        }
    }

    public enum RenderQuality
    {
        LOWEST,
        LOW,
        MEDIUM,
        HIGH;

        @Override
        public String toString()
        {
            return super.toString().charAt(0) + super.toString().replace('_', ' ').toLowerCase().substring(1);
        }
    }
}
