package ui;

import game.BlankSquare;
import game.GameState;
import game.Square;
import game.WalkableSquare;
import processing.core.*;

/**
 * The minimap overlay that displays a 2D top down view of the game world.
 *
 * @author Jack & Ben
 *
 */
public class Minimap extends DrawingComponent {

	private final int SIZE = 20;
	private GameState gameState;

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

		// set the initial game state
		update(gameState);
	}

	@Override
	public void update(GameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public void draw(float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);
		p.stroke(0, 70);

		// Draw at half opacity
		p.tint(255, 127);

		Square[][] board = gameState.getBoard();

		// Go through each square, drawing it
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Square current = board[j][i];

				// TODO: DISTINGUISH BETWEEN INDOOR/OUTDOOR

				// Walkable square
				if (current instanceof WalkableSquare) {
					p.image(OUTDOOR_GROUND, j * SIZE, i * SIZE, SIZE, SIZE);
				}
				//Dont draw blankSquares
				if(current instanceof BlankSquare){

				}
				// Unwalkable square
				else {
					p.image(INDOOR_GROUND, j * SIZE, i * SIZE, SIZE, SIZE);
				}
			}

		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}