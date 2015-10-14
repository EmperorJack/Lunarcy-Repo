package ui.renderer;

import processing.core.PApplet;
import processing.core.PImage;
import saito.objloader.OBJModel;
import saito.objtools.OBJTransform;
import game.GameState;

/**
 * Represents the skybox that rotates the game world. Has a skybox model,
 * essentially a sky textured cube and a separate sun image. The skybox rotates
 * relative to the time of the current gamestate.
 *
 * @author Jack
 *
 */
public class Skybox {

	// the parent processing canvas
	private Canvas p;

	// skybox fields
	private final OBJModel skyModel;
	private final PImage sun;
	private final PImage planet;
	private final int ORBIT_OFFSET;
	private final int ORBIT_OBJECT_SIZE;

	public Skybox(Canvas p, float MODEL_SCALE) {
		this.p = p;

		// load the obj model
		skyModel = new OBJModel(p, "assets/models/space_skybox.obj");

		// transformer to transform the skybox model correctly
		OBJTransform objectTransformer = new OBJTransform(p);
		objectTransformer.scaleOBJ(skyModel, MODEL_SCALE);
		skyModel.drawMode(OBJModel.POLYGON);

		// load the orbiting planet images
		sun = p.loadImage("/assets/skybox/osirion-flare-09.png");
		planet = p.loadImage("/assets/skybox/planet.png");

		// set the sun offset and size
		ORBIT_OFFSET = (int) (1000 * MODEL_SCALE);
		ORBIT_OBJECT_SIZE = (int) (500 * MODEL_SCALE);
	}

	/**
	 * Draw the skybox in the state provided by the given game state.
	 *
	 * @param gameState
	 */
	public void draw(GameState gameState) {
		p.pushMatrix();
		p.pushStyle();

		// get the current time from the game state
		int time = gameState.getTime();

		// compute the angle to rotate the skybox by
		float angle = PApplet.map(time, 0, 99, 0, PApplet.TWO_PI);

		// rotate the skybox relative to the current time
		p.rotateX(angle);

		// draw the skybox model
		skyModel.draw();

		// draw the sun image
		drawOrbitingObject(sun, ORBIT_OFFSET);

		// draw the planet image
		drawOrbitingObject(planet, -ORBIT_OFFSET);

		p.popStyle();
		p.popMatrix();
	}

	/**
	 * Draws an orbiting image (sun or planet) in front of the skybox/
	 *
	 * @param image
	 *            The image to draw.
	 * @param offset
	 *            The offset from the origin to draw the image.
	 */
	private void drawOrbitingObject(PImage image, int offset) {
		p.pushMatrix();

		// translate to the orbiting object position
		p.translate(0, 0, offset);

		p.imageMode(PApplet.CENTER);

		// draw the orbiting object image
		p.image(image, 0, 0, ORBIT_OBJECT_SIZE, ORBIT_OBJECT_SIZE);

		p.popMatrix();
	}
}
