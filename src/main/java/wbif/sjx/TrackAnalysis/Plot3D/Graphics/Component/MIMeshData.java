package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.FaceMI.IndexSet.NULL_INDEX;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class MIMeshData {

    private ArrayList<Vector3f> vertexPositions;
    private ArrayList<Vector2f> textureCoords;
    private ArrayList<FaceMI> indexs;

    public MIMeshData(Vector3f[] vertexPositions, Vector2f[] texCoords, FaceMI[] indexs) {
        this(vertexPositions == null ? null : new ArrayList<>(Arrays.asList(vertexPositions)), texCoords == null ? null : new ArrayList<>(Arrays.asList(texCoords)), indexs == null ? null : new ArrayList<>(Arrays.asList(indexs)));
    }

    public MIMeshData(ArrayList<Vector3f> vertexPositions, ArrayList<Vector2f> textureCoords, ArrayList<FaceMI> indexs) {
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
        this.indexs = indexs;
    }

    public ArrayList<Vector3f> getVertexPositions() {
        return vertexPositions;
    }

    public ArrayList<Vector2f> getTextureCoords() {
        return textureCoords;
    }

    public ArrayList<FaceMI> getIndexs() {
        return indexs;
    }

    public SIMeshData toSIMeshData() {
        return ToSIMeshData(this);
    }

    private static SIMeshData ToSIMeshData(MIMeshData meshData) {
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<FaceSI> faces = new ArrayList<>();
        HashMap<FaceMI.IndexSet, Integer> map = new HashMap<>();

        int indexOffset = 0;

        for (FaceMI faceMI : meshData.getIndexs()) {
            int[] faceSIIndexs = new int[faceMI.getFaceOrder()];

            for (int i = 0; i < faceMI.getFaceOrder(); i++) {
                FaceMI.IndexSet indexSet = faceMI.getIndexSets()[i];

                if (!map.containsKey(indexSet)) {
                    map.put(indexSet, indexOffset);
                    faceSIIndexs[i] = indexOffset;
                    indexOffset++;

                    final int vertexPositionIndex = indexSet.getVertexPositionIndex();
                    final int textureCoordinateIndex = indexSet.getTextureCoordinateIndex();

                    vertices.add(new Vertex(
                            meshData.getVertexPositions().get(vertexPositionIndex),
                            textureCoordinateIndex == NULL_INDEX || meshData.getTextureCoords() == null ? null : meshData.getTextureCoords().get(textureCoordinateIndex)
                    ));
                } else {
                    faceSIIndexs[i] = map.get(indexSet);
                }
            }

            faces.add(new FaceSI(faceSIIndexs));
        }

        return new SIMeshData(vertices, faces);
    }
}
