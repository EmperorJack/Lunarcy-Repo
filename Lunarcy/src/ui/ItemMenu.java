package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.Container;
import game.Entity;
import game.GameState;
import game.Item;
import game.Player;
import game.Square;
import game.WalkableSquare;

public class ItemMenu extends Menu implements MouseListener {

	private Item item;
	private final static String[] buttons = new String[] { "Drop item",
			"Put item" };

	private GameState gameState;

	public ItemMenu(Canvas p, GameState gameState, int playerID, Item item) {
		super(p, gameState, playerID, item.getName(), buttons);
		p.addMouseListener(this);
		this.gameState = gameState;
		this.item = item;
	}


	/**
	 * THIS SHOULD BE ELSEWHERE.
	 * Returns the ID of the container in the players direction,
	 * or null if it is empty.
	 * @return
	 */
	private int findContainerID(){
		Player player = gameState.getPlayer(playerID);
		WalkableSquare square = (WalkableSquare)gameState.getSquare(player.getLocation());
		square.hasContainer(player.getOrientation());

		for(Entity entity : square.getEntities(player.getOrientation())){
			if (entity instanceof Container) {
				return entity.entityID;
			}
		}

		return -1;
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
					p.dropItem(item.entityID);
					item = null;
					break;
				case "Put item":
					p.putItem(item.entityID, findContainerID());
					item = null;
					break;
				}
				
				//Hide the menu, as we have made a selection
				p.setMenu(null);
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
