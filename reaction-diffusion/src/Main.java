import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

public class Main extends PApplet {

    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    public PImage initImage;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT, P2D);
        initImage = new PImage(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        Random random = new Random();
        background(0);
        int k = initImage.pixels.length;
        for (int i = 0; i < k/3; i++) {
            int j = random.nextInt(k);
            if(random.nextBoolean()) {
                initImage.pixels[j] = color(random.nextInt(255), 0, 0);
            }else{
                initImage.pixels[j] = color(0, random.nextInt(255), 0);
            }

        }
        initImage.updatePixels();

    }

    @Override
    public void draw() {
        image(initImage, 0, 0);
    }
}
