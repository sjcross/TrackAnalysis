package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by JDJFisher on 04/08/2017.
 */
public class CollectionBounds {

    private final Vector3f centrePosition;
    private final Vector3f minPosition;
    private final Vector3f maxPosition;

    private final float width;
    private final float length;
    private final float height;

    public CollectionBounds(double[][] spacialLimits) {

        //Convert from Z-up orientation to Y-up
        minPosition = new Vector3f(
                (float) spacialLimits[0][0],
                (float) spacialLimits[2][0],
                (float) spacialLimits[1][0]
        );
        maxPosition = new Vector3f(
                (float) spacialLimits[0][1],
                (float) spacialLimits[2][1],
                (float) spacialLimits[1][1]
        );

        width = maxPosition.getX() - minPosition.getX();
        height = maxPosition.getY() - minPosition.getY();
        length = maxPosition.getZ() - minPosition.getZ();

        centrePosition = new Vector3f(
                minPosition.getX() + width / 2,
                minPosition.getY() + height / 2,
                minPosition.getZ() + length / 2
        );
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

}
