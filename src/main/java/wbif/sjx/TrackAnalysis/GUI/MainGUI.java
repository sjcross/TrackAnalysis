package wbif.sjx.TrackAnalysis.GUI;

import ij.ImagePlus;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class MainGUI implements ActionListener {
    private JFrame frame = new JFrame();
    private int frameWidth = 300;
    private int frameHeight = 600;
    private int elementHeight = 30;
    private JScrollPane moduleScrollPane = new JScrollPane();

    private TrackCollection tracks;
    private ImagePlus ipl;

    private static final String TRACK_SUMMARY = new TrackSummary(null,null,0,0).getTitle();
    private static final String DIRECTIONAL_PERSISTENCE = new DirectionalPersistenceControl(null,null,0,0).getTitle();
    private static final String DIRECTIONALITY_RATIO = new DirectionalityRatioControl(null,null,0,0).getTitle();
    private static final String EUCLIDEAN_DISTANCE = new EuclideanDistanceControl(null,null,0,0).getTitle();
    private static final String MEAN_SQUARED_DISPLACEMENT = new MSDControl(null,null,0,0).getTitle();
//    private static final String NEAREST_NEIGHBOUR_DISTANCE = new NearestNeighbourCalculator(null,null,0,0).getTitle();
    private static final String TRACK_DURATION = new TrackDurationControl(null,null,0,0).getTitle();
    private static final String MOTILITY_PLOT = new MotilityPlotControl(null,null,0,0).getTitle();
    private static final String SHOW_TRACK_ID = new ShowTrackIDControl(null,null,0,0).getTitle();
    private static final String TOTAL_PATH_LENGTH = new TotalPathLengthControl(null,null,0,0).getTitle();
    private static final String TRACK_INTENSITY = new TrackIntensityControl(null,null,0,0).getTitle();
    private static final String TRACK_PLOT = new TrackPlotControl(null,null,0,0).getTitle();

    private static final String MODULE_CHANGED = "Module changed";

    String[] moduleListWithIpl = new String[]{
            TRACK_SUMMARY,
//            DIRECTIONAL_PERSISTENCE,
            DIRECTIONALITY_RATIO,
            EUCLIDEAN_DISTANCE,
            MEAN_SQUARED_DISPLACEMENT,
            MOTILITY_PLOT,
//            NEAREST_NEIGHBOUR_DISTANCE,
            SHOW_TRACK_ID,
            TOTAL_PATH_LENGTH,
            TRACK_DURATION,
            TRACK_INTENSITY,
//            TRACK_PLOT

    };

    String[] moduleListWithoutIpl = new String[]{
            TRACK_SUMMARY,
//            DIRECTIONAL_PERSISTENCE,
            DIRECTIONALITY_RATIO,
            EUCLIDEAN_DISTANCE,
            MEAN_SQUARED_DISPLACEMENT,
            MOTILITY_PLOT,
//            NEAREST_NEIGHBOUR_DISTANCE,
            TOTAL_PATH_LENGTH,
            TRACK_DURATION,
//            TRACK_PLOT

    };

    public MainGUI(TrackCollection tracks, ImagePlus ipl) {
        this.tracks = tracks;
        this.ipl = ipl;

    }

    public void create() {
        // Setting location of panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);

        frame.setLayout(new GridBagLayout());
        frame.setTitle("Track analysis (v"+getClass().getPackage().getImplementationVersion()+")");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,0,5);

        // The module list used depends on whether an image was provided
        String[] moduleList = ipl == null ? moduleListWithoutIpl : moduleListWithIpl;
        JComboBox<String> comboBox = new JComboBox<>(moduleList);
        comboBox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        comboBox.addActionListener(this);
        comboBox.setActionCommand(MODULE_CHANGED);
        frame.add(comboBox,c);

        changeModule(moduleListWithIpl[0]);
        moduleScrollPane.setBorder(null);
        c.gridy++;
        frame.add(moduleScrollPane,c);

        frame.validate();
        frame.setVisible(true);
        frame.pack();

    }

    private void changeModule(String module) {
        ModuleControl control;
        if (module.equals(TRACK_SUMMARY)) {
            control = new TrackSummary(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(DIRECTIONAL_PERSISTENCE)) {
            control = new DirectionalPersistenceControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(DIRECTIONALITY_RATIO)) {
            control = new DirectionalityRatioControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(EUCLIDEAN_DISTANCE)) {
            control = new EuclideanDistanceControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(MEAN_SQUARED_DISPLACEMENT)) {
            control = new MSDControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(MOTILITY_PLOT)) {
            control = new MotilityPlotControl(tracks, ipl, frameWidth, elementHeight);

//        } else if (module.equals(NEAREST_NEIGHBOUR_DISTANCE)) {
//            control = new NearestNeighbourCalculator(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(SHOW_TRACK_ID)) {
            control = new ShowTrackIDControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(TOTAL_PATH_LENGTH)) {
            control = new TotalPathLengthControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(TRACK_DURATION)) {
            control = new TrackDurationControl(tracks, ipl, frameWidth, elementHeight);

        } else if (module.equals(TRACK_INTENSITY)) {
            control = new TrackIntensityControl(tracks, ipl, frameWidth, elementHeight);

        }  else if (module.equals(TRACK_PLOT)) {
            control = new TrackPlotControl(tracks, ipl, frameWidth, elementHeight);

        } else {
            return;

        }

        JPanel modulePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        JPanel panel = control.getCommonControls();
        c.gridy++;
        modulePanel.add(panel,c);

        panel = control.getExtraControls();
        if (panel != null) {
            c.gridy++;
            modulePanel.add(panel, c);
        }

        moduleScrollPane.setViewportView(modulePanel);
        moduleScrollPane.repaint();
        moduleScrollPane.validate();

        frame.pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MODULE_CHANGED)) {
            String module = (String) ((JComboBox) e.getSource()).getSelectedItem();
            changeModule(module);

        }
    }
}
