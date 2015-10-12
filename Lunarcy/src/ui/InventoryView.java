package ui;

import java.util.List;
import java.util.Map;

import game.GameState;
import game.Item;
import processing.core.PImage;

/**
 * Displays an Inventory bar in the Bottom left corner
 * holding all the items which the current player has in their
 * inventory.
 *
 * @author Ben
 *
 */
public class InventoryView extends Bar {


	public InventoryView(Canvas p, GameState gameState, int playerID, Map<String, PImage> entityImages) {
		super(25, (int) (Canvas.TARGET_HEIGHT * 0.85), gameState.getPlayer(playerID).getInventory(), p, gameState, playerID, entityImages);

		items = gameState.getPlayer(playerID).getInventory();
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// Get all the players items
		items = gameState.getPlayer(playerID).getInventory();

		super.draw(gameState, delta);

	}


}
