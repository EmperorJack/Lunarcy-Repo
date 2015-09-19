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
		size(initialWidth, initialHeight, P3D);
		frameRate(30);
		
		// load temp images
		backdrop = loadImage("assets/backgrounds/temp-backdrop.jpg");
		shrek = new Animation("assets/animations/shrek/shrek_", 20);

		// audio setup
		this.minim = new Minim(this);
		// VERY IMPORTANT PUSH
		double random = Math.random();
		if (random < 0.5) {
			this.track = minim.loadFile("assets/audio/important.mp3");
		} else {
			this.track = minim.loadFile("assets/audio/important2.mp3");
		}
		this.track.play();
	}

	/**
	 * Renders the game state per frame.
	 */
	public void draw() {
		background(255);

		// adjust matrix scaling and offset
		translate(xOffset, yOffset);
		scale(scalingAmount);
		image(backdrop, 0, 0);
		scale(4);
		shrek.display(0, 0);
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

		public Animation(String imagePrefix, int count) {
			imageCount = count;
			images = new PImage[imageCount];

			for (int i = 0; i < imageCount; i++) {
				String filename = imagePrefix + nf(i, 4) + ".jpg";
				images[i] = loadImage(filename);
			}
		}

		public void display(float x, float y) {
			frame = (frame + 1) % imageCount;
			image(images[frame], x, y);
		}
	}
}