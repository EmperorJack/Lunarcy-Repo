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
	private final int[][] BOARD = new int[][] { { 0, 0, 1 }, { 1, 0, 1 }, { 0, 0, 0 }, { 0, 1, 0 }, { 1, 0, 0 },
			{ 0, 0, 0 }, { 1, 2, 1 }, { 0, 0, 0 } };
	private final int SIZE = 30;

	// How far in from the left (x axis)
	private final int LEFT_PADDING = 25;
	// How far down from the top (y axis)
	private final int TOP_PADDING = 25;

	public Minimap(PApplet p, GameState gameState) {
		super(p, gameState);
	}

	@Override
	public void update(GameState state) {
		// TODO update the minimap

	}

	@Override
	public void draw(float delta) {
		p.pushStyle();

		// Go through each square, drawing it
		for (int i = 0; i < BOARD.length; i++) {
			for (int j = 0; j < BOARD[i].length; j++) {
				// Set the colour based on square type
				switch (BOARD[i][j]) {
				case 0:
					p.fill(55, 255, 255, 20);
					break;
				case 1:
					p.fill(0, 0, 0, 70);
					break;

				case 2:
					p.fill(255, 0, 0, 90);
					break;
				}

				p.rect(LEFT_PADDING + i * SIZE, TOP_PADDING + j * SIZE, SIZE, SIZE);
			}
		}

		p.popStyle();
	}
}