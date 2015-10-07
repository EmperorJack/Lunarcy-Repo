package ui;

import game.Direction;
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

	// camera fields
	private final int PLAYER_VIEW_HEIGHT = -200;
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
		Player player = gameState.getPlayer(playerID);

		// get the players from the current game state
		Player[] players = gameState.getPlayers();

		// position the camera to the player position and orientation
		setCamera(player.getLocation(), player.getOrientation(), delta);

		// render the lights
		p.ambientLight(50, 50, 50);
		p.pointLight(200, 200, 200, actualCameraEye.x, PLAYER_VIEW_HEIGHT,
				actualCameraEye.z);

		// draw the space skybox with no lighting
		SKYBOX.draw();

		// draw the game world
		WORLD.draw();

		// draw the players
		for (int i = 0; i < players.length; i++) {
			// don't draw a sprite for this player or a player in the same
			// location as this player
			if (i != player.getId()
					&& !players[i].getLocation().equals(player.getLocation())) {
				p.pushMatrix();
				p.pushStyle();

				Player currentPlayer = players[i];

				// use the current player colour and position
				p.tint(player.getColour().getRGB());
				PVector position = new PVector(currentPlayer.getLocation()
						.getX(), currentPlayer.getLocation().getY());

				// translate to the player location
				p.translate(position.x * SQUARE_SIZE + SQUARE_SIZE / 2, -300,
						position.y * SQUARE_SIZE + SQUARE_SIZE / 2);

				// rotate the player to face this player
				float angle = PApplet.atan2(cameraEye.z - position.y,
						cameraEye.x - position.x);
				angle = angle * (180 / PApplet.PI);
				p.rotateY(-PVector.angleBetween(position, cameraEye));

				// draw the player astronaut image
				p.imageMode(p.CENTER);
				p.image(ASTRONAUT, 0, 0, 300, 600);

				p.popStyle();
				p.popMatrix();
			}
		}

		// translate to the camera position
		p.translate(actualCameraEye.x, PLAYER_VIEW_HEIGHT, actualCameraEye.z);

		// draw the space skybox with no lighting
		// p.noLights();
		// SKYBOX.draw();

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
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
