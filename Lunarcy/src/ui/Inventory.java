package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import game.GameState;
import game.Item;
import game.Player;

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

	// Menu alignment and sizing
	private final int MENU_SIZE, MENU_LEFT_PADDING, MENU_TOP_PADDING,
			MENU_BUTTON_HEIGHT;
	private boolean drawMenu;
	private Item chosenItem;
	private String[] buttons = new String[] { "Drop item" };

	private List<Item> inventory;

	public Inventory(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);
		inventory = gameState.getPlayer(playerID).getInventory();

		LEFT_PADDING = 25;
		TOP_PADDING = (int) (p.height * 0.8);
		INVENTORY_WIDTH = (int) (p.width * 0.3);
		ITEM_SIZE = 35;
		ITEM_SPACING = 20;

		MENU_SIZE = 300;
		MENU_LEFT_PADDING = p.width / 2 - MENU_SIZE / 2;
		MENU_TOP_PADDING = p.height / 2 - MENU_SIZE / 2;
		MENU_BUTTON_HEIGHT = 50;

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
		p.rect(0, 0, INVENTORY_WIDTH, ITEM_SIZE);

		p.noFill();

		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				// TEMP: While we discuss images for items
				p.image(p.loadImage("/assets/items/"
						+ inventory.get(i).getImageName() + ".png"), i * ITEM_SIZE
						+ ITEM_SPACING, 0, ITEM_SIZE, ITEM_SIZE);
			}
		}

		// Display the Item options menu if an item has been clicked
		if (drawMenu && chosenItem != null) {
			drawMenu();
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	private void drawMenu() {
		p.pushMatrix();

		// Translate Back to 0,0
		p.translate(-LEFT_PADDING, -TOP_PADDING);

		// Translate to top left of our menu
		p.translate(MENU_LEFT_PADDING, MENU_TOP_PADDING);

		p.fill(0, 0, 0, 100);
		p.rect(0, 0, MENU_SIZE, MENU_SIZE);
		p.fill(255,255,255,100);
		p.text(chosenItem.toString(), 0, 0, MENU_SIZE, MENU_BUTTON_HEIGHT);

		p.rect(0, MENU_BUTTON_HEIGHT + ITEM_SPACING, MENU_SIZE,
				MENU_BUTTON_HEIGHT);

		p.fill(0, 0, 0, 100);
		p.text("Drop item", 0, MENU_BUTTON_HEIGHT + ITEM_SPACING, MENU_SIZE,
				MENU_BUTTON_HEIGHT);

		p.popMatrix();
	}

	/**
	 * Returns true the specified x,y is on the menu, false if not.
	 *
	 * @param x
	 *            : The clicked x location
	 * @param y
	 *            : The clicked y location
	 * @return
	 */
	private boolean onMenu(int x, int y) {
		return x > MENU_LEFT_PADDING && x < MENU_LEFT_PADDING + MENU_SIZE
				&& y > MENU_TOP_PADDING && y < MENU_TOP_PADDING + MENU_SIZE;
	}

	private String getButtonClicked(int x, int y) {
		// Size of each button
		int height = MENU_BUTTON_HEIGHT + ITEM_SPACING;
		// Where the player clicked
		int clickedY = y - MENU_TOP_PADDING;

		// The index of the button in our array
		int index = clickedY / height;

		// The top index is not a button jsut the title, so skip it
		index--;

		//Bound check
		if(index < 0 || index >= buttons.length){
			return null;
		}

		return buttons[index];
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

			// If we reclick an item, hide the menu
			if (item != null && item.equals(chosenItem)) {
				chosenItem = null;
				drawMenu = false;
				System.out.println("Hiding menu");
			}
			// Else show the menu
			else if (item != null) {
				System.out.println("Showing menu");
				chosenItem = item;
				drawMenu = true;
			}
		} else if (onMenu(x, y)) {

			String button = getButtonClicked(x, y);
			if(button!=null){

				switch(button){
					case "Drop item":
						p.dropItem(chosenItem.entityID);
						drawMenu = false;
						break;
				}

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
