package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.Direction;
import game.Entity;
import game.GameState;
import game.Player;
import game.WalkableSquare;

/**
 * This us the menu which displays all the items available to be picked up in a
 * given square. If there are no items in a square it will just display a blank
 * menu
 *
 * @author evansben1
 *
 */
public class PickMenu extends Menu implements MouseListener  {

	private WalkableSquare square;
	private GameState gameState;

	public PickMenu(Canvas p, InteractionController entityController, GameState gameState, int playerID) {
		super(p, entityController, gameState, playerID, "Pickup", null);
		p.addMouseListener(this);
		update(gameState);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		update(gameState);
		if (entityController.menuActive()) {
			super.draw(gameState, delta);
		}
	}

	private void update(GameState gameState){
		this.gameState = gameState;
		Player player = gameState.getPlayer(playerID);
		square = (WalkableSquare) gameState.getSquare(player.getLocation());
		super.updateButtons(square.getEntityNames(player.getOrientation()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (onMenu(x, y) && entityController.menuActive()) {
			// Gets the ID of the clicked item
			int clickedIndex = getIndexClicked(x, y);

			// If it was a valid ID
			if (clickedIndex >= 0) {

				//Get the ID from the squares items
				Direction dir = gameState.getPlayer(playerID).getOrientation();
				int clickedID = square.getEntities(dir).get(clickedIndex).entityID;

				entityController.pickupItem(clickedID);
				entityController.menuActive(false);
				return;
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
