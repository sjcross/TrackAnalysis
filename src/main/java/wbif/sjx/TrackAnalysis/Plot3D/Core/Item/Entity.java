package wbif.sjx.TrackAnalysis.Plot3D.Core.Item;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Texture;
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
    private Texture texture;
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
        Matrix4f result = Matrix4f.identity();
        result.apply(Matrix4f.translation(position.getX(), position.getZ(), position.getY()));
        result.apply(Matrix4f.rotation(rotation.getX(), rotation.getZ(), rotation.getY()));
        result.apply(Matrix4f.stretch(scale.getX(), scale.getZ(), scale.getY()));
        return result;
    }

    public void render(ShaderProgram shaderProgram, FrustumCuller frustumCuller){
        if(frustumCuller == null || frustumCuller.isInsideFrustum(this)) {
            boolean hasTexture = texture != null;

            shaderProgram.setMatrix4fUniform("combinedTransformationMatrix", getGlobalMatrix());
            shaderProgram.setColourUniform("colour", colour);

            shaderProgram.setBooleanUniform("hasTexture", hasTexture);

            if(hasTexture){
                shaderProgram.setIntUniform("textureSampler", 0);
                mesh.render(texture);
            }else {
                mesh.render();
            }
        }
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
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

    public Vector3f getScale(){
        return scale;
    }

    public void setScale(float sf){
        scale.set(sf, sf, sf);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
