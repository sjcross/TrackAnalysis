#version 450

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform bool useTexture;
uniform int frame;
uniform sampler2DArray textureSampler;
uniform vec3 colour;

void main()
{
    if(useTexture)
    {
        fragmentColour = vec4(texture(textureSampler, vec3(outTextureCoord, frame)).xyz, 1);
    }
    else
    {
        fragmentColour = vec4(colour, 1);
    }
}

