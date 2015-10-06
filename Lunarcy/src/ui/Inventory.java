package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import game.GameState;
import game.Item;

/**
 * Displays an Inventory bar in the Bottom left corner, when items are clicked
 * displays a menu in center screen.
 *
 * @author Ben
 *
 */
public class Inventory extends DrawingComponent implements MouseListener {

	// Inventory bar alignment and sizing
	private int LEFT_PADDING, TOP_PADDING, INVENTORY_WIDTH;
	private final int ITEM_SIZE;
	private final int ITEM_SPACING;
	private ItemMenu menu;
	private GameState gamestate;
	private Item lastChosen;

	private List<Item> inventory;

	public Inventory(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);

		this.gamestate = gameState;
		inventory = gameState.getPlayer(playerID).getInventory();
		LEFT_PADDING = 25;
		TOP_PADDING = (int) (p.height * 0.8);
		INVENTORY_WIDTH = (int) (p.width * 0.3);
		ITEM_SIZE = 35;
		ITEM_SPACING = 20;

		p.addMouseListener(this);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// Get all the players items
		this.gamestate = gameState;
		inventory = gameState.getPlayer(playerID).getInventory();

		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the background
		p.fill(0, 0, 0, 100);
		p.rect(0, 0, INVENTORY_WIDTH, ITEM_SIZE);

		p.noFill();

		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				// TEMP: While we discuss images for items
				p.image(p.loadImage("/assets/items/"
						+ inventory.get(i).imageName + ".png"), i * ITEM_SIZE
						+ ITEM_SPACING, 0, ITEM_SIZE, ITEM_SIZE);
			}
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();

		if(menu != null){
			menu.draw(gameState, delta);
		}
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
		int width = ITEM_SIZE + ITEM_SPACING;
		// Where the player clicked
		int clickedX = x - LEFT_PADDING;
		// Which item this corresponds to in our Set
		int index = clickedX / width;

		// Make sure our index is within bounds
		if (index < 0 || index >= inventory.size()) {
			return null;
		}

		return inventory.get(index);
	}

	/**
	 * Returns true if the coordinates entered are inside the inventory.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean onInventoryBar(int x, int y) {
		return x > LEFT_PADDING && x < LEFT_PADDING + INVENTORY_WIDTH
				&& y > TOP_PADDING && y < TOP_PADDING + ITEM_SIZE;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Get the mouses coordinates
		int x = e.getX();
		int y = e.getY();

		// Only find the item if the click was on the inventory
		if (onInventoryBar(x, y)) {
			Item item = getItemAt(x, y);

			//If its the first time clicking, make a new menu
			if(menu==null){
				menu = new ItemMenu(p, gamestate, y, item);
				lastChosen = item;
			}
			//If they reclick an item, remove it from the menu
			else if(item!=null && item.equals(lastChosen)){
				menu.updateItem(null);
				lastChosen = null;
			}
			//If its different to our last item, update the menu
			else if(item!=null){
				menu.updateItem(item);
				lastChosen = item;
			}
		}

	}

	/* Unused MouseListener classes */
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
