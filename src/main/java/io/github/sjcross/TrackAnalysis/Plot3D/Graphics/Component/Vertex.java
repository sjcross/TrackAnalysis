package io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component;

import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Vertex {

    public static final int SIZE = Vector3f.SIZE + Vector2f.SIZE;

    private Vector3f position;
    private Vector2f textureCoord;

    public Vertex(Vector3f position) {
        this(position, null);
    }

    public Vertex(Vector3f position, Vector2f textureCoord) {
        this.position = position;
        this.textureCoord = textureCoord;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getTextureCoord() {
        return textureCoord;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setTextureCoord(Vector2f textureCoord) {
        this.textureCoord = textureCoord;
    }
}

