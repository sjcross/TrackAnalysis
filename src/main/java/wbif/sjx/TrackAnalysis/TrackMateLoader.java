package wbif.sjx.TrackAnalysis;

import fiji.plugin.trackmate.*;
import fiji.plugin.trackmate.action.AbstractTMAction;

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
        // Creating the ArrayList to hold all Tracks
        ArrayList<Track> tracks = new ArrayList<>(model.getTrackModel().nTracks(false));

        // Converting tracks in TrackMate model to internal Track Objects
        TrackModel trackModel = model.getTrackModel();
        Set<Integer> trackIDs = trackModel.trackIDs(false);

        for (Integer trackID:trackIDs) {
            ArrayList<Spot> spots = new ArrayList<>(model.getTrackModel().trackSpots(trackID));

            // Creating stores for the spots
            double[] x = new double[spots.size()];
            double[] y = new double[spots.size()];
            double[] z = new double[spots.size()];
            int[] f = new int[spots.size()];

            // Sorting spots based on frame number
            spots.sort((o1, o2) -> {
                double t1 = o1.getFeature(Spot.FRAME);
                double t2 = o2.getFeature(Spot.FRAME);
                return t1 > t2 ? 1 : t1 == t2 ? 0 : -1;
            });

            // Getting x,y,f and 2-channel spot intensities from TrackMate results
            int counter = 0;
            for (Spot spot : spots) {
                // Getting coordinates
                x[counter] = spot.getFeature(Spot.POSITION_X);
                y[counter] = spot.getFeature(Spot.POSITION_Y);
                z[counter] = spot.getFeature(Spot.POSITION_Z);
                f[counter++] = (int) Math.round(spot.getFeature(Spot.FRAME));

            }

            Track track = new Track(x,y,z,f);
            tracks.add(track);

        }

        // Running TrackAnalysis
        new TrackAnalysis(tracks);

    }

    @Override
    public void setLogger( final Logger logger ) {
        this.logger = logger;

    }
}

