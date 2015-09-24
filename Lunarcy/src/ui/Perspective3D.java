package ui;

import processing.core.*;
import game.GameState;

/**
 * The view that displays the player perspective of the game world in 3D.
 * Renders onto a 3D PGrapihcs layer before drawing onto the parent canvas.
 * 
 * @author Jack & Kelly
 *
 */
public class Perspective3D extends DrawingComponent {

	// 3D graphics layer
	private PGraphics g;

	// temporary background images
	private Animation shrek;

	// camera
	public PVector camEye;
	public PVector camCenter;
	public float rotationAngle = 0;
	public float elevationAngle = 0;

	public Perspective3D(PApplet p, GameState gameState, PGraphics g) {
		super(p, gameState);

		// use the given graphics layer as the 3D renderer
		this.g = g;
		shrek = new Animation("assets/animations/shrek/shrek_", 20);

		// Camera setup
		camEye = new PVector(0, -30, 0);
		camCenter = new PVector(0, 0, 0);
	}

	@Override
	public void update(GameState state) {
		// TODO update the 3D perspective

	}

	@Override
	public void draw() {
		handleInput();

		// allow drawing onto the graphics layer
		g.beginDraw();

		// push matrix and style information onto the stack
		g.pushMatrix();
		g.pushStyle();

		// TODO draw the 3D perspective
		g.background(255);

		g.stroke(0);
		g.camera(camEye.x, camEye.y, camEye.z, camCenter.x, camCenter.y,
				camCenter.z, 0.0f, 1, 0);
		// float fov = PApplet.PI / 3.0f;
		// float cameraZ = (g.height / 2.0f) / PApplet.tan(fov / 2.0f);
		// float aspect = PApplet.parseFloat(g.width)
		// / PApplet.parseFloat(g.height);
		// //g.perspective(fov, aspect, cameraZ / 100.0f, cameraZ * 100.0f);
		g.pushMatrix();
		g.fill(127);
		g.rotateX(PApplet.PI / 2);
		g.rectMode(PApplet.CENTER);
		g.rect(0, 0, 1000, 1000);
		g.popMatrix();
		g.translate(0, -20, -80);
		g.fill(0, 0, 255);
		g.sphere(20);

		// test image
		g.translate(0, 0, -500);
		// g.rotateY(PApplet.radians(p.frameCount));
		g.scale(4);
		shrek.display(0, 0);

		// pop matrix and style information from the stack
		g.popStyle();
		g.popMatrix();

		// finish drawing onto the graphics layer
		g.endDraw();

		// draw the 3D graphics layer onto the parent canvas
		p.image(g, 0, 0);
	}

	public class Animation {
		private PImage[] images;
		private int imageCount;
		private int frame;
		private boolean halfRate = false;

		public Animation(String imagePrefix, int count) {
			imageCount = count;
			images = new PImage[imageCount];

			for (int i = 0; i < imageCount; i++) {
				String filename = imagePrefix + PApplet.nf(i, 4) + ".jpg";
				images[i] = p.loadImage(filename);
			}
		}

		public void display(float x, float y) {
			halfRate = !halfRate;
			if (halfRate)
				frame = (frame + 1) % imageCount;
			g.image(images[frame], x, y);
		}
	}

	public void handleInput() {
		float rotationAngle = PApplet.map(p.mouseX, 0, p.width, 0,
				PApplet.TWO_PI);
		float elevationAngle = PApplet
				.map(p.mouseY, 0, p.height, 0, PApplet.PI);
		PVector move = new PVector(0, 0);
		if (p.keyPressed) {
			if (p.key == 'w' || p.key == 'W') {
				move = new PVector(3, 0);
				move.rotate(rotationAngle);
			}
			if (p.key == 'a' || p.key == 'A') {
				move = new PVector(0, -3);
				move.rotate(rotationAngle);
			}
			if (p.key == 's' || p.key == 'S') {
				move = new PVector(-3, 0);
				move.rotate(rotationAngle);
			}
			if (p.key == 'd' || p.key == 'D') {
				move = new PVector(0, 3);
				move.rotate(rotationAngle);
			}
		}
		updateCamera(rotationAngle, elevationAngle, move);
	}

	public void updateCamera(float rotationAngle, float elevationAngle,
			PVector move) {
		camEye.x += move.x;
		camEye.z += move.y;
		camCenter.x = PApplet.cos(rotationAngle) + camEye.x;
		camCenter.z = PApplet.sin(rotationAngle) + camEye.z;
		camCenter.y = -PApplet.cos(elevationAngle) + camEye.y;
	}
}
