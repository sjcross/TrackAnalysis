#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform vec4 colour;
uniform int hasTexture;
uniform sampler2D textureSampler;

void main(){
    if(hasTexture == 1){
        fragmentColour = texture(textureSampler, outTextureCoord);
    }else{
        fragmentColour = colour;
    }
}


