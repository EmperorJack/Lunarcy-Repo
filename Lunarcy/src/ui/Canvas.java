package ui;

import ddf.minim.*;
import game.GameState;
import processing.core.*;

/**
 * The primary Processing PApplet, our drawing canvas. This canvas maintains the
 * dimensions of the entire drawing area. It calls upon separate drawing
 * components to render each frame, these include the 3D perspective and heads
 * up display components.
 * 
 * @author Jack
 *
 */
@SuppressWarnings("serial")
public class Canvas extends PApplet {

	// canvas dimensional fields
	private final int maxWidth;
	private final int maxHeight;
	private float scalingAmount = 1;
	private int xOffset, yOffset = 0;

	// draw tick fields
	private static int TARGET_FPS = 60;

	// game state fields
	private GameState gameState;
	private boolean stateUpdated;

	// drawing components
	private Perspective3D perspective;
	private Minimap minimap;

	// audio fields
	private Minim minim;
	private AudioPlayer track;

	/**
	 * Setup a new Processing Canvas.
	 *
	 * @param w
	 *            The maximum parent frame width.
	 * @param h
	 *            The maximum parent frame height.
	 * @param gameState
	 *            The initial state of the game to be drawn.
	 */
	public Canvas(int w, int h, GameState gameState) {
		this.maxWidth = w;
		this.maxHeight = h;
		this.gameState = gameState;
	}

	/**
	 * Initializes the canvas when init() is called.
	 */
	public void setup() {
		// setup the size and use 3D renderer
		size(maxWidth, maxHeight, OPENGL);
		hint(ENABLE_OPENGL_4X_SMOOTH);

		// initialize the 3D perspective component
		perspective = new Perspective3D(this, gameState);

		// initialize the heads up display components
		minimap = new Minimap(this, gameState);

		// audio setup
		minim = new Minim(this);
		track = minim.loadFile("assets/audio/important4.mp3");
		track.play();
		// /track.loop();
	}

	/**
	 * Updates the game state by replacing the local copy with a new one.
	 * 
	 * @param gameState
	 *            The new state of the game to be drawn.
	 */
	public synchronized void setGameState(GameState gameState) {
		this.gameState = gameState;

		// enable updating of drawing components next frame
		stateUpdated = true;
	}

	/**
	 * Update the separate drawing components if the game state has been
	 * updated.
	 */
	public synchronized void update() {
		if (stateUpdated) {
			// update each component
			perspective.update(gameState);
			minimap.update(gameState);

			// the state has now been updated
			stateUpdated = false;
		}
	}

	/**
	 * Renders the game state each frame.
	 */
	public void draw() {
		// compute the delta time this frame tick
		float delta = TARGET_FPS / frameRate;

		// first update all the components
		update();

		// clear the screen
		background(100);

		// image(backdrop, 0, 0);

		// adjust matrix scaling and offset
		translate(xOffset, yOffset);
		scale(scalingAmount);
		
		// draw the 3D perspective
		perspective.draw(delta);

		// allow drawing onto the heads up display layer
		hint(DISABLE_DEPTH_TEST);
		hint(ENABLE_DEPTH_TEST);
		camera();
		noLights();

		translate(xOffset, yOffset);
		scale(scalingAmount);

		// draw the heads up display components
		minimap.draw(delta);

		// draw the frame rate string
		fill(255);
		textSize(40);
		text(frameRate, maxWidth - 200, 50);
		text(delta, maxWidth - 200, 100);

		// draw the black borders
		fill(0);
		rect(0, 0, maxWidth, -maxHeight);
		rect(0, maxHeight, maxWidth, maxHeight);
		rect(0, 0, -maxWidth, maxHeight);
		rect(maxWidth, 0, maxWidth, maxHeight);
	}

	/**
	 * Update the scaling amount when the parent frame is resized.
	 *
	 * @param newWidth
	 *            The new parent frame width.
	 * @param newHeight
	 *            The new parent frame height.
	 */
	public void adjustScaling(int newWidth, int newHeight) {
		// compute the scaling per axis
		float xScale = (float) newWidth / maxWidth;
		float yScale = (float) newHeight / maxHeight;

		// use the smallest scaling value so content fits on screen
		if (xScale < yScale) {
			scalingAmount = xScale;
			xOffset = 0;

			// offset the canvas halfway down the y axis
			yOffset = (int) (newHeight - maxHeight * scalingAmount) / 2;
		} else {
			scalingAmount = yScale;

			// offset the canvas halfway along the x axis
			xOffset = (int) (newWidth - maxWidth * scalingAmount) / 2;
			yOffset = 0;
		}
	}
}