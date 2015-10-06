package ui;

import game.GameState;
import game.Item;

import java.awt.event.MouseListener;

/**
 * Displays a menu in the middle pof the screen. This should not be instantiated
 *
 * @author evansben1
 *
 */
public abstract class Menu extends DrawingComponent implements MouseListener {

	// Menu alignment and sizing
	private final int MENU_SIZE, MENU_LEFT_PADDING, MENU_TOP_PADDING, MENU_BUTTON_HEIGHT;

	private String[] buttons;

	private final int ITEM_SPACING;
	// Displayed at top of menu
	private String title;

	public Menu(Canvas p, GameState gameState, int playerID, String title, String[] buttons) {
		super(p, gameState, playerID);

		this.title = title;
		this.buttons = buttons;

		MENU_SIZE = 300;
		MENU_LEFT_PADDING = p.width / 2 - MENU_SIZE / 2;
		MENU_TOP_PADDING = p.height / 2 - MENU_SIZE / 2;
		MENU_BUTTON_HEIGHT = 50;
		ITEM_SPACING = 20;
	}
	
	protected void updateButtons(String[] buttons){
		this.buttons = buttons;
	}

	protected void updateTitle(String title) {
		this.title = title;
	}

	/**
	 * Draws the menu on centre screen
	 */

	@Override
	public void draw(GameState gameState, float delta) {

		// Display the Item options menu if an item has been clicked
		p.pushMatrix();

		// Translate to top left of our menu
		p.translate(MENU_LEFT_PADDING, MENU_TOP_PADDING);

		p.fill(0, 0, 0, 200);
		p.rect(0, 0, MENU_SIZE, MENU_SIZE);
		p.fill(255, 255, 255, 100);
		p.text(title, 0, 0, MENU_SIZE, MENU_BUTTON_HEIGHT);

		// Draw all the buttons
		for (int i = 0; i < buttons.length; i++) {
			p.fill(255, 255, 255, 100);

			// Draw the button background
			p.rect(0, (MENU_BUTTON_HEIGHT + ITEM_SPACING) * (i + 1), MENU_SIZE, MENU_BUTTON_HEIGHT);

			p.fill(0, 0, 0, 100);

			// Draw the button text
			p.text(buttons[i], 0, (MENU_BUTTON_HEIGHT + ITEM_SPACING) * (i + 1), MENU_SIZE, MENU_BUTTON_HEIGHT);
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
	protected boolean onMenu(int x, int y) {
		return x > MENU_LEFT_PADDING && x < MENU_LEFT_PADDING + MENU_SIZE && y > MENU_TOP_PADDING
				&& y < MENU_TOP_PADDING + MENU_SIZE;
	}

	/**
	 * Returns the title of the clicked button, or null if the click was not on
	 * a valid button. If you need the index of a button, use
	 * getIndexClicked(x,y) instead.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected String getButtonClicked(int x, int y) {
		// Size of each button
		int height = MENU_BUTTON_HEIGHT + ITEM_SPACING;
		// Where the player clicked
		int clickedY = y - MENU_TOP_PADDING;

		// The index of the button in our array
		int index = clickedY / height;

		// The top index is not a button just the title, so skip it
		index--;

		// Bound check
		if (index < 0 || index >= buttons.length) {
			return null;
		}

		return buttons[index];
	}

	/**
	 * Returns the index of the button clicked, or -1 if it was an invalid
	 * click. If you need the Text from a button, use getButtonClicked(x,y)
	 * instead.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected int getIndexClicked(int x, int y) {
		// Size of each button
		int height = MENU_BUTTON_HEIGHT + ITEM_SPACING;
		// Where the player clicked
		int clickedY = y - MENU_TOP_PADDING;

		// The index of the button in our array
		int index = clickedY / height;

		// The top index is not a button just the title, so skip it
		index--;

		// Bound check
		if (index < 0 || index >= buttons.length) {
			return -1;
		}

		return index;
	}

}
