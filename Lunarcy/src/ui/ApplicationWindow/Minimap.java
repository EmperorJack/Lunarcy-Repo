package ui.ApplicationWindow;

import java.util.Map;

import game.Direction;
import game.Door;
import game.EmptyWall;
import game.GameState;
import game.Player;
import game.Ship;
import game.Square;
import game.WalkableSquare;
import game.Wall;
import processing.core.*;
import ui.DrawingComponent;
import ui.renderer.Canvas;

/**
 * The minimap overlay that displays a 2D top down
 * view of the game world, displaying the current players
 * location with a red arrow.
 *
 * @author evansben1
 *
 */
public class Minimap extends DrawingComponent {

	//The size of each location on the map
	private final int SQUARE_SIZE = 20;

	// How far in from the left (x axis)
	private final int LEFT_PADDING = 25;
	// How far down from the top (y axis)
	private final int TOP_PADDING = 25;

	// Sizing for the whole minimap
	private final float MINIMAP_SIZE = 200;

	// Images to preload
	private final PImage OUTDOOR_GROUND;
	private final PImage INDOOR_GROUND;

	public Minimap(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);

		//Preload the images
		OUTDOOR_GROUND = p.loadImage("assets/minimap/outdoor.png");
		INDOOR_GROUND = p.loadImage("assets/minimap/indoor.png");

		//Scale them to be the specified size
		OUTDOOR_GROUND.resize(SQUARE_SIZE, SQUARE_SIZE);
		INDOOR_GROUND.resize(SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		p.pushMatrix();
		p.pushStyle();

		// translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);
		p.stroke(0, 70);

		// Draw at half opacity
		p.tint(255, 127);

		//Get the current board
		Square[][] board = gameState.getBoard();

		// Scale map
		float xScale = 1;
		float yScale = 1;

		// The map is too big vertically so		for(Wall w: ) we must scale
		if (board.length * SQUARE_SIZE > MINIMAP_SIZE) {
			yScale = MINIMAP_SIZE / (board.length * SQUARE_SIZE);
		}

		// The map is too big horizontally so we must scale
		if (board[0].length * SQUARE_SIZE > MINIMAP_SIZE) {
			xScale = MINIMAP_SIZE / (board[0].length * SQUARE_SIZE);
		}

		p.scale(xScale, yScale);

		// Go through each square, drawing it
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Square current = board[j][i];

				// So the translation is independent each iteration
				p.pushMatrix();
				p.pushStyle();

				// Change 0,0 to be our squares position, for simplicity when drawing
				p.translate(i * SQUARE_SIZE, j * SQUARE_SIZE);

				drawSquare(current);

				p.popStyle();
				p.popMatrix();
			}
		}

		drawPlayer(gameState.getPlayer(playerID));

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}


	/**
	 * Draws the specified square if it is
	 * Walkable one. Does not draw blank squares.
	 *
	 * @param square
	 */
	private void drawSquare(Square square) {
		// Only draw Walkable squares
		if (!(square instanceof WalkableSquare)) {
			return;
		}

		WalkableSquare walk = (WalkableSquare) square;

		// if the square is the ship
		if (walk instanceof Ship) {
			// tint the square green
			p.tint(0, 255, 0, 100);
		}

		//Depending on if it's inside/outside draw the correct image
		p.image(walk.isInside() ? INDOOR_GROUND : OUTDOOR_GROUND, 0, 0, SQUARE_SIZE, SQUARE_SIZE);

		//Add all the walls to the minimap for this square
		drawWalls(square.getWalls());

	}

	private void drawWalls(Map<Direction, Wall> walls) {
		p.stroke(0, 100);

		Wall north = walls.get(Direction.NORTH);
		Wall south = walls.get(Direction.SOUTH);
		Wall east = walls.get(Direction.EAST);
		Wall west = walls.get(Direction.WEST);

		// Only draw the walls if they are not an EmptyWall, or a door

		if (!(north instanceof EmptyWall) && !(north instanceof Door)) {
			p.line(0, 0, SQUARE_SIZE, 0);
		}
		if (!(south instanceof EmptyWall) && !(south instanceof Door)) {
			p.line(0, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		}
		if (!(east instanceof EmptyWall) && !(east instanceof Door)) {
			p.line(SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
		}
		if (!(west instanceof EmptyWall) && !(west instanceof Door)) {
			p.line(0, 0, 0, SQUARE_SIZE);
		}
	}

	/**
	 * Draws the specified player
	 * at there location an arrow facing the direction
	 * they are walking.
	 *
	 * @param player
	 */
	private void drawPlayer(Player player) {
		// Set our colour to be red
		p.fill(255, 0, 0);

		// Find players location
		float playerX = player.getLocation().getX() * SQUARE_SIZE;
		float playerY = player.getLocation().getY() * SQUARE_SIZE;

		// Rotate around center point to be players direction
		int degrees = player.getOrientation().ordinal() * 90;

		p.translate(playerX + SQUARE_SIZE / 2, playerY + SQUARE_SIZE / 2);
		p.rotate(PApplet.radians(degrees));

		// Draw our player as an arrow facing their direction
		p.triangle(0, -SQUARE_SIZE / 2, -SQUARE_SIZE / 2, SQUARE_SIZE / 2,
				SQUARE_SIZE / 2, SQUARE_SIZE / 2);
	}
}