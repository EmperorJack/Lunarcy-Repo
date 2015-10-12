package ui;

import game.Direction;
import game.GameState;
import game.Item;
import game.Location;
import game.Player;
import game.Square;
import game.WalkableSquare;

import java.util.List;
import java.util.Map;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import saito.objloader.OBJModel;
import saito.objtools.OBJTransform;
import bots.Rover;

/**
 * The view that displays the player perspective of the game world in 3D.
 * Renders onto a 3D PGrapihcs layer before drawing onto the parent canvas.
 *
 * @author Jack & Kelly
 *
 */
public class Perspective3D extends DrawingComponent {

	// 3D world
	private final WorldModel WORLD;
	private final int SQUARE_SIZE = 1000;
	private final float MODEL_SCALE = SQUARE_SIZE / 2.5f;
	private final OBJModel SKYBOX;

	// character images
	private final PImage ASTRONAUT;
	private final PImage ROVER;

	// character drawing fields
	private final int CHARACTER_WIDTH = 200;
	private final int CHARACTER_HEIGHT = 400;
	private final int CHARACTER_Y_OFFSET = -200;
	private final int PLAYER_NAME_Y_OFFSET = -200;

	// entity drawing fields
	private final Map<String, PImage> entityImages;
	private final int ITEM_SIZE = 150;
	private final int ITEM_Y_OFFSET = -75;
	private final int ITEM_INNER_PADDING = -250;

	// camera fields
	private final int PLAYER_VIEW_HEIGHT = -180;
	private PVector cameraEye;
	private PVector actualCameraEye;
	private PVector targetCameraEye;
	private PVector cameraCenter;
	private PVector targetCameraCenter;
	private float targetRotationAngle;
	private float animPercent;
	private boolean animating;

	// camera perspective fields
	private final float FOV;
	private final int ASPECT_RATIO;
	private final float NEAR_CULLING_DISTANCE;
	private final float FAR_CULLING_DISTANCE;

	public Perspective3D(Canvas p, GameState gameState, int playerID,
			Map<String, PImage> entityImages) {
		super(p, gameState, playerID);

		// world model setup
		WORLD = new WorldModel(p, gameState.getBoard(), MODEL_SCALE,
				SQUARE_SIZE);

		// space skybox setup
		SKYBOX = new OBJModel(p, "assets/models/space_skybox.obj");
		OBJTransform objectTransformer = new OBJTransform(p);
		objectTransformer.scaleOBJ(SKYBOX, MODEL_SCALE);
		SKYBOX.drawMode(OBJModel.POLYGON);

		// character image setup
		ASTRONAUT = p.loadImage("assets/characters/Player.png");
		ROVER = p.loadImage("assets/characters/Rover.png");

		// entity image setup
		this.entityImages = entityImages;

		// camera eye setup (position)
		cameraEye = new PVector(0, PLAYER_VIEW_HEIGHT, 0);
		actualCameraEye = new PVector(0, PLAYER_VIEW_HEIGHT, 0);
		targetCameraEye = new PVector(0, PLAYER_VIEW_HEIGHT, 0);

		// camera center setup (rotation / orientation)
		cameraCenter = new PVector(0, -PApplet.cos(PApplet.HALF_PI)
				+ PLAYER_VIEW_HEIGHT, 0);
		targetCameraCenter = new PVector(0, -PApplet.cos(PApplet.HALF_PI)
				+ PLAYER_VIEW_HEIGHT, 0);
		targetRotationAngle = 0;
		animPercent = 1;
		animating = false;

		// camera perspective setup
		float cameraZ = ((p.height / 2.0f) / PApplet
				.tan(PApplet.PI * 60f / 360.0f));
		FOV = PApplet.PI / 2.5f;
		ASPECT_RATIO = (int) Canvas.TARGET_WIDTH / Canvas.TARGET_HEIGHT;
		NEAR_CULLING_DISTANCE = cameraZ / 10.0f;
		FAR_CULLING_DISTANCE = cameraZ * 10000.0f;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// get the player from the current game state
		Player thisPlayer = gameState.getPlayer(playerID);

		// get the players from the current game state
		Player[] players = gameState.getPlayers();

		// get the rovers from the current game state
		Set<Rover> rovers = gameState.getRovers();

		// position the camera to the player position and orientation
		setCamera(thisPlayer.getLocation(), thisPlayer.getOrientation(), delta);

		// render the lights
		p.ambientLight(50, 50, 50);
		p.pointLight(200, 200, 200, actualCameraEye.x, PLAYER_VIEW_HEIGHT,
				actualCameraEye.z);

		p.pushMatrix();

		// translate to the camera position
		p.translate(actualCameraEye.x, PLAYER_VIEW_HEIGHT, actualCameraEye.z);

		// draw the space skybox with no lighting
		SKYBOX.draw();
		p.popMatrix();

		// draw the game world
		WORLD.draw();

		// draw the players
		drawPlayers(thisPlayer, players);

		// draw the rovers
		drawRovers(thisPlayer, rovers);

		// draw the entities
		drawEntites(thisPlayer, gameState.getBoard());

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	/**
	 * Draw all the players in their relative positions for the current game
	 * state.
	 *
	 * @param thisPlayer
	 *            The player this perspective is relative too.
	 * @param players
	 *            List of players in the game.
	 */
	private void drawPlayers(Player thisPlayer, Player[] players) {
		for (int i = 0; i < players.length; i++) {
			// don't draw a player in the same location as this player
			if (!players[i].getLocation().equals(thisPlayer.getLocation())) {
				p.pushMatrix();
				p.pushStyle();

				Player currentPlayer = players[i];

				// tint the image with the current player colour
				p.tint(currentPlayer.getColour().getRGB());

				// get the current player position in 3D space
				PVector position = new PVector(currentPlayer.getLocation()
						.getX() * SQUARE_SIZE + SQUARE_SIZE / 2,
						CHARACTER_Y_OFFSET, currentPlayer.getLocation().getY()
								* SQUARE_SIZE + SQUARE_SIZE / 2);

				// translate to the current player location
				p.translate(position.x, position.y, position.z);

				// rotate the current player sprite to face this player
				rotateRelativeTo(position);

				// draw the player astronaut image
				p.imageMode(PApplet.CENTER);
				p.image(ASTRONAUT, 0, 0, CHARACTER_WIDTH, CHARACTER_HEIGHT);

				// draw the player name text
				p.fill(currentPlayer.getColour().getRGB());
				p.textSize(36);
				p.text(currentPlayer.getName(), 0, PLAYER_NAME_Y_OFFSET);

				p.popStyle();
				p.popMatrix();
			}
		}
	}

	/**
	 * Draw all the rovers in their relative positions for the current game
	 * state.
	 *
	 * @param rovers
	 *            List of rovers in the game.
	 */
	private void drawRovers(Player thisPlayer, Set<Rover> rovers) {
		for (Rover currentRover : rovers) {

			// if the rover is not in this player's location
			if (!currentRover.getLocation().equals(thisPlayer.getLocation())) {
				p.pushMatrix();
				p.pushStyle();

				// get the rover position in 3D space
				PVector position = new PVector(currentRover.getLocation()
						.getX() * SQUARE_SIZE + SQUARE_SIZE / 2,
						CHARACTER_Y_OFFSET, currentRover.getLocation().getY()
								* SQUARE_SIZE + SQUARE_SIZE / 2);

				// translate to the rover location
				p.translate(position.x, position.y, position.z);

				// rotate the current rover sprite to face this player
				rotateRelativeTo(position);

				// draw the player rover image
				p.imageMode(PApplet.CENTER);
				p.image(ROVER, 0, 0, CHARACTER_WIDTH, CHARACTER_HEIGHT);

				p.popStyle();
				p.popMatrix();
			}
		}
	}

	/**
	 * Draw all the entities in their relative positions for the current game
	 * state.
	 *
	 * @param board
	 *            The game board that contains all the entities.
	 */
	private void drawEntites(Player thisPlayer, Square[][] board) {
		p.pushMatrix();
		p.pushStyle();

		p.imageMode(PApplet.CENTER);

		// for each square in the game state board
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {

				// if the location is not in this player's location
				if (!new Location(col, row).equals(thisPlayer.getLocation())) {

					// get the square at row, col
					Square s = board[row][col];

					// if the square is not walkable then there must be no items
					if (!(s instanceof WalkableSquare)) {
						continue;
					}

					p.pushMatrix();

					// compute the square position
					int x = col * SQUARE_SIZE + SQUARE_SIZE / 2;
					int z = row * SQUARE_SIZE + SQUARE_SIZE / 2;

					// get the position vector in 3D space
					PVector position = new PVector(x, ITEM_Y_OFFSET, z);

					// translate to the current square location
					p.translate(x, ITEM_Y_OFFSET, z);

					// for each direction
					for (Direction dir : Direction.values()) {

						// get the entities for the current direction
						List<Item> items = s.getItems(dir);

						// for each entity
						for (int i = 0; i < items.size(); i++) {
							p.pushMatrix();

							// compute the offset for the current entity image
							int offset = (int) ((i + 1)
									/ (float) (items.size() + 1) * SQUARE_SIZE)
									- SQUARE_SIZE / 2;

							// setup the x and z offsets
							int xOffset = 0;
							int zOffset = 0;

							// set the offsets based on the current direction
							switch (dir) {
							case NORTH:
								xOffset = offset;
								zOffset = ITEM_INNER_PADDING;
								break;

							case EAST:
								xOffset = -ITEM_INNER_PADDING;
								zOffset = offset;
								break;

							case SOUTH:
								xOffset = -offset;
								zOffset = -ITEM_INNER_PADDING;
								break;

							case WEST:
								xOffset = ITEM_INNER_PADDING;
								zOffset = -offset;
								break;
							}

							// translate to the current entity offsets
							p.translate(xOffset, 0, zOffset);

							// rotate the current entity to face this player
							rotateRelativeTo(position);

							// draw the entity image
							p.image(entityImages.get(items.get(i)
									.getImageName()), 0, 0, ITEM_SIZE,
									ITEM_SIZE);

							p.popMatrix();
						}
					}
					p.popMatrix();
				}
			}
		}

		p.popStyle();
		p.popMatrix();
	}

	/**
	 * Given a position vector perform a rotation to face this player's
	 * perspective.
	 *
	 * @param position
	 *            The position vector to rotate in reference to.
	 */
	private void rotateRelativeTo(PVector position) {
		float angle = PApplet.atan2(actualCameraEye.z - position.z,
				actualCameraEye.x - position.x);
		p.rotateY(-(angle - PApplet.HALF_PI));
	}

	/**
	 * Set the camera position and facing direction to given location and
	 * orientation.
	 *
	 * @param location
	 *            The location to move the camera to.
	 * @param orientation
	 *            The direction the camera should face.
	 */
	private void setCamera(Location location, Direction orientation, float delta) {
		// compute the camera position from the given location
		float newEyeX = location.getX() * SQUARE_SIZE + SQUARE_SIZE / 2;
		float newEyeZ = location.getY() * SQUARE_SIZE + SQUARE_SIZE / 2;

		// comute the camera orientation from the given direction
		float newRotationAngle = orientation.ordinal() * PApplet.HALF_PI
				- PApplet.HALF_PI;

		float newCenterX = PApplet.cos(newRotationAngle);
		float newCenterZ = PApplet.sin(newRotationAngle);

		// check if the camera position or orientation has changed
		if (!animating
				&& (newEyeX != cameraEye.x || newEyeZ != cameraEye.z || newRotationAngle != targetRotationAngle)) {

			// update the target camera position
			targetCameraEye.x = newEyeX;
			targetCameraEye.z = newEyeZ;

			// update the target camera orientation
			targetCameraCenter.x = newCenterX;
			targetCameraCenter.z = newCenterZ;

			// update the target rotation angle
			targetRotationAngle = newRotationAngle;

			// begin the interpolation animation between states
			animPercent = 0;
			animating = true;
		}

		// compute the interpolated camera position
		actualCameraEye.x = PApplet.floor(PApplet.lerp(cameraEye.x,
				targetCameraEye.x, animPercent));
		actualCameraEye.z = PApplet.floor(PApplet.lerp(cameraEye.z,
				targetCameraEye.z, animPercent));

		// compute the interpolated camera orientation
		float centerX = PApplet.lerp(cameraCenter.x + actualCameraEye.x,
				targetCameraCenter.x + actualCameraEye.x, animPercent);
		float centerZ = PApplet.lerp(cameraCenter.z + actualCameraEye.z,
				targetCameraCenter.z + actualCameraEye.z, animPercent);

		// check if camera animation has finished
		if (animPercent >= 1) {
			// set the actual camera position to the target
			cameraEye.x = targetCameraEye.x;
			cameraEye.z = targetCameraEye.z;

			// set the actual camera orientaiton to the target
			cameraCenter.x = targetCameraCenter.x;
			cameraCenter.z = targetCameraCenter.z;

			animating = false;
		} else {
			// update the animation percentage
			animPercent += (0.08 * delta);
		}

		// set the perspective and render distance
		p.perspective(FOV, ASPECT_RATIO, NEAR_CULLING_DISTANCE,
				FAR_CULLING_DISTANCE);

		// set the camera to the correct position and orientation
		p.camera(actualCameraEye.x, cameraEye.y, actualCameraEye.z, centerX,
				cameraCenter.y, centerZ, 0.0f, 1, 0);
	}
}
