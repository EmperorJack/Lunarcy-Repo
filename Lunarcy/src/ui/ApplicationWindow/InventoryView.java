package ui.ApplicationWindow;

import java.util.Map;

import game.GameState;
import processing.core.PImage;
import ui.renderer.Canvas;

/**
 * Displays an Inventory bar in the Bottom left corner
 * holding all the items which the current player has in
 * their inventory.
 *
 * @author evansben1
 *
 */
public class InventoryView extends Bar {


	public InventoryView(Canvas p, GameState gameState, int playerID, Map<String, PImage> entityImages) {
		super(25, (int) (Canvas.TARGET_HEIGHT * 0.85), gameState.getPlayer(playerID).getInventory(), p, gameState, playerID, entityImages);

		items = gameState.getPlayer(playerID).getInventory();
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// Get the current players inventory
		items = gameState.getPlayer(playerID).getInventory();

		//Draw the bar with the updated items
		super.draw(gameState, delta);

	}


}
