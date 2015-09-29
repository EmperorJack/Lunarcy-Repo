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

	private GameState gameState;
	private Player player;
	private int playerID;

	public Inventory(int playerID, PApplet p, GameState gameState) {
		super(p, gameState);

		// set the initial game state
		update(gameState);
	}

	@Override
	public void update(GameState gameState) {
		this.gameState = gameState;
		player = gameState.getPlayer(playerID);
	}

	@Override
	public void draw(float delta) {
		p.noStroke();

		p.pushMatrix();
		p.pushStyle();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);
		
		//Draw all the players items
		List<Item> inventory = player.getInventory();
		for(int i=0; i<inventory.size(); i++){
			//TEMP: While we discuss images for items
			p.rect(i*SIZE, 0, SIZE, SIZE);
		}
		
		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

}
