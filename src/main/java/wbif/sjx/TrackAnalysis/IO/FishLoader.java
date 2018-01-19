package wbif.sjx.TrackAnalysis.IO;

import ij.*;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import wbif.sjx.TrackAnalysis.TrackAnalysis;
import wbif.sjx.common.Object.Point;
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
        double distXY = 1;
        double distZ = 1;
        String units = "px";

        // Loading the text file
        String fName = "C:\\Users\\sc13967\\Downloads\\trajectories_nogaps_tests.txt";
        File inputFile = new File(fName);
        try {
            FileReader fileReader = new FileReader(fName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Reading header line to get the number of tracked objects (number of columns divided by 3)
            String line = bufferedReader.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line,"\t");
            int nTracks = stringTokenizer.countTokens()/3;
            System.out.println(nTracks+" tracks");

            // Initialising tracks
            TrackCollection tracks = new TrackCollection();
            for (int i=0;i<nTracks;i++) {
                Track track = new Track(distXY,distZ,units);
                tracks.put(i,track);

            }

            // Reading subsequent lines
            line = bufferedReader.readLine();

            int f = 0; // Frame number
            while (line != null) {
                stringTokenizer = new StringTokenizer(line,"\t");

                int col = 0;
                while (stringTokenizer.hasMoreTokens()) {
//                    int ID = Integer.parseInt(rt.getStringValue(idCol,row));
//                    double x = Double.parseDouble(rt.getStringValue(xCol,row));
//                    double y = Double.parseDouble(rt.getStringValue(yCol,row));
//
//                    tracks.get(ID).add(new Point(x,y,0,f));

                    col++;

                }

                // Getting next line
                line = bufferedReader.readLine();
                f++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        new FishLoader().run("");

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
        TrackCollection tracks = doImport(ipl);

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
    public TrackCollection doImport(ImagePlus ipl) {
        // Getting default calibration value
        double distXY = 1;
        double distZ = 1;
        String units = "px";
        if (ipl != null) {
            distXY = ipl.getCalibration().getX(1);
            distZ = ipl.getCalibration().getZ(1);
            units = ipl.getCalibration().getXUnit();

        }

//        GenericDialog gd = new GenericDialog("Import tracks from results table");
//        gd.addNumericField("Applied XY calibration (dist/px)",distXY,3);
//        gd.addNumericField("Applied Z calibration (dist/slice)",distZ,3);
//        gd.addStringField("Units",units);
//        gd.showDialog();
//
//        // Getting calibration
//        distXY = gd.getNextNumber();
//        distZ = gd.getNextNumber();
//        units = gd.getNextString();

        // Getting the file to read
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.showDialog(null,"Open");

        File inputFile = jFileChooser.getSelectedFile();
        System.out.println(inputFile.getAbsolutePath());
//        // Converting to Track objects
//        TrackCollection tracks = new TrackCollection();
//
//        for (int row=0;row<rt.getCounter();row++) {
//            int ID = Integer.parseInt(rt.getStringValue(idCol,row));
//            double x = Double.parseDouble(rt.getStringValue(xCol,row));
//            double y = Double.parseDouble(rt.getStringValue(yCol,row));
//            double z = Double.parseDouble(rt.getStringValue(zCol,row));
//            int f = Integer.parseInt(rt.getStringValue(fCol,row));
//
//            tracks.putIfAbsent(ID, new Track(distXY,distZ,units));
//            tracks.get(ID).add(new Point(x,y,z,f));
//
//        }
//
//        // Sorting spots in each track to ensure they are in chronological order
//        for (Track track:tracks.values()) {
//            track.sort((o1, o2) -> {
//                double t1 = o1.getF();
//                double t2 = o2.getF();
//                return t1 > t2 ? 1 : t1 == t2 ? 0 : -1;
//            });
//        }
//
//        return tracks;

        return null;

    }

}
