package wbif.sjx.TrackAnalysis.IO;

import ij.*;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import wbif.sjx.TrackAnalysis.TrackAnalysis;
import wbif.sjx.common.Object.LUTs;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;
import wbif.sjx.common.Process.SwitchTAndZ;

import javax.swing.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class JordansSuperSpeedyLoaderOfWin implements PlugIn {
    /**
     * Main method for debugging.
     * @param args
     */
    public static void main(String[] args) {
        new ImageJ();
        IJ.runMacroFile("C:\\Users\\sc13967\\Local Documents\\ImageJMacros\\Import_Results_Table.ijm");
//        IJ.runMacroFile("C:\\Users\\sc13967\\Documents\\ImageJ Macros\\Import_Results_Table.ijm");
//        IJ.runMacroFile("E:\\Stephen\\ImageJ Macros\\ImageJMacros\\Import_Results_Table.ijm");


        new JordansSuperSpeedyLoaderOfWin().run("");

    }

    /**
     * Run by ImageJ.
     * @param s
     */
    public void run(String s) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

                // Getting results from ResultsTable
        ResultsTable rt = ResultsTable.getResultsTable();
        TrackCollection tracks = doImport(rt,null);

        // Running TrackAnalysis
        new TrackAnalysis(tracks,null);

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
        int xPosIdx = (int) Prefs.get("TrackAnalysis.xPosIdx",0);
        int yPosIdx = (int) Prefs.get("TrackAnalysis.yPosIdx",0);
        int zPosIdx = (int) Prefs.get("TrackAnalysis.zPosIdx",0);
        int frameIdx = (int) Prefs.get("TrackAnalysis.frameIdx",0);


        // Getting default calibration value
        double distXY = 1;
        double distZ = 1;
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

            tracks.putIfAbsent(ID, new Track(distXY,distZ,units));
            tracks.get(ID).put(f,new Point(x,y,z,f));

        }

//        // Sorting spots in each track to ensure they are in chronological order
//        for (Track track:tracks.values()) {
//            track.sort((o1, o2) -> {
//                double t1 = o1.getF();
//                double t2 = o2.getF();
//                return t1 > t2 ? 1 : t1 == t2 ? 0 : -1;
//            });
//        }

        return tracks;

    }
}
