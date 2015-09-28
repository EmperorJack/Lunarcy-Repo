package ui;

import game.GameState;
import processing.core.*;

/**
 * The minimap overlay that displays a 2D top down view of the game world.
 * 
 * @author Jack & Ben
 *
 */
public class Minimap extends DrawingComponent {

	// TEMPORARY FIELDS: while we do not have a board in gamestate
	private final int[][] BOARD = new int[][] { { 0, 0, 1 }, { 2, 1, 1 }, { 0, 0, 0 }, { 0, 1, 0 }, { 1, 0, 0 },
			{ 0, 0, 0 }, { 1, 1, 1 }, { 0, 0, 2 } };
	private final int SIZE = 30;

	// How far in from the left (x axis)
	private final int LEFT_PADDING = 25;
	// How far down from the top (y axis)
	private final int TOP_PADDING = 25;

	// Images to preload
	private final PImage OUTDOOR_GROUND;
	private final PImage INDOOR_GROUND;
	// private final PImage UNACCESIBLE_SQUARE;

	public Minimap(PApplet p, GameState gameState) {
		super(p, gameState);

		OUTDOOR_GROUND = p.loadImage("assets/minimap/outdoor.png");
		INDOOR_GROUND = p.loadImage("assets/minimap/indoor.png");

		OUTDOOR_GROUND.resize(SIZE, SIZE);
		INDOOR_GROUND.resize(SIZE, SIZE);
	}

	@Override
	public void update(GameState gameState) {
		// TODO update the minimap

	}

	@Override
	public void draw(float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);
		p.stroke(0, 70);

		// Go through each square, drawing it
		for (int i = 0; i < BOARD.length; i++) {
			for (int j = 0; j < BOARD[i].length; j++) {
				// Set the colour based on square type
				switch (BOARD[i][j]) {
				// OUTDOOR SQUARE
				case 0:
					p.image(OUTDOOR_GROUND, i * SIZE, j * SIZE);
					break;
				// INDOOR SQUARE
				case 1:
					p.image(INDOOR_GROUND, i * SIZE, j * SIZE);
					break;
				// EMPTY SQUARE DONT DRAW
				case 2:
					break;
				}
			}

		}
		
		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}