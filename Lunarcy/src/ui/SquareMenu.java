package ui;

import java.awt.event.MouseEvent;

import game.Direction;
import game.Entity;
import game.GameState;
import game.Item;
import game.Key;
import game.Player;
import game.Square;
import game.WalkableSquare;

/**
 * This us the menu which displays all the items available to be picked up in a
 * given square. If there are no items in a square it will just display a blank
 * menu
 *
 * @author evansben1
 *
 */
public class SquareMenu extends Menu {

	private boolean shouldDraw;
	private WalkableSquare square;

	public SquareMenu(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID, "Pickup", null);
		p.addMouseListener(this);
		shouldDraw = false;
		update(gameState);
	}

	public void shouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}
	
	public boolean shouldDraw() {
		return shouldDraw;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		if (shouldDraw) {
			update(gameState);
			super.draw(gameState, delta);
		}
	}
	
	private void update(GameState gameState){
		Player player = gameState.getPlayer(playerID);
		square = (WalkableSquare) gameState.getSquare(player.getLocation());
		
		Direction dir = player.getOrientation();
		
		// TESTCODE
		square.addEntity(dir, new Key(1, 1));
		square.addEntity(dir, new Key(2, 2));
		square.addEntity(dir, new Key(3, 3));
		
		String[] buttons = new String[square.getEntities(dir).size()];
		int i = 0;
		for (Entity entity : square.getEntities(dir)) {
			buttons[i] = entity.getImageName();
			i++;
		}
		
		updateButtons(buttons);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		shouldDraw = !shouldDraw;
		
		if (onMenu(x, y)) {
			// Gets the ID of the clicked item
			int clickedID = getIndexClicked(x, y);
						
			// If it was a valid ID
			if (clickedID >= 0) {
				p.pickupItem(clickedID);
				shouldDraw = false;
			}

		}
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
