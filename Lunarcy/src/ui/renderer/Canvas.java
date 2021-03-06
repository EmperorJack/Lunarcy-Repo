package ui.renderer;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import network.Client;
import game.GameState;
import game.Item;
import game.Key;
import game.Player;
import game.WalkableSquare;
import processing.core.*;
import ui.DrawingComponent;
import ui.DrawingComponentFactory;
import ui.InteractionController;

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
	public static final int TARGET_WIDTH = 1280;
	public static final int TARGET_HEIGHT = 720;
	private final int WIDTH;
	private final int HEIGHT;
	private final float scalingAmount;
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
	private boolean gameWon;
	private Player winner;

	// drawing components
	private InteractionController interactionControl;
	private DrawingComponent perspective;
	private ArrayList<DrawingComponent> hud;

	// entity images
	private Map<String, PImage> entityImages;
	private final int ENTITY_SIZE = 100;

	// helmet mask field
	private PImage helmet;

	// security level colour fields
	private final static int GREEN = 1;
	private final static int ORANGE = 2;
	private final static int RED = 3;

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
	public Canvas(int width, int height, Client client, GameState gameState,
			boolean hardwareRenderer) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.client = client;
		this.gameState = gameState;

		// determine the scaling amount
		scalingAmount = computeScaling(WIDTH, HEIGHT);

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
		size(WIDTH, HEIGHT, renderer);
		System.out.println(scalingAmount + " : " + xOffset + ", " + yOffset);

		// setup the interaction controller
		interactionControl = new InteractionController(client, gameState,
				player, this);

		// load the entity images
		entityImages = loadEntityImages();

		// load the helmet mask image
		helmet = loadImage("/assets/hud/helmet_mask.png");

		// setup the drawing component factory
		DrawingComponentFactory factory = new DrawingComponentFactory(this,
				gameState, playerID, interactionControl, entityImages);

		// get a 3D perspective component
		perspective = factory
				.getDrawingComponent(DrawingComponentFactory.PERSPECTIVE3D);

		// setup the HUD components list
		hud = new ArrayList<DrawingComponent>();

		// get the HUD drawing components
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.PLAYERVIEW));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.OXYGEN));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.MINIMAP));
		hud.add(factory
				.getDrawingComponent(DrawingComponentFactory.INVENTORYVIEW));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.ENTITYVIEW));
		hud.add(factory
				.getDrawingComponent(DrawingComponentFactory.OBJECTIVEVIEW));
		hud.add(factory
				.getDrawingComponent(DrawingComponentFactory.CONTAINERVIEW));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.POPUP));
		hud.add(factory.getDrawingComponent(DrawingComponentFactory.BAGVIEW));

		// audio setup
		// minim = new Minim(this);
		// track = minim.loadFile("assets/audio/*.mp3");
		// track.play();

		// drawing setup
		noStroke();
		hint(ENABLE_DEPTH_MASK);

		// text setup
		hint(ENABLE_NATIVE_FONTS);
		PFont font = createFont("Arial", 36);
		textFont(font);
		textSize(12);
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

			// update interaction controller
			interactionControl.update(player, gameState);

			// check if a player has won the game
			if (gameState.getShip().hasLaunched()) {
				gameWon = true;
				winner = gameState.getShip().getPilot();
			}

			// the state has now been updated
			stateUpdated = false;
		}
	}

	/**
	 * Renders the game state each frame.
	 */
	public void draw() {
		// push matrix and style information onto the stack
		pushMatrix();
		pushStyle();

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

		// pop matrix and style information onto the stack
		popMatrix();
		popStyle();

		// allow drawing onto the heads up display layer
		hint(DISABLE_DEPTH_TEST);
		hint(ENABLE_DEPTH_TEST);
		camera();
		perspective();
		noLights();

		// scale in reference to the canvas scaling amount
		scale(scalingAmount);

		// if the player is outside
		if (!((WalkableSquare) gameState.getSquare(player.getLocation()))
				.isInside()) {

			// draw the helmet mask overlay
			image(helmet, 0, 0, 1280, 720);
		}

		// draw the heads up display components
		for (DrawingComponent component : hud) {
			component.draw(gameState, delta);
		}

		// if the interaction controller has an item being dragged
		Item draggedItem = interactionControl.getDraggedItem();
		if (draggedItem != null) {
			pushMatrix();
			pushStyle();

			// if the dragged item is a key
			if (draggedItem instanceof Key) {

				// tint the image with the correct security colour
				tint(Canvas.getSecurityColour(
						((Key) draggedItem).getAccessLevel(), false));
			}

			int x = interactionControl.getDraggedItemX();
			int y = interactionControl.getDraggedItemY();

			// draw the dragged item
			imageMode(PApplet.CENTER);
			image(entityImages.get(draggedItem.getImageName()), x, y,
					ENTITY_SIZE, ENTITY_SIZE);

			popMatrix();
			popStyle();
		}

		// if the game has been won
		if (gameWon) {

			// if this player won the game
			if (winner.equals(player)) {

				// display a popup with winner information
				String description = "Congratulations! You have won this round of Lunarcy!";
				interactionControl.updatePopup("ROUND WON", description);
			} else {
				// display a popup with loser information
				String description = winner.getName()
						+ " has won this round of Lunarcy! Better luck next time.";
				interactionControl.updatePopup("ROUND LOST", description);
			}
		}

		// printCanvasInfo(delta);
	}

	/**
	 * Update the scaling amount given a width and height different to the
	 * target width and height.
	 *
	 * @param newWidth
	 *            The new parent frame width.
	 * @param newHeight
	 *            The new parent frame height.
	 */
	private float computeScaling(int newWidth, int newHeight) {
		float scalingAmount;

		// compute the scaling per axis
		float xScale = (float) newWidth / TARGET_WIDTH;
		float yScale = (float) newHeight / TARGET_HEIGHT;

		// use the smallest scaling value so content fits on screen
		if (xScale < yScale) {
			scalingAmount = xScale;
			xOffset = 0;

			// offset the canvas halfway down the y axis
			yOffset = (int) (newHeight - TARGET_HEIGHT * scalingAmount) / 2;
		} else {
			scalingAmount = yScale;

			// offset the canvas halfway along the x axis
			xOffset = (int) (newWidth - TARGET_WIDTH * scalingAmount) / 2;
			yOffset = 0;
		}

		return scalingAmount;
	}

	/**
	 * Returns the scaling amount required to scale the canvas to draw in
	 * reference to the target width and target height.
	 *
	 * @return The float to scale by.
	 */
	public float getScaling() {
		return scalingAmount;
	}

	/**
	 * Returns the colour used to tint secured items based on the given access
	 * level.
	 *
	 * @param accessLevel
	 *            The access level of the colour requested.
	 * @return The colour in RGB to tint for the given access level.
	 */
	public static int getSecurityColour(int accessLevel, boolean darker) {
		Color color = null;

		// depending on the given access level
		switch (accessLevel) {

		case GREEN:
			// green level access required
			color = new Color(85, 255, 0);
			break;

		case ORANGE:
			// orange level access required
			color = new Color(255, 175, 0);
			break;

		case RED:
			// red level access required
			color = new Color(255, 0, 0);
			break;

		default:
			// no level access required
			color = new Color(150, 150, 150);
		}

		// if the requested colour should be darker
		if (darker) {
			color = color.darker();
		}

		return color.getRGB();
	}

	/**
	 * Loads all the entity (item and container) images for each unique name in
	 * the all_items.txt file.
	 *
	 * @return Map of String (entity name) to image.
	 */
	private Map<String, PImage> loadEntityImages() {
		// create a new map from strings to images
		Map<String, PImage> entityImages = new HashMap<String, PImage>();

		// create a scanner to parse in the items file
		try {
			Scanner scanner = new Scanner(new File(
					"assets/items/all_entities.txt"));

			// while the scanner has entity names left to parse
			while (scanner.hasNextLine()) {
				// load in the unique entity name
				String name = scanner.nextLine();

				// load in the entity image from the given name
				entityImages.put(name, loadImage("/assets/items/" + name
						+ ".png"));
			}

			// close the scanner
			scanner.close();
		} catch (FileNotFoundException e) {
			// file load exception has occured
			e.printStackTrace();
		}

		return entityImages;
	}

	/**
	 * Prints out some useful information. Current framerate, delta time, player
	 * position and orientation.
	 *
	 * @param delta
	 */
	@SuppressWarnings("unused")
	private void printCanvasInfo(float delta) {
		pushMatrix();
		pushStyle();

		fill(255);
		textSize(40);

		// draw the frame rate string
		text(frameRate, TARGET_WIDTH - 200, 50);
		text(delta, TARGET_WIDTH - 200, 100);

		// draw player position and orientation strings
		gameState.getPlayer(playerID);
		text(player.getLocation().getX() + " : " + player.getLocation().getY(),
				TARGET_WIDTH - 200, 150);
		text(player.getOrientation().toString(), TARGET_WIDTH - 200, 200);

		popStyle();
		popMatrix();
	}
}