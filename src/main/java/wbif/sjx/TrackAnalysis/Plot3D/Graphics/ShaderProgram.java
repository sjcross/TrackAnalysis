package wbif.sjx.TrackAnalysis.Plot3D.Graphics;





import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector4f;
import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final String shaderName;

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram(String shaderName) throws Exception {
        this.shaderName = shaderName;
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception(String.format("Could not create %s Shader", shaderName));
        }
        uniforms = new HashMap();
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws Exception {
        String shaderTypeName;
        if(shaderType == GL_VERTEX_SHADER){
            shaderTypeName = "Vertex";
        }else if(shaderType == GL_FRAGMENT_SHADER){
            shaderTypeName = "Fragment";
        }else {
            shaderTypeName = "Unknown";
        }

        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception(String.format("Error creating %s %s Shader", shaderName, shaderTypeName));
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception(String.format("Error compiling %s %s Shader: %s", shaderName, shaderTypeName, glGetShaderInfoLog(shaderId, 1024)));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception(String.format("Error linking %s Shader code: %s", shaderName, glGetProgramInfoLog(programId, 1024)));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.out.println(String.format("Warning validating %s Shader code: %s", shaderName, glGetShaderInfoLog(programId, 1024)));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        unbind();
        if (programId != 0) {
            glDetachShader(programId, vertexShaderId);
            glDetachShader(programId, fragmentShaderId);
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);
            glDeleteProgram(programId);
        }
    }

    public void createUniform(String name){
        int location = glGetUniformLocation(programId, name);
        if(location < 0){
           // throw new Exception("Could not find uniform: " + name);
        }
        uniforms.put(name, location);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void setMatrix4fUniform(String uniformName, Matrix4f matrix) {
        glUniformMatrix4fv(uniforms.get(uniformName), false, matrix.toFloatBuffer());
    }

    public void setVector3fUniform(String uniformName, Vector3f vector){
        glUniform3f(uniforms.get(uniformName), vector.getX(), vector.getY(), vector.getZ());
    }

    public void setColourUniform(String uniformName, Color colour) {
        Vector4f Colour = DataTypeUtils.toOpenGlColour(colour);
        glUniform4f(uniforms.get(uniformName), Colour.getX(), Colour.getY(), Colour.getZ(), Colour.getW());
    }

    public void setIntUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setFloatUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }
}
