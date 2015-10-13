package ui.ApplicationWindow;

import java.util.Map;

import game.Bag;
import game.Container;
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
public class BagView extends Bar {

	Bag selectedBag;

	public BagView(Canvas p, GameState gameState, int playerID, Map<String, PImage> entityImages) {
		super(25, (int) (Canvas.TARGET_HEIGHT * 0.75), gameState.getPlayer(playerID).getInventory(), p, gameState, playerID, entityImages);
	}

	/**
	 * Updates the selected bag, to
	 * be param bag.
	 *
	 * @param bag
	 */
	public void update(Bag bag){
		this.selectedBag = bag;
	}


	public Bag getBag(){
		return selectedBag;
	}

	@Override
	public void draw(GameState gameState, float delta) {

		//Dont draw if there's no bag
		if(selectedBag == null){
			return;
		}

		//Update the items based on bags contents
		items = selectedBag.getItems();

		//Draw the bar with the updated items
		super.draw(gameState, delta);

	}


}
