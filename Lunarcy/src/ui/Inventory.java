package ui;

import java.util.List;

import game.GameState;
import game.Item;
import game.Player;
import processing.core.PApplet;

/**
 * Displays the oxygen bar in the bottom right corner of the canvas
 *
 * @author b
 *
 */
public class Inventory extends DrawingComponent {

	// Where the oxygen bar starts (x)
	private final int LEFT_PADDING = (int) (p.width * 0.1);
	// Where the oxygen bar starts (y)
	private final int TOP_PADDING = (int) (p.height * 0.8);

	private final int SIZE = 15;

	public Inventory(PApplet p, GameState gameState, int playerID) {
		super(p, gameState, playerID);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// get the player from the current game state
		Player player = gameState.getPlayer(playerID);

		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw all the players items
		List<Item> inventory = player.getInventory();

		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				// TEMP: While we discuss images for items
				p.rect(i * SIZE, 0, SIZE, SIZE);
			}
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

}
