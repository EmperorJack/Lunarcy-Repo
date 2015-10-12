package ui;

import java.util.Map;

import processing.core.PImage;
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

	// interaction controller for entity interactions across componenets
	private InteractionController interactionControl;

	// map of unique item images
	Map<String, PImage> entityImages;

	// types of components available
	public static final int INVENTORYVIEW = 0;
	public static final int MINIMAP = 1;
	public static final int OXYGEN = 2;
	public static final int PERSPECTIVE3D = 3;
	public static final int ENTITYVIEW = 4;
	public static final int MENU = 5;
	public static final int OBJECTIVEVIEW = 6;
	public static final int CONTAINERVIEW = 7;

	public DrawingComponentFactory(Canvas p, GameState gameState, int playerID,
			InteractionController interactionControl,
			Map<String, PImage> entityImages) {
		this.p = p;
		this.gameState = gameState;
		this.playerID = playerID;
		this.interactionControl = interactionControl;
		this.entityImages = entityImages;
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

		case INVENTORYVIEW:
			InventoryView inventory = new InventoryView(p, gameState, playerID,
					entityImages);
			interactionControl.setInventory(inventory);
			return inventory;

		case OBJECTIVEVIEW:
			return new ObjectiveView(p, gameState, playerID, entityImages);

		case CONTAINERVIEW:
			ContainerView container = new ContainerView(p, gameState, playerID,
					entityImages);
			interactionControl.setContainerView(container);
			return container;

		case ENTITYVIEW:
			EntityView entityView = new EntityView(p, gameState, playerID, entityImages);
			interactionControl.setEntityView(entityView);
			return entityView;

		case MINIMAP:
			return new Minimap(p, gameState, playerID);

		case OXYGEN:
			return new Oxygen(p, gameState, playerID);

		case PERSPECTIVE3D:
			return new Perspective3D(p, gameState, playerID, entityImages);

		}

		// invalid component entered
		throw new IllegalArgumentException(
				"Invalid drawing component type entered.");
	}
}
