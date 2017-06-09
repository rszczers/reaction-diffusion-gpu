import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;
import java.util.Random;


public class Main extends PApplet {
    private PFont roboto_regular;

    private PGraphics shaderLayer;
    private PGraphics presentationLayer;

    private PShader turingShader;
    private PShader interShader;

    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    private final static int SAMPLE_WIDTH = 800;
    private final static int SAMPLE_HEIGHT = 800;

    private static boolean drawPresentation = true;
    private static int brushType = 0;
    private static float brushSize = 8.f;
    private static int brushDensity = 60;
    private static float brushWeight = 2.0f;
    private static boolean drawCursor = true;

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
                backbuffer.pixels[j] = color(random.nextInt(255), 0, random.nextInt(100));
            } else {
                backbuffer.pixels[j] = color(0, random.nextInt(255), random.nextInt(100));
            }
        }
        backbuffer.updatePixels();
//        backbuffer = loadImage("init.jpg");

        shaderLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P3D);

        shaderLayer.beginDraw();
            shaderLayer.image(backbuffer, 0, 0, WIDTH, HEIGHT);
        shaderLayer.endDraw();

        presentationLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P3D);
        turingShader = loadShader("turingFrag.glsl");
        interShader = loadShader("interFrag.glsl");

        roboto_regular = createFont("Roboto-Regular.ttf", 14);
        textFont(roboto_regular);
//        frameRate(60);

        noCursor();
        noStroke();
    }

    @Override
    public void draw() {
        background(0);
        shaderLayer.beginDraw();
            turingShader.set("uSampler", shaderLayer);
            turingShader.set("sourceDimensions", (float) SAMPLE_WIDTH, (float) SAMPLE_HEIGHT);
            shaderLayer.shader(turingShader);
            shaderLayer.rect(0, 0, SAMPLE_WIDTH, SAMPLE_HEIGHT);
            shaderLayer.resetShader();
        shaderLayer.endDraw();

        if (mousePressed == true) {
            shaderLayer.beginDraw();
                shaderLayer.noStroke();
            switch(brushType) {
                case 0: // red
                    for (int i = 0; i < brushDensity; i++) {
                        shaderLayer.fill(200+random(55), 0.0f, random(100), 55 + random(200));
                        shaderLayer.rect(mouseX + random(-brushSize, brushSize), mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                    }
                    break;
                case 1: // green
                    for (int i = 0; i < brushDensity; i++) {
                        shaderLayer.fill(0.0f, 200 + random(55), random(100), 55 + random(200));
                        shaderLayer.rect(mouseX + random(-brushSize, brushSize), mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                    }
                    break;
                case 2: // eraser
                    for (int i = 0; i < brushDensity; i++) {
                        shaderLayer.fill(0.0f, 0.0f, random(100), 255);
                        shaderLayer.rect(mouseX + random(-brushSize, brushSize), mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                    }
                    break;
                case 3: //random
                    for (int i = 0; i < brushDensity; i++) {
                        shaderLayer.fill(random(255), random(255), random(100), 155 + random(100));
                        shaderLayer.rect(mouseX + random(-brushSize, brushSize), mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                    }
                    break;
            }
            shaderLayer.endDraw();
        }

        presentationLayer.beginDraw();
            interShader.set("uSampler", shaderLayer);
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
        if (drawPresentation == false) {
            fill(5);
            rect(0.0f, 20.0f, 90f, 20f);
            fill(245);
            text("Backbuffer", 10.0f, 35.0f);
        }

        if (drawCursor) {
            switch (brushType) {
                case 0:
                    fill(255f, 0f, 0f, 25f);
                    break;
                case 1:
                    fill(0f, 255f, 0f, 25f);
                    break;
                case 2:
                    fill(0f, 0f, 255f, 25f);
                    break;
                case 3:
                    fill(255f, 0f, 255f, 25f);
                    break;
                default:
                    fill(255f, 255f, 255f, 25f);
                    break;
            }
            rect(mouseX - brushSize, mouseY - brushSize, 2 * brushSize, 2 * brushSize);
        }
    }

    public void keyPressed() {
        if (key == 'p' || key == 'P') {
            drawPresentation = !drawPresentation;
        }
        if (key == '1' || key == 'q' || key == 'Q') {
            brushType = 0;
        }
        if (key == '2' || key == 'w' || key == 'W') {
            brushType = 1;
        }
        if (key == '3' || key == 'e' || key == 'E') {
            brushType = 2;
        }
        if (key == '4' || key == 'a' || key == 'A') {
            brushType = 3;
        }
        if (key == '+' || key == '=') {
            brushSize *= 2;
        }
        if (key == '-') {
            if (brushSize > 1.0) {
                brushSize /= 2;
            }
        }
        if (key == ']') {
            brushWeight += 1.0f;
        }
        if (key == '[') {
            if (brushWeight > 0) {
                brushWeight -= 1.0f;
            }
        }
        if (key == 'r' || key == 'R') {
            settings();
            setup();
        }
        if (key == 'c' || key == 'C') {
            drawCursor = !drawCursor;
        }
    }
}
