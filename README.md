# reaction-diffusion-gpu
Solution of reaction-diffusion system with glsl shader and explicit finite-difference method.

![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t.gif)

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
In project directory run
<pre>
java -jar bin/reaction-diffusion.jar
</pre>

## Preview
![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t5.png)
![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t0.png)
![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t1.png)
![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t2.png)
![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t3.png)
![](https://github.com/rszczers/reaction-diffusion-gpu/blob/master/t4.png)
