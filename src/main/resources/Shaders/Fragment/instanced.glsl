#version 450

in float vsInstancedColour;

out vec4 fragmentColour;

uniform vec3 colour;
uniform bool useInstancedColour;

void main(){
    if(useInstancedColour)
    {
        fragmentColour = vec4(1, vsInstancedColour, vsInstancedColour, 1);
    }
    else
    {
        fragmentColour = vec4(colour, 1);
    }
}


