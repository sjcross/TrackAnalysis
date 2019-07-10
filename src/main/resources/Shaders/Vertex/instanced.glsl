#version 450

layout (location=0) in vec3 inVertexPosition;
layout (location=1) in float instancedColour;
layout (location=2) in mat4 instancedGlobalMatrix;

uniform mat4 projectedViewMatrix;
uniform mat4 motilityPlotMatrix;
uniform bool motilityPlot;

out float vsInstancedColour;

void main()
{
    mat4 combinedTransformation;

    if(motilityPlot)
    {
       combinedTransformation = projectedViewMatrix * motilityPlotMatrix * instancedGlobalMatrix;
    }
    else
    {
       combinedTransformation = projectedViewMatrix * instancedGlobalMatrix;
    }

    vsInstancedColour = instancedColour;

    gl_Position = combinedTransformation * vec4(inVertexPosition, 1.0);
}