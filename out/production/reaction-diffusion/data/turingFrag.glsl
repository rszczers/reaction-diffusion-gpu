precision highp float;

uniform vec2 sourceDimensions;
uniform sampler2D uSampler;


vec2 RTurk(vec2 val, float beta) {
    float s = 0.3125;
    return vec2(
            s * (16 - val.r * val.g),
            s * (val.r * val.g - val.g - beta));
}

void main() {
    float dx = 1.0/sourceDimensions.x;
    float dy = 1.0/sourceDimensions.y;

//  float dt = 1.008*(dx*dy);
//    float dt = 1.0073*(dx*dy);
    float dt = 1.01*(dx*dy);

    //vec2 D = vec2(0.25, 0.0625);
    vec2 D = vec2(0.25, 0.25);
    vec2 pos = gl_FragCoord.xy/sourceDimensions;

    float z = texture2D(uSampler, pos).z;
    float beta = 12 + (0.1 - z/500.0);

    vec2 cellStep = 1.0 / sourceDimensions;
    vec2 cellStepX = vec2(cellStep.x, 0.0);
    vec2 cellStepY = vec2(0.0, -cellStep.y);

    vec2 o = texture2D(uSampler, pos).xy;
    vec2 e = texture2D(uSampler, pos + cellStepX).xy;
    vec2 w = texture2D(uSampler, pos - cellStepX).xy;
    vec2 n = texture2D(uSampler, pos + cellStepY).xy;
    vec2 s = texture2D(uSampler, pos - cellStepY).xy;

    vec2 laplacian = (e - 2 * o + w)/pow(dx, 2) + (s - 2 * o + n)/pow(dy, 2);

//    vec2 react = R(o, alpha, beta);
    vec2 react = RTurk(o, beta);

    vec2 delta = D * laplacian + react;

    vec3 of = vec3(o + dt * delta, z);
    gl_FragColor = vec4(of, 1.0);
}
