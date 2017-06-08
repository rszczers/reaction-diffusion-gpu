attribute vec2 position;
varying vec2 unitPos;
void main()
{
    unitPos = (position.xy + 1.0) / 2.0;
    gl_Position = vec4(position.x, position.y, 0, 1);
}