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
public class Inventory extends DrawingComponent{

	// Where the Inventory bar starts (x)
	private final int LEFT_PADDING = 25;
	// Where the Inventory bar starts (y)
	private final int TOP_PADDING = (int) (p.height * 0.8);

	//Size of each item
	private final int SIZE = 35;
	//Space between each item
	private final int SPACING = 20;

	private Player player;
	private int playerID;

	public Inventory(int playerID, PApplet p, GameState gameState) {
		super(p, gameState);

		// set the initial game state
		update(gameState);
	}

	@Override
	public void update(GameState gameState) {
		player = gameState.getPlayer(playerID);
	}

	@Override
	public void draw(float delta) {
		p.pushMatrix();
		p.pushStyle();

		p.stroke(1);

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		//Draw all the players items
		List<Item> inventory = player.getInventory();
		
		p.textSize(SIZE);
		p.noFill();

		if(inventory !=null){
			for(int i=0; i<inventory.size(); i++){
				//TEMP: While we discuss images for items
				p.rect(i*SIZE+SPACING, 0, SIZE, SIZE);
				
				//Draw the first character of item name (TEMPORARY)
				p.text(inventory.get(i).imageName.charAt(0), i*SIZE+SPACING, SIZE);
			}
		}

		// pop matrix and style information from the stack	
		p.popStyle();
		p.popMatrix();
	}

}
