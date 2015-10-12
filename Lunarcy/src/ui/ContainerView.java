package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import game.Container;
import game.GameState;
import game.Item;
import processing.core.PImage;

/**
 * Displays all the items in the selected
 * container, in the top centre of the screen.
 *
 * @author Ben
 *
 */
public class ContainerView extends Bar {

	private Container container;

	public ContainerView(Canvas p, GameState gameState, int playerID, Map<String, PImage> entityImages) {
		super((int)(Canvas.TARGET_WIDTH * 0.3) , 50, gameState.getPlayer(playerID).getInventory(), p, gameState, playerID, entityImages);

		items = new ArrayList<Item>();
	}

	/**
	 * Changes the current container
	 * to a new one
	 */

	public void updateContainer(Container container){
		this.container = container;
	}

	@Override
	public void draw(GameState gameState, float delta) {

		//Dont draw if there is no container set
		if(container==null){
			return;
		}

		items = container.getItems();


		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the background
		p.fill(0, 0, 0, 100);
		p.rect(0, 0, BAR_WIDTH, ITEM_SIZE);

		p.noFill();

		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				p.image(ENTITY_IMAGES.get(items.get(i).getImageName()), i
						* (ITEM_SIZE + ITEM_SPACING), 0, ITEM_SIZE, ITEM_SIZE);
			}
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();

	}


}
