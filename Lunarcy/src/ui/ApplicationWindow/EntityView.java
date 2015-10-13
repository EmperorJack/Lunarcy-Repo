package ui.ApplicationWindow;

import java.util.List;
import java.util.Map;

import game.GameState;
import game.Player;
import game.Item;
import game.SolidContainer;
import game.Square;
import processing.core.PApplet;
import processing.core.PImage;
import ui.DrawingComponent;
import ui.renderer.Canvas;

/**
 * Displays the entities (containers and items) in the same square as the player
 * that can be interacted with from a 2D perspective.
 *
 * @author Jack
 *
 */
public class EntityView extends DrawingComponent {

	// entity drawing fields
	private final int ITEM_SIZE = 200;
	private final int CONTAINER_SIZE = 600;
	private final int TOP_PADDING_ITEMS = 400;
	private final int TOP_PADDING_CONTAINER = 30;

	// all possible entity images
	private final Map<String, PImage> entityImages;

	// currently held items
	private List<Item> items;

	// currently held container
	private SolidContainer container;

	public EntityView(Canvas p, GameState gameState, int playerID,
			Map<String, PImage> entityImages) {
		super(p, gameState, playerID);
		this.entityImages = entityImages;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// get the player from the current game state
		Player thisPlayer = gameState.getPlayer(playerID);

		// get the square the player is currently in
		Square square = gameState.getSquare(thisPlayer.getLocation());

		// get the container in the current square for this player direction
		container = square.getContainer(thisPlayer.getOrientation());

		p.imageMode(PApplet.CENTER);

		// if the container currently exists
		if (container != null) {
			p.pushMatrix();

			// translate to allow for top padding
			p.translate(0, TOP_PADDING_CONTAINER);

			String containerImageName = container.getImageName();

			// draw the container
			p.image(entityImages.get(containerImageName),
					Canvas.TARGET_WIDTH / 2.0f, CONTAINER_SIZE / 2,
					CONTAINER_SIZE, CONTAINER_SIZE);

			p.popMatrix();
		}

		// get the items in the current square for the player direction
		items = square.getItems(thisPlayer.getOrientation());

		// translate to allow for top padding
		p.translate(0, TOP_PADDING_ITEMS);

		// for each item
		for (int i = 0; i < items.size(); i++) {
			// compute the x position to place the image
			int xPos = (int) ((i + 1) / (float) (items.size() + 1) * Canvas.TARGET_WIDTH);

			// draw the item image
			p.image(entityImages.get(items.get(i).getImageName()), xPos,
					ITEM_SIZE / 2, ITEM_SIZE, ITEM_SIZE);
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	/**
	 * Returns the item at the clicked position, or null if the click was not on
	 * a valid item.
	 *
	 * @param x
	 *            The clicked x location.
	 * @param y
	 *            The clicked y location.
	 * @return The item clicked on or null if no item.
	 */
	public Item getItemAt(int x, int y) {
		// first check the y position is within the bounds
		if (TOP_PADDING_ITEMS <= y && y <= (TOP_PADDING_ITEMS + ITEM_SIZE)) {

			// for each item
			for (int i = 0; i < items.size(); i++) {

				// compute the x position of the item
				int xPos = (int) ((i + 1) / (float) (items.size() + 1) * Canvas.TARGET_WIDTH);

				// check if the x position is within bounds of the current item
				if ((xPos - ITEM_SIZE / 2) <= x && x <= (xPos + ITEM_SIZE / 2)) {
					return items.get(i);
				}
			}
		}

		// no item found at given position
		return null;
	}

	public SolidContainer getContainerAt(int x, int y) {
		// if a container currently exists
		if (container != null) {

			// first check the y position is within the bounds
			if ((TOP_PADDING_ITEMS - CONTAINER_SIZE / 2) <= y
					&& y <= (TOP_PADDING_ITEMS + CONTAINER_SIZE / 2)) {

				// check the x position is within the bounds
				if (Canvas.TARGET_WIDTH / 2 - CONTAINER_SIZE / 2 <= x
						&& x <= Canvas.TARGET_WIDTH / 2 + CONTAINER_SIZE / 2) {
					return container;
				}
			}
		}

		// no container exists or found at given position
		return null;
	}
}
