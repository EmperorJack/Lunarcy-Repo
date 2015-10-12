package ui;

import java.util.List;
import java.util.Map;

import game.Container;
import game.Entity;
import game.GameState;
import game.Player;
import game.Item;
import game.Square;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Displays the entities (containers and items) that can be interacted with from
 * the current player perspective.
 *
 * @author Jack
 *
 */
public class EntityView extends DrawingComponent {

	// entity drawing fields
	private final int ITEM_SIZE = 200;
	private final int TOP_PADDING = 400;

	// all possible entity images
	private final Map<String, PImage> entityImages;

	// currently held items
	private List<Item> items;

	// currently held container
	private Container container;

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

		// if the container is existing
		// TODO DRAW CONTAINER

		// get the items in the current square for the player direction
		items = square.getItems(thisPlayer.getOrientation());

		// check there is at least one item to draw
		if (items.size() >= 1) {

			// translate to allow for top padding
			p.translate(0, TOP_PADDING);

			p.imageMode(PApplet.CENTER);

			// for each item
			for (int i = 0; i < items.size(); i++) {
				// compute the x position to place the image
				int xPos = (int) ((i + 1) / (float) (items.size() + 1) * Canvas.TARGET_WIDTH);

				// draw the item image
				p.image(entityImages.get(items.get(i).getImageName()), xPos,
						ITEM_SIZE / 2, ITEM_SIZE, ITEM_SIZE);
			}
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
	public Entity getItemAt(int x, int y) {
		// first check the y position is within the bounds of the entity view
		if (TOP_PADDING <= y && y <= (Canvas.TARGET_HEIGHT)) {

			// for each item
			for (int i = 0; i < items.size(); i++) {

				// compute the x position of the item
				int xPos = (int) ((i + 1) / (float) (items.size() + 1) * Canvas.TARGET_WIDTH);

				// check if the mouse position is within bounds of the item
				if ((xPos - ITEM_SIZE / 2) <= x && x <= (xPos + ITEM_SIZE / 2)) {
					return items.get(i);
				}
			}
		}

		// no item found at given position
		return null;
	}
}
