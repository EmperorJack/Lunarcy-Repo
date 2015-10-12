package ui;

import game.GameState;
import game.Player;
import game.ShipPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * This overlay appears in the top right corner of the canvas, showing which
 * items are necessary to fix the ship. Highlighting the ones you must still
 * collect, and graying out the ones you have already collected.
 *
 * @author Ben
 *
 */
public class ObjectiveView extends DrawingComponent {

	// How far in from the left (x axis)
	private final int LEFT_PADDING = (int) (Canvas.TARGET_WIDTH * 0.8);

	// How far down from the top (y axis)
	private final int TOP_PADDING = 25;

	// The height each item occupies in the panel
	private final int ITEM_SIZE = 50;

	// All the items which the player must collect to win
	private final Set<ShipPart> NEEDED_ITEMS;

	// Sizing for this panel
	private final float WIDTH;
	private final float HEIGHT;

	// Preload our images for drawing
	private final Map<String, PImage> ENTITY_IMAGES;

	public ObjectiveView(Canvas p, GameState gameState, int playerID,
			Map<String, PImage> entityImages) {
		super(p, gameState, playerID);

		gameState.getShip().testAddRequireditems();

		// The needed items are all the ships missing parts
		NEEDED_ITEMS = gameState.getShip().getParts();

		// Height is based on amount of items needed, + room for the heading
		HEIGHT = ITEM_SIZE * (NEEDED_ITEMS.size() + 1);

		// Width is fixed
		WIDTH = 200;

		ENTITY_IMAGES = entityImages;

	}

	@Override
	public void draw(GameState gameState, float delta) {

		// Retrieve the player inventory to cross check their items to the
		// needed items
		Player player = gameState.getPlayer(playerID);
		List<ShipPart> shipParts = new ArrayList<ShipPart>();

		if (player != null) {
			shipParts = player.getShipParts();
		}

		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the background
		p.fill(0, 0, 0, 100);
		p.rect(0, 0, WIDTH, HEIGHT);

		// Draw the title
		p.fill(255, 255, 255, 200);
		p.textSize(15);
		p.textAlign(PApplet.CENTER);
		p.text("Parts needed", 0, 0, WIDTH, ITEM_SIZE);

		p.textSize(15);
		p.textAlign(PApplet.LEFT, PApplet.CENTER);

		int i = 1;
		for (ShipPart neededPart : NEEDED_ITEMS) {
			p.noTint();

			boolean hasPart = false;

			// if the player has an item, gray it out
			for (ShipPart shipPart : shipParts) {
				if (shipPart.getTypeID() == neededPart.getTypeID()) {
					hasPart = true;
					break;
				}
			}

			//If they do not have the part, gray it out
			if(!hasPart){
				p.tint(127, 200);
			}


			p.image(ENTITY_IMAGES.get(neededPart.getImageName()), 0, ITEM_SIZE * i, ITEM_SIZE,
					ITEM_SIZE);

			p.text(neededPart.getName(), ITEM_SIZE, ITEM_SIZE * i + (ITEM_SIZE / 2));

			i++;
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}
}