package ui.ApplicationWindow;

import java.util.ArrayList;
import java.util.Map;

import game.Container;
import game.GameState;
import game.Item;
import game.Player;
import game.SolidContainer;
import game.WalkableSquare;
import processing.core.PImage;
import ui.renderer.Canvas;

/**
 * Displays all the items in the selected container, in a bar which draws inside
 * the container.
 *
 * @author evansben1
 *
 */
public class ContainerView extends Bar {

	private SolidContainer container;

	public ContainerView(Canvas p, GameState gameState, int playerID,
			Map<String, PImage> entityImages) {

		super((int) (Canvas.TARGET_WIDTH * 0.325),
				Canvas.TARGET_HEIGHT / 2 - 50, gameState.getPlayer(playerID)
						.getInventory(), p, gameState, playerID, entityImages);

		items = new ArrayList<Item>();
	}

	@Override
	public void draw(GameState gameState, float delta) {

		// Set the container based on the players current square
		Player player = gameState.getPlayer(playerID);
		WalkableSquare square = (WalkableSquare) gameState.getSquare(player
				.getLocation());
		container = square.getContainer(player.getOrientation());

		// Dont draw if there is no container, or if the container is shut
		if (container == null || !container.isOpen()) {
			return;
		}

		//Get thew items from within the container
		items = container.getItems();

		super.draw(gameState, delta);

	}

	public Container getContainer() {
		return container;
	}

}
