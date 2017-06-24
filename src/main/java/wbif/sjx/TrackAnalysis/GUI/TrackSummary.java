package wbif.sjx.TrackAnalysis.GUI;

import ij.IJ;
import ij.Prefs;
import ij.measure.ResultsTable;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class TrackSummary extends ModuleControl {
    private JCheckBox numberOfObjectsCheckbox;
    private JCheckBox trackDurationCheckbox;
    private JCheckBox spatialStatisticsCheckbox;

    public TrackSummary(TrackCollection tracks, int panelWidth, int elementHeight) {
        super(tracks, panelWidth, elementHeight);
    }

    public void getNumberOfObjects() {
        int[][] n = tracks.getNumberOfObjects();
        DecimalFormat df = new DecimalFormat("#.##");

        IJ.log("Number of objects (total): "+String.valueOf(tracks.size()));

//        ResultsTable rt = ResultsTable.getResultsTable();
//        double[] f = ImageJOps.getColumn(rt, "FRAME");
//        int max_fr = (int) new Max().evaluate(f);
//        double[] num_objs = new double[max_fr];
//
//        for (int i=0;i<max_fr;i++) {
//            num_objs[i] = correspondingRows(f,i).length;
//
//        }
//
//        double average_num_objs = new Mean().evaluate(num_objs);
//        IJ.log("Mean number of objects per frame: "+String.valueOf(df.format(average_num_objs)));
//
//        double stdev_num_objs = new StandardDeviation().evaluate(num_objs);
//        IJ.log("Std. dev. number of objects per frame: "+String.valueOf(df.format(stdev_num_objs)));
//
//        IJ.log(" ");

    }

    @Override
    public String getTitle() {
        return "Track summary";
    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,5,0,5);

        // Checkbox for number of objects
        boolean numberOfObjects = Prefs.get("TrackAnalysis.TrackSummary.numberOfObjects",true);
        numberOfObjectsCheckbox = new JCheckBox("Output number of objects");
        numberOfObjectsCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        numberOfObjectsCheckbox.setSelected(numberOfObjects);
        panel.add(numberOfObjectsCheckbox,c);

        // Track duration
        boolean trackDuration = Prefs.get("TrackAnalysis.TrackSummary.trackDuration",true);
        trackDurationCheckbox = new JCheckBox("Output track duration");
        trackDurationCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        trackDurationCheckbox.setSelected(trackDuration);
        c.gridy++;
        panel.add(trackDurationCheckbox,c);

        // Spatial statistics
        boolean spatialStatistics = Prefs.get("TrackAnalysis.TrackSummary.spatialStatistics",true);
        spatialStatisticsCheckbox = new JCheckBox("Output spatial statistics");
        spatialStatisticsCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        spatialStatisticsCheckbox.setSelected(spatialStatistics);
        c.gridy++;
        panel.add(spatialStatisticsCheckbox,c);

        return panel;

    }

    @Override
    public void run(int ID) {


    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
