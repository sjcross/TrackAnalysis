#version 330

layout (location=0) in vec3 inVertexPosition;

uniform mat4 combinedViewMatrix;
uniform mat4 combinedTransformationMatrix;

 void main()
 {
     gl_Position = combinedViewMatrix * combinedTransformationMatrix * vec4(inVertexPosition, 1.0);
 }