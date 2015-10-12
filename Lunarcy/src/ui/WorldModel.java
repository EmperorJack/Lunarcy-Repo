package ui;

import game.Direction;
import game.Door;
import game.Ship;
import game.SolidWall;
import game.Square;
import game.WalkableSquare;
import game.Wall;

import java.util.ArrayList;

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
	private Canvas p;

	// the unique model assets
	private final OBJModel floorInsideObj;
	private final OBJModel ceilingObj;
	private final OBJModel northWallInsideObj;
	private final OBJModel eastWallInsideObj;
	private final OBJModel southWallInsideObj;
	private final OBJModel westWallInsideObj;
	private final OBJModel floorOutsideObj;
	private final OBJModel northWallOutsideObj;
	private final OBJModel eastWallOutsideObj;
	private final OBJModel southWallOutsideObj;
	private final OBJModel westWallOutsideObj;
	private final OBJModel doorNorthObj;
	private final OBJModel doorEastObj;
	private final OBJModel doorSouthObj;
	private final OBJModel doorWestObj;
	private final OBJModel shipObj;

	// lists of all distinct models that make up world 3D geometry
	private ArrayList<OBJWrapper> insideModels;
	private ArrayList<OBJWrapper> outsideModels;
	private ArrayList<OBJWrapper> doorModels;

	// ship obj wrapper
	private OBJWrapper ship;

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
	public WorldModel(Canvas p, Square[][] board, float MODEL_SCALE,
			int SQUARE_SIZE) {
		this.p = p;

		// transformer to transform the modular assets correctly
		OBJTransform transformer = new OBJTransform(p);

		// setup the inside object model modular assets
		floorInsideObj = setupObjectModel("floor_inside", transformer,
				MODEL_SCALE);
		ceilingObj = setupObjectModel("ceiling", transformer, MODEL_SCALE);
		northWallInsideObj = setupObjectModel("wall_inside_north", transformer,
				MODEL_SCALE);
		eastWallInsideObj = setupObjectModel("wall_inside_east", transformer,
				MODEL_SCALE);
		southWallInsideObj = setupObjectModel("wall_inside_south", transformer,
				MODEL_SCALE);
		westWallInsideObj = setupObjectModel("wall_inside_west", transformer,
				MODEL_SCALE);

		// setup the outside object model modular assets
		floorOutsideObj = setupObjectModel("floor_outside", transformer,
				MODEL_SCALE);
		northWallOutsideObj = setupObjectModel("wall_outside_north",
				transformer, MODEL_SCALE);
		eastWallOutsideObj = setupObjectModel("wall_outside_east", transformer,
				MODEL_SCALE);
		southWallOutsideObj = setupObjectModel("wall_outside_south",
				transformer, MODEL_SCALE);
		westWallOutsideObj = setupObjectModel("wall_outside_west", transformer,
				MODEL_SCALE);

		// setup the door object model modular assets
		doorNorthObj = setupObjectModel("door_north", transformer, MODEL_SCALE);
		doorEastObj = setupObjectModel("door_east", transformer, MODEL_SCALE);
		doorSouthObj = setupObjectModel("door_south", transformer, MODEL_SCALE);
		doorWestObj = setupObjectModel("door_west", transformer, MODEL_SCALE);

		// setup the ship object model
		shipObj = setupObjectModel("ship", transformer, MODEL_SCALE);

		// initialize distinct model list
		insideModels = new ArrayList<OBJWrapper>();
		outsideModels = new ArrayList<OBJWrapper>();
		doorModels = new ArrayList<OBJWrapper>();

		// parse the game board into a world model file
		parseGameWorld(board, SQUARE_SIZE);
	}

	/**
	 * Given a board from the inital game state, convert it into a 3D model that
	 * can be rendered.
	 *
	 * @param board
	 *            The board to generate 3D geometry for.
	 * @param SQUARE_SIZE
	 *            The size of the game squares.
	 */
	private void parseGameWorld(Square[][] board, int SQUARE_SIZE) {
		// for each square in the game state board
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {

				// get the square at row, col
				Square s = board[row][col];

				// if the square is walkable
				if (s instanceof WalkableSquare) {
					WalkableSquare ws = (WalkableSquare) s;

					// if the square is the ship tile
					if (s instanceof Ship) {
						ship = new OBJWrapper(shipObj, SQUARE_SIZE * col, 0,
								SQUARE_SIZE * row);
						continue;
					}

					// if the square is indoor
					if (ws.isInside()) {
						// create an indoor floor model
						insideModels.add(new OBJWrapper(floorInsideObj,
								SQUARE_SIZE * col, 0, SQUARE_SIZE * row));

						// create a ceiling model
						insideModels.add(new OBJWrapper(ceilingObj, SQUARE_SIZE
								* col, 0, SQUARE_SIZE * row));
					} else {
						// create an outdoor floor model
						outsideModels.add(new OBJWrapper(floorOutsideObj,
								SQUARE_SIZE * col, 0, SQUARE_SIZE * row));
					}

					// check if the square has a north wall
					checkWalls(ws, Direction.NORTH, SolidWall.class,
							northWallInsideObj, northWallOutsideObj,
							SQUARE_SIZE, col, row, insideModels, outsideModels);

					// check if the square has a north door
					checkWalls(ws, Direction.NORTH, Door.class, doorNorthObj,
							doorNorthObj, SQUARE_SIZE, col, row, doorModels,
							doorModels);

					// check if the square has an east wall
					checkWalls(ws, Direction.EAST, SolidWall.class,
							eastWallInsideObj, eastWallOutsideObj, SQUARE_SIZE,
							col, row, insideModels, outsideModels);

					// check if the square has an east door
					checkWalls(ws, Direction.EAST, Door.class, doorEastObj,
							doorEastObj, SQUARE_SIZE, col, row, doorModels,
							doorModels);

					// check if the square has a south wall
					checkWalls(ws, Direction.SOUTH, SolidWall.class,
							southWallInsideObj, southWallOutsideObj,
							SQUARE_SIZE, col, row, insideModels, outsideModels);

					// check if the square has a south door
					checkWalls(ws, Direction.SOUTH, Door.class, doorSouthObj,
							doorSouthObj, SQUARE_SIZE, col, row, doorModels,
							doorModels);

					// check if the square has a west wall
					checkWalls(ws, Direction.WEST, SolidWall.class,
							westWallInsideObj, westWallOutsideObj, SQUARE_SIZE,
							col, row, insideModels, outsideModels);

					// check if the square has a west door
					checkWalls(ws, Direction.WEST, Door.class, doorWestObj,
							doorWestObj, SQUARE_SIZE, col, row, doorModels,
							doorModels);
				}
			}
		}
	}

	/**
	 * Check if the walls for the given direction of the given square has wall
	 * of the given type. If so create the wall.
	 *
	 * @param ws
	 *            The walkable square in question.
	 * @param direction
	 *            The direction to check for walls.
	 * @param type
	 *            The type of wall to check for.
	 * @param insideObjModel
	 *            The inside wall model to create.
	 * @param outsideObjModel
	 *            The outside wall model to create.
	 * @param SQUARE_SIZE
	 *            The size of the game squares.
	 * @param x
	 *            x position of the walkable square.
	 * @param y
	 *            y position of the walkable square.
	 * @param insideTarget
	 *            The target list to place a new inside model.
	 * @param outsideTarget
	 *            The target list to place a new outside model.
	 */
	private <T extends Wall> void checkWalls(WalkableSquare ws,
			Direction direction, Class<T> type, OBJModel insideObjModel,
			OBJModel outsideObjModel, int SQUARE_SIZE, int x, int y,
			ArrayList<OBJWrapper> insideTarget,
			ArrayList<OBJWrapper> outsideTarget) {

		// if the square has a wall of given type for the given direction
		if (type.isInstance(ws.getWalls().get(direction))) {
			// create a new wall model
			createWall(ws, insideObjModel, outsideObjModel, SQUARE_SIZE, x, y,
					insideTarget, outsideTarget);
		}
	}

	/**
	 * Given a walkable square that is known to have a wall on a certain side,
	 * check if the square is inside or outside and create the correct wall
	 * model.
	 *
	 * @param ws
	 *            The walkable square in question.
	 * @param insideObjModel
	 *            The inside wall model to create.
	 * @param outsideObjModel
	 *            The outside wall model to create.
	 * @param SQUARE_SIZE
	 *            The size of the game squares.
	 * @param x
	 *            x position of the walkable square.
	 * @param y
	 *            y position of the walkable square.
	 * @param insideTarget
	 *            The target list to place a new inside model.
	 * @param outsideTarget
	 *            The target list to place a new outside model.
	 */
	private void createWall(WalkableSquare ws, OBJModel insideObjModel,
			OBJModel outsideObjModel, int SQUARE_SIZE, int x, int y,
			ArrayList<OBJWrapper> insideTarget,
			ArrayList<OBJWrapper> outsideTarget) {
		// if the square is indoor
		if (ws.isInside()) {
			// create a west indoor wall model
			insideTarget.add(new OBJWrapper(insideObjModel, SQUARE_SIZE * x, 0,
					SQUARE_SIZE * y));
		} else {
			// create a west outdoor wall model
			outsideTarget.add(new OBJWrapper(outsideObjModel, SQUARE_SIZE * x,
					0, SQUARE_SIZE * y));
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
	private OBJModel setupObjectModel(String objName,
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
		p.pushMatrix();
		p.pushStyle();
		p.fill(150);

		// draw the inside models
		for (OBJWrapper objModel : insideModels) {
			objModel.draw();
		}

		// draw the door models
		for (OBJWrapper objModel : doorModels) {
			objModel.draw();
		}

		p.fill(108, 99, 92);

		// draw the outside models
		for (OBJWrapper objModel : outsideModels) {
			objModel.draw();
		}

		p.fill(120);
		p.stroke(0);

		// draw the ship model
		ship.draw();

		p.popStyle();
		p.popMatrix();
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
