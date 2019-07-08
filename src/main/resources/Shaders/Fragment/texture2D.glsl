#version 450

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform bool useTexture;
uniform sampler2D textureSampler;
uniform vec3 colour;

void main(){
    if(useTexture)
    {
        fragmentColour = vec4(texture(textureSampler, outTextureCoord).xyz, 1);
    }
    else
    {
        fragmentColour = vec4(colour, 1);
    }
}

