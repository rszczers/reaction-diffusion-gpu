#version 150
//
//#ifdef GL_ES
//precision highp float;
//precision highp int;
//#endif

#define PROCESSING_TEXTURE_SHADER

uniform sampler2D texture;
uniform vec2 texOffset;

uniform vec2 u_resolution;
uniform vec2 u_mouse;

uniform float Du;
uniform float Dv;
uniform float k;
uniform float f;
uniform float s;
uniform float dt;
uniform int lapNo;
uniform bool r;

varying vec4 vertColor;
varying vec4 vertTexCoord;

vec2 RTurk(vec2 val, float beta) {
    return vec2(
            s * (16.0 - val.r * val.g),
            s * (val.r * val.g - val.g - beta));
}

vec2 grayScott(vec2 val) {
   return vec2(
            -val.r * val.g * val.g + f * (1.0 - val.r),
             val.r * val.g * val.g - (k + f) * val.g );
}

vec2 ReactionInhibitor(vec2 val) {
    float b = 0.1;
    return vec2(val.r * val.r / val.g - b * val.r,
                val.r * val.r - val.g);
}

    float kernelA[9] = float[9](0.0, 1.0, 0.0,
                               1.0, -4.0, 1.0,
                               0.0, 1.0, 0.0);

    float kernelB[9] = float[9](0.05, 0.2, 0.05,
                               0.2, -1.0, 0.2,
                               0.05, 0.2, 0.05);

    float kernelC[25] = float[25](
        0.0, 0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0, 0.0,
        1.0, 1.0, -4.0, 1.0, 1.0,
        0.0, 0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0, 0.0);

    float kernelD[81] = float[81] (
        0.0, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 0.0,
        1.0, 2.0, 4.0, 5.0, 5.0, 5.0, 4.0, 2.0, 1.0,
        1.0, 4.0, 5.0, 3.0, 0.0, 3.0, 5.0, 4.0, 1.0,
        2.0, 6.0, 3.0, -12.0, -24.0, -12.0, 3.0, 4.0, 2.0,
        2.0, 5.0, 0.0, -24.0, -40.0, -24.0, 3.0, 5.0, 2.0,
        2.0, 6.0, 3.0, -12.0, -24.0, -12.0, 3.0, 4.0, 2.0,
        1.0, 4.0, 5.0, 3.0, 0.0, 3.0, 5.0, 4.0, 1.0,
        1.0, 2.0, 4.0, 5.0, 5.0, 5.0, 4.0, 2.0, 1.0,
        0.0, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 0.0);

void main() {
    float dx = 1.0;
    float dy = 1.0;

    vec2 D = vec2(Du, Dv);

    vec2 cellStepX = vec2(texOffset.x, 0.0);
    vec2 cellStepY = vec2(0.0, texOffset.y);
    vec2 f = vec2(0.0, 0.0);

    int xlen;
    int ylen;
    int xoffset;
    int yoffset;

    if (lapNo == 0) {
        xlen = 3;
        ylen = 3;
        yoffset = 1;
        xoffset = 1;

        for(int i = 0; i < ylen; i++) {
             for(int j = 0; j < xlen; j++) {
                 f += kernelA[i * xlen + j] * texture2D(texture,
                     vertTexCoord.st + (j - xoffset) * cellStepX + (i - yoffset) * cellStepY).xy;
             }
        }
    } else if (lapNo == 1) {
        xlen = 3;
        ylen = 3;
        yoffset = 1;
        xoffset = 1;

        for(int i = 0; i < ylen; i++) {
             for(int j = 0; j < xlen; j++) {
                 f += kernelB[i * xlen + j] * texture2D(texture,
                     vertTexCoord.st + (j - xoffset) * cellStepX + (i - yoffset) * cellStepY).xy;
             }
        }
    } else if (lapNo == 2) {
        xlen = 5;
        ylen = 5;
        yoffset = 2;
        xoffset = 2;

        for(int i = 0; i < ylen; i++) {
             for(int j = 0; j < xlen; j++) {
                 f += kernelC[i * xlen + j] * texture2D(texture,
                     vertTexCoord.st + (j - xoffset) * cellStepX + (i - yoffset) * cellStepY).xy;
             }
        }
    } else if (lapNo == 3) {
        xlen = 9;
        ylen = 9;
        yoffset = 4;
        xoffset = 4;

        for(int i = 0; i < ylen; i++) {
             for(int j = 0; j < xlen; j++) {
                 f += kernelD[i * xlen + j]/40.0 * texture2D(texture,
                     vertTexCoord.st + (j - xoffset) * cellStepX + (i - yoffset) * cellStepY).xy;
             }
        }
    }


    vec2 uv = texture2D(texture, vertTexCoord.st).xy;
    vec2 laplacian = f / pow(dx, 2.0);

    vec2 react;
    float z = texture2D(texture, vertTexCoord.st).z;

    if (r) {
        react = grayScott(uv);
        z = 0.0;
    } else {
        float beta = 12.0 + z/10.0;
        react = RTurk(uv, beta);
    }

    vec2 delta = clamp(D * laplacian + react, -1.0, 1.0);
    vec3 o = vec3(uv + dt * delta, z);

    gl_FragColor = vec4(o, 1.0);
}
