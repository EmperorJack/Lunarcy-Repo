package ui.ApplicationWindow;

import java.util.List;
import java.util.Map;

import processing.core.PImage;
import ui.DrawingComponent;
import ui.renderer.Canvas;
import game.GameState;
import game.Item;

/**
 * Represents a component which is drawn as a "bar", ie the Inventory Bar and
 * Container bar.
 *
 * Every bar contains a List<Items> to be drawn within the bar.
 *
 * @author evansben1
 *
 */
public abstract class Bar extends DrawingComponent {

	// The bars alignment and sizing
	protected int LEFT_PADDING, TOP_PADDING, BAR_WIDTH;

	// Sizing of the individual items within the bar
	protected final int ITEM_SIZE;
	protected final int ITEM_SPACING;

	// Which items the bar currently contains
	protected List<Item> items;

	// So we can preload all our images of the items to draw
	protected final Map<String, PImage> ENTITY_IMAGES;

	public Bar(int leftPadding, int topPadding, List<Item> items, Canvas p,
			GameState gameState, int playerID, Map<String, PImage> entityImages) {

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
	 * Draws the bar at LEFT_PADDING, TOP_PADDING, BAR_WIDTH, ITEM_SIZE (x,y,width,height)
	 * with the specified items.
	 */
	@Override
	public void draw(GameState gameState, float delta) {

		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the background
		p.fill(0, 0, 0, 100);
		p.rect(0, 0, BAR_WIDTH, ITEM_SIZE);

		// p.imageMode(PApplet.CENTER);

		p.noFill();

		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				p.image(ENTITY_IMAGES.get(items.get(i).getImageName()), i
						* (ITEM_SIZE + ITEM_SPACING), 0, ITEM_SIZE, ITEM_SIZE);
			}
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();

	}

	/**
	 * Returns the item at the clicked position, or null if the click
	 * was not on a valid item.
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

		// Where the player clicked on the bar
		int clickedX = x - LEFT_PADDING;

		// Which item this corresponds to in our List
		int index = clickedX / width;

		// Make sure our index is within bounds
		if (index < 0 || index >= items.size()) {
			return null;
		}

		// If it was within bounds, return the value at the corresponding index
		return items.get(index);
	}

	/**
	 * Returns whether the coordinates entered are inside
	 * the bar.
	 *
	 * @param x
	 * @param y
	 * @return True if the given position is on the inventory bar, otherwise false.
	 */
	public boolean onBar(int x, int y) {
		return x > LEFT_PADDING && x < LEFT_PADDING + BAR_WIDTH
				&& y > TOP_PADDING && y < TOP_PADDING + ITEM_SIZE;
	}

}
