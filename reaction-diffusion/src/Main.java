import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;

import java.security.cert.TrustAnchor;
import java.util.Random;


public class Main extends PApplet {
    private PFont roboto_regular;

    private PGraphics shaderLayer;
    private PGraphics presentationLayer;

    private PShader turingShader;
    private PShader interShader;

    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;

    private final static int SAMPLE_WIDTH = 800;
    private final static int SAMPLE_HEIGHT = 600;

    private static boolean drawPresentation = true;

    public PImage backbuffer;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT, P3D);
        backbuffer = new PImage(SAMPLE_WIDTH, SAMPLE_HEIGHT);
    }

    @Override
    public void setup() {
        Random random = new Random();
        background(0);
        int k = backbuffer.pixels.length;
        for (int i = 0; i < k; i++) {
            int j = random.nextInt(k);
            if(random.nextBoolean()) {
                backbuffer.pixels[j] = color(random.nextInt(255), 0, 0);
            }else{
                backbuffer.pixels[j] = color(0, random.nextInt(255), 0);
            }
        }

        roboto_regular = createFont("Roboto-Regular.ttf", 14);

        backbuffer.updatePixels();
//        backbuffer = loadImage("init.png");
        shaderLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P3D);
        presentationLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P3D);
        turingShader = loadShader("turingFrag.glsl");
        interShader = loadShader("interFrag.glsl");
        textFont(roboto_regular);
        frameRate(60);
    }

    @Override
    public void draw() {
        shaderLayer.beginDraw();
            turingShader.set("uSampler", backbuffer);
            turingShader.set("sourceDimensions", (float) SAMPLE_WIDTH, (float) SAMPLE_HEIGHT);
            shaderLayer.shader(turingShader);
            shaderLayer.rect(0, 0, SAMPLE_WIDTH, SAMPLE_HEIGHT);
            shaderLayer.resetShader();
        shaderLayer.endDraw();

        backbuffer = shaderLayer.get();

        presentationLayer.beginDraw();
            interShader.set("uSampler", backbuffer);
            interShader.set("sourceDimensions", (float) SAMPLE_WIDTH, (float) SAMPLE_HEIGHT);
            presentationLayer.shader(interShader);
            presentationLayer.rect(0, 0, WIDTH, HEIGHT);
            presentationLayer.resetShader();
        presentationLayer.endDraw();

        if (drawPresentation == true) {
            image(presentationLayer, 0, 0, WIDTH, HEIGHT);
        } else {
            image(shaderLayer, 0, 0, WIDTH, HEIGHT);
        }

        fill(5);
        rect(0.0f, 0.0f, 50f, 20f);
        fill(245);
        text((int)frameRate, 10.0f, 15.0f);
    }

    public void mouseClicked() {
        if (mouseButton == LEFT) {

        }
        if (mouseButton == RIGHT) {

        }
    }

    public void keyPressed() {
        if (key == 'p' || key == '+') {
            drawPresentation = !drawPresentation;
        }
        if (key == '=' || key == '+') {

        }
        if (key == '-' || key == '_') {

        }
        if (key == 'r' || key == 'R') {
            settings();
        }
    }
}
