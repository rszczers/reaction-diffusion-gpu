import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;

import java.util.Random;


public class Main extends PApplet {
    private PGraphics shaderLayer;
    private PShader turingShader;
    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    private final static int SAMPLE_WIDTH = 800;
    private final static int SAMPLE_HEIGHT = 800;


    public PImage backbuffer;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT, P2D);
        backbuffer = new PImage(SAMPLE_WIDTH, SAMPLE_HEIGHT);
    }

    @Override
    public void setup() {
        Random random = new Random();
        background(0);
        int k = backbuffer.pixels.length;
        for (int i = 0; i < k/3; i++) {
            int j = random.nextInt(k);
            if(random.nextBoolean()) {
                backbuffer.pixels[j] = color(random.nextInt(255), 0, 0);
            }else{
                backbuffer.pixels[j] = color(0, random.nextInt(255), 0);
            }

        }
        backbuffer.updatePixels();
        shaderLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P2D);
        turingShader = loadShader("turingFrag.glsl");
    }

    @Override
    public void draw() {
        frameRate(30);
        shaderLayer.beginDraw();
            shaderLayer.background(0);
            turingShader.set("uSampler", backbuffer);
            turingShader.set("sourceDimensions", (float) SAMPLE_WIDTH, (float) SAMPLE_HEIGHT);
            shaderLayer.shader(turingShader);
            shaderLayer.rect(0, 0, width, height);
            backbuffer = shaderLayer.get();
            shaderLayer.resetShader();
        shaderLayer.endDraw();
        image(shaderLayer, 0, 0, WIDTH, HEIGHT);
    }
}
