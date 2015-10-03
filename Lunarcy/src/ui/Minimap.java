package ui;

import java.util.Map;

import game.BlankSquare;
import game.Direction;
import game.EmptyWall;
import game.GameState;
import game.Player;
import game.Square;
import game.WalkableSquare;
import game.Wall;
import processing.core.*;

/**
 * The minimap overlay that displays a 2D top down view of the game world.
 *
 * @author Jack & Ben
 *
 */
public class Minimap extends DrawingComponent {

	private final int SQUARE_SIZE = 20;

	// How far in from the left (x axis)
	private final int LEFT_PADDING = 25;
	// How far down from the top (y axis)
	private final int TOP_PADDING = 25;

	// Sizing for the minimap
	private final float MINIMAP_SIZE = 300;

	// Images to preload
	private final PImage OUTDOOR_GROUND;
	private final PImage INDOOR_GROUND;

	// private final PImage UNACCESIBLE_SQUARE;

	public Minimap(PApplet p, GameState gameState, int playerID) {
		super(p, gameState, playerID);

		OUTDOOR_GROUND = p.loadImage("assets/minimap/outdoor.png");
		INDOOR_GROUND = p.loadImage("assets/minimap/indoor.png");

		OUTDOOR_GROUND.resize(SQUARE_SIZE, SQUARE_SIZE);
		INDOOR_GROUND.resize(SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// get the player from the current game state
		Player player = gameState.getPlayer(playerID);

		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);
		p.stroke(0, 70);

		// Draw at half opacity
		p.tint(255, 127);

		Square[][] board = gameState.getBoard();

		// Scale map
		float xScale = 1;
		float yScale = 1;

		// The map is too big vertically so we must scale
		if (board.length * SQUARE_SIZE > MINIMAP_SIZE) {
			yScale = MINIMAP_SIZE / (board.length * SQUARE_SIZE);
		}

		// Map is too big horizontally so we must scale
		if (board[0].length * SQUARE_SIZE > MINIMAP_SIZE) {
			xScale = MINIMAP_SIZE / (board[0].length * SQUARE_SIZE);
		}

		p.scale(xScale, yScale);

		// Go through each square, drawing it
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Square current = board[j][i];

				// So the translation is independant each iteration
				p.pushMatrix();

				p.translate(i * SQUARE_SIZE, j * SQUARE_SIZE);

				// Walkable square
				if (current instanceof WalkableSquare) {
					p.image(OUTDOOR_GROUND, 0, 0, SQUARE_SIZE, SQUARE_SIZE);
				}
				// Dont draw blankSquares
				if (current instanceof BlankSquare) {
					p.popMatrix();
					continue;
				}
				// Unwalkable square
				else {
					p.image(INDOOR_GROUND, 0, 0, SQUARE_SIZE, SQUARE_SIZE);
				}

				// Draw the four walls
				Map<Direction, Wall> walls = current.getWalls();

				p.stroke(0, 100);

				// Only draw the walls if they are not an EmptyWall
				if (!(walls.get(Direction.NORTH) instanceof EmptyWall)) {
					p.line(0, 0, SQUARE_SIZE, 0);
				}

				if (!(walls.get(Direction.EAST) instanceof EmptyWall)) {
					p.line(SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
				}

				if (!(walls.get(Direction.SOUTH) instanceof EmptyWall)) {
					p.line(0, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				}

				if (!(walls.get(Direction.WEST) instanceof EmptyWall)) {
					p.line(0, 0, 0, SQUARE_SIZE);
				}

				p.popMatrix();

			}

		}

		// Draw player in their current location
		p.fill(255, 0, 0);
		p.rect(player.getLocation().getX() * SQUARE_SIZE, player.getLocation()
				.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}