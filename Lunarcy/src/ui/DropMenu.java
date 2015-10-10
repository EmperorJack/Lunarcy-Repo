package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.GameState;
import game.Item;

public class DropMenu extends Menu implements MouseListener {

	private Item item;
	private final static String[] buttons = new String[] { "Drop item",
			"Put item" };

	private GameState gameState;

	public DropMenu(Canvas p, InteractionController entityController, GameState gameState, int playerID, Item item) {
		super(p, entityController, gameState, playerID, item.getName(), buttons);
		p.addMouseListener(this);
		this.gameState = gameState;
		this.item = item;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (onMenu(x, y) && item!=null) {

			System.out.println("In ");

			String button = getButtonClicked(x, y);
			if (button != null) {
				switch (button) {
					case "Drop item":
						entityController.dropItem(item.entityID);
						item = null;
						break;
					case "Put item":
						//TODO: Put
						item = null;
						break;
					}

				//Hide the menu, as we have made a selection
				entityController.setMenu(null);
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
