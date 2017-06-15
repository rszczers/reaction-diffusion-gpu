#ifdef GL_ES
precision highp float;
precision highp int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform sampler2D texture;
uniform vec2 texOffset;
uniform vec3 ca;
uniform vec3 cb;

varying vec4 vertColor;
varying vec4 vertTexCoord;

void main(void) {

  vec2 uv = texture2D(texture, vertTexCoord.st).rg;
  float d = smoothstep(0.2, 0.8, uv.r - uv.g);
  vec3 color = mix(cb/255.0, ca/255.0, d);
  gl_FragColor = vec4(color, 1.0);
}