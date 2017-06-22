# reaction-diffusion-gpu
solution of r-d system by glsl and explicit finite-difference method


![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t.gif)

(Click the animation to view full youtube demo)

## Controls
<pre>
LMB – paint,
1 – activator brush,
2 – inhibitor brush,
3 – eraser,
4 – random brush,
5 – pen,
]/[ – increase/decrease density,
+/- – increase/decrease brush size,
L – switch laplacian kernel,
Z – switch between Gray-Scott / G. Turk reaction model,
r – reset with full activator,
R - reset with full inhibitor,
b - reset with blank screen,
t - reset with random data,
P – switch between raw and postprocessed view,
C – draw cursor. 
</pre>

## Usage
<pre>
java -jar bin/reaction-diffusion-gpu.jar
</pre>
