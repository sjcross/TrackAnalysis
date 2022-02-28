package io.github.sjcross.TrackAnalysis.Plot3D.Core.Item;

import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Texture.Texture;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Matrix4f;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.Quaternion;
import io.github.sjcross.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import io.github.sjcross.TrackAnalysis.Plot3D.Utils.RNG;

import java.awt.*;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class Entity {

    private final Mesh mesh;
    private Color colour;
    private Texture texture;
    private Vector3f position;
    private Quaternion rotation;
    private Vector3f scale;

    public Entity(Mesh mesh) {
        this(mesh, RNG.Colour(), null);
    }

    public Entity(Mesh mesh, Color colour) {
        this(mesh, colour, null);
    }

    public Entity(Mesh mesh, Texture texture) {
        this(mesh, RNG.Colour(), texture);
    }

    public Entity(Mesh mesh, Color colour, Texture texture) {
        this.mesh = mesh;
        this.colour = colour;
        this.texture = texture;
        this.position = new Vector3f();
        this.rotation = new Quaternion();
        this.scale = new Vector3f(1, 1, 1);
    }

    private Matrix4f getGlobalMatrix() {
        Matrix4f result = Matrix4f.Translation(position);
        result.multiply(Matrix4f.QuaternionRotation(rotation));
        result.multiply(Matrix4f.Stretch(scale));
        return result;
    }

    public void render(ShaderProgram mainShader) {
        mainShader.setMatrix4fUniform("globalMatrix", getGlobalMatrix());

        if (mesh.isSupportsTexture() && texture != null) {
            mainShader.setBooleanUniform("useTexture", true);
            texture.bind();
        } else {
            mainShader.setBooleanUniform("useTexture", false);
            mainShader.setColourUniformRGB("colour", colour);
        }

        mesh.render();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(float sf) {
        scale.set(sf, sf, sf);
    }
}
