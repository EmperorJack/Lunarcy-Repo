package ui.renderer;

import game.Direction;
import game.Door;
import game.LockedDoor;
import game.Player;
import game.Ship;
import game.SolidWall;
import game.Square;
import game.WalkableSquare;

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

	// inside model objects
	private final OBJModel floorInsideObj;
	private final OBJModel ceilingObj;
	private final OBJModel northWallInsideObj;
	private final OBJModel eastWallInsideObj;
	private final OBJModel southWallInsideObj;
	private final OBJModel westWallInsideObj;

	// outside model objects
	private final OBJModel floorOutsideObj;
	private final OBJModel northWallOutsideObj;
	private final OBJModel eastWallOutsideObj;
	private final OBJModel southWallOutsideObj;
	private final OBJModel westWallOutsideObj;

	// door frame model objects
	private final OBJModel doorFrameNorthObj;
	private final OBJModel doorFrameEastObj;
	private final OBJModel doorFrameSouthObj;
	private final OBJModel doorFrameWestObj;

	// door open / closed model objects
	private final OBJModel doorOpenNorthObj;
	private final OBJModel doorOpenEastObj;
	private final OBJModel doorOpenSouthObj;
	private final OBJModel doorOpenWestObj;
	private final OBJModel doorClosedNorthObj;
	private final OBJModel doorClosedEastObj;
	private final OBJModel doorClosedSouthObj;
	private final OBJModel doorClosedWestObj;

	// ship model object
	private final OBJModel shipObj;

	// lists of all distinct models that make up world 3D geometry
	private ArrayList<OBJWrapper> insideModels;
	private ArrayList<OBJWrapper> outsideModels;
	private ArrayList<OBJWrapper> doorModels;
	private ArrayList<OBJWrapperLockedDoor> lockedDoorModels;

	// ship obj wrapper
	private OBJWrapper ship;

	// square size and model scale
	private final int SQUARE_SIZE;

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
		this.SQUARE_SIZE = SQUARE_SIZE;

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

		// setup the door frame object model modular assets
		doorFrameNorthObj = setupObjectModel("door_frame_north", transformer,
				MODEL_SCALE);
		doorFrameEastObj = setupObjectModel("door_frame_east", transformer,
				MODEL_SCALE);
		doorFrameSouthObj = setupObjectModel("door_frame_south", transformer,
				MODEL_SCALE);
		doorFrameWestObj = setupObjectModel("door_frame_west", transformer,
				MODEL_SCALE);

		// setup the door open / closed model modular assets
		doorOpenNorthObj = setupObjectModel("door_open_north", transformer,
				MODEL_SCALE);
		doorOpenEastObj = setupObjectModel("door_open_east", transformer,
				MODEL_SCALE);
		doorOpenSouthObj = setupObjectModel("door_open_south", transformer,
				MODEL_SCALE);
		doorOpenWestObj = setupObjectModel("door_open_west", transformer,
				MODEL_SCALE);
		doorClosedNorthObj = setupObjectModel("door_closed_north", transformer,
				MODEL_SCALE);
		doorClosedEastObj = setupObjectModel("door_closed_east", transformer,
				MODEL_SCALE);
		doorClosedSouthObj = setupObjectModel("door_closed_south", transformer,
				MODEL_SCALE);
		doorClosedWestObj = setupObjectModel("door_closed_west", transformer,
				MODEL_SCALE);

		// setup the ship object model
		shipObj = setupObjectModel("ship", transformer, MODEL_SCALE);

		// initialize distinct model list
		insideModels = new ArrayList<OBJWrapper>();
		outsideModels = new ArrayList<OBJWrapper>();
		doorModels = new ArrayList<OBJWrapper>();
		lockedDoorModels = new ArrayList<OBJWrapperLockedDoor>();

		// parse the game board into a world model file
		parseGameWorld(board, SQUARE_SIZE);
	}

	/**
	 * Draw the entire world model by drawing each list of distinct models
	 * separately.
	 */
	public void draw(Player player) {
		p.pushMatrix();
		p.pushStyle();
		p.fill(150);

		// draw the inside models
		for (OBJWrapper objModel : insideModels) {
			objModel.draw();
		}

		// draw the unlocked door models
		for (OBJWrapper objModel : doorModels) {
			objModel.draw();
		}

		// draw the locked door models
		for (OBJWrapperLockedDoor objModel : lockedDoorModels) {
			objModel.draw(player);
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
					checkWalls(ws, Direction.NORTH, col, row);

					// check if the square has an east wall
					checkWalls(ws, Direction.EAST, col, row);

					// check if the square has a south wall
					checkWalls(ws, Direction.SOUTH, col, row);

					// check if the square has a west wall
					checkWalls(ws, Direction.WEST, col, row);
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
	 * @param dir
	 *            The direction to check for walls.
	 * @param x
	 *            x position of the walkable square.
	 * @param y
	 *            y position of the walkable square.
	 */
	private void checkWalls(WalkableSquare ws, Direction dir, int x, int y) {

		// if the square has a wall for the given direction
		if (ws.getWalls().get(dir) instanceof SolidWall) {

			// create a new wall model
			createWall(ws, dir, x, y);
		}
		// if the square has a locked door for the given direction
		else if (ws.getWalls().get(dir) instanceof LockedDoor) {

			LockedDoor door = (LockedDoor) ws.getWalls().get(dir);

			// create a new locked door model
			lockedDoorModels.add(new OBJWrapperLockedDoor(
					getDoorFrameModel(dir), SQUARE_SIZE * x, 0,
					SQUARE_SIZE * y, door, getDoorOpenModel(dir),
					getDoorClosedModel(dir)));
		}
		// if the square has an unlocked door for the given direction
		else if (ws.getWalls().get(dir) instanceof Door) {

			// create a new door frame model
			doorModels.add(new OBJWrapper(getDoorFrameModel(dir), SQUARE_SIZE
					* x, 0, SQUARE_SIZE * y));

			// create a new open door model
			doorModels.add(new OBJWrapper(getDoorOpenModel(dir), SQUARE_SIZE
					* x, 0, SQUARE_SIZE * y));
		}
	}

	/**
	 * Given a walkable square that is known to have a wall on a certain side,
	 * check if the square is inside or outside and create the correct wall
	 * model.
	 *
	 * @param ws
	 *            The walkable square in question.
	 * @param dir
	 *            The direction to add a wall too.
	 * @param x
	 *            x position of the walkable square.
	 * @param y
	 *            y position of the walkable square.
	 */
	private void createWall(WalkableSquare ws, Direction dir, int x, int y) {
		boolean inside = ws.isInside();

		// if the square is indoor
		if (inside) {
			// create a west indoor wall model
			insideModels.add(new OBJWrapper(getWallModel(dir, inside),
					SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
		} else {
			// create a west outdoor wall model
			outsideModels.add(new OBJWrapper(getWallModel(dir, inside),
					SQUARE_SIZE * x, 0, SQUARE_SIZE * y));
		}
	}

	/**
	 * Return the correct wall model depending on the given direction and inside
	 * state.
	 *
	 * @param dir
	 *            Direction the wall is in the square.
	 * @param inside
	 *            If the square is inside or not.
	 * @return The correct wall model.
	 */
	private OBJModel getWallModel(Direction dir, Boolean inside) {
		// get the door model based on the given direction
		switch (dir) {
		case NORTH:
			return inside ? northWallInsideObj : northWallOutsideObj;

		case EAST:
			return inside ? eastWallInsideObj : eastWallOutsideObj;

		case SOUTH:
			return inside ? southWallInsideObj : southWallOutsideObj;

		case WEST:
			return inside ? westWallInsideObj : westWallOutsideObj;
		}

		// will not return null
		return null;
	}

	/**
	 * Return the correct door frame model depending on the given direction and
	 * inside state.
	 *
	 * @param dir
	 *            Direction the door frame is in the square.
	 * @param inside
	 *            If the square is inside or not.
	 * @return The correct door frame model.
	 */
	private OBJModel getDoorFrameModel(Direction dir) {
		// get the door model based on the given direction
		switch (dir) {
		case NORTH:
			return doorFrameNorthObj;

		case EAST:
			return doorFrameEastObj;

		case SOUTH:
			return doorFrameSouthObj;

		case WEST:
			return doorFrameWestObj;
		}

		// will not return null
		return null;
	}

	/**
	 * Return the correct door open model depending on the given direction and
	 * inside state.
	 *
	 * @param dir
	 *            Direction the door is in the square.
	 * @param inside
	 *            If the square is inside or not.
	 * @return The correct door model.
	 */
	private OBJModel getDoorOpenModel(Direction dir) {
		// get the door model based on the given direction
		switch (dir) {
		case NORTH:
			return doorOpenNorthObj;

		case EAST:
			return doorOpenEastObj;

		case SOUTH:
			return doorOpenSouthObj;

		case WEST:
			return doorOpenWestObj;
		}

		// will not return null
		return null;
	}

	/**
	 * Return the correct door closed model depending on the given direction and
	 * inside state.
	 *
	 * @param dir
	 *            Direction the door is in the square.
	 * @param inside
	 *            If the square is inside or not.
	 * @return The correct door model.
	 */
	private OBJModel getDoorClosedModel(Direction dir) {
		// get the door model based on the given direction
		switch (dir) {
		case NORTH:
			return doorClosedNorthObj;

		case EAST:
			return doorClosedEastObj;

		case SOUTH:
			return doorClosedSouthObj;

		case WEST:
			return doorClosedWestObj;
		}

		// will not return null
		return null;
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

	/**
	 * A wrapper class for a locked door Object Model. Contains translation data
	 * to draw the model at a set position. Contains a reference to the door so
	 * it can check if a player can enter it or not, which determines if it
	 * should be drawn in the closed or open state.
	 *
	 * @author Jack
	 *
	 */
	private class OBJWrapperLockedDoor extends OBJWrapper {

		// associated door object
		public LockedDoor door;

		// open and closed door models
		public OBJModel openObj;
		public OBJModel closedObj;

		public OBJWrapperLockedDoor(OBJModel obj, int tX, int tY, int tZ,
				LockedDoor door, OBJModel openObj, OBJModel closedObj) {
			super(obj, tX, tY, tZ);
			this.door = door;
			this.openObj = openObj;
			this.closedObj = closedObj;
		}

		public void draw(Player player) {
			p.pushMatrix();
			p.pushStyle();
			p.translate(tX, tY, tZ);

			// draw the door frame
			obj.draw();

			// colour the model with the correct security colour
			p.fill(Canvas.getSecurityColour(door.getKeyCode(), true));

			// if the player can enter this door
			if (door.canPass(player)) {

				// draw door in open state
				openObj.draw();
			} else {
				// draw door in closed state
				closedObj.draw();
			}

			p.popStyle();
			p.popMatrix();
		}
	}
}
