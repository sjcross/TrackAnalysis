package io.github.sjcross.TrackAnalysis.IO;

import ij.*;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import io.github.sjcross.TrackAnalysis.TrackAnalysis;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

import javax.swing.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackPlotLoader implements PlugIn {
    /**
     * Main method for debugging.
     * @param args
     */

    private static final String resDir = System.getProperty("user.dir") + "\\src\\main\\resources\\";

    public static void main(String[] args) {
        new ImageJ();

        IJ.runMacroFile(resDir + "Import_Results_Table.ijm");
//        IJ.runMacroFile("C:\\Users\\Jordan Fisher\\Documents\\Programming\\Java\\TrackAnalysis-3DTrackRenders\\src\\main\\resources\\Import_Results_Table.ijm");
//        IJ.runMacroFile("C:\\Users\\sc13967\\Local Documents\\ImageJMacros\\Import_Results_Table.ijm");
//        IJ.runMacroFile("C:\\Users\\sc13967\\Documents\\ImageJ Macros\\Import_Results_Table.ijm");
//        IJ.runMacroFile("E:\\Stephen\\ImageJ Macros\\ImageJMacros\\Import_Results_Table.ijm");

        new TrackPlotLoader().run("");
    }

    /**
     * Run by ImageJ.
     * @param s
     */
    public void run(String s) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Getting results from ResultsTable
        ResultsTable rt = ResultsTable.getResultsTable();
        TrackCollection tracks = doImport(rt,null);

        ImagePlus ipl = IJ.openImage(resDir + "tracks\\MAX_SimulatedTracks100_halfheight_z.tif");

        // Running TrackAnalysis
        new TrackAnalysis(tracks,ipl);
    }

    /**
     * Convert tracks stored in the results table to an ArrayList of Track objects.
     * @return
     */
    public TrackCollection doImport(ResultsTable rt, ImagePlus ipl) {
        // Get available ResultsTable columns
        String[] headings = rt.getHeadings();

        // Getting default heading selections
        int trackIDIdx = (int) Prefs.get("TrackAnalysis.trackIDIdx",0);
        int xPosIdx = (int) Prefs.get("TrackAnalysis.xPosIdx",2);
        int yPosIdx = (int) Prefs.get("TrackAnalysis.yPosIdx",3);
        int zPosIdx = (int) Prefs.get("TrackAnalysis.zPosIdx",4);
        int frameIdx = (int) Prefs.get("TrackAnalysis.frameIdx",1);

        // Getting default calibration value
        String units = "px";

        // Updating column headings
        String idCol = headings[trackIDIdx];
        String xCol = headings[xPosIdx];
        String yCol = headings[yPosIdx];
        String zCol = headings[zPosIdx];
        String fCol = headings[frameIdx];

        // Converting to Track objects
        TrackCollection tracks = new TrackCollection();

        for (int row=0;row<rt.getCounter();row++) {
            int ID = Integer.parseInt(rt.getStringValue(idCol,row));
            double x = Double.parseDouble(rt.getStringValue(xCol,row));
            double y = Double.parseDouble(rt.getStringValue(yCol,row));
            double z = Double.parseDouble(rt.getStringValue(zCol,row));
            int f = Integer.parseInt(rt.getStringValue(fCol,row));

            tracks.putIfAbsent(ID, new Track(units));
            tracks.get(ID).addTimepoint(x,y,z,f);
        }

        return tracks;
    }
}
