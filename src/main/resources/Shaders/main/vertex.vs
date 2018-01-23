#version 330

layout (location=0) in vec3 inVertexPosition;

uniform mat4 projectedViewMatrix;
uniform mat4 globalMatrix;

 void main(){
     gl_Position = projectedViewMatrix * globalMatrix * vec4(inVertexPosition, 1.0);
 }