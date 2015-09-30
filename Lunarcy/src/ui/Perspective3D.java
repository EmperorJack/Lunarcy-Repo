package ui;

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

	// player fields
	private int playerID;
	private Player player;
	private Player[] players;

	// camera fields
	private PVector cameraEye;
	private PVector cameraCenter;

	public Perspective3D(PApplet p, GameState gameState, int playerID) {
		// public Perspective3D(PApplet p, GameState gameState) {
		super(p, gameState);

		// world model setup
		world = new WorldModel(p, gameState.getBoard(), MODEL_SCALE,
				SQUARE_SIZE);

		// camera setup
		cameraEye = new PVector(0, -100, 0);
		cameraCenter = new PVector(0, -PApplet.cos(PApplet.PI / 2) - 100, 0);

		// set the initial game state
		update(gameState);
	}

	@Override
	public void update(GameState gameState) {
		System.out.println("Perspective ID: " + playerID);

		// update the player associated with this client
		player = gameState.getPlayer(playerID);

		// update the players given from the updated game state
		players = gameState.getPlayers();

		// update the camera to the player position and orientation
		setCamera(player.getLocation(), player.getOrientation());
	}

	@Override
	public void draw(float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// position the camera
		p.camera(cameraEye.x, cameraEye.y, cameraEye.z, cameraCenter.x,
				cameraCenter.y, cameraCenter.z, 0.0f, 1, 0);

		// light source
		p.pushMatrix();
		p.ambientLight(50, 50, 50);
		p.pointLight(200, 200, 200, player.getLocation().getX() * SQUARE_SIZE
				+ SQUARE_SIZE / 2, -100, player.getLocation().getY()
				* SQUARE_SIZE + SQUARE_SIZE / 2);
		p.popMatrix();

		// UNCOMMENT THE FOLLOWING LINES FOR A GOOD TIME
		// p.scale(PApplet.sin(PApplet.radians(p.frameCount)));
		// p.rotateX(PApplet.radians(p.frameCount));
		// p.rotateY(PApplet.radians(p.frameCount));
		// p.rotateZ(PApplet.radians(p.frameCount / 4));

		// draw the game world
		world.draw();

		// draw the players
		for (int i = 0; i < players.length; i++) {
			// don't draw a sprite for this player
			if (i != playerID) {
				p.pushMatrix();
				p.pushStyle();

				Player player = players[i];
				// use the player colour
				//p.fill(player.getColour().getRGB());

				Location location = player.getLocation();

				// draw the player
				p.translate(location.getX() * SQUARE_SIZE + SQUARE_SIZE / 2,
						location.getY() * SQUARE_SIZE + SQUARE_SIZE / 2);
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
		// set the camera position
		cameraEye.x = location.getX() * SQUARE_SIZE + SQUARE_SIZE / 2;
		cameraEye.z = location.getY() * SQUARE_SIZE + SQUARE_SIZE / 2;
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

		// rotate the camera to the correct orientation
		cameraCenter.x = PApplet.cos(rotAngle) + cameraEye.x;
		cameraCenter.z = PApplet.sin(rotAngle) + cameraEye.z;
	}
}
