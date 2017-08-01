package wbif.sjx.TrackAnalysis.GUI;

import com.sun.org.apache.bcel.internal.generic.SWITCH;
import ij.ImagePlus;
import ij.Prefs;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Camera;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Engine;
import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.TrackEntity;
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

    private final static String PLOT = "Plot tracks";

    private JCheckBox showTrailCheckbox;
    private final static String SHOW_TRAILS = "Show trails";

    private JCheckBox showAxesCheckbox;
    private final static String SHOW_AXES = "Show axes";

    private JCheckBox showBoundingBoxCheckbox;
    private final static String SHOW_BOUNDING_BOX = "Show bounding box";

    private JCheckBox motilityPlotCheckbox;
    private final static String MOTILITY_PLOT = "Motility plot";

    private JSlider FOVSlider;
    private final static String FOV_SLIDER = "FOV Slider";

    private Engine engine;
//    private Plot3D_Options options;

    public TrackPlotControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
        engine = new Engine(tracks);
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
        c.gridwidth = 2;
        c.insets = new Insets(20,5,20,5);

        // Button to run ensemble plotting
        JButton plotAllButton = new JButton(PLOT);
        plotAllButton.setPreferredSize(new Dimension(panelWidth,elementHeight));
        plotAllButton.addActionListener(this);
        panel.add(plotAllButton,c);

        return panel;

    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,5,0,5);

        boolean showTrail = TrackEntityCollection.showTrail_DEFAULT;
        showTrailCheckbox = new JCheckBox(SHOW_TRAILS);
        showTrailCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showTrailCheckbox.setSelected(showTrail);
        showTrailCheckbox.setEnabled(false);
        showTrailCheckbox.addActionListener(this);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(showTrailCheckbox,c);

        boolean showAxes = Scene.showAxes_DEFAULT;
        showAxesCheckbox = new JCheckBox(SHOW_AXES);
        showAxesCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showAxesCheckbox.setSelected(showAxes);
        showAxesCheckbox.setEnabled(false);
        showAxesCheckbox.addActionListener(this);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(showAxesCheckbox,c);

        boolean showBoundingBox = Scene.showBoundingBox_DEFAULT;
        showBoundingBoxCheckbox = new JCheckBox(SHOW_BOUNDING_BOX);
        showBoundingBoxCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        showBoundingBoxCheckbox.setSelected(showBoundingBox);
        showBoundingBoxCheckbox.setEnabled(false);
        showBoundingBoxCheckbox.addActionListener(this);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(showBoundingBoxCheckbox,c);

        boolean motilityPlot = TrackEntityCollection.motilityPlot_DEFAULT;
        motilityPlotCheckbox = new JCheckBox(MOTILITY_PLOT);
        motilityPlotCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        motilityPlotCheckbox.setSelected(motilityPlot);
        motilityPlotCheckbox.setEnabled(false);
        motilityPlotCheckbox.addActionListener(this);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(motilityPlotCheckbox,c);

        int FOV = Camera.FOV_DEFAULT;
        FOVSlider = new JSlider(JSlider.HORIZONTAL, Camera.FOV_MINIMUM, Camera.FOV_MAXIMUM, FOV);
        FOVSlider.setPreferredSize(new Dimension(panelWidth,elementHeight));
        FOVSlider.setEnabled(false);
        FOVSlider.addChangeListener(this);
        FOVSlider.setName(FOV_SLIDER);
        c.gridy++;
        panel.add(new JLabel(FOV_SLIDER),c);
        c.gridy++;
        c.insets = new Insets(5,0,20,5);
        panel.add(FOVSlider,c);

        return panel;
    }

    @Override
    public void run(int ID) {
        new Thread(() -> engine.start()).start();
        showTrailCheckbox.setEnabled(true);
        showAxesCheckbox.setEnabled(true);
        showBoundingBoxCheckbox.setEnabled(true);
        motilityPlotCheckbox.setEnabled(true);
        FOVSlider.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

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
            case FOV_SLIDER:
                engine.getCamera().setFOV(((JSlider)e.getSource()).getValue());
                break;
            default:
                break;
        }
    }
}
