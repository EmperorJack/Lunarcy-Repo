package user_interface;

import processing.core.*;
import ddf.minim.*;

@SuppressWarnings("serial")
public class Canvas extends PApplet {

	// canvas dimensional fields
	public final int initialWidth;
	public final int initialHeight;
	public float scalingAmount = 1;
	public int xOffset, yOffset = 0;

	// temporary background images
	public PImage backdrop;
	public Animation shrek;

	// audio fields
	public Minim minim;
	public AudioPlayer track;
	
	//3D
	public FPSEngine engine;
	public PGraphics canvas3D;
	


	/**
	 * Setup a new Processing Canvas.
	 * 
	 * @param w
	 *            The initial parent frame width.
	 * @param h
	 *            The initial parent frame height.
	 */
	public Canvas(int w, int h) {
		this.initialWidth = w;
		this.initialHeight = h;
	}

	/**
	 * When the canvas has been init().
	 */
	public void setup() {
		// setup the size and use 3D renderer
		size(initialWidth, initialHeight, OPENGL);
		smooth(4);
		frameRate(30);
		
		// load temp images
		backdrop = loadImage("assets/backgrounds/temp-backdrop.jpg");
		shrek = new Animation("assets/animations/shrek/shrek_", 20);

		// audio setup
		this.minim = new Minim(this);
		// VERY IMPORTANT PUSH
		double random = Math.random();
			this.track = minim.loadFile("assets/audio/important3.mp3");
		this.track.play();
		
		//SETUP 3D ENVIRONMENT
		canvas3D = createGraphics(initialWidth, initialHeight, OPENGL);
		engine = new FPSEngine(canvas3D, this);
	
	}

	/**
	 * Renders the game state per frame.
	 */
	public void draw() {
		background(255);
		handleInput();
		// adjust matrix scaling and offset
		translate(xOffset, yOffset);
		scale(scalingAmount);
		//image(backdrop, 0, 0);
		pushMatrix();
		scale(4);
		//shrek.display(0, 0);
		popMatrix();
		engine.draw();
		image(engine.canvas3D, 0, 0);
	}
	
	void handleInput(){
		  float rotationAngle = map(mouseX, 0, width, 0, TWO_PI);
		  float elevationAngle = map(mouseY, 0, height, 0, PI);
			PVector move = new PVector(0,0);
			if (keyPressed){
				if (key == 'w' || key == 'W'){
					move = new PVector(3,0);
					move.rotate(rotationAngle);
					
				}
				if (key == 'a' || key == 'A'){
					move = new PVector(0,-3);
					move.rotate(rotationAngle);
				}
				if (key == 's' || key == 'S'){
					move = new PVector(-3,0);
					move.rotate(rotationAngle);
				}
				if (key == 'd' || key == 'D'){
					move = new PVector(0,3);
					move.rotate(rotationAngle);
				}
			}
		  engine.updateCamera(rotationAngle, elevationAngle , move);
	}
	

	/**
	 * Update the scaling amount when the parent frame is resized.
	 * 
	 * @param width
	 *            The new parent frame width.
	 * @param height
	 *            The new parent frame height.
	 */
	public void adjustScaling(int width, int height) {
		// compute the scaling per axis
		float xScale = (float) width / initialWidth;
		float yScale = (float) height / initialHeight;

		// use the smallest scaling value so content fits on screen
		if (xScale < yScale) {
			scalingAmount = xScale;
			xOffset = 0;

			// offset the canvas halfway down the y axis
			yOffset = (int) (height - initialHeight * scalingAmount) / 2;
		} else {
			scalingAmount = yScale;

			// offset the canvas halfway along the x axis
			xOffset = (int) (width - initialWidth * scalingAmount) / 2;
			yOffset = 0;
		}
	}

	private class Animation {
		private PImage[] images;
		private int imageCount;
		private int frame;
		private boolean halfRate = false;

		public Animation(String imagePrefix, int count) {
			imageCount = count;
			images = new PImage[imageCount];

			for (int i = 0; i < imageCount; i++) {
				String filename = imagePrefix + nf(i, 4) + ".jpg";
				images[i] = loadImage(filename);
			}
		}

		public void display(float x, float y) {
			halfRate = !halfRate;
			if (halfRate) frame = (frame + 1) % imageCount;
			image(images[frame], x, y);
		}
	}
}