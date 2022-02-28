package io.github.sjcross.TrackAnalysis.GUI;

import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.measure.ResultsTable;
import io.github.sjcross.TrackAnalysis.GUI.Control.PlotableModule;
import io.github.sjcross.common.analysis.MSDCalculator;
import io.github.sjcross.common.mathfunc.CumStat;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

/**
 * Created by sc13967 on 24/06/2017.
 */
public class TrackSummary extends PlotableModule
{
    private JCheckBox frameByFrameCheckbox;
    private JCheckBox numberOfObjectsCheckbox;
    private JCheckBox trackDurationCheckbox;
    private JCheckBox spatialStatisticsCheckbox;
    private JCheckBox diffusionCoefficientCheckbox;
    private JTextField nPointsTextField;

    public TrackSummary(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0,5,0,5);

        // Full track details
        boolean fullTrackDetails = Prefs.get("TrackAnalysis.TrackSummary.fullTrackDetails",true);
        frameByFrameCheckbox = new JCheckBox("Full track details (frame-by-frame)");
        frameByFrameCheckbox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        frameByFrameCheckbox.setSelected(fullTrackDetails);
        c.gridy++;
        panel.add(frameByFrameCheckbox,c);

        // Checkbox for number of objects
        boolean numberOfObjects = Prefs.get("TrackAnalysis.TrackSummary.numberOfObjects",true);
        numberOfObjectsCheckbox = new JCheckBox("Output number of objects");
        numberOfObjectsCheckbox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        numberOfObjectsCheckbox.setSelected(numberOfObjects);
        panel.add(numberOfObjectsCheckbox,c);

        // Track duration
        boolean trackDuration = Prefs.get("TrackAnalysis.TrackSummary.trackDuration",true);
        trackDurationCheckbox = new JCheckBox("Output track duration");
        trackDurationCheckbox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        trackDurationCheckbox.setSelected(trackDuration);
        c.gridy++;
        panel.add(trackDurationCheckbox,c);

        // Spatial statistics
        boolean spatialStatistics = Prefs.get("TrackAnalysis.TrackSummary.spatialStatistics",true);
        spatialStatisticsCheckbox = new JCheckBox("Output spatial statistics");
        spatialStatisticsCheckbox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        spatialStatisticsCheckbox.setSelected(spatialStatistics);
        c.gridy++;
        panel.add(spatialStatisticsCheckbox,c);

        // Diffusion coefficient
        boolean diffusionCoefficient = Prefs.get("TrackAnalysis.TrackSummary.diffusionCoefficient",false);
        diffusionCoefficientCheckbox = new JCheckBox("Output diffusion coefficient");
        diffusionCoefficientCheckbox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        diffusionCoefficientCheckbox.setSelected(diffusionCoefficient);
        c.gridy++;
        panel.add(diffusionCoefficientCheckbox,c);

        // Label and text field to get number of points to use for diffusion coefficient calculation
        JTextField label = new JTextField("Number of frames");
        label.setPreferredSize(new Dimension(3*frameWidth/4,elementHeight));
        label.setEditable(false);
        label.setBorder(null);
        c.gridwidth = 1;
        c.gridy++;
        c.insets = new Insets(5,5,20,5);
        panel.add(label,c);

        int nPoints = (int) Prefs.get("TrackAnalysis.TrackSummary.nPoints",25);
        nPointsTextField = new JTextField();
        nPointsTextField.setPreferredSize(new Dimension(frameWidth/4-5,elementHeight));
        nPointsTextField.setText(String.valueOf(nPoints));
        c.gridx++;
        c.insets = new Insets(5,0,20,5);
        panel.add(nPointsTextField,c);
    }

    @Override
    public void plotAll()
    {
        boolean fullTrackDetails = frameByFrameCheckbox.isSelected();
        Prefs.set("TrackAnalysis.fullTrackDetails",fullTrackDetails);

        boolean numberOfObjects = numberOfObjectsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.numberOfObjects",numberOfObjects);

        boolean trackDuration = trackDurationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.trackDuration",trackDuration);

        boolean spatialStatistics = spatialStatisticsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.spatialStatistics",spatialStatistics);

        boolean diffusionCoefficient = diffusionCoefficientCheckbox.isSelected();
        Prefs.set("TrackAnalysis.diffusionCoefficient",diffusionCoefficient);

        ResultsTable rt = new ResultsTable();

        IJ.log("++SUMMARY OF ALL TRACKS++");
        IJ.log(" ");

        int i=0;
        for (int key:tracks.keySet()) {
            rt.setValue("ID",i++,key);
        }

        showTrackSummary(rt);
        if (numberOfObjects) showNumberOfObjects();
        if (trackDuration) showTrackDuration(rt);
        if (spatialStatistics) showTrackSpatialStatistics(rt);
        if (diffusionCoefficient) showDiffusionCoefficient(rt);

        ResultsTable fullRT = new ResultsTable();
        if (fullTrackDetails) showFullTrackResults(fullRT);
        fullRT.show("Full track details");

        rt.show("Track summary");
    }

    @Override
    public void plotTrack(int ID)
    {
        boolean fullTrackDetails = frameByFrameCheckbox.isSelected();
        Prefs.set("TrackAnalysis.fullTrackDetails",fullTrackDetails);

        boolean numberOfObjects = numberOfObjectsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.numberOfObjects",numberOfObjects);

        boolean trackDuration = trackDurationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.trackDuration",trackDuration);

        boolean spatialStatistics = spatialStatisticsCheckbox.isSelected();
        Prefs.set("TrackAnalysis.spatialStatistics",spatialStatistics);

        boolean diffusionCoefficient = diffusionCoefficientCheckbox.isSelected();
        Prefs.set("TrackAnalysis.diffusionCoefficient",diffusionCoefficient);

        ResultsTable rt = new ResultsTable();

        IJ.log("++SUMMARY OF TRACK "+ID+"++");
        IJ.log(" ");

        rt.setValue("ID",0,ID);

        showTrackSummary(ID,rt);
        if (trackDuration) showTrackDuration(ID,rt);
        if (spatialStatistics) showTrackSpatialStatistics(ID,rt);
        if (diffusionCoefficient) showDiffusionCoefficient(ID,rt);

        ResultsTable fullRT = new ResultsTable();
        if (fullTrackDetails) showFullTrackResults(ID,fullRT);
        fullRT.show("Full track details");

        rt.show("Track summary");
    }

    private void showTrackSummary(ResultsTable rt) {
        DecimalFormat df = new DecimalFormat("#.###");
        String units = tracks.values().iterator().next().getUnits();

        int i=0;
        for (Track track:tracks.values()) {
            double[][] meanPos = track.getMeanPosition();

            rt.setValue("Mean x-pos ("+units+")", i, df.format(meanPos[0][0])); //Distances reported in image units
            rt.setValue("Mean y-pos ("+units+")", i, df.format(meanPos[0][1])); //Distances reported in image units
            rt.setValue("Mean z-pos ("+units+")", i, df.format(meanPos[0][2])); //Distances reported in image units
            rt.setValue("Stdev x-pos ("+units+")", i, df.format(meanPos[1][0])); //Distances reported in image units
            rt.setValue("Stdev y-pos ("+units+")", i, df.format(meanPos[1][1])); //Distances reported in image units
            rt.setValue("Stdev z-pos ("+units+")", i++, df.format(meanPos[1][2])); //Distances reported in image units

        }
    }

    private void showTrackSummary(int ID, ResultsTable rt) {
        Track track = tracks.get(ID);

        DecimalFormat df = new DecimalFormat("#.###");
        String units = track.getUnits();

        double[][] meanPos = track.getMeanPosition();

        rt.setValue("Mean x-pos ("+units+")", 0, df.format(meanPos[0][0])); //Distances reported in image units
        rt.setValue("Mean y-pos ("+units+")", 0, df.format(meanPos[0][1])); //Distances reported in image units
        rt.setValue("Mean z-pos ("+units+")", 0, df.format(meanPos[0][2])); //Distances reported in image units
        rt.setValue("Stdev x-pos ("+units+")", 0, df.format(meanPos[1][0])); //Distances reported in image units
        rt.setValue("Stdev y-pos ("+units+")", 0, df.format(meanPos[1][1])); //Distances reported in image units
        rt.setValue("Stdev z-pos ("+units+")", 0, df.format(meanPos[1][2])); //Distances reported in image units

    }

    private void showFullTrackResults(ResultsTable rt) {
        String units = tracks.values().iterator().next().getUnits();

        int i = 0;
        for (int currentID:tracks.keySet()) {
            Track track = tracks.get(currentID);

//            TreeMap<Integer,double[]> nnDistances = track.getNearestNeighbourDistance(tracks);
            TreeMap<Integer,Double> rollingTotalPath = track.getRollingTotalPathLength();
            TreeMap<Integer,Double> rollingEuclideanDistance = track.getRollingEuclideanDistance();
            TreeMap<Integer,Double> rollingDirectionalityRatio= track.getRollingDirectionalityRatio();
            TreeMap<Integer,Double> instantaneousSpeed = track.getInstantaneousSpeed();

            for (int f:track.getF()){
                rt.setValue("ID", i, currentID);
                rt.setValue("X (" + units + ")", i, track.getX(f));
                rt.setValue("Y (" + units + ")", i, track.getY(f));
                rt.setValue("Z (" + units + ")", i, track.getZ(f));
//                rt.setValue("NN ID", i, nnDistances.get(f)[0]);
//                rt.setValue("NN distance (" + units + ")", i, nnDistances.get(f)[1]);
                rt.setValue("Rolling path length (" + units + ")", i, rollingTotalPath.get(f));
                rt.setValue("Rolling Euclidean distance (" + units + ")", i, rollingEuclideanDistance.get(f));
                rt.setValue("Rolling directionality ratio", i, rollingDirectionalityRatio.get(f));
                rt.setValue("Instantaneous speed ("+units+"/frame)", i, instantaneousSpeed.get(f));

                i++;

            }
        }
    }

    private void showFullTrackResults(int ID, ResultsTable rt) {
        Track track = tracks.get(ID);
        String units = track.getUnits();

//            TreeMap<Integer,double[]> nnDistances = track.getNearestNeighbourDistance(tracks);
        TreeMap<Integer,Double> rollingTotalPath = track.getRollingTotalPathLength();
        TreeMap<Integer,Double> rollingEuclideanDistance = track.getRollingEuclideanDistance();
        TreeMap<Integer,Double> rollingDirectionalityRatio= track.getRollingDirectionalityRatio();
        TreeMap<Integer,Double> instantaneousSpeed = track.getInstantaneousSpeed();

        int i = 0;
        for (int f:track.getF()){
            rt.setValue("ID", i, ID);
            rt.setValue("X (" + units + ")", i, track.getX(f));
            rt.setValue("Y (" + units + ")", i, track.getY(f));
            rt.setValue("Z (" + units + ")", i, track.getZ(f));
//                rt.setValue("NN ID", i, nnDistances.get(f)[0]);
//                rt.setValue("NN distance (" + units + ")", i, nnDistances.get(f)[1]);
            rt.setValue("Rolling path length (" + units + ")", i, rollingTotalPath.get(f));
            rt.setValue("Rolling Euclidean distance (" + units + ")", i, rollingEuclideanDistance.get(f));
            rt.setValue("Rolling directionality ratio", i, rollingDirectionalityRatio.get(f));
            rt.setValue("Instantaneous speed ("+units+"/frame)", i, instantaneousSpeed.get(f));

            i++;
        }
    }

    private void showNumberOfObjects() {
        int[][] frameAndNumber = tracks.getNumberOfObjects(false);
        DecimalFormat df = new DecimalFormat("#.###");

        IJ.log("Number of objects (total): "+String.valueOf(tracks.size()));

        CumStat cs = new CumStat();
        for (int n:frameAndNumber[1]) {
            cs.addMeasure(n);
        }

        IJ.log("Mean number of objects per frame: "+String.valueOf(df.format(cs.getMean())));
        IJ.log("Std. dev. number of objects per frame: "+String.valueOf(df.format(cs.getStd())));

        IJ.log(" ");

    }

    private void showTrackDuration(ResultsTable rt) {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat dfS = new DecimalFormat("#");
        CumStat cs = new CumStat();

        int i = 0;
        for (Track track:tracks.values()) {
            int[] f = track.getF();
            rt.setValue("Start (frame)", i, dfS.format(f[0]));
            rt.setValue("End (frame)", i, dfS.format(f[f.length-1]));
            rt.setValue("Duration (frames)",i++,track.getDuration());

            cs.addMeasure(track.getDuration());
        }

        IJ.log("Mean track duration: "+String.valueOf(df.format(cs.getMean()))+" frames");
        IJ.log("Std. dev. of track duration: "+String.valueOf(df.format(cs.getStd()))+" frames");

        IJ.log(" ");

    }

    private void showTrackDuration(int ID, ResultsTable rt) {
        DecimalFormat df = new DecimalFormat("#.###");
        Track track = tracks.get(ID);

        rt.setValue("Duration (frames)",0,track.getDuration());

        IJ.log("Track duration: "+String.valueOf(df.format(track.getDuration()))+" frames");
        IJ.log(" ");

    }

    private void showTrackSpatialStatistics(ResultsTable rt) {
        DecimalFormat df = new DecimalFormat("#.###");
        String units = tracks.values().iterator().next().getUnits();

        // Using CumStats to keep memory requirements smaller
        CumStat csStep = new CumStat();
        CumStat csSpeed = new CumStat();
        CumStat csEuclDist = new CumStat();
        CumStat csTotalDist = new CumStat();
        CumStat csInstDirRatio = new CumStat();
        CumStat csDirRatio = new CumStat();

        int i = 0;
        for (Track track:tracks.values()) {
            rt.setValue("Euclidean distance ("+units+")",i,track.getEuclideanDistance());
            rt.setValue("Total path length ("+units+")",i,track.getTotalPathLength());
            rt.setValue("Directionality ratio ",i,track.getDirectionalityRatio());

            // Whole track statistics
            csEuclDist.addMeasure(track.getEuclideanDistance());
            csTotalDist.addMeasure(track.getTotalPathLength());
            csDirRatio.addMeasure(track.getDirectionalityRatio());

            // Step-by-step statistics
            TreeMap<Integer,Double> steps = track.getInstantaneousStepSizes();
            steps.values().stream().forEach(csStep::addMeasure);
            rt.setValue("Mean step size ("+units+")",i,new CumStat(steps.values()).getMean());

            TreeMap<Integer,Double> speeds = track.getInstantaneousSpeed();
            speeds.values().stream().forEach(csSpeed::addMeasure);
            rt.setValue("Mean inst. vel. ("+units+"/frame)",i,new CumStat(speeds.values()).getMean());

            TreeMap<Integer,Double> dirRatio = track.getRollingDirectionalityRatio();
            dirRatio.values().stream().forEach(csInstDirRatio::addMeasure);
            rt.setValue("Mean inst. dir. ratio",i++,new CumStat(dirRatio.values()).getMean());

        }

        IJ.log("Mean Euclidean distance: "+String.valueOf(df.format(csEuclDist.getMean()))+" "+units);
        IJ.log("Std. dev. of Euclidean distances: "+String.valueOf(df.format(csEuclDist.getStd()))+" "+units);
        IJ.log("Maximum Euclidean distance: "+String.valueOf(df.format(csEuclDist.getMax()))+" "+units);
        IJ.log("Minimum Euclidean distance: "+String.valueOf(df.format(csEuclDist.getMin()))+" "+units);
        IJ.log(" ");

        IJ.log("Mean total path length: "+String.valueOf(df.format(csTotalDist.getMean()))+" "+units);
        IJ.log("Std. dev. of total path lengths: "+String.valueOf(df.format(csTotalDist.getStd()))+" "+units);
        IJ.log("Maximum total path length: "+String.valueOf(df.format(csTotalDist.getMax()))+" "+units);
        IJ.log("Minimum total path length: "+String.valueOf(df.format(csTotalDist.getMin()))+" "+units);
        IJ.log(" ");

        IJ.log("Mean directionality ratio: "+String.valueOf(df.format(csDirRatio.getMean())));
        IJ.log("Std. dev. of directionality ratios: "+String.valueOf(df.format(csDirRatio.getStd())));
        IJ.log("Maximum directionality ratio: "+String.valueOf(df.format(csDirRatio.getMax())));
        IJ.log("Minimum directionality ratio: "+String.valueOf(df.format(csDirRatio.getMin())));
        IJ.log(" ");

        IJ.log("Mean step length: "+String.valueOf(df.format(csStep.getMean()))+" "+units);
        IJ.log("Std. dev. of step lengths: "+String.valueOf(df.format(csStep.getStd()))+" "+units);
        IJ.log("Maximum step length: "+String.valueOf(df.format(csStep.getMax()))+" "+units);
        IJ.log("Minimum step length: "+String.valueOf(df.format(csStep.getMin()))+" "+units);
        IJ.log(" ");

        IJ.log("Mean instantaneous speed: "+String.valueOf(df.format(csSpeed.getMean()))+" "+units+"/frame");
        IJ.log("Std. dev. of instantaneous velocities: "+String.valueOf(df.format(csSpeed.getStd()))+" "+units+"/frame");
        IJ.log("Maximum instantaneous speed: "+String.valueOf(df.format(csSpeed.getMax()))+" "+units+"/frame");
        IJ.log("Minimum instantaneous speed: "+String.valueOf(df.format(csSpeed.getMin()))+" "+units+"/frame");
        IJ.log(" ");

        IJ.log("Mean instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getMean())));
        IJ.log("Std. dev. of instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getStd())));
        IJ.log("Maximum instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getMax())));
        IJ.log("Minimum instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getMin())));
        IJ.log(" ");

    }

    private void showTrackSpatialStatistics(int ID, ResultsTable rt) {
        Track track = tracks.get(ID);
        DecimalFormat df = new DecimalFormat("#.###");
        String units = track.getUnits();

        // Using CumStats to keep memory requirements smaller
        CumStat csStep = new CumStat();
        CumStat csSpeed = new CumStat();
        CumStat csInstDirRatio = new CumStat();

        // Step-by-step statistics
        TreeMap<Integer,Double> steps = track.getInstantaneousStepSizes();
        steps.values().stream().forEach(csStep::addMeasure);

        TreeMap<Integer,Double> speeds = track.getInstantaneousSpeed();
        speeds.values().stream().forEach(csSpeed::addMeasure);

        TreeMap<Integer,Double> dirRatio = track.getRollingDirectionalityRatio();
        dirRatio.values().stream().forEach(csInstDirRatio::addMeasure);

        rt.setValue("Euclidean distance ("+units+")",0,track.getEuclideanDistance());
        rt.setValue("Total path length ("+units+")",0,track.getTotalPathLength());
        rt.setValue("Directionality ratio ",0,track.getDirectionalityRatio());
        rt.setValue("Mean step size ("+units+")",0,new CumStat(steps.values()).getMean());
        rt.setValue("Mean inst. vel. ("+units+"/frame)",0,new CumStat(speeds.values()).getMean());
        rt.setValue("Mean inst. dir. ratio",0,new CumStat(dirRatio.values()).getMean());

        IJ.log("Euclidean distance: "+String.valueOf(df.format(track.getEuclideanDistance()))+" "+units);
        IJ.log("Total path length: "+String.valueOf(df.format(track.getTotalPathLength()))+" "+units);
        IJ.log("Directionality ratio: "+String.valueOf(df.format(track.getDirectionalityRatio())));
        IJ.log(" ");

        IJ.log("Mean step length: "+String.valueOf(df.format(csStep.getMean()))+" "+units);
        IJ.log("Std. dev. of step lengths: "+String.valueOf(df.format(csStep.getStd()))+" "+units);
        IJ.log("Maximum step length: "+String.valueOf(df.format(csStep.getMax()))+" "+units);
        IJ.log("Minimum step length: "+String.valueOf(df.format(csStep.getMin()))+" "+units);
        IJ.log(" ");

        IJ.log("Mean instantaneous speed: "+String.valueOf(df.format(csSpeed.getMean()))+" "+units+"/frame");
        IJ.log("Std. dev. of instantaneous velocities: "+String.valueOf(df.format(csSpeed.getStd()))+" "+units+"/frame");
        IJ.log("Maximum instantaneous speed: "+String.valueOf(df.format(csSpeed.getMax()))+" "+units+"/frame");
        IJ.log("Minimum instantaneous speed: "+String.valueOf(df.format(csSpeed.getMin()))+" "+units+"/frame");
        IJ.log(" ");

        IJ.log("Mean instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getMean())));
        IJ.log("Std. dev. of instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getStd())));
        IJ.log("Maximum instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getMax())));
        IJ.log("Minimum instantaneous directionality ratio: "+String.valueOf(df.format(csInstDirRatio.getMin())));
        IJ.log(" ");

    }

    private void showDiffusionCoefficient(ResultsTable rt) {
        DecimalFormat df = new DecimalFormat("0.000E0");
        DecimalFormat dfS = new DecimalFormat("#");
        String units = tracks.values().iterator().next().getUnits();

        int nPoints = (int) Math.round(Double.parseDouble(nPointsTextField.getText()));

        TreeMap<Integer,CumStat> msd = tracks.getAverageMSD();
        double[] linearFit = MSDCalculator.getLinearFit(msd,nPoints);

        IJ.log("Diffusion coefficient (single average MSD): "+String.valueOf(df.format(linearFit[0]/4))+" "+units+"^2/frame");
        IJ.log("Least squares gradient (single average MSD): "+String.valueOf(df.format(linearFit[0]))+" "+units+"^2/frame");
        IJ.log("Least squares intercept (single average MSD): "+String.valueOf(df.format(linearFit[1]))+" "+units+"^2");
        IJ.log("Calculated over "+String.valueOf(dfS.format(linearFit[2]))+" frames");
        IJ.log(" ");

        int i = 0;
        for (Track track:tracks.values()) {
            double[] fit = track.getMSDLinearFit(nPoints);
            rt.setValue("Diffusion coefficient ("+units+"^2/frame)",i++,df.format(fit[0]/4));
        }
    }

    private void showDiffusionCoefficient(int ID, ResultsTable rt) {
        Track track = tracks.get(ID);
        DecimalFormat df = new DecimalFormat("0.000E0");
        DecimalFormat dfS = new DecimalFormat("#");
        String units = track.getUnits();

        int nPoints = (int) Math.round(Double.parseDouble(nPointsTextField.getText()));

        double[] linearFit = track.getMSDLinearFit(nPoints);

        rt.setValue("Diffusion coefficient ("+units+"^2/frame)",0,df.format(linearFit[0]/4));

        IJ.log("Diffusion coefficient: "+String.valueOf(df.format(linearFit[0]/4))+" "+units+"^2/frame");
        IJ.log("Least squares gradient: "+String.valueOf(df.format(linearFit[0]))+" "+units+"^2/frame");
        IJ.log("Least squares intercept: "+String.valueOf(df.format(linearFit[1]))+" "+units+"^2");
        IJ.log("Calculated over "+String.valueOf(dfS.format(linearFit[2]))+" frames");
        IJ.log(" ");
    }
}
