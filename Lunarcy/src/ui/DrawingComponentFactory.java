package ui;

import processing.core.PApplet;
import game.GameState;

public class DrawingComponentFactory {

	// PApplet to construct components with
	private PApplet p;

	// game state to construct components with
	private GameState gameState;
	
	// player ID to construct components with
	private int playerID;

	// types of components available
	public static final int INVENTORY = 0;
	public static final int MINIMAP = 1;
	public static final int OXYGEN = 2;
	public static final int PERSPECTIVE3D = 3;

	public DrawingComponentFactory(PApplet p, GameState gameState, int playerID) {
		this.p = p;
		this.gameState = gameState;
		this.playerID = playerID;
	}

	public DrawingComponent getDrawingComponent(int type) {
		// depending on the type of component requested
		switch (type) {

		case INVENTORY:
			return new Inventory(p, gameState, playerID);

		case MINIMAP:
			return new Minimap(p, gameState, playerID);

		case OXYGEN:
			return new Oxygen(p, gameState, playerID);

		case PERSPECTIVE3D:
			return new Perspective3D(p, gameState, playerID);
		}

		// invalid component entered
		throw new IllegalArgumentException(
				"Invalid drawing component type entered.");
	}
}
