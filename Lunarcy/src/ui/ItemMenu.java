package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.GameState;
import game.Item;

public class ItemMenu extends Menu {

	private Item item;
	private final static String[] buttons = new String[] { "Drop item",
			"Put in container" };

	public ItemMenu(Canvas p, GameState gameState, int playerID, Item item) {
		super(p, gameState, playerID, item.getName(), buttons);
		p.addMouseListener(this);
	}

	public void updateItem(Item item) {
		this.item = item;
		if (item != null) {
			updateTitle(item.getName());
		}
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// Only draw if there is an item
		if (item != null) {
			super.draw(gameState, delta);
		}
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
					System.out.println("Dropping");
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
