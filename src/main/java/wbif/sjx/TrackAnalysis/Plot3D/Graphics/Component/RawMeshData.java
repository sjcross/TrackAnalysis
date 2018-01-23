package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.util.ArrayList;

public class RawMeshData{
    private ArrayList<Vector3f> vertices;
    private ArrayList<Face> faces;

    public RawMeshData(ArrayList<Vector3f> vertices, ArrayList<Face> faces){
        this.vertices = vertices;
        this.faces = faces;
    }

    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }
}
