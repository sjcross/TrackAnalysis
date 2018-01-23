//TODO: Need to import distance calibration

package wbif.sjx.TrackAnalysis.IO;

import fiji.plugin.trackmate.*;
import fiji.plugin.trackmate.action.AbstractTMAction;
import ij.ImagePlus;
import ij.measure.Calibration;
import wbif.sjx.TrackAnalysis.TrackAnalysis;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Track;
import wbif.sjx.common.Object.TrackCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by sc13967 on 12/06/2017.
 */
public class TrackMateLoader extends AbstractTMAction {
    private final SelectionModel selectionModel;

    private final Model model;

    private Logger logger;

    public TrackMateLoader(final Model model, final SelectionModel selectionModel ) {
        this.model = model;
        this.selectionModel = selectionModel;

    }

    @Override
    public void execute(TrackMate trackMate) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        ImagePlus ipl = trackMate.getSettings().imp;
        Calibration calibration = ipl.getCalibration();

        double distXY = calibration.getX(1);
        double distZ = calibration.getZ(1);
        String units = calibration.getXUnit();

        // Creating the ArrayList to hold all Tracks
        TrackCollection tracks = new TrackCollection();

        // Converting tracks in TrackMate model to internal Track Objects
        TrackModel trackModel = model.getTrackModel();
        Set<Integer> trackIDs = trackModel.trackIDs(true);

        for (Integer trackID:trackIDs) {
            ArrayList<Spot> spots = new ArrayList<>(model.getTrackModel().trackSpots(trackID));

            // Getting x,y,f and 2-channel spot intensities from TrackMate results
            for (Spot spot : spots) {
                double x = spot.getFeature(Spot.POSITION_X);
                double y = spot.getFeature(Spot.POSITION_Y);
                double z = spot.getFeature(Spot.POSITION_Z);
                int f = (int) Math.round(spot.getFeature(Spot.FRAME));

                tracks.putIfAbsent(trackID, new Track(distXY,distZ,units));
                tracks.get(trackID).addTimepoint(x,y,z,f);

            }
        }

        // Running TrackAnalysis
//        new TrackAnalysis(tracks, ipl);
        new TrackAnalysis(tracks, null);

    }

    @Override
    public void setLogger( final Logger logger ) {
        this.logger = logger;

    }
}

