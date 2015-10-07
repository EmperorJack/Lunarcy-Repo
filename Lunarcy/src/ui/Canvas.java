package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import control.Client;
import control.DropAction;
import control.MoveAction;
import control.OrientAction;
import control.PickupAction;
import control.PutAction;
import ddf.minim.*;
import game.GameState;
import game.Player;
import processing.core.*;

/**
 * The primary Processing PApplet, our drawing canvas. This canvas maintains the
 * dimensions of the entire drawing area. It calls upon separate drawing
 * components to render each frame, these include the 3D perspective and heads
 * up display components.
 *
 * @author Jack and Ben
 *
 */

@SuppressWarnings("serial")
public class Canvas extends PApplet implements KeyListener, MouseListener {

	// canvas dimensional fields
	private final int maxWidth;
	private final int maxHeight;
	private float scalingAmount = 1;
	private int xOffset, yOffset = 0;

	// renderer fields
	private static final int TARGET_FPS = 60;
	private final String renderer;

	// client field
	private final Client client;
	private final int playerID;
	private Player player;

	// game state fields
	private GameState gameState;
	private GameState updatedState;
	private boolean stateUpdated;

	// drawing components
	private EntityController entityControl;
	private DrawingComponent perspective;
	private ArrayList<DrawingComponent> hud;

	// menu field
	private Menu menu;

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
	public Canvas(int w, int h, Client client, GameState gameState,
			boolean hardwareRenderer) {
		this.maxWidth = w;
		this.maxHeight = h;
		this.client = client;
		this.gameState = gameState;

		// get the player id from the client entity
		playerID = client.getPlayerID();
		player = gameState.getPlayer(playerID);

		// determine which renderer should be used
		if (hardwareRenderer) {
			// use the hardware OpenGL renderer
			renderer = OPENGL;
		} else {
			// use the software P3D renderer
			renderer = P3D;
		}

	}

	/**
	 * Initializes the canvas when init() is called.
	 */
	public void setup() {
		// setup the size and use 3D renderer
		size(maxWidth, maxHeight, renderer);

		// setup the entity controller
		entityControl = new EntityController(client, gameState, player, this);

		// setup the drawing component factory
		DrawingComponentFactory factory = new DrawingComponentFactory(this,
				gameState, playerID, entityControl);

		// get a 3D perspective component
		perspective = factory
				.getDrawingComponent(DrawingComponentFactory.PERSPECTIVE3D);

		// get the HUD components
		hud = new ArrayList<DrawingComponent>();

		hud.add(factory.getDrawingComponent(DrawingComponentFactory.OXYGEN));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.MINIMAP));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.INVENTORY));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.ENTITYVIEW));

		// audio setup
		// minim = new Minim(this);
		// track = minim.loadFile("assets/audio/*.mp3");
		// track.play();

		// drawing setup
		noStroke();
	}

	/**
	 * Updates the game state by replacing the local copy with a new one.
	 *
	 * @param gameState
	 *            The new state of the game to be drawn.
	 */
	public synchronized void setGameState(GameState updatedState) {
		this.updatedState = updatedState;

		// enable updating of drawing components next frame
		stateUpdated = true;
	}

	/**
	 * Update the separate drawing components if the game state has been
	 * updated.
	 */
	public synchronized void update() {
		if (stateUpdated) {
			// replace the game state with the new updated state
			gameState = updatedState;

			// update player field
			player = gameState.getPlayer(playerID);

			// update entity controller
			entityControl.update(gameState, player);

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

		// adjust matrix scaling and offset
		translate(xOffset, yOffset);
		scale(scalingAmount);

		// draw the 3D perspective
		perspective.draw(gameState, delta);

		// allow drawing onto the heads up display layer
		hint(DISABLE_DEPTH_TEST);
		hint(ENABLE_DEPTH_TEST);
		camera();
		perspective();
		noLights();

		translate(xOffset, yOffset);
		scale(scalingAmount);

		// draw the heads up display components
		for (DrawingComponent component : hud) {
			component.draw(gameState, delta);
		}

		// draw the frame rate string
		fill(255);
		textSize(40);
		text(frameRate, maxWidth - 200, 50);
		text(delta, maxWidth - 200, 100);

		// draw player position and orientation
		Player player = gameState.getPlayer(playerID);
		text(player.getLocation().getX() + " : " + player.getLocation().getY(),
				maxWidth - 200, 150);
		text(player.getOrientation().toString(), maxWidth - 200, 200);

		// draw the black borders
		fill(0);
		rect(0, 0, maxWidth, -10 * maxHeight); // top
		rect(0, maxHeight, maxWidth, 10 * maxHeight); // bottom
		rect(0, 0, -10 * maxWidth, maxHeight); // left
		rect(maxWidth, 0, 10 * maxWidth, maxHeight); // right

		if (entityControl.getMenu() != null) {
			entityControl.getMenu().draw(gameState, delta);
		}
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