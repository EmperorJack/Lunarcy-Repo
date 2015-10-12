package ui;

import saito.objloader.OBJModel;
import saito.objtools.OBJTransform;
import game.GameState;

public class Skybox {

	// the parent processing canvas
	private Canvas p;

	// skybox fields
	private final OBJModel model;

	public Skybox(Canvas p, float MODEL_SCALE) {
		this.p = p;

		// load the obj model from the given name
		model = new OBJModel(p, "assets/models/space_skybox.obj");

		// transformer to transform the skybox model correctly
		OBJTransform objectTransformer = new OBJTransform(p);
		objectTransformer.scaleOBJ(model, MODEL_SCALE);
		model.drawMode(OBJModel.POLYGON);
	}

	/**
	 * Draw the skybox in the state provided by the given game state.
	 *
	 * @param gameState
	 */
	public void draw(GameState gameState) {
		p.pushMatrix();

		// get the current time from the game state
		//int time = gameState.getTime();

		// rotate in

		// draw the skybox model
		model.draw();

		p.popMatrix();
	}
}
