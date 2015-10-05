package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import game.GameState;
import game.Item;
import game.Player;

/**
 * Displays the oxygen bar in the bottom right corner of the canvas
 *
 * @author b
 *
 */
public class Inventory extends DrawingComponent implements MouseListener {

	// Where the Inventory bar starts (x)
	private final int LEFT_PADDING = 25;
	// Where the Inventory bar starts (y)
	private final int TOP_PADDING = (int) (p.height * 0.8);
	private final int INVENTORY_WIDTH = (int) (p.width * 0.3);

	// Size of each item
	private final int SIZE = 35;
	// Space between each item
	private final int SPACING = 20;

	private final int MENU_SIZE = 300;
	private boolean drawMenu;

	private List<Item> inventory;

	public Inventory(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);
		inventory = gameState.getPlayer(playerID).getInventory();

		p.addMouseListener(this);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// Get all the players items
		inventory = gameState.getPlayer(playerID).getInventory();

		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the background
		p.fill(0, 0, 0, 100);
		p.rect(0, 0, INVENTORY_WIDTH, SIZE);

		p.noFill();

		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				// TEMP: While we discuss images for items
				p.image(p.loadImage("/assets/items/"
						+ inventory.get(i).imageName + ".png"), i * SIZE
						+ SPACING, 0, SIZE, SIZE);
			}
		}

		if (drawMenu) {
			drawMenu();
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
	 *            : The clicked x location
	 * @param y
	 *            : The clicked y location
	 * @return
	 */
	private Item getItemAt(int x, int y) {
		// Width of each item
		int width = SIZE + SPACING;
		// Where the player clicked
		int clickedX = x - LEFT_PADDING;
		// Which item this corresponds to in our Set
		int index = clickedX / width;

		// Make sure our index is within bounds
		if (index < 0 || index > inventory.size()) {
			return null;
		}

		return inventory.get(index);
	}

	private void drawMenu() {
		p.pushMatrix();

		p.translate(-LEFT_PADDING, -TOP_PADDING); // Back to 0,0
		p.fill(0, 0, 0, 100);
		p.rect(p.width / 2 - MENU_SIZE / 2, p.height / 2 - MENU_SIZE / 2, MENU_SIZE, MENU_SIZE);

		p.popMatrix();
	}

	/**
	 * Returns true if the coordinates entered are inside the inventory.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean withinBounds(int x, int y) {
		return x > LEFT_PADDING && x < LEFT_PADDING + INVENTORY_WIDTH && y > TOP_PADDING && y < TOP_PADDING + SIZE;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Get the mouses coordinates
		int x = e.getX();
		int y = e.getY();

		// Only find the item if the click was on the inventory
		if (withinBounds(x, y)) {
			Item item = getItemAt(x, y);

			if (item != null) {
				// Show display
				drawMenu = true;
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
