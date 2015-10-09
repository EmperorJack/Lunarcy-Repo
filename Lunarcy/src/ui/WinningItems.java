package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import game.Direction;
import game.EmptyWall;
import game.Entity;
import game.GameState;
import game.Player;
import game.Square;
import game.WalkableSquare;
import game.Wall;
import processing.core.*;

/**
 * This overlay appears in the top right
 * corner of the canvas, showing which
 * items are necessary to fix the ship.
 * Highlighting the ones you must
 * still collect, and graying out the ones
 * you have already collected.
 *
 * @author Ben
 *
 */
public class WinningItems extends DrawingComponent {

	// How far in from the left (x axis)
	private final int LEFT_PADDING = (int) (p.width*0.8);
	// How far down from the top (y axis)
	private final int TOP_PADDING = 25;

	//The height each item occupies in the panel
	private final int ITEM_SIZE = 50;

	//All the items which the player must collect to win
	private final List<Entity> NEEDED_ITEMS;	

	// Sizing for this panel
	private final float WIDTH;
	private final float HEIGHT;
	
	private final Map<String, PImage> ENTITY_IMAGES;

	public WinningItems(Canvas p, GameState gameState, int playerID, Map<String, PImage> entityImages) {
		super(p, gameState, playerID);
		
		gameState.getShip().testAddRequireditems();
		
		NEEDED_ITEMS = new ArrayList<>(gameState.getShip().getParts());
		
		//Height is based on amount of items needed, + room for the heading
		HEIGHT = ITEM_SIZE*(NEEDED_ITEMS.size()+1);
		
		//Width is fixed
		WIDTH = 200;
		
		ENTITY_IMAGES = entityImages;
		
	}

	@Override
	public void draw(GameState gameState, float delta) {

		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);
		
		//Draw the background
		p.fill(0,0,0,100);
		p.rect(0, 0, WIDTH, HEIGHT);
		
		//Draw the title
		p.fill(255,255,255,200);
		p.textSize(15);
		p.textAlign(p.CENTER);
		p.text("Parts needed", 0, 0, WIDTH, ITEM_SIZE);
		
		p.textSize(15);
		p.textAlign(p.LEFT, p.CENTER);

		int i = 1;
		for(Entity entity: NEEDED_ITEMS){
			p.noTint();
			
			String name = entity.getImageName();
			
			//if the current player has the item, tint it
			if(gameState.getPlayer(playerID)!=null && gameState.getPlayer(playerID).getInventory().contains(entity)){
				p.tint(0, 126); 
			}
			
			p.image(ENTITY_IMAGES.get(name), 0, ITEM_SIZE*i, ITEM_SIZE, ITEM_SIZE);
			
			
			p.text(name, ITEM_SIZE, ITEM_SIZE*i+(ITEM_SIZE/2));
			
			
			i++;
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}