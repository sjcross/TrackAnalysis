package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;
import wbif.sjx.common.Object.Track;

import java.awt.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class TrackEntity {
    private static Mesh pointMesh;
    private final Track track;
    private Color colour;

    public TrackEntity(Track track){
        this(track, RNG.Colour());
    }

    public TrackEntity(Track track, Color colour){
        this.track = track;
        this.colour = colour;
    }

    public void render(ShaderProgram shaderProgram, int frame){
        if(frame >= 0 && frame < track.size()) {
            shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", Matrix4f.translation(DataTypeUtils.toVector3f(track.get(frame))));
            shaderProgram.setColourUniform("colour", colour);
            pointMesh.render();
        }
    }

    public Track getTrack() {
        return track;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public static void setPointMesh(Mesh mesh){
        pointMesh = mesh;
    }
}
