package ui;

import java.util.List;
import java.util.Map;

import game.Entity;
import game.GameState;
import game.Player;
import processing.core.PApplet;
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
	private final int ENTITY_SIZE = 200;
	private final int TOP_PADDING = 400;

	// all possible entity images
	private final Map<String, PImage> entityImages;

	// currently held entites
	private List<Entity> entities;

	public EntityView(Canvas p, GameState gameState, int playerID,
			Map<String, PImage> entityImages) {
		super(p, gameState, playerID);
		this.entityImages = entityImages;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// get the player from the current game state
		Player thisPlayer = gameState.getPlayer(playerID);

		// get the entities in the current square for the player direction
		entities = gameState.getSquare(thisPlayer.getLocation()).getEntities(
				thisPlayer.getOrientation());

		// check there is at least one entity to draw
		if (entities.size() >= 1) {

			// translate to allow for top padding
			p.translate(0, TOP_PADDING);

			p.imageMode(PApplet.CENTER);

			// for each entity
			for (int i = 0; i < entities.size(); i++) {
				// compute the x position to place the image
				int xPos = (int) ((i + 1) / (float) (entities.size() + 1) * Canvas.TARGET_WIDTH);

				// draw the entity image
				p.image(entityImages.get(entities.get(i).getImageName()), xPos,
						ENTITY_SIZE / 2, ENTITY_SIZE, ENTITY_SIZE);
			}
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	/**
	 * Returns the entity at the clicked position, or null if the click was not
	 * on a valid entity.
	 *
	 * @param x
	 *            The clicked x location.
	 * @param y
	 *            The clicked y location.
	 * @return The entity clicked on or null if no entity.
	 */
	public Entity getEntityAt(int x, int y) {
		// first check the y position is within the bounds of the entity view
		if (TOP_PADDING <= y && y <= (Canvas.TARGET_HEIGHT * p.getScaling())) {

			// for each entity
			for (int i = 0; i < entities.size(); i++) {

				// compute the x position of the entity
				int xPos = (int) ((i + 1) / (float) (entities.size() + 1) * Canvas.TARGET_WIDTH);

				// check if the mouse position is within bounds of the entity
				if ((xPos - ENTITY_SIZE / 2) <= x
						&& x <= (xPos + ENTITY_SIZE / 2)) {
					return entities.get(i);
				}
			}
		}

		// no entity found at given position
		return null;
	}
}
