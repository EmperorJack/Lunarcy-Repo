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

	private Player player;
	private int playerID;

	public Minimap(int playerID, PApplet p, GameState gameState) {
		super(p, gameState);

		OUTDOOR_GROUND = p.loadImage("assets/minimap/outdoor.png");
		INDOOR_GROUND = p.loadImage("assets/minimap/indoor.png");

		OUTDOOR_GROUND.resize(SIZE, SIZE);
		INDOOR_GROUND.resize(SIZE, SIZE);

		this.playerID = playerID;
		// set the initial game state
		update(gameState);
	}

	@Override
	public void update(GameState gameState) {
		this.gameState = gameState;
		player = gameState.getPlayer(playerID);
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

				//So the translation is independant each iteration
				p.pushMatrix();

				p.translate(i * SIZE, j * SIZE);

				// Walkable square
				if (current instanceof WalkableSquare) {
					p.image(OUTDOOR_GROUND, 0, 0, SIZE, SIZE);
				}
				//Dont draw blankSquares
				if(current instanceof BlankSquare){
					p.popMatrix();
					continue;
				}
				// Unwalkable square
				else {
					p.image(INDOOR_GROUND, 0, 0, SIZE, SIZE);
				}

				//Draw the four walls
				Map<Direction, Wall> walls = current.getWalls();

				p.stroke(0, 100);

				//Only draw the walls if they are not an EmptyWall
				if(!(walls.get(Direction.NORTH) instanceof EmptyWall)){
					p.line(0, 0, SIZE, 0);
				}

				if(!(walls.get(Direction.EAST) instanceof EmptyWall)){
					p.line(SIZE, 0, SIZE, SIZE);
				}

				if(!(walls.get(Direction.SOUTH) instanceof EmptyWall)){
					p.line(0, SIZE, SIZE, SIZE);
				}

				if(!(walls.get(Direction.WEST) instanceof EmptyWall)){
					p.line(0, 0, 0, SIZE);
				}

				p.popMatrix();

			}

		}

		//Draw player in their current location
		p.fill(255,0,0);
		p.rect(player.getLocation().getX()*SIZE, player.getLocation().getY()*SIZE, SIZE, SIZE);

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}