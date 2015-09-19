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

	// temporary background image
	public PImage backdrop;

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
		this.backdrop = loadImage("assets/backgrounds/temp-backdrop.jpg");

		// audio setup
		this.minim = new Minim(this);
		// VERY IMPORTANT PUSH
		if (random(10) < 5) {
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
		// TODO scaling not quite perfect yet
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

}