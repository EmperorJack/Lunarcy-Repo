package ui;

import java.util.Map;

import processing.core.PImage;
import ui.ApplicationWindow.BagView;
import ui.ApplicationWindow.ContainerView;
import ui.ApplicationWindow.EntityView;
import ui.ApplicationWindow.InventoryView;
import ui.ApplicationWindow.Minimap;
import ui.ApplicationWindow.ObjectiveView;
import ui.ApplicationWindow.Oxygen;
import ui.ApplicationWindow.PlayerView;
import ui.ApplicationWindow.PopupDisplay;
import ui.renderer.Canvas;
import ui.renderer.Perspective3D;
import game.GameState;

/**
 * Factory class used to construct drawing different drawing components for the
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

	// interaction controller for entity interactions across components
	private InteractionController interactionControl;

	// map of unique item images
	private Map<String, PImage> entityImages;

	// types of components available
	public static final int INVENTORYVIEW = 0;
	public static final int MINIMAP = 1;
	public static final int OXYGEN = 2;
	public static final int PERSPECTIVE3D = 3;
	public static final int ENTITYVIEW = 4;
	public static final int OBJECTIVEVIEW = 5;
	public static final int CONTAINERVIEW = 6;
	public static final int POPUP = 7;
	public static final int PLAYERVIEW = 8;
	public static final int BAGVIEW = 9;

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
			EntityView entityView = new EntityView(p, gameState, playerID,
					entityImages);
			interactionControl.setEntityView(entityView);
			return entityView;

		case MINIMAP:
			return new Minimap(p, gameState, playerID);

		case OXYGEN:
			return new Oxygen(p, gameState, playerID);

		case PERSPECTIVE3D:
			return new Perspective3D(p, gameState, playerID, entityImages);

		case POPUP:
			PopupDisplay popup = new PopupDisplay(null, null, p, gameState,
					playerID);
			interactionControl.setPopup(popup);
			return popup;

		case PLAYERVIEW:
			return new PlayerView(p, gameState, playerID);

		case BAGVIEW:
			BagView bagView = new BagView(p, gameState, playerID,
					entityImages);
			interactionControl.setBagView(bagView);
			return bagView;

		}

		// invalid component entered
		throw new IllegalArgumentException(
				"Invalid drawing component type entered.");
	}
}
