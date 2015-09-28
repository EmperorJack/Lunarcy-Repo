package ui;

import processing.core.*;
import game.*;

/**
 * The view that displays the player perspective of the game world in 3D.
 * Renders onto a 3D PGrapihcs layer before drawing onto the parent canvas.
 *
 * @author Jack & Kelly
 *
 */
public class Perspective3D extends DrawingComponent {

	// temporary background image
	private Animation tempGifAnimation;

	// 3D world
	private WorldModel world;
	private final int SQUARE_SIZE = 500;
	private final float MODEL_SCALE = SQUARE_SIZE / 2.5f;
	private final int vel = SQUARE_SIZE / 50;
	private int r, g, b;

	// camera fields
	private PVector camEye;
	private PVector camCenter;
	private float rotationAngle = 0;
	private float elevationAngle = 0;

	public Perspective3D(PApplet p, GameState gameState) {
		// public Perspective3D(PApplet p, GameState gameState) {
		super(p, gameState);

		// temp gif
		tempGifAnimation = new Animation("assets/animations/shrek/shrek_", 20);

		// world model setup
		world = new WorldModel(p, gameState.getBoard(), MODEL_SCALE,
				SQUARE_SIZE);

		// camera setup
		camEye = new PVector(SQUARE_SIZE, -100, SQUARE_SIZE);
		camCenter = new PVector(0, 0, 0);
	}

	@Override
	public void update(GameState gameState) {
		// TODO update the Perspective3D with updated game state
	}

	@Override
	public void draw(float delta) {
		handleInput(delta);

		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// position the camera
		p.camera(camEye.x, camEye.y, camEye.z, camCenter.x, camCenter.y,
				camCenter.z, 0.0f, 1, 0);

		// test north pointer and sphere
		p.pushMatrix();
		p.noStroke();
		p.fill(0, 255, 0);
		p.rotateX(PApplet.PI / 2);
		p.rect(10, 0, -20, -SQUARE_SIZE);
		p.fill(255, 0, 0);
		p.sphere(10);

		// test light source and sphere
		p.pushMatrix();
		p.translate(500, 500, SQUARE_SIZE / 2);
		if (p.frameCount % 60 == 0) {
			r = (int) p.random(200) + 55;
			g = (int) p.random(200) + 55;
			b = (int) p.random(200) + 55;
		}
		p.pointLight(r, g, b, 0, 0, 0);
		p.fill(0, 0, 255);
		p.sphere(10);
		p.popMatrix();

		// test rotating image plane animation
		p.rotateX(-PApplet.PI / 2);
		p.rotateY(PApplet.radians(p.frameCount));
		tempGifAnimation.update(delta);
		tempGifAnimation.display(0, 0, SQUARE_SIZE, SQUARE_SIZE);
		p.popMatrix();

		// UNCOMMENT THE FOLLOWING LINES FOR A GOOD TIME
		// p.scale(PApplet.sin(PApplet.radians(p.frameCount)));
		// p.rotateX(PApplet.radians(p.frameCount));
		// p.rotateY(PApplet.radians(p.frameCount));
		// p.rotateZ(PApplet.radians(p.frameCount / 4));

		// draw the game world
		world.draw();

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	public class Animation {
		private PImage[] images;
		private int imageCount;
		private int frame;
		private float percent;
		private boolean halfRate = false;

		public Animation(String imagePrefix, int count) {
			imageCount = count;
			images = new PImage[imageCount];

			for (int i = 0; i < imageCount; i++) {
				String filename = imagePrefix + PApplet.nf(i, 4) + ".jpg";
				images[i] = p.loadImage(filename);
			}
		}

		public void update(float delta) {
			halfRate = !halfRate;
			if (halfRate) {
				frame = ((int) (percent += delta)) % imageCount;
			}
		}

		public void display(float x, float y) {
			p.image(images[frame], x, y);
		}

		public void display(float x, float y, float w, float h) {
			p.image(images[frame], x, y, w, h);
		}
	}

	private void handleInput(float delta) {
		rotationAngle = PApplet
				.map(p.mouseX, 0, p.width, 0, PApplet.TWO_PI * 2);
		elevationAngle = PApplet.map(p.mouseY, 0, p.height, 0, PApplet.PI);
		PVector move = new PVector(0, 0);
		if (p.keyPressed) {
			if (p.key == 'w' || p.key == 'W') {
				move = new PVector(vel * delta, 0);
				move.rotate(rotationAngle);
			}
			if (p.key == 'a' || p.key == 'A') {
				move = new PVector(0, -vel * delta);
				move.rotate(rotationAngle);
			}
			if (p.key == 's' || p.key == 'S') {
				move = new PVector(-vel * delta, 0);
				move.rotate(rotationAngle);
			}
			if (p.key == 'd' || p.key == 'D') {
				move = new PVector(0, vel * delta);
				move.rotate(rotationAngle);
			}
		}
		updateCamera(move);
	}

	private void updateCamera(PVector move) {
		camEye.x += move.x;
		camEye.z += move.y;
		camCenter.x = PApplet.cos(rotationAngle) + camEye.x;
		camCenter.z = PApplet.sin(rotationAngle) + camEye.z;
		camCenter.y = -PApplet.cos(elevationAngle) + camEye.y;
	}
}
