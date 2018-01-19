package wbif.sjx.TrackAnalysis.IO;

import ij.*;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import wbif.sjx.TrackAnalysis.TrackAnalysis;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Timepoint;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;
import wbif.sjx.common.Process.SwitchTAndZ;

import javax.swing.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads tracks from results table.  Tracks are stored in terms of calibrated distances.
 */
public class FishLoader implements PlugIn {
    /**
     * Main method for debugging.
     * @param args
     */
    public static void main(String[] args) {
        new ImageJ();
        new FishLoader().run("");

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
        String window = getImageWindow();
        ImagePlus ipl;
        if (!window.equals("None")) {
            ipl = WindowManager.getImage(window);
            if (ipl.getNFrames()==1 & ipl.getNSlices() > 1) {
                int yn_bt = JOptionPane.showConfirmDialog(null, "Swap frames (t) and slices (z)?","Swap dimensions",JOptionPane.YES_NO_OPTION);
                if (yn_bt == JOptionPane.YES_OPTION) {
                    SwitchTAndZ.run(ipl);
                }

            }
        } else {
            ipl = null;
        }

        // Getting results from ResultsTable
        TrackCollection tracks = null;
        try {
            tracks = doImport(ipl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Running TrackAnalysis
        new TrackAnalysis(tracks,ipl);

    }

    /**
     * Gets a String[] containing the names of the results table and image windows to be processed.  If no image is to
     * be loaded, the second argument is "null".
     * @return
     */
    private static String getImageWindow() {
        String[] imageListTemp = WindowManager.getImageTitles();

        if (imageListTemp.length == 0) return "None";

        String[] imageList = new String[imageListTemp.length+1];
        imageList[0] = "None";
        System.arraycopy(imageListTemp, 0, imageList, 1, imageList.length - 1);

        GenericDialog gd = new GenericDialog("Select windows");
        gd.addChoice("Image",imageList,imageList[0]);
        gd.showDialog();

        if (gd.wasCanceled()) return null;

        return gd.getNextChoice();

    }

    /**
     * Convert tracks stored in the text file to an ArrayList of Track objects.
     * @return
     */
    public TrackCollection doImport(ImagePlus ipl) throws IOException {
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
        gd.addNumericField("Applied XY calibration (dist/px)",distXY,3);
        gd.addNumericField("Applied Z calibration (dist/slice)",distZ,3);
        gd.addStringField("Units",units);
        gd.showDialog();

        // Getting calibration
        distXY = gd.getNextNumber();
        distZ = gd.getNextNumber();
        units = gd.getNextString();

        // Getting the file to read
        String filePath = Prefs.get("TrackAnalysis.filePath","");
        JFileChooser jFileChooser = new JFileChooser(filePath);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.showDialog(null,"Open");

        File inputFile = jFileChooser.getSelectedFile();
        Prefs.set("TrackAnalysis.filePath",inputFile.getAbsolutePath());

        TrackCollection tracks = new TrackCollection();
        FileReader fileReader = new FileReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // Reading header line to get the number of tracked objects (number of columns divided by 3)
        String line = bufferedReader.readLine();
        StringTokenizer stringTokenizer = new StringTokenizer(line,"\t");
        int nTracks = stringTokenizer.countTokens()/3;

        // Initialising tracks
        for (int i=0;i<nTracks;i++) {
            tracks.put(i+1,new Track(distXY,distZ,units));
        }

        // Reading subsequent lines
        line = bufferedReader.readLine();

        int f = 0; // Frame number
        while (line != null) {
            stringTokenizer = new StringTokenizer(line,"\t");

            int col = 0;
            double x = 0;
            double y = 0;
            while (stringTokenizer.hasMoreTokens()) {
                double next = Double.parseDouble(stringTokenizer.nextToken());

                if (col%3 == 0) {
                    x = next;
                } else if (col%3 == 1){
                    y = next;
                } else if (col%3 == 2) {
                    int ID = col/3+1;
                    tracks.get(ID).addTimepoint(x,y,0,f);
                }

                col++;
            }

            // Getting next line
            line = bufferedReader.readLine();
            f++;
        }

        return tracks;

    }
}
