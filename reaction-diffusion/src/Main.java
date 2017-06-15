import processing.core.*;
import processing.opengl.PShader;
import java.util.Random;


public class Main extends PApplet {
    private PFont roboto_regular;

    private PGraphics shaderLayer;
    private PGraphics presentationLayer;

    private PShader turingShader;
    private PShader renderShader;

    private final static int WIDTH = 1024;
    private final static int HEIGHT = 1024;

    private final static int SAMPLE_WIDTH = 1024;
    private final static int SAMPLE_HEIGHT = 1024;

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
        size(WIDTH, HEIGHT, P2D);
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
//        backbuffer = loadImage("init.png");
        shaderLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P3D);

        shaderLayer.beginDraw();
            shaderLayer.image(backbuffer, 0, 0, WIDTH, HEIGHT);
//            shaderLayer.background(255.0f, 0, 0);
        shaderLayer.endDraw();

        presentationLayer = createGraphics(SAMPLE_WIDTH, SAMPLE_HEIGHT, P2D);
        turingShader = loadShader("turingFrag.glsl");
        renderShader = loadShader("renderFrag.glsl");

        roboto_regular = createFont("Roboto-Regular.ttf", 14);
        textFont(roboto_regular);
//        frameRate(30);

        noCursor();
        noStroke();
    }

    @Override
    public void draw() {
        renderShader.set("ca", new PVector(115, 98, 110));
        renderShader.set("cb", new PVector(247,228,190));

        if (mousePressed == true) mouseEvent();

        shaderLayer.beginDraw();
            shaderLayer.filter(turingShader);
        shaderLayer.endDraw();

        image(shaderLayer, 0, 0, WIDTH, HEIGHT);

        if (drawPresentation == false) {
            fill(5);
            rect(0.0f, 20.0f, 90f, 20f);
            fill(245);
            text("Backbuffer", 10.0f, 35.0f);
        } else {
            filter(renderShader);
        }

        if (drawCursor) {
            cursor();
        }
        drawfps();
    }

    public void cursor() {
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
            case 4:
                fill(255f, 0f, 255f, 25f);
                break;
            default:
                fill(255f, 255f, 255f, 25f);
                break;
        }
        rect(mouseX - brushSize, mouseY - brushSize, 2 * brushSize, 2 * brushSize);
    }

    public void drawfps() {
        fill(5);
        rect(0.0f, 0.0f, 50f, 20f);
        fill(245);
        text((int)frameRate, 10.0f, 15.0f);
    }

    public void mouseEvent() {
        shaderLayer.beginDraw();
        shaderLayer.noStroke();
        switch(brushType) {
            case 0: // red
                for (int i = 0; i < brushDensity; i++) {
                    shaderLayer.fill(200+random(55), 0.0f, random(100), 55 + random(200));
                    shaderLayer.rect(mouseX + random(-brushSize, brushSize),
                            mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                }
                break;
            case 1: // green
                for (int i = 0; i < brushDensity; i++) {
                    shaderLayer.rect(mouseX + random(-brushSize, brushSize),
                            mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                }
                break;
            case 2: // eraser
                for (int i = 0; i < brushDensity; i++) {
                    shaderLayer.fill(0.0f, 0.0f, random(100), 255);
                    shaderLayer.rect(mouseX + random(-brushSize, brushSize),
                            mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                }
                break;
            case 3: //random
                for (int i = 0; i < brushDensity; i++) {
                    shaderLayer.fill(random(255), random(255), random(100), 155 + random(100));
                    shaderLayer.rect(mouseX + random(-brushSize, brushSize),
                            mouseY + random(-brushSize, brushSize), brushWeight, brushWeight);
                }
                break;
            case 4:
                shaderLayer.stroke(0,200,0);
                shaderLayer.strokeWeight(15);
                shaderLayer.line(pmouseX, pmouseY, mouseX, mouseY);
                break;
        }
        shaderLayer.endDraw();
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
        if (key == '5') {
            brushType = 4;
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
