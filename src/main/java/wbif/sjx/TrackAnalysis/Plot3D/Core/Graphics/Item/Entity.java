package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Maths;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.RNG;

import java.awt.*;

/**
 * Created by sc13967 on 31/07/2017.
 */
public class Entity {
    private final Mesh mesh;
    private Color colour;
    private Vector3f position;
    private Vector3f rotation;

    public Entity(Mesh mesh){
        this(mesh, RNG.Colour());
    }

    public Entity(Mesh mesh, Color colour){
        this.mesh = mesh;
        this.colour = colour;
        position = new Vector3f();
        rotation = new Vector3f();
    }

    private Matrix4f getGlobalMatrix(){
        Matrix4f result = Matrix4f.identity();
        result.apply(Matrix4f.translation(position));
        result.apply(Matrix4f.rotation(rotation));
//        result.apply(Matrix4f.enlargement(scale));
        return result;
    }

    public void render(ShaderProgram shaderProgram){
        shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", getGlobalMatrix());
        shaderProgram.setColourUniform("colour", colour);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getRotation(){
        Maths.normaliseRotationVector(rotation);
        return rotation;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getPosition(){
        return position;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
