#version 120

uniform vec2 sourceDimensions;
uniform sampler2D uSampler;

void main() {

    vec2 pos = vec2(gl_FragCoord.x/sourceDimensions.x, 1.0 - gl_FragCoord.y/sourceDimensions.y);
    vec2 p = texture2D(uSampler, pos).xy;

    vec3 o = vec3(p.x, 0.0, 0.0);

    if (p.x > p.y) {
        o = vec3(0.0, p.y, 0.0);
    }

    gl_FragColor = vec4(o, 1.0);
}
