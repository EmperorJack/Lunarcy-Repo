package ui;

import java.util.List;
import java.util.Map;

import processing.core.PImage;
import game.GameState;
import game.Item;

public abstract class Bar extends DrawingComponent {

	// The bars alignment and sizing
	protected int LEFT_PADDING, TOP_PADDING, BAR_WIDTH;
	protected final int ITEM_SIZE;
	protected final int ITEM_SPACING;

	//Which items the bar contains
	protected List<Item> items;

	// So we can preload all our images based on the entitys name
	protected final Map<String, PImage> ENTITY_IMAGES;

	public Bar(int leftPadding, int topPadding, List<Item> items, Canvas p, GameState gameState, int playerID, Map<String, PImage> entityImages) {
		super(p, gameState, playerID);

		LEFT_PADDING = leftPadding;
		TOP_PADDING = topPadding;
		BAR_WIDTH = (int) (Canvas.TARGET_WIDTH * 0.3);
		ITEM_SIZE = 50;
		ITEM_SPACING = 10;
		ENTITY_IMAGES = entityImages;

		this.items = items;

	}

	/**
	 * Returns the item at the clicked position, or null if the click was not on
	 * a valid item.
	 *
	 * @param x
	 *            : The clicked x location
	 * @param y
	 *            : The clicked y location
	 * @return
	 */
	public Item getItemAt(int x, int y) {

		// Width of each item
		int width = ITEM_SIZE + ITEM_SPACING;

		// Where the player clicked
		int clickedX = x - LEFT_PADDING;

		// Which item this corresponds to in our Set
		int index = clickedX / width;

		// Make sure our index is within bounds
		if (index < 0 || index >= items.size()) {
			return null;
		}

		//If it was within bounds, return the value at the corresponding index
		return items.get(index);
	}

	/**
	 * Returns true if the coordinates entered are inside the
	 * inventory bar.
	 *
	 * @param x
	 * @param y
	 * @return True if the given position on the inventory bar.
	 */
	public boolean onBar(int x, int y) {
		return x > LEFT_PADDING && x < LEFT_PADDING + BAR_WIDTH
				&& y > TOP_PADDING && y < TOP_PADDING + ITEM_SIZE;
	}


}
