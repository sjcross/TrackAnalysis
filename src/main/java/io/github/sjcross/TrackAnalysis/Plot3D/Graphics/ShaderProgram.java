package io.github.sjcross.trackanalysis.Plot3D.Graphics;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.sjcross.trackanalysis.Plot3D.Math.Matrix4f;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector3f;
import io.github.sjcross.trackanalysis.Plot3D.Math.vectors.Vector4f;
import io.github.sjcross.trackanalysis.Plot3D.Utils.DataUtils;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class ShaderProgram {

    private final String shaderName;
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;
    private final HashMap<String, HashMap<String, String>> structs;

    public ShaderProgram(String shaderName, String vertexShaderName, String fragmentShaderName) throws Exception {
        this.shaderName = shaderName;
        programId = glCreateProgram();

        if (programId == 0)
            throw new Exception(String.format("Could not create %s Shader", shaderName));

        uniforms = new HashMap<>();
        structs = new HashMap<>();

        String vertexSrc = loadVertexShader(vertexShaderName);
        String fragmentSrc = loadFragmentShader(fragmentShaderName);

        createVertexShader(vertexSrc);
        createFragmentShader(fragmentSrc);

        link();

        createUniforms(vertexSrc);
        createUniforms(fragmentSrc);

        structs.clear();
    }

    private String loadVertexShader(String vertexShaderName) throws Exception {
        return DataUtils.loadAsString(String.format("Shaders/Vertex/%s.glsl", vertexShaderName));
    }

    private String loadFragmentShader(String fragmentShaderName) throws Exception {
        return DataUtils.loadAsString(String.format("Shaders/Fragment/%s.glsl", fragmentShaderName));
    }

    private void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    private void createFragmentShader(String shaderCode) throws Exception {
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

    private void link() throws Exception {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final Pattern structBodyPattern = Pattern.compile("struct\\s+\\D\\w*\\s*\\{(\\s*\\D\\w*\\s+\\D\\w*\\s*;\\s*)+}\\s*;");
    private static final Pattern uniformShaderLinePattern = Pattern.compile("uniform\\s+\\D\\w*(\\[\\w+])?\\s+\\D\\w+\\s*;");

    private void createUniforms(String shaderCode) throws Exception{
        Matcher matcher = structBodyPattern.matcher(shaderCode);

        while (matcher.find()) {
            String structCode = matcher.group();

            String[] tokens = structCode.split("[{}]"); // splits in to body, head, tail
            String head = tokens[0].replaceAll("struct|\\s", "");
            String body = tokens[1];

            body = body.replaceAll("[\\s\n]+", " ");
            body = body.replaceAll("(?<!\\w)\\s", ""); // negative lookbehind: (?<!a)b, which is b not preceeded by a
            String[] bodyComponents = body.split(";");

            HashMap<String, String> variableDataTypeMap = new HashMap<>();

            for(String component : bodyComponents){
                tokens = component.split("\\s");
                String dataType = tokens[0];
                String variableName = tokens[1];
                variableDataTypeMap.put(variableName, dataType);
            }

            structs.put(head, variableDataTypeMap);
        }

        matcher = uniformShaderLinePattern.matcher(shaderCode);

        while (matcher.find()) {
            String uniformShaderLine = matcher.group();
            uniformShaderLine = uniformShaderLine.replaceAll("uniform\\s+",""); //remove head
            String[] tokens = uniformShaderLine.split("\\s+"); //split
            String dataTypeComponent = tokens[0];
            String uniformName = tokens[1].substring(0, tokens[1].length() - 1); //remove semicolon

            String[] subComponents = dataTypeComponent.split("[\\[\\]]");
            String dataType = subComponents[0];

            switch (subComponents.length){
                case 1:
                    createUniform(dataType, uniformName);
                    break;
                case 2:
                    String arraySizePointerString = subComponents[1];
                    if(arraySizePointerString.matches("\\d+")){
                        createUniformArray(dataType, uniformName, Integer.parseInt(arraySizePointerString));
                    }else {
                        createUniformArray(dataType, uniformName, getGLSLConstIntValue(arraySizePointerString, shaderCode));
                    }
                    break;
                default:
                    throw new Exception("Unsupported uniform decleration");
            }
        }
    }

    private static int getGLSLConstIntValue(String constName, String shaderCode) {
        final Pattern pattern = Pattern.compile(String.format("const\\s+int\\s+%s\\s*=\\s*\\d+\\s*;", constName));
        Matcher matcher = pattern.matcher(shaderCode);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group().replaceAll("\\D+", ""));
        } else {
            return 0;
        }
    }

    private void createUniformArray(String dataType, String uniformName, int size) throws Exception{
        for(int i = 0; i < size; i++){
            createUniform(dataType, String.format("%s[%d]", uniformName, i));
        }
    }

    private void createUniform(String dataType, String uniformName) throws Exception{
        switch (dataType){
            case "bool":
            case "uint":
            case "int":
            case "float":
            case "double":
            case "vec2":
            case "vec3":
            case "vec4":
            case "bvec2":
            case "bvec3":
            case "bvec4":
            case "uvec2":
            case "uvec3":
            case "uvec4":
            case "ivec2":
            case "ivec3":
            case "ivec4":
            case "dvec2":
            case "dvec3":
            case "dvec4":
            case "mat2":
            case "mat3":
            case "mat4":
            case "sampler2D":
            case "sampler2DArray":
                createUniform(uniformName);
                break;
            default:
                if(structs.containsKey(dataType)){
                    HashMap<String, String> attributeMap = structs.get(dataType);
                    for(String attribute : attributeMap.keySet()){
                        createUniform(attributeMap.get(attribute), String.format("%s.%s", uniformName, attribute));
                    }
                }else {
                    throw new Exception(String.format("Cannot create the %s uniform type", dataType));
                }
                break;
        }
    }

    private void createUniform(String name) throws Exception{
        int uniformLocation = glGetUniformLocation(programId, name);

        if(uniformLocation == 0xFFFFFFFF){
            new Exception(String.format("The uniform: '%s' for the '%s' shader program is not used", name, shaderName)).printStackTrace();
        }else {
            uniforms.put(name, uniformLocation);
        }
    }

    public void setMatrix4fUniform(String uniformName, Matrix4f matrix) {
        glUniformMatrix4fv(uniforms.get(uniformName), false, matrix.toFloatBuffer());
    }

    public void setVector3fUniform(String uniformName, Vector3f vector){
        glUniform3f(uniforms.get(uniformName), vector.getX(), vector.getY(), vector.getZ());
    }

    public void setVector4fUniform(String uniformName, Vector4f vector){
        glUniform4f(uniforms.get(uniformName), vector.getX(), vector.getY(), vector.getZ(), vector.getW());
    }

    public void setColourUniformRGB(String uniformName, Color colour) {
        setVector3fUniform(uniformName, DataUtils.toVector3f(colour));
    }

    public void setColourUniformRGBA(String uniformName, Color colour) {
        setVector4fUniform(uniformName, DataUtils.toVector4f(colour));
    }

    public void setTextureSamplerUniform(String uniformName, int textureUnit) {
        if(0 <= textureUnit && textureUnit <=  glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS)) {
            setIntUniform(uniformName, textureUnit);
        }
    }

    public void setIntUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setFloatUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setBooleanUniform(String uniformName, boolean state){
        glUniform1i(uniforms.get(uniformName), state ? 1 : 0);
    }
}