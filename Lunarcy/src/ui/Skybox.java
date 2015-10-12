package ui;

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
	private final int SUN_OFFSET;
	private final int SUN_SIZE;

	public Skybox(Canvas p, float MODEL_SCALE) {
		this.p = p;

		// load the obj model
		skyModel = new OBJModel(p, "assets/models/space_skybox.obj");

		// transformer to transform the skybox model correctly
		OBJTransform objectTransformer = new OBJTransform(p);
		objectTransformer.scaleOBJ(skyModel, MODEL_SCALE);
		skyModel.drawMode(OBJModel.POLYGON);

		// load the sun image
		sun = p.loadImage("/assets/items/shipPart_GRAVITY_CORE.png");
		// TODO replace temp image with actual sun image here

		// set the sun offset and size
		SUN_OFFSET = (int) (1000 * MODEL_SCALE);
		SUN_SIZE = (int) (250 * MODEL_SCALE);
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
		// int time = gameState.getTime();
		// TODO get game state time, will need mapping from degrees to range
		int time = (int) (p.frameCount);

		// rotate the skybox relative to the current time
		p.rotateX(PApplet.radians(time));

		// draw the skybox model
		skyModel.draw();

		// translate to the sun position
		p.translate(0, 0, SUN_OFFSET);

		p.imageMode(PApplet.CENTER);

		// draw the sun image
		p.image(sun, 0, 0, SUN_SIZE, SUN_SIZE);

		p.popStyle();
		p.popMatrix();
	}
}
