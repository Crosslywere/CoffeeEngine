#version 460 core

layout (location = 0) in vec3 aPos;
layout (location = 3) in vec3 aNorm;

uniform mat4 proj, model;

out vec3 normal;

void main() {
    normal = aNorm;
    gl_Position = proj * model * vec4(aPos, 1.0);
}