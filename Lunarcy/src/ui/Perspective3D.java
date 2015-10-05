package ui;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import game.Direction;
import game.GameState;
import game.Location;
import game.Player;
import processing.core.*;

/**
 * The view that displays the player perspective of the game world in 3D.
 * Renders onto a 3D PGrapihcs layer before drawing onto the parent canvas.
 *
 * @author Jack & Kelly
 *
 */
public class Perspective3D extends DrawingComponent {

	// 3D world
	private WorldModel world;
	private final int SQUARE_SIZE = 500;
	private final float MODEL_SCALE = SQUARE_SIZE / 2.5f;

	// camera fields
	private PVector cameraEye;
	private PVector cameraCenter;
	private PVector targetCameraEye;
	private PVector targetCameraCenter;
	private float percent;

	// camera perspective fields
	private final float FOV;
	private final int ASPECT_RATIO;
	private final float NEAR_CULLING_DISTANCE;
	private final float FAR_CULLING_DISTANCE;

	public Perspective3D(PApplet p, GameState gameState, int playerID) {
		super(p, gameState, playerID);

		// world model setup
		world = new WorldModel(p, gameState.getBoard(), MODEL_SCALE,
				SQUARE_SIZE);

		// camera setup
		cameraEye = new PVector(0, -100, 0);
		cameraCenter = new PVector(0, -PApplet.cos(PApplet.PI / 2) - 100, 0);
		targetCameraEye = new PVector(0, -100, 0);
		targetCameraCenter = new PVector(0, -PApplet.cos(PApplet.PI / 2) - 100,
				0);
		percent = 1.0f;

		// camera perspective setup
		float cameraZ = ((p.height / 2.0f) / PApplet
				.tan(PApplet.PI * 60f / 360.0f));
		FOV = PApplet.PI / 2.0f;
		ASPECT_RATIO = (int) p.width / p.height;
		NEAR_CULLING_DISTANCE = cameraZ / 10.0f;
		FAR_CULLING_DISTANCE = cameraZ * 10000.0f;
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// get the player from the current game state
		Player player = gameState.getPlayer(playerID);

		// get the players from the current game state
		Player[] players = gameState.getPlayers();

		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// position the camera to the player position and orientation
		setCamera(player.getLocation(), player.getOrientation());

		// light source
		p.pushMatrix();
		p.ambientLight(50, 50, 50);
		p.pointLight(200, 200, 200, player.getLocation().getX() * SQUARE_SIZE
				+ SQUARE_SIZE / 2, -100, player.getLocation().getY()
				* SQUARE_SIZE + SQUARE_SIZE / 2);
		p.popMatrix();

		// draw the game world
		world.draw();

		// draw the players
		for (int i = 0; i < players.length; i++) {
			// don't draw a sprite for this player
			if (i != player.getId()) {
				p.pushMatrix();
				p.pushStyle();

				Player currentPlayer = players[i];

				// use the player colour
				// p.fill(player.getColour().getRGB());

				Location location = currentPlayer.getLocation();

				// draw the player
				System.out.println("Player " + i + " is at " + location.getX()
						+ ", " + location.getY());
				p.translate(location.getX() * SQUARE_SIZE + SQUARE_SIZE / 2,
						-100, location.getY() * SQUARE_SIZE + SQUARE_SIZE / 2);
				p.sphere(30);

				p.popStyle();
				p.popMatrix();
			}
		}

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
	private void setCamera(Location location, Direction orientation) {
		// check if camera animation has finished
		// if (percent == 1.0f) {
		// // set the actual camera position to the target position
		// cameraEye.x = targetCameraEye.x;
		// cameraEye.z = targetCameraEye.z;
		// } else {
		// // update the animation percentage
		// percent += 0.05f;
		// }

		// compute the camera position
		// float newEyeX = location.getX() * SQUARE_SIZE + SQUARE_SIZE / 2;
		// float newEyeZ = location.getY() * SQUARE_SIZE + SQUARE_SIZE / 2;

		cameraEye.x = location.getX() * SQUARE_SIZE + SQUARE_SIZE / 2;
		cameraEye.z = location.getY() * SQUARE_SIZE + SQUARE_SIZE / 2;

		// check if the camera position has changed
		// if (newEyeX != cameraEye.x || newEyeZ != cameraEye.z) {
		// // update the target camera position
		// targetCameraEye.x = newEyeX;
		// targetCameraEye.z = newEyeZ;
		//
		// // begin the interpolation animation between states
		// percent = 0;
		// }

		float rotAngle = 0;

		// depending on the orientation
		switch (orientation) {
		case NORTH:
			// set north rotation angle
			rotAngle = -PApplet.PI / 2;
			break;

		case EAST:
			// set east rotation angle
			rotAngle = 0;
			break;

		case SOUTH:
			// set south rotation angle
			rotAngle = PApplet.PI / 2;
			break;

		case WEST:
			// set west rotation angle
			rotAngle = PApplet.PI;
			break;
		}

		// set the perspective and render distance
		p.perspective(FOV, ASPECT_RATIO, NEAR_CULLING_DISTANCE,
				FAR_CULLING_DISTANCE);

		// compute the interpolated camera position
		// float eyeX = PApplet.lerp(cameraEye.x, targetCameraEye.x, percent);
		// float eyeZ = PApplet.lerp(cameraEye.z, targetCameraEye.z, percent);
		// float centerX = PApplet.lerp(cameraCenter.x, targetCameraCenter.x,
		// percent);
		// float centerZ = PApplet.lerp(cameraCenter.z, targetCameraCenter.z,
		// percent);

		// rotate the camera to the correct orientation
		// cameraCenter.x = PApplet.cos(rotAngle) + eyeX;
		// cameraCenter.z = PApplet.sin(rotAngle) + eyeZ;
		// targetCameraCenter.x = cameraCenter.x;
		// targetCameraCenter.z = cameraCenter.z;

		cameraCenter.x = PApplet.cos(rotAngle) + cameraEye.x;
		cameraCenter.z = PApplet.sin(rotAngle) + cameraEye.z;

		// position the camera
		// p.camera(eyeX, cameraEye.y, eyeZ, centerX, cameraCenter.y, centerZ,
		// 0.0f, 1, 0);
		// System.out.println(percent);
		// System.out.println(cameraEye.x + ", " + targetCameraEye.x + " : "
		// + eyeX);
		p.camera(cameraEye.x, cameraEye.y, cameraEye.z, cameraCenter.x,
				cameraCenter.y, cameraCenter.z, 0.0f, 1, 0);
	}
}
