precision mediump float;

uniform vec2 sourceDimensions;
uniform sampler2D uSampler;

vec2 R(vec2 val, float alpha, float beta) {
    return
        vec2(val.r - pow(val.r, 3.0) - val.g + alpha, (val.r - val.g) * beta);
}

void main() {
    float dt = 0.00000000000000000000000001;
    float dx = 1.0;
    vec2 D = vec2(1.0, 100.0);

    float alpha = -0.005;
    float beta = 10.0;

    vec2 pos = vec2(gl_FragCoord.x/sourceDimensions.x, 1.0 - gl_FragCoord.y/sourceDimensions.y);

    vec2 cellStep = 1.0 / sourceDimensions;
    vec2 cellStepX = vec2(cellStep.x, 0.0);
    vec2 cellStepY = vec2(0.0, -cellStep.y);

    vec2 o = texture2D(uSampler, pos).xy;
    vec2 e = texture2D(uSampler, pos + cellStepX).xy;
    vec2 w = texture2D(uSampler, pos - cellStepX).xy;
    vec2 n = texture2D(uSampler, pos + cellStepY).xy;
    vec2 s = texture2D(uSampler, pos - cellStepY).xy;

    vec2 laplacian = (- 4 * o + e + w + n + s) / pow(dx, 2);

    vec2 react = R(o, alpha, beta);

    vec2 delta = dt * (D * laplacian + react);

    vec3 of = vec3(o + delta, 0.5);

    gl_FragColor = vec4(of, 1.0);

}
