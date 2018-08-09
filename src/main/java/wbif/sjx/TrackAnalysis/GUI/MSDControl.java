//package wbif.sjx.TrackAnalysis.GUI;
//
//import ij.ImagePlus;
//import ij.Prefs;
//import ij.gui.Plot;
//import wbif.sjx.common.Analysis.MSDCalculator;
//import wbif.sjx.common.MathFunc.CumStat;
//import wbif.sjx.common.Object.Track;
//import wbif.sjx.common.Object.TrackCollection;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.util.Arrays;
//
///**
// * Created by sc13967 on 25/06/2017.
// */
//public class MSDControl extends ModuleControl {
//    JCheckBox fitLineCheckbox;
//    private JTextField nPointsTextField;
//
//    public MSDControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
//        super(tracks, ipl, panelWidth, elementHeight);
//    }
//
//    @Override
//    public String getTitle() {
//        return "Mean squared displacement plot";
//    }
//
//    @Override
//    public JPanel getExtraControls() {
//        JPanel panel = new JPanel(new GridBagLayout());
//
//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = 0;
//        c.gridy = 0;
//        c.gridwidth = 2;
//        c.insets = new Insets(0,5,0,5);
//
//        // Checkbox for number of objects
//        boolean fitLine = Prefs.get("TrackAnalysis.MSD.fitLine",true);
//        fitLineCheckbox = new JCheckBox("Fit line to start");
//        fitLineCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
//        fitLineCheckbox.setSelected(fitLine);
//        panel.add(fitLineCheckbox,c);
//
//        // Label and text field to get number of points to use for diffusion coefficient calculation
//        JTextField label = new JTextField("Number of frames");
//        label.setPreferredSize(new Dimension(3*panelWidth/4,elementHeight));
//        label.setEditable(false);
//        label.setBorder(null);
//        c.gridwidth = 1;
//        c.gridy++;
//        c.insets = new Insets(5,5,20,5);
//        panel.add(label,c);
//
//        int nPoints = (int) Prefs.get("TrackAnalysis.MSD.nPoints",10);
//        nPointsTextField = new JTextField();
//        nPointsTextField.setPreferredSize(new Dimension(panelWidth/4-5,elementHeight));
//        nPointsTextField.setText(String.valueOf(nPoints));
//        c.gridx++;
//        c.insets = new Insets(5,0,20,5);
//        panel.add(nPointsTextField,c);
//
//        return panel;
//
//    }
//
//    @Override
//    public void run(int ID) {
//        boolean pixelDistances = calibrationCheckbox.isSelected();
//        Prefs.set("TrackAnalysis.pixelDistances",pixelDistances);
//
//        boolean fitLine = fitLineCheckbox.isSelected();
//        Prefs.set("TrackAnalysis.MSD.fitLine",fitLine);
//
//        int nPoints = (int) Math.round(Double.parseDouble(nPointsTextField.getText()));
//        Prefs.set("TrackAnalysis.MSD.nPoints",nPoints);
//
//        if (ID == -1) {
//            double[][] msd = tracks.getAverageMSD(pixelDistances);
//            double[] errMin = new double[msd[0].length];
//            double[] errMax = new double[msd[0].length];
//
//            for (int i=0;i<errMin.length;i++) {
//                errMin[i] = msd[1][i] - msd[2][i];
//                errMax[i] = msd[1][i] + msd[2][i];
//            }
//
//            String units = tracks.values().iterator().next().getUnits(pixelDistances);
//            Plot plot = new Plot("Mean squared displacement (all tracks)","Interval (frames)","Mean squared displacement ("+units+"^2)");
//            plot.setColor(Color.BLACK);
//            plot.addPoints(msd[0],msd[1],Plot.LINE);
//            plot.setColor(Color.RED);
//            plot.addPoints(msd[0],errMin,Plot.LINE);
//            plot.addPoints(msd[0],errMax,Plot.LINE);
//
//            if (fitLine) {
//                double[] fit = MSDCalculator.getLinearFit(msd[0], msd[1], nPoints);
//                double[] x = new double[nPoints];
//                double[] y = new double[nPoints];
//
//                for (int i = 0; i < x.length; i++) {
//                    x[i] = msd[0][i];
//                    y[i] = fit[0] * msd[0][i] + fit[1];
//                }
//                plot.setColor(Color.CYAN);
//                plot.addPoints(x, y, Plot.LINE);
//
//            }
//
//            plot.setLimitsToFit(true);
//            plot.show();
//
//        } else {
//            Track track = tracks.get(ID);
//            double[] f = track.getFAsDouble();
//            CumStat[] cs = track.getMSD(pixelDistances);
//            double[] msd = Arrays.stream(cs).mapToDouble(CumStat::getMean).toArray();
//
//            String units = track.getUnits(pixelDistances);
//            Plot plot = new Plot("Mean squared displacement (track "+ID+")","Interval (frames)","Mean squared displacement ("+units+")");
//            plot.setColor(Color.BLACK);
//            plot.addPoints(f,msd,Plot.LINE);
//
//            if (fitLine) {
//                double[] fit = MSDCalculator.getLinearFit(f, msd, nPoints);
//                double[] x = new double[(int) fit[2]];
//                double[] y = new double[(int) fit[2]];
//
//                for (int i = 0; i < x.length; i++) {
//                    x[i] = f[i];
//                    y[i] = fit[0] * f[i] + fit[1];
//                }
//
//                plot.setColor(Color.CYAN);
//                plot.addPoints(x, y, Plot.LINE);
//
//            }
//
//            plot.setLimitsToFit(true);
//            plot.show();
//
//        }
//    }
//
//    @Override
//    public void extraActions(ActionEvent e) {
//
//    }
//}

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import wbif.sjx.common.Analysis.MSDCalculator;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by sc13967 on 25/06/2017.
 */
public class MSDControl extends ModuleControl {
    JCheckBox fitLineCheckbox;
    private JTextField nPointsTextField;

    public MSDControl(TrackCollection tracks, ImagePlus ipl, int panelWidth, int elementHeight) {
        super(tracks, ipl, panelWidth, elementHeight);
    }

    @Override
    public String getTitle() {
        return "Mean squared displacement plot";
    }

    @Override
    public JPanel getExtraControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0,5,0,5);

        // Checkbox for number of objects
        boolean fitLine = Prefs.get("TrackAnalysis.MSD.fitLine",true);
        fitLineCheckbox = new JCheckBox("Fit line to start");
        fitLineCheckbox.setPreferredSize(new Dimension(panelWidth,elementHeight));
        fitLineCheckbox.setSelected(fitLine);
        panel.add(fitLineCheckbox,c);

        // Label and text field to get number of points to use for diffusion coefficient calculation
        JTextField label = new JTextField("Number of frames");
        label.setPreferredSize(new Dimension(3*panelWidth/4,elementHeight));
        label.setEditable(false);
        label.setBorder(null);
        c.gridwidth = 1;
        c.gridy++;
        c.insets = new Insets(5,5,20,5);
        panel.add(label,c);

        int nPoints = (int) Prefs.get("TrackAnalysis.MSD.nPoints",10);
        nPointsTextField = new JTextField();
        nPointsTextField.setPreferredSize(new Dimension(panelWidth/4-5,elementHeight));
        nPointsTextField.setText(String.valueOf(nPoints));
        c.gridx++;
        c.insets = new Insets(5,0,20,5);
        panel.add(nPointsTextField,c);

        return panel;

    }

    @Override
    public void run(int ID) {
        boolean pixelDistances = calibrationCheckbox.isSelected();
        Prefs.set("TrackAnalysis.pixelDistances",pixelDistances);

        boolean fitLine = fitLineCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MSD.fitLine",fitLine);

        int nPoints = (int) Math.round(Double.parseDouble(nPointsTextField.getText()));
        Prefs.set("TrackAnalysis.MSD.nPoints",fitLine);

        if (ID == -1) {
            TreeMap<Integer,CumStat> msd = tracks.getAverageMSD(pixelDistances);
            double[] errMin = new double[msd.size()];
            double[] errMax = new double[msd.size()];

            double[] df = msd.keySet().stream().mapToDouble(v->v).toArray();
            double[] msdMean = msd.values().stream().mapToDouble(CumStat::getMean).toArray();
            double[] msdStd = msd.values().stream().mapToDouble(CumStat::getStd).toArray();

            for (int i=0;i<errMin.length;i++) {
                errMin[i] = msdMean[i] - msdStd[i];
                errMax[i] = msdMean[i] + msdStd[i];
            }

            String units = tracks.values().iterator().next().getUnits(pixelDistances);
            Plot plot = new Plot("Mean squared displacement (all tracks)","Interval (frames)","Mean squared displacement ("+units+"^2)");
            plot.setColor(Color.BLACK);
            plot.addPoints(df,msdMean,Plot.LINE);
            plot.setColor(Color.RED);
            plot.addPoints(df,errMin,Plot.LINE);
            plot.addPoints(df,errMax,Plot.LINE);

            if (fitLine) {
                double[] fit = MSDCalculator.getLinearFit(msd, nPoints);
                double[] y = new double[df.length];

                for (int i = 0; i < df.length; i++) {
                    y[i] = fit[0] * df[i] + fit[1];
                }
                plot.setColor(Color.CYAN);
                plot.addPoints(df, y, Plot.LINE);

            }

            plot.setLimitsToFit(true);
            plot.show();

        } else {
            Track track = tracks.get(ID);
            TreeMap<Integer,CumStat> msd = track.getMSD(pixelDistances);
            double[] df = msd.keySet().stream().mapToDouble(v->v).toArray();
            double[] msdVals = msd.values().stream().mapToDouble(CumStat::getMean).toArray();

            String units = track.getUnits(pixelDistances);
            Plot plot = new Plot("Mean squared displacement (track "+ID+")","Interval (frames)","Mean squared displacement ("+units+")");
            plot.setColor(Color.BLACK);
            plot.addPoints(df,msdVals,Plot.LINE);

            if (fitLine) {
                double[] fit = MSDCalculator.getLinearFit(msd, nPoints);
                double[] y = new double[(int) fit[2]];

                for (int i = 0; i < df.length; i++) {
                    y[i] = fit[0] * df[i] + fit[1];
                }

                plot.setColor(Color.CYAN);
                plot.addPoints(df, y, Plot.LINE);

            }

            plot.setLimitsToFit(true);
            plot.show();

        }
    }

    @Override
    public void extraActions(ActionEvent e) {

    }
}
