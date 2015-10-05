package ui;

import java.util.List;

import game.GameState;
import game.Item;
import game.Player;

/**
 * Displays the oxygen bar in the bottom right corner of the canvas
 *
 * @author b
 *
 */
public class Inventory extends DrawingComponent {

	// Where the Inventory bar starts (x)
	private final int LEFT_PADDING = 25;
	// Where the Inventory bar starts (y)
	private final int TOP_PADDING = (int) (p.height * 0.8);

	// Size of each item
	private final int SIZE = 35;
	// Space between each item
	private final int SPACING = 20;

	public Inventory(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// get the player from the current game state
		Player player = gameState.getPlayer(playerID);

		p.pushMatrix();
		p.pushStyle();

		p.stroke(1);

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw all the players items
		List<Item> inventory = player.getInventory();

		p.textSize(SIZE);
		p.noFill();

		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				// TEMP: While we discuss images for items
				p.image(p.loadImage("/assets/items/"
						+ inventory.get(i).imageName + ".png"), i * SIZE
						+ SPACING, 0, SIZE, SIZE);
			}
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

}
