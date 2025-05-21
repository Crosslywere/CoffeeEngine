#version 460 core

layout (location = 0) out vec4 oPixelColor;
in vec3 normal;

void main() {
    oPixelColor = vec4(abs(normal), 1.0); // Colors the surface pure white
}