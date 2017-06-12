package wbif.sjx.TrackAnalysis;

import fiji.plugin.trackmate.action.TrackMateAction;
import fiji.plugin.trackmate.action.TrackMateActionFactory;
import fiji.plugin.trackmate.gui.TrackMateGUIController;
import ij.IJ;
import org.scijava.plugin.Plugin;

import javax.swing.*;

@Plugin(type = TrackMateActionFactory.class)

/**
 * Loads tracks directly from TrackMate, then launches TrackAnalysis.
 */
public class TrackMateLoaderFactory implements TrackMateActionFactory {
    private static final String INFO_TEXT = "<html>Load tracks into track analysis plugin</html>";
    private static final String KEY = "LAUNCH_TRACK_ANALYSIS";
    private static final String NAME = "Launch track analysis";

    public TrackMateAction create(final TrackMateGUIController controller) {
        return new TrackMateLoader(controller.getPlugin().getModel(), controller.getSelectionModel());

    }

    public String getInfoText() {
        return INFO_TEXT;
    }

    public ImageIcon getIcon() {
        return null;
    }

    public String getKey() {
        return KEY;
    }

    public String getName() {
        return NAME;
    }

}
