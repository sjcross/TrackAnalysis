package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.ShaderProgram;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.FrustumCuller;
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
    private Vector3f scale;

    public Entity(Mesh mesh){
        this(mesh, RNG.Colour());
    }

    public Entity(Mesh mesh, Color colour){
        this.mesh = mesh;
        this.colour = colour;
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1,1,1);
    }

    private Matrix4f getGlobalMatrix(){
        Matrix4f result = Matrix4f.Translation(position);
        result.multiply(Matrix4f.EulerRotation(rotation));
        result.multiply(Matrix4f.Stretch(scale));
        return result;
    }

    public void render(ShaderProgram shaderProgram){
        if(FrustumCuller.getInstance().isInsideFrustum(this)) {

            shaderProgram.setMatrix4fUniform("globalMatrix", getGlobalMatrix());
            shaderProgram.setColourUniform("colour", colour);

            mesh.render();
        }
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
        return rotation;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getPosition(){
        return position;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f getScale(){
        return scale;
    }

    public void setScale(float sf){
        scale.set(sf, sf, sf);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
