package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.GenerateMesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.common.Object.TrackCollection;

import java.awt.*;

/**
 * Created by sc13967 on 04/08/2017.
 */
public class BoundingBox {

    private final Vector3f centrePosition;
    private final Vector3f minPosition;
    private final Vector3f maxPosition;

    private final float width;
    private final float length;
    private final float height;

    private final Entity boundingBox;

    public BoundingBox(TrackCollection tracks){
        double[][] spacialLimits = tracks.getSpatialLimits(true);

        //Convert from XZY to XYZ
        minPosition = new Vector3f(
                (float)spacialLimits[0][0],
                (float)spacialLimits[2][0],
                (float)spacialLimits[1][0]
        );
        maxPosition = new Vector3f(
                (float)spacialLimits[0][1],
                (float)spacialLimits[2][1],
                (float)spacialLimits[1][1]
        );

        width = maxPosition.getX() - minPosition.getX();
        height = maxPosition.getY() - minPosition.getY();
        length = maxPosition.getZ() - minPosition.getZ();

        centrePosition = new Vector3f(
                minPosition.getX() + width / 2,
                minPosition.getY() + height / 2,
                minPosition.getZ() + length / 2
        );

        boundingBox = new Entity(GenerateMesh.cuboidFrame(width, height, length, 2), Color.white);
        boundingBox.getPosition().set(centrePosition);
    }

    public void render(ShaderProgram shaderProgram){
        boundingBox.render(shaderProgram);
    }

    public Vector3f getCentrePosition() {
        return centrePosition;
    }

    public Vector3f getMinPosition() {
        return minPosition;
    }

    public Vector3f getMaxPosition() {
        return maxPosition;
    }

    public float getWidth() {
        return width;
    }

    public float getLength() {
        return length;
    }

    public float getHeight() {
        return height;
    }

    public Entity getBoundingBox() {
        return boundingBox;
    }
}
