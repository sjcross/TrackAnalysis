#version 330

layout (location=0) in vec3 inVertexPosition;
layout (location=1) in vec2 inTextureCoord;

out vec2 outTextureCoord;

uniform mat4 combinedViewMatrix;
uniform mat4 combinedTransformationMatrix;

 void main()
 {
     gl_Position = combinedViewMatrix * combinedTransformationMatrix * vec4(inVertexPosition, 1.0);
     outTextureCoord = inTextureCoord;
 }