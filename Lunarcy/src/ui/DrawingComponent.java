package ui;

import ui.renderer.Canvas;
import game.GameState;

/**
 * Represents a display component that is rendered onto the canvas.
 *
 * @author Jack
 *
 */
public abstract class DrawingComponent {

	// the parent processing canvas
	protected Canvas p;

	// the id of the client
	protected int playerID;


	public DrawingComponent(Canvas p, GameState gameState, int playerID) {
		// set the parent canvas to draw onto
		this.p = p;

		// set the player ID for this component
		this.playerID = playerID;
	}

	/**
	 * Draw the component.
	 *
	 * @param gameState
	 *            The current game state to draw the component in reference to.
	 * @param delta
	 *            The delta time for the current draw tick.
	 */
	public abstract void draw(GameState gameState, float delta);
}
