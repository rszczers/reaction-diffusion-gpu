precision mediump float;

uniform vec2 sourceDimensions;
uniform sampler2D uSampler;

void main() {
    vec2 pos = gl_FragCoord.xy/sourceDimensions;
    vec2 p = texture2D(uSampler, pos).xy;

    vec3 o = vec3(p.y, p.y, p.y/2);

    if (p.x > p.y) {
        o = vec3(p.x/2, p.x, p.x);
    }

    gl_FragColor = vec4(o, 1.0);
}
