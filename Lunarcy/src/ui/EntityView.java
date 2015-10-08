package ui;

import java.util.List;
import java.util.Map;

import game.Entity;
import game.GameState;
import game.Player;
import processing.core.PImage;

/**
 * Displays the entities (containers and items) that can be interacted with from
 * the current player perspective.
 *
 * @author Jack
 *
 */
public class EntityView extends DrawingComponent {

	// entity drawing fields
	private final int ENTITY_SIZE = 100;
	private final int TOP_PADDING = 500;

	// all possible entity images
	private final Map<String, PImage> ENTITY_IMAGES;

	public EntityView(Canvas p, GameState gameState, int playerID,
			Map<String, PImage> ENTITY_IMAGES) {
		super(p, gameState, playerID);
		this.ENTITY_IMAGES = ENTITY_IMAGES;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// get the player from the current game state
		Player thisPlayer = gameState.getPlayer(playerID);

		// get the entities in the current square for the player direction
		List<Entity> entities = gameState.getSquare(thisPlayer.getLocation())
				.getEntities(thisPlayer.getOrientation());

		// get the number of entities in this list
		int numberEntities = entities.size();

		// check there is at least one entity to draw
		if (numberEntities >= 1) {

			// translate to allow for top padding
			p.translate(0, TOP_PADDING);

			// p.fill(255, 0, 0);
			// p.rect(0, 0, p.width, p.height);
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}
