attribute vec4 position;
varying vec2 unitPos;

void main() {
    unitPos = (position.xy + 1.0) / 2.0;
    gl_Position = vec4(pos, 0, 1);
}