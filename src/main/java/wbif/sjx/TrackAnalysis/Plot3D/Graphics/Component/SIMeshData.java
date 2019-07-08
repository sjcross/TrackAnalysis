package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import java.util.ArrayList;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class SIMeshData {

    private ArrayList<Vertex> vertices;
    private ArrayList<FaceSI> faces;

    public SIMeshData(ArrayList<Vertex> vertices, ArrayList<FaceSI> faces){
        this.vertices = vertices;
        this.faces = faces;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<FaceSI> getFaces() {
        return faces;
    }
}
