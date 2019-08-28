package wbif.sjx.TrackAnalysis.IO;

import fiji.stacks.Hyperstack_rearranger;
import ij.*;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.HyperStackConverter;
import ij.plugin.PlugIn;
import wbif.sjx.TrackAnalysis.TrackAnalysis;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;
import wbif.sjx.common.Process.SwitchTAndZ;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Loads tracks from results table.  Tracks are stored in terms of calibrated distances.
 */
public class ResultsTableLoader implements PlugIn {
    /**
     * Main method for debugging.
     * @param args
     */
    public static void main(String[] args) {
        try {
            new ImageJ();
            String pathToMacro = URLDecoder.decode(TrackAnalysis.class.getResource("/Import_Results_Table.ijm").getPath(),"UTF-8");
            IJ.runMacro("waitForUser");
            new ResultsTableLoader().run("");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

        // Getting the current windows
        String[] windows = getWindows();
        if (windows == null) return;

        IJ.renameResults(windows[0], "Results");

        ImagePlus ipl;
        if (!windows[1].equals("None")) {
            ipl = WindowManager.getImage(windows[1]);
            if (ipl.getNFrames()==1 & ipl.getNSlices() > 1) {
                int yn_bt = JOptionPane.showConfirmDialog(null, "Swap frames (t) and slices (z)?","Swap dimensions",JOptionPane.YES_NO_OPTION);
                if (yn_bt == JOptionPane.YES_OPTION) {
                    ipl.setDimensions(ipl.getNChannels(),ipl.getNFrames(),ipl.getNSlices());
                    ipl.getCalibration().setTimeUnit("frame");
                    ipl.getCalibration().frameInterval = 1;
                }
            }
        } else {
            ipl = null;
        }

        // Getting results from ResultsTable
        ResultsTable rt = ResultsTable.getResultsTable();
        TrackCollection tracks = doImport(rt,ipl);

        // Running TrackAnalysis
        new TrackAnalysis(tracks,ipl);

    }

    /**
     * Gets a String[] containing the names of the results table and image windows to be processed.  If no image is to
     * be loaded, the second argument is "null".
     * @return
     */
    private static String[] getWindows() {
        String[] tableList = WindowManager.getNonImageTitles();
        String[] imageListTemp = WindowManager.getImageTitles();

        String[] imageList = new String[imageListTemp.length+1];
        imageList[0] = "None";
        System.arraycopy(imageListTemp, 0, imageList, 1, imageList.length - 1);

        GenericDialog gd = new GenericDialog("Select windows");
        gd.addChoice("Tracks",tableList,tableList[0]);
        gd.addChoice("Image",imageList,imageList[0]);
        gd.showDialog();

        if (gd.wasCanceled()) return null;

        String[] windows = new String[2];
        windows[0] = gd.getNextChoice();
        windows[1] = gd.getNextChoice();

        return windows;

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

        // Checking these indices are within range of the current results table
        trackIDIdx = trackIDIdx < headings.length ? trackIDIdx : 0;
        xPosIdx = xPosIdx < headings.length ? xPosIdx : 0;
        yPosIdx = yPosIdx < headings.length ? yPosIdx : 0;
        zPosIdx = zPosIdx < headings.length ? zPosIdx : 0;
        frameIdx = frameIdx < headings.length ? frameIdx : 0;

        // Getting default calibration value
        double distXY = 1;
        double distZ = 1;
        String units = "px";
        if (ipl != null) {
            distXY = ipl.getCalibration().getX(1);
            distZ = ipl.getCalibration().getZ(1);
            units = ipl.getCalibration().getXUnit();

        }

        GenericDialog gd = new GenericDialog("Import tracks from results table");
        gd.addChoice("Track ID column",headings,headings[trackIDIdx]);
        gd.addChoice("X-position column",headings,headings[xPosIdx]);
        gd.addChoice("Y-position column",headings,headings[yPosIdx]);
        gd.addChoice("Z-position column",headings,headings[zPosIdx]);
        gd.addChoice("Frame number column",headings,headings[frameIdx]);
        gd.addNumericField("Applied XY calibration (dist/px)",distXY,3);
        gd.addNumericField("Applied Z calibration (dist/slice)",distZ,3);
        gd.addStringField("Units",units);
        gd.showDialog();

        // Getting column indices
        trackIDIdx = gd.getNextChoiceIndex();
        xPosIdx = gd.getNextChoiceIndex();
        yPosIdx = gd.getNextChoiceIndex();
        zPosIdx = gd.getNextChoiceIndex();
        frameIdx = gd.getNextChoiceIndex();

        // Getting calibration
        units = gd.getNextString();

        // Storing indices as preferences
        Prefs.set("TrackAnalysis.trackIDIdx",trackIDIdx);
        Prefs.set("TrackAnalysis.xPosIdx",xPosIdx);
        Prefs.set("TrackAnalysis.yPosIdx",yPosIdx);
        Prefs.set("TrackAnalysis.zPosIdx",zPosIdx);
        Prefs.set("TrackAnalysis.frameIdx",frameIdx);

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
