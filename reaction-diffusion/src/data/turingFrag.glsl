precision highp float;

uniform vec2 sourceDimensions;
uniform sampler2D uSampler;

vec2 R(vec2 val, float alpha, float beta) {
//    float s = 20.3125;
    float s = 0.325;
//    return
//        vec2(val.r - pow(val.r, 3.0) - val.g + alpha,
//            (val.r - val.g) * beta);
    return vec2(
            s * (16 - val.r * val.g),
            s * (val.r * val.g - val.g - 12));
}



void main() {
// Łatki
//    float dt = 0.0000000004047;
//    float dx = 0.00001;
//    vec2 D = vec2(0.125, 0.0625);

// Podwójne Łatki
//    float dx = 1.0/sourceDimensions.x;
//    float dy = 1.0/sourceDimensions.y;
//    float dt = 0.0000085;
//    vec2 D = vec2(0.085, 0.0625);

// Podwójne Łatki
    float dx = 1.0/sourceDimensions.x;
    float dy = 1.0/sourceDimensions.y;
    float dt = 0.0000085;
//    float dt = 0.001;
    vec2 D = vec2(0.065, 0.0065);

// Kwadraty
//    float dt = 0.000006;
//    float dx = 0.01;
//    vec2 D = vec2(0.125, 0.0625);



    float alpha = 1.005;
    float beta = 1.1;

    vec2 pos = vec2(gl_FragCoord.x/sourceDimensions.x, 1.0 - gl_FragCoord.y/sourceDimensions.y);

    vec2 cellStep = 1.0 / sourceDimensions;
    vec2 cellStepX = vec2(cellStep.x, 0.0);
    vec2 cellStepY = vec2(0.0, -cellStep.y);

    vec2 o = texture2D(uSampler, pos).xy;
    vec2 e = texture2D(uSampler, pos + cellStepX).xy;
    vec2 w = texture2D(uSampler, pos - cellStepX).xy;
    vec2 n = texture2D(uSampler, pos + cellStepY).xy;
    vec2 s = texture2D(uSampler, pos - cellStepY).xy;

    vec2 laplacian = (- 4 * o + e + w + n + s) / (dx * dy);

    vec2 react = R(o, alpha, beta);

    vec2 delta = dt * (D * laplacian + react);

    vec3 of = clamp(vec3(o + delta, 0.0), 0.0, 1.0);

    gl_FragColor = vec4(of, 1.0);
}
