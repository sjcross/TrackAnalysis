//TODO: Need to import distance calibration

package wbif.sjx.TrackAnalysis.IO;

import fiji.plugin.trackmate.*;
import fiji.plugin.trackmate.action.AbstractTMAction;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import wbif.sjx.TrackAnalysis.Objects.Point;
import wbif.sjx.TrackAnalysis.Objects.Track;
import wbif.sjx.TrackAnalysis.Objects.TrackCollection;
import wbif.sjx.TrackAnalysis.TrackAnalysis;

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
        ImagePlus ipl = IJ.getImage();
        Calibration calibration = ipl.getCalibration();

        // Creating the ArrayList to hold all Tracks
        TrackCollection tracks = new TrackCollection(calibration.getX(1),calibration.getZ(1));

        // Converting tracks in TrackMate model to internal Track Objects
        TrackModel trackModel = model.getTrackModel();
        Set<Integer> trackIDs = trackModel.trackIDs(false);

        for (Integer trackID:trackIDs) {
            ArrayList<Spot> spots = new ArrayList<>(model.getTrackModel().trackSpots(trackID));

            // Getting x,y,f and 2-channel spot intensities from TrackMate results
            for (Spot spot : spots) {
                // NEED TO GET TRACK ID
                int ID = TRACK ID
                double x = spot.getFeature(Spot.POSITION_X)/tracks.getDistXY();
                double y = spot.getFeature(Spot.POSITION_Y)/tracks.getDistXY();
                double z = spot.getFeature(Spot.POSITION_Z)/tracks.getDistZ();
                int f = (int) Math.round(spot.getFeature(Spot.FRAME));

                tracks.putIfAbsent(ID, new Track());
                tracks.get(ID).add(new Point(x,y,z,f));

            }
        }
        System.out.println(tracks.size()+"_rand: "+tracks.get(124).getF()[2]);
        // Sorting spots in each track to ensure they are in chronological order
        for (Track track:tracks.values()) {
            track.sort((o1, o2) -> {
                double t1 = o1.getF();
                double t2 = o2.getF();
                return t1 > t2 ? 1 : t1 == t2 ? 0 : -1;
            });
        }

        System.out.println(tracks.size()+"_rand: "+tracks.get(124).getF()[2]);

        // Running TrackAnalysis
        new TrackAnalysis(tracks);

    }

    @Override
    public void setLogger( final Logger logger ) {
        this.logger = logger;

    }
}

