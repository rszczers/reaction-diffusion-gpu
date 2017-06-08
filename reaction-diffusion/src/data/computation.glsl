precision mediump float;

uniform vec2 sourceDimensions;
uniform sampler2D uSampler;
varying vec2 unitPos;

void main() {
    vec2 cellStep = 1.0 / sourceDimensions;
    vec2 cellStep1 = vec2(cellStep.x, -cellStep.y);
    vec2 cellStepX = vec2(cellStep.x, 0);
    vec2 cellStepY = vec2(0, -cellStep.y);

    vec4 co0 = texture2D(uSampler, unitPos);
    vec4 co1 = texture2D(uSampler, unitPos + cellStep);
    vec4 co2 = texture2D(uSampler, unitPos - cellStep);
    vec4 co3 = texture2D(uSampler, unitPos + cellStep1);
    vec4 co4 = texture2D(uSampler, unitPos - cellStep1);
    vec4 co5 = texture2D(uSampler, unitPos + cellStepX);
    vec4 co6 = texture2D(uSampler, unitPos - cellStepX);
    vec4 co7 = texture2D(uSampler, unitPos + cellStepY);
    vec4 co8 = texture2D(uSampler, unitPos - cellStepY);

    float me = floor(co0.r + 0.5);
    float neighbors = floor(co1.r + co2.r + co3.r + co4.r + co5.r + co6.r + co7.r + co8.r + 0.5);
    vec4 co = vec4(0,co0.g * 0.9,0,1);
    if(((me == 1.0) && (neighbors == 2.0 || neighbors == 3.0)) || (neighbors == 3.0))
        co = vec4(1,1,1,1);

    // vec4 co = (co0 + co1 + co2 + co3 + co4 + co5 + co6 + co7 + co8) / 9.0;

    gl_FragColor = co;

    // gl_FragColor = texture2D(uSampler, unitPos);
}
