package ui;

import game.GameState;

/**
 * Factory class used to construct drawing different drawing componenets for the
 * canvas. Can create a variety of heads up display components or the 3D
 * perspective component.
 *
 * @author Jack
 *
 */
public class DrawingComponentFactory {

	// PApplet to construct components with
	private Canvas p;

	// game state to construct components with
	private GameState gameState;

	// player ID to construct components with
	private int playerID;

	// entity controller for entity interactions across componenets
	private EntityController entityControl;

	// types of components available
	public static final int INVENTORY = 0;
	public static final int MINIMAP = 1;
	public static final int OXYGEN = 2;
	public static final int PERSPECTIVE3D = 3;
	public static final int ENTITYVIEW = 4;
	public static final int MENU = 5;

	public DrawingComponentFactory(Canvas p, GameState gameState, int playerID,
			EntityController entityControl) {
		this.p = p;
		this.gameState = gameState;
		this.playerID = playerID;
		this.entityControl = entityControl;
	}

	/**
	 * Returns a new drawing component of the given type.
	 *
	 * @param type
	 *            The type of component requested.
	 * @return The new component.
	 */
	public DrawingComponent getDrawingComponent(int type) {
		// depending on the type of component requested
		switch (type) {

		case INVENTORY:
			Inventory inventory = new Inventory(p, entityControl, gameState, playerID);
			entityControl.setInventory(inventory);
			return inventory;

		case MINIMAP:
			return new Minimap(p, gameState, playerID);

		case OXYGEN:
			return new Oxygen(p, gameState, playerID);

		case PERSPECTIVE3D:
			return new Perspective3D(p, gameState, playerID);

		case ENTITYVIEW:
			EntityView entityView = new EntityView(p, gameState, playerID);
			entityControl.setEntityView(entityView);
			return entityView;

		}

		// invalid component entered
		throw new IllegalArgumentException(
				"Invalid drawing component type entered.");
	}
}
