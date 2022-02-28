//TODO: Need to import distance calibration

package io.github.sjcross.TrackAnalysis.IO;

import java.util.ArrayList;
import java.util.Set;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.SelectionModel;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.TrackModel;
import fiji.plugin.trackmate.action.AbstractTMAction;
import ij.ImagePlus;
import ij.measure.Calibration;
import io.github.sjcross.TrackAnalysis.TrackAnalysis;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

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

                tracks.putIfAbsent(trackID, new Track(units));
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

