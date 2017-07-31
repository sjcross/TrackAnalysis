#version 330

layout (location=0) in vec3 inVertexPosition;

uniform mat4 projectionMatrix;
uniform mat4 cameraMatrix;
uniform mat4 combinedTransformationMatrix;

void main()
{
    gl_Position = projectionMatrix * cameraMatrix * combinedTransformationMatrix * vec4(inVertexPosition, 1.0);
}