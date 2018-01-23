#version 330

layout (location=0) in vec3 inVertexPosition;
layout (location=1) in mat4 instancedGlobalMatrix;

uniform mat4 projectedViewMatrix;

 void main(){
     gl_Position = projectedViewMatrix * inGlobalMatrix * vec4(inVertexPosition, 1.0);
 }