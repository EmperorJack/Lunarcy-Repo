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
 * game state board with modular 3D components. Lists holds all the distinct 3D
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
	private OBJModel floorObj;
	private OBJModel northWallObj;
	private OBJModel eastWallObj;
	private OBJModel southWallObj;
	private OBJModel westWallObj;

	// lists of all distinct models that make up world 3D geometry
	private ArrayList<OBJWrapper> floor;
	private ArrayList<OBJWrapper> ceiling;
	private ArrayList<OBJWrapper> northWalls;
	private ArrayList<OBJWrapper> eastWalls;
	private ArrayList<OBJWrapper> southWalls;
	private ArrayList<OBJWrapper> westWalls;

	// constant square size for positioning models
	private final int SQUARE_SIZE;
	private final float MODEL_SCALE;

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

		// set the sizing constant
		this.SQUARE_SIZE = SQUARE_SIZE;
		this.MODEL_SCALE = MODEL_SCALE;

		// transformer to transform the modular assets correctly
		OBJTransform transformer = new OBJTransform(p);

		// setup the object model modular assets
		floorObj = setupObjectModel("floor", transformer);
		northWallObj = setupObjectModel("wall_north", transformer);
		eastWallObj = setupObjectModel("wall_east", transformer);
		southWallObj = setupObjectModel("wall_south", transformer);
		westWallObj = setupObjectModel("wall_west", transformer);

		// initialize distinct model lists
		floor = new ArrayList<OBJWrapper>();
		ceiling = new ArrayList<OBJWrapper>();
		northWalls = new ArrayList<OBJWrapper>();
		eastWalls = new ArrayList<OBJWrapper>();
		southWalls = new ArrayList<OBJWrapper>();
		westWalls = new ArrayList<OBJWrapper>();

		// for each square in the game state board
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				// get the square at x, y
				Square s = board[y][x];

				// if the square is walkable
				if (s instanceof WalkableSquare) {
					WalkableSquare ws = (WalkableSquare) s;

					// create a floor model
					floor.add(new OBJWrapper(floorObj, SQUARE_SIZE * x, 0,
							SQUARE_SIZE * y));

					// if the square is indoor
					if (ws.isInside()) {
						// create a ceiling model
						ceiling.add(new OBJWrapper(floorObj, SQUARE_SIZE * x,
								0, SQUARE_SIZE * y));
					}

					// if the square has a north wall
					if (ws.getWalls().get(Direction.NORTH) instanceof SolidWall) {
						// create a north wall model
						northWalls.add(new OBJWrapper(northWallObj, SQUARE_SIZE
								* x, 0, SQUARE_SIZE * y));
					}

					// if the square has an east wall
					if (ws.getWalls().get(Direction.EAST) instanceof SolidWall) {
						// create a east wall model
						eastWalls.add(new OBJWrapper(eastWallObj, SQUARE_SIZE
								* x, 0, SQUARE_SIZE * y));
					}

					// if the square has a south wall
					if (ws.getWalls().get(Direction.SOUTH) instanceof SolidWall) {
						// create a a south
						southWalls.add(new OBJWrapper(southWallObj, SQUARE_SIZE
								* x, 0, SQUARE_SIZE * y));
					}

					// if the square has a west wall
					if (ws.getWalls().get(Direction.WEST) instanceof SolidWall) {
						// create a west wall
						westWalls.add(new OBJWrapper(westWallObj, SQUARE_SIZE
								* x, 0, SQUARE_SIZE * y));
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
			OBJTransform objectTransformer) {
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

		// draw the floor
		p.pushMatrix();

		for (OBJWrapper o : floor) {
			o.draw();
		}
		p.popMatrix();

		// draw the ceiling
		p.pushMatrix();
		p.translate(0, -SQUARE_SIZE, 0);

		for (OBJWrapper o : ceiling) {
			o.draw();
		}
		p.popMatrix();

		// draw the north walls
		p.pushMatrix();

		for (OBJWrapper o : northWalls) {
			o.draw();
		}
		p.popMatrix();

		// draw the east walls
		p.pushMatrix();

		for (OBJWrapper o : eastWalls) {
			o.draw();
		}
		p.popMatrix();

		// draw the south walls
		p.pushMatrix();

		for (OBJWrapper o : southWalls) {
			o.draw();
		}
		p.popMatrix();

		// draw the west walls
		p.pushMatrix();

		for (OBJWrapper o : westWalls) {
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
