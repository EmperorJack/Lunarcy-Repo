package ui;

import game.Direction;
import game.SolidWall;
import game.Square;
import game.WalkableSquare;

import java.util.ArrayList;

import processing.core.*;
import saito.objloader.*;
import saito.objtools.*;

/**
 * Represents the 3D model for the game. The geometry is generated based on the
 * game state board with modular 3D components. A list holds all the distinct 3D
 * models where each have unique transformations required to be drawn correctly,
 * depending on the map layout.
 *
 * @author Jack
 *
 */
public class WorldModel {

	// the parent processing canvas
	private PApplet p;

	// modular assets that make up 3D geometry
	// private final OBJModel floorInsideObj;
	// private final OBJModel ceilingObj;
	// private final OBJModel northWallInsideObj;
	// private final OBJModel eastWallInsideObj;
	// private final OBJModel southWallInsideObj;
	// private final OBJModel westWallInsideObj;

	// lists of all distinct models that make up world 3D geometry
	// private ArrayList<OBJWrapper> floor;
	// private ArrayList<OBJWrapper> ceiling;
	// private ArrayList<OBJWrapper> northWalls;
	// private ArrayList<OBJWrapper> eastWalls;
	// private ArrayList<OBJWrapper> southWalls;
	// private ArrayList<OBJWrapper> westWalls;

	// lists of all distinct models that make up world 3D geometry
	private ArrayList<OBJWrapper> insideWorld;
	private ArrayList<OBJWrapper> outsideWorld;

	/**
	 * Setup a new world model with the given board. Each square will be
	 * interpreted into 3D geometry depending on it's walls and inside or
	 * outside state.
	 *
	 * @param p
	 *            The parent PApplet to draw onto.
	 * @param board
	 *            The board of squares from the initial game state.
	 * @param MODEL_SCALE
	 *            What factor object models should be scaled by.
	 * @param SQUARE_SIZE
	 *            The size each square is in game units.
	 */
	public WorldModel(PApplet p, Square[][] board, float MODEL_SCALE,
			int SQUARE_SIZE) {
		this.p = p;

		// transformer to transform the modular assets correctly
		OBJTransform transformer = new OBJTransform(p);

		// setup the inside object model modular assets
		OBJModel floorInsideObj = setupObjectModel("floor_inside", transformer,
				MODEL_SCALE);
		OBJModel ceilingObj = setupObjectModel("ceiling", transformer,
				MODEL_SCALE);
		OBJModel northWallInsideObj = setupObjectModel("wall_inside_north",
				transformer, MODEL_SCALE);
		OBJModel eastWallInsideObj = setupObjectModel("wall_inside_east",
				transformer, MODEL_SCALE);
		OBJModel southWallInsideObj = setupObjectModel("wall_inside_south",
				transformer, MODEL_SCALE);
		OBJModel westWallInsideObj = setupObjectModel("wall_inside_west",
				transformer, MODEL_SCALE);

		// setup the outside object model modular assets
		OBJModel floorOutsideObj = setupObjectModel("floor_outside",
				transformer, MODEL_SCALE);

		// initialize distinct model lists
		// floor = new ArrayList<OBJWrapper>();
		// ceiling = new ArrayList<OBJWrapper>();
		// northWalls = new ArrayList<OBJWrapper>();
		// eastWalls = new ArrayList<OBJWrapper>();
		// southWalls = new ArrayList<OBJWrapper>();
		// westWalls = new ArrayList<OBJWrapper>();

		// initialize distinct model list
		insideWorld = new ArrayList<OBJWrapper>();
		outsideWorld = new ArrayList<OBJWrapper>();

		// for each square in the game state board
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				// get the square at x, y
				Square s = board[y][x];

				// if the square is walkable
				if (s instanceof WalkableSquare) {
					WalkableSquare ws = (WalkableSquare) s;

					// if the square is indoor
					if (ws.isInside()) {
						// create an indoor floor model
						insideWorld.add(new OBJWrapper(floorInsideObj,
								SQUARE_SIZE * x, 0, SQUARE_SIZE * y));

						// create a ceiling model
						insideWorld.add(new OBJWrapper(ceilingObj, SQUARE_SIZE
								* x, 0, SQUARE_SIZE * y));
					} else {
						// create an outdoor floor model
						outsideWorld.add(new OBJWrapper(floorOutsideObj,
								SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
					}

					// if the square has a north wall
					if (ws.getWalls().get(Direction.NORTH) instanceof SolidWall) {

						// if the square is indoor
						if (ws.isInside()) {
							// create a north indoor wall model
							insideWorld.add(new OBJWrapper(northWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						} else {
							// create a north outdoor wall model
							outsideWorld.add(new OBJWrapper(northWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						}
					}

					// if the square has an east wall
					if (ws.getWalls().get(Direction.EAST) instanceof SolidWall) {

						// if the square is indoor
						if (ws.isInside()) {
							// create a east indoor wall model
							insideWorld.add(new OBJWrapper(eastWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						} else {
							// create a east outdoor wall model
							outsideWorld.add(new OBJWrapper(eastWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						}
					}

					// if the square has a south wall
					if (ws.getWalls().get(Direction.SOUTH) instanceof SolidWall) {

						// if the square is indoor
						if (ws.isInside()) {
							// create a south indoor wall model
							insideWorld.add(new OBJWrapper(southWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						} else {
							// create a south outdoor wall model
							outsideWorld.add(new OBJWrapper(southWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						}
					}

					// if the square has a west wall
					if (ws.getWalls().get(Direction.WEST) instanceof SolidWall) {

						// if the square is indoor
						if (ws.isInside()) {
							// create a west indoor wall model
							insideWorld.add(new OBJWrapper(westWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						} else {
							// create a west outdoor wall model
							outsideWorld.add(new OBJWrapper(westWallInsideObj,
									SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
						}
					}
				}
			}
		}
	}

	/**
	 * Load a new object model from file.
	 *
	 * @param objName
	 *            The name of the object model to load.
	 * @param objectTransformer
	 *            The transformer used to transform the new model.
	 * @return The newly setup object model.
	 */
	public OBJModel setupObjectModel(String objName,
			OBJTransform objectTransformer, float MODEL_SCALE) {
		// load the object model from path
		OBJModel objectModel = new OBJModel(p, "assets/models/" + objName
				+ ".obj");

		// setup the model
		objectTransformer.scaleOBJ(objectModel, MODEL_SCALE);
		objectModel.disableMaterial();
		objectModel.drawMode(OBJModel.POLYGON);

		return objectModel;
	}

	/**
	 * Draw the entire world model by drawing each list of distinct models
	 * separately.
	 */
	public void draw() {
		p.pushStyle();
		p.fill(150);

		// draw the inside world
		p.pushMatrix();
		for (OBJWrapper o : insideWorld) {
			o.draw();
		}
		p.popMatrix();

		p.fill(108, 99, 92);

		// draw the outside world
		p.pushMatrix();
		for (OBJWrapper o : outsideWorld) {
			o.draw();
		}
		p.popMatrix();

		p.popStyle();
	}

	/**
	 * A wrapper class for an Object Model. Contains translation data to draw
	 * the model at a set position.
	 *
	 * @author Jack
	 *
	 */
	private class OBJWrapper {

		// the 3D model to draw
		public OBJModel obj;

		// the translation data for this distinct object model
		public int tX;
		public int tY;
		public int tZ;

		public OBJWrapper(OBJModel obj, int tX, int tY, int tZ) {
			this.obj = obj;
			this.tX = tX;
			this.tY = tY;
			this.tZ = tZ;
		}

		/**
		 * Draw this object model after translating to the set position.
		 */
		public void draw() {
			p.pushMatrix();
			p.translate(tX, tY, tZ);
			obj.draw();
			p.popMatrix();
		}
	}
}
