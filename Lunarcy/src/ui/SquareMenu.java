package ui;

import java.awt.event.MouseEvent;

import game.Direction;
import game.Entity;
import game.GameState;
import game.Item;
import game.Square;
import game.WalkableSquare;

/**
 * This us the menu which displays
 * all the items available to be picked up
 * in a given square. If there are no
 * items in a square it will just display a blank menu
 *
 * @author evansben1
 *
 */
public class SquareMenu extends Menu {


	public SquareMenu(Canvas p, GameState gameState, int playerID, String[] buttons) {
		super(p, gameState, playerID, "Pickup", buttons);
		p.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//TODO: PICKUP ITEM AT CLICKED LOCATION
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
