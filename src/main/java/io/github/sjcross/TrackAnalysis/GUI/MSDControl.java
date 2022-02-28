package io.github.sjcross.TrackAnalysis.GUI;

import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.elementHeight;
import static io.github.sjcross.TrackAnalysis.GUI.Control.MainGUI.frameWidth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Plot;
import io.github.sjcross.TrackAnalysis.GUI.Control.PlotableModule;
import io.github.sjcross.common.analysis.MSDCalculator;
import io.github.sjcross.common.mathfunc.CumStat;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

/**
 * Created by sc13967 on 25/06/2017.
 */
public class MSDControl extends PlotableModule
{
    private JCheckBox fitLineCheckbox;
    private JTextField nPointsTextField;

    public MSDControl(TrackCollection tracks, ImagePlus ipl) {
        super(tracks, ipl);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0,5,0,5);

        // Checkbox for number of objects
        boolean fitLine = Prefs.get("TrackAnalysis.MSD.fitLine",true);
        fitLineCheckbox = new JCheckBox("Fit line to start");
        fitLineCheckbox.setPreferredSize(new Dimension(frameWidth,elementHeight));
        fitLineCheckbox.setSelected(fitLine);
        panel.add(fitLineCheckbox,c);

        // Label and text field to get number of points to use for diffusion coefficient calculation
        JTextField label = new JTextField("Number of frames");
        label.setPreferredSize(new Dimension(3*frameWidth/4,elementHeight));
        label.setEditable(false);
        label.setBorder(null);
        c.gridwidth = 1;
        c.gridy++;
        c.insets = new Insets(5,5,20,5);
        panel.add(label,c);

        int nPoints = (int) Prefs.get("TrackAnalysis.MSD.nPoints",10);
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
        boolean fitLine = fitLineCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MSD.fitLine",fitLine);

        int nPoints = (int) Math.round(Double.parseDouble(nPointsTextField.getText()));
        Prefs.set("TrackAnalysis.MSD.nPoints",fitLine);

        TreeMap<Integer,CumStat> msd = tracks.getAverageMSD();
        double[] errMin = new double[msd.size()];
        double[] errMax = new double[msd.size()];

        double[] df = msd.keySet().stream().mapToDouble(v->v).toArray();
        double[] msdMean = msd.values().stream().mapToDouble(CumStat::getMean).toArray();
        double[] msdStd = msd.values().stream().mapToDouble(CumStat::getStd).toArray();

        for (int i=0;i<errMin.length;i++) {
            errMin[i] = msdMean[i] - msdStd[i];
            errMax[i] = msdMean[i] + msdStd[i];
        }

        String units = tracks.values().iterator().next().getUnits();
        Plot msdPlot = new Plot("Mean squared displacement (all tracks)","Interval (frames)","Mean squared displacement ("+units+"^2)");
        msdPlot.setColor(Color.BLACK);
        msdPlot.addPoints(df,msdMean,Plot.LINE);
        msdPlot.setColor(Color.RED);
        msdPlot.addPoints(df,errMin,Plot.LINE);
        msdPlot.addPoints(df,errMax,Plot.LINE);

        if (fitLine) {
            double[] fit = MSDCalculator.getLinearFit(msd, nPoints);
            double[] y = new double[df.length];

            for (int i = 0; i < df.length; i++) {
                y[i] = fit[0] * df[i] + fit[1];
            }
            msdPlot.setColor(Color.CYAN);
            msdPlot.addPoints(df, y, Plot.LINE);

        }

        msdPlot.setLimitsToFit(true);
        msdPlot.show();
    }

    @Override
    public void plotTrack(int ID)
    {
        boolean fitLine = fitLineCheckbox.isSelected();
        Prefs.set("TrackAnalysis.MSD.fitLine",fitLine);

        int nPoints = (int) Math.round(Double.parseDouble(nPointsTextField.getText()));
        Prefs.set("TrackAnalysis.MSD.nPoints",fitLine);

        Track track = tracks.get(ID);
        TreeMap<Integer,CumStat> msd = track.getMSD();
        double[] df = msd.keySet().stream().mapToDouble(v->v).toArray();
        double[] msdVals = msd.values().stream().mapToDouble(CumStat::getMean).toArray();

        String units = track.getUnits();
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
