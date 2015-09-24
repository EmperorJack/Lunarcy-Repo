package ui;

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
	private final int initialWidth;
	private final int initialHeight;
	private float scalingAmount = 1;
	private int xOffset, yOffset = 0;

	// game state field
	private GameState gameState;
	private boolean stateUpdated;

	// drawing components
	private Perspective3D perspective;
	private Minimap minimap;
	private PImage backdrop;

	// audio fields
	// private Minim minim;

	// public AudioPlayer track;

	// 3D
	// public FPSEngine engine;
	// public PGraphics canvas3D;

	/**
	 * Setup a new Processing Canvas.
	 *
	 * @param w
	 *            The initial parent frame width.
	 * @param h
	 *            The initial parent frame height.
	 * @param gameState
	 *            The initial state of the game to be drawn.
	 */
	public Canvas(int w, int h, GameState gameState) {
		this.initialWidth = w;
		this.initialHeight = h;
		this.gameState = gameState;

		// initialize the drawing components
		perspective = new Perspective3D(this, gameState);
		minimap = new Minimap(this, gameState);
		backdrop = loadImage("assets/backgrounds/temp-backdrop.jpg");
	}

	/**
	 * When the canvas has been init().
	 */
	public void setup() {
		// setup the size and use 3D renderer
		size(initialWidth, initialHeight, P3D);

		// audio setup
		// this.minim = new Minim(this);
		// VERY IMPORTANT PUSH
		// double random = Math.random();
		// this.track = minim.loadFile("assets/audio/important3.mp3");
		// this.track.play();

		// SETUP 3D ENVIRONMENT
		// canvas3D = createGraphics(initialWidth, initialHeight, P3D);
		// engine = new FPSEngine(canvas3D, this);

	}

	/**
	 * Updates the game state by replacing the local copy with a new one.
	 * 
	 * @param gameState
	 *            The new state of the game to be drawn.
	 */
	public synchronized void setGameState(GameState gameState) {
		this.gameState = gameState;
		stateUpdated = true;
	}

	/**
	 * Update the separate drawing components if the game state has been
	 * updated.
	 */
	public synchronized void update() {
		if (stateUpdated) {
			// update each component
			perspective.draw();
			minimap.update(gameState);

			// the state has now been updated
			stateUpdated = false;
		}
	}

	/**
	 * Renders the game state each frame.
	 */
	public void draw() {
		// first update all the components
		update();

		// now begin rendering the game state
		background(255);
		// handleInput();

		// adjust matrix scaling and offset
		translate(xOffset, yOffset);
		scale(scalingAmount);
		
		image(backdrop, 0, 0);

		// draw the 3D perspective
		perspective.draw();

		// draw the heads up display components
		minimap.draw();

		// engine.draw();
		// image(engine.canvas3D, 0, 0);
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

	// public void handleInput() {
	// float rotationAngle = map(mouseX, 0, width, 0, TWO_PI);
	// float elevationAngle = map(mouseY, 0, height, 0, PI);
	// PVector move = new PVector(0, 0);
	// if (keyPressed) {
	// if (key == 'w' || key == 'W') {
	// move = new PVector(3, 0);
	// move.rotate(rotationAngle);
	// }
	// if (key == 'a' || key == 'A') {
	// move = new PVector(0, -3);
	// move.rotate(rotationAngle);
	// }
	// if (key == 's' || key == 'S') {
	// move = new PVector(-3, 0);
	// move.rotate(rotationAngle);
	// }
	// if (key == 'd' || key == 'D') {
	// move = new PVector(0, 3);
	// move.rotate(rotationAngle);
	// }
	// }
	// engine.updateCamera(rotationAngle, elevationAngle, move);
	// }
}