package ui;

import java.util.List;
import java.util.Set;

import bots.Rover;
import game.Direction;
import game.Entity;
import game.GameState;
import game.Location;
import game.Player;
import processing.core.*;
import saito.objloader.OBJModel;
import saito.objtools.OBJTransform;

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

	// camera fields
	private final int PLAYER_VIEW_HEIGHT = -150;
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

	public Perspective3D(Canvas p, GameState gameState, int playerID) {
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
		ASTRONAUT = p.loadImage("assets/characters/astronaut.png");
		ROVER = p.loadImage("assets/characters/OgreMan.png");

		// camera eye setup (position)
		cameraEye = new PVector(0, PLAYER_VIEW_HEIGHT, 0);
		actualCameraEye = new PVector(0, PLAYER_VIEW_HEIGHT, 0);
		targetCameraEye = new PVector(0, PLAYER_VIEW_HEIGHT, 0);

		// camera center setup (rotation / orientaiton)
		cameraCenter = new PVector(0, -PApplet.cos(PApplet.PI / 2)
				+ PLAYER_VIEW_HEIGHT, 0);
		targetCameraCenter = new PVector(0, -PApplet.cos(PApplet.PI / 2)
				+ PLAYER_VIEW_HEIGHT, 0);
		targetRotationAngle = 0;
		animPercent = 1;
		animating = false;

		// camera perspective setup
		float cameraZ = ((p.height / 2.0f) / PApplet
				.tan(PApplet.PI * 60f / 360.0f));
		FOV = PApplet.PI / 2.5f;
		ASPECT_RATIO = (int) p.width / p.height;
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
		drawRovers(rovers);

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
			// don't draw a sprite for this player or a player in the same
			// location as this player
			if (i != thisPlayer.getId()
					&& !players[i].getLocation().equals(
							thisPlayer.getLocation())) {
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
	private void drawRovers(Set<Rover> rovers) {
		for (Rover currentRover : rovers) {
			p.pushMatrix();
			p.pushStyle();

			// tint the image with the rover colour
			// p.tint(0, 0, 0);

			// get the rover position in 3D space
			PVector position = new PVector(currentRover.getLocation().getX()
					* SQUARE_SIZE + SQUARE_SIZE / 2, CHARACTER_Y_OFFSET,
					currentRover.getLocation().getY() * SQUARE_SIZE
							+ SQUARE_SIZE / 2);

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
		p.rotateY(angle - PApplet.PI / 2);
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
		float newRotationAngle = 0;

		// depending on the given orientation
		switch (orientation) {

		// set north rotation angle
		case NORTH:
			newRotationAngle = -PApplet.PI / 2;
			break;

		// set east rotation angle
		case EAST:
			newRotationAngle = 0;
			break;

		// set south rotation angle
		case SOUTH:
			newRotationAngle = PApplet.PI / 2;
			break;

		// set west rotation angle
		case WEST:
			newRotationAngle = PApplet.PI;
			break;
		}

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
