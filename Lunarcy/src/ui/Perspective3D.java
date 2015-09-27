package ui;

import processing.core.*;
import saito.objloader.*;
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
	private OBJModel floorModel;
	private OBJModel wallModel;
	private Square[][] world;
	private final int SQUARE_SIZE = 500;
	private final float MODEL_SCALE = SQUARE_SIZE / 2.5f;
	private final int vel = SQUARE_SIZE / 50;

	// camera fields
	private PVector camEye;
	private PVector camCenter;
	private float rotationAngle = 0;
	private float elevationAngle = 0;

	public Perspective3D(PApplet p, GameState gameState) {
		// public Perspective3D(PApplet p, GameState gameState) {
		super(p, gameState);

		tempGifAnimation = new Animation("assets/animations/shrek/shrek_", 20);

		// camera setup
		camEye = new PVector(0, -150, 0);
		camCenter = new PVector(0, 0, 0);

		// world setup
		floorModel = new OBJModel(p, "assets/models/floor.obj");
		wallModel = new OBJModel(p, "assets/models/wall.obj");
	}

	@Override
	public void update(GameState gameState) {
		world = gameState.getBoard();
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

		// float fov = PApplet.PI / 3.0f;
		// float cameraZ = (p.height / 2.0f) / PApplet.tan(fov / 2.0f);
		// float aspect = PApplet.parseFloat(p.width)
		// / PApplet.parseFloat(p.height);
		// p.perspective(fov, aspect, cameraZ / 100.0f, cameraZ * 100.0f);

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
		p.pointLight(200, 255, 200, 0, 0, 0);
		p.fill(0, 0, 255);
		p.sphere(10);
		p.popMatrix();

		// test rotating image plane animation
		p.rotateX(-PApplet.PI / 2);
		p.rotateY(PApplet.radians(p.frameCount));
		tempGifAnimation.update(delta);
		tempGifAnimation.display(0, 0, SQUARE_SIZE, SQUARE_SIZE);
		p.popMatrix();

		// draw test board
		p.pushMatrix();
		p.stroke(0);
		p.strokeWeight(5);
		p.rotateX(PApplet.PI / 2);

		// VERY VERBOSE need to think of way to store map on construction!
		for (int y = 0; y < world.length; y++) {
			for (int x = 0; x < world[0].length; x++) {
				Square s = world[y][x];
				if (s instanceof WalkableSquare) {
					WalkableSquare ws = (WalkableSquare) s;

					p.pushMatrix();
					p.translate(SQUARE_SIZE * x, SQUARE_SIZE * y);

					renderFloor();
					if (ws.isInside()) {
						renderCeiling();
					}

					if (ws.getWalls().get(Direction.North) instanceof SolidWall) {
						renderWall(0, 0, 1, 0);
					}

					if (ws.getWalls().get(Direction.East) instanceof SolidWall) {
						renderWall(1, 0, 1, 1);
					}

					if (ws.getWalls().get(Direction.South) instanceof SolidWall) {
						renderWall(0, 1, 1, 0);
					}

					if (ws.getWalls().get(Direction.West) instanceof SolidWall) {
						renderWall(0, 0, 1, 1);
					}
					p.popMatrix();
				}
			}
		}
		p.popMatrix();

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	private void renderFloor() {
		p.pushMatrix();

		// test drawing the floor obj model
		p.translate(0, SQUARE_SIZE);
		p.scale(MODEL_SCALE);
		p.rotateX(-PApplet.PI / 2);

		p.fill(100);
		floorModel.disableMaterial();
		floorModel.drawMode(OBJModel.POLYGON);
		floorModel.draw();

		p.popMatrix();
	}

	private void renderCeiling() {
		p.pushMatrix();
		p.translate(0, 0, SQUARE_SIZE);

		p.fill(100);
		p.rect(0, 0, SQUARE_SIZE, SQUARE_SIZE);

		p.popMatrix();
	}

	private void renderWall(int transX, int transY, int rotateX, int rotateY) {
		p.pushMatrix();
		p.translate(SQUARE_SIZE * transX, SQUARE_SIZE * transY);
		p.rotateX(PApplet.PI / 2 * rotateX);
		p.rotateY(PApplet.PI / 2 * rotateY);

		// test drawing the wall obj model
		p.translate(0, SQUARE_SIZE);
		p.scale(MODEL_SCALE);

		p.fill(150);
		wallModel.disableMaterial();
		wallModel.drawMode(OBJModel.POLYGON);
		wallModel.draw();
		//rect(0, 0, SQUARE_SIZE, SQUARE_SIZE);

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
