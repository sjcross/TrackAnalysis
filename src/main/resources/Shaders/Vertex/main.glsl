#version 450

layout (location=0) in vec3 inVertexPosition;
layout (location=1) in vec2 inTextureCoord;

out vec2 outTextureCoord;

uniform mat4 projectedViewMatrix;
uniform mat4 globalMatrix;

void main()
{
    gl_Position = projectedViewMatrix * globalMatrix * vec4(inVertexPosition, 1.0);
    outTextureCoord = inTextureCoord;
}