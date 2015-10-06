package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.GameState;
import game.Item;

public class ItemMenu extends DrawingComponent implements MouseListener {

	// Menu alignment and sizing
	private final int MENU_SIZE, MENU_LEFT_PADDING, MENU_TOP_PADDING,
			MENU_BUTTON_HEIGHT;
	private String[] buttons = new String[] { "Drop item", "Put in container"};
	private final int ITEM_SPACING;
	private Item item;

	public ItemMenu(Canvas p, GameState gameState, int playerID, Item item) {
		super(p, gameState, playerID);

		this.item = item;
		MENU_SIZE = 300;
		MENU_LEFT_PADDING = p.width / 2 - MENU_SIZE / 2;
		MENU_TOP_PADDING = p.height / 2 - MENU_SIZE / 2;
		MENU_BUTTON_HEIGHT = 50;
		ITEM_SPACING = 20;

		p.addMouseListener(this);
	}

	public void updateItem(Item item) {
		this.item = item;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		if (item == null) return;

		// Display the Item options menu if an item has been clicked
		p.pushMatrix();

		// Translate to top left of our menu
		p.translate(MENU_LEFT_PADDING, MENU_TOP_PADDING);

		p.fill(0, 0, 0, 200);
		p.rect(0, 0, MENU_SIZE, MENU_SIZE);
		p.fill(255, 255, 255, 100);
		p.text(item.toString(), 0, 0, MENU_SIZE, MENU_BUTTON_HEIGHT);

		//Draw all the buttons
		for(int i=0; i<buttons.length; i++){
			p.fill(255, 255, 255, 100);

			//Draw the button background
			p.rect(0, (MENU_BUTTON_HEIGHT + ITEM_SPACING)*(i+1), MENU_SIZE,
					MENU_BUTTON_HEIGHT);

			p.fill(0, 0, 0, 100);

			//Draw the button text
			p.text(buttons[i], 0, (MENU_BUTTON_HEIGHT + ITEM_SPACING)*(i+1), MENU_SIZE,
					MENU_BUTTON_HEIGHT);

		}


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

		// Bound check
		if (index < 0 || index >= buttons.length) {
			return null;
		}

		return buttons[index];
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (onMenu(x, y)) {

			String button = getButtonClicked(x, y);
			if (button != null) {
				switch (button) {
				case "Drop item":
					p.dropItem(item.entityID);
					item = null;
					break;
				}
			}

		}

	}

	/* Unused methods for MouseListener */
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
