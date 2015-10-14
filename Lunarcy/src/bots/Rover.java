package bots;

import game.Direction;
import game.GameState;
import game.Location;
import game.Player;
import game.Character;

import java.io.Serializable;

/**
 * A rover is a 'bot' in the game. Each rover can be following one of two
 * movement strategies, at a given time.
 *
 * 1. Roam: Follow a random path, change to #2 when a player is spotted.
 *
 * 2. Track: Track and follow a specific Player, using shortest path to attempt
 * to capture them. Gives up after the player gets too far away, and reverts to
 * the Roam Strategy.
 *
 * @author Ben
 *
 */
public class Rover implements Character, Serializable {

	private static final long serialVersionUID = 1L;

	// The current movement strategy we are following
	private MoveStrategy movementStrategy;

	// The next location which the rover is set to follow
	private Location nextStep;

	// Mantains the rovers position (x,y) at any given time
	private Location currentLocation;
	private Direction orientation;

	// So we can reset the rover to their original location if neccesary
	private final Location startSpawn;

	public Rover(Location spawnLocation) {
		this.startSpawn = spawnLocation;
		this.currentLocation = spawnLocation;

		this.movementStrategy = new RoamMovement();
		this.orientation = Direction.NORTH;
	}

	/**
	 * Moves the rover one step along its currentPath, generating a new path if
	 * necessary and also changing the movement strategy when applicable.
	 */

	public void tick(GameState gameState) {
		// Set our nextStep to based on the strategys next step
		nextStep = movementStrategy.nextStep(this, gameState);

		// if the next step is null, they're stuck
		if (nextStep == null) {
			// So set there movement to be roaming
			movementStrategy = new RoamMovement();
			return;
		}

		// Face the direction the next square is in
		orientation = currentLocation.getDirection(nextStep);

		// At this point the nextStep must be valid so move the rover
		currentLocation = nextStep;

		// If the rover is somehow inside, send them back to the start
		// This shouldn't happen but just to be safe
		if (!gameState.isOutside(currentLocation)) {
			restartRover();
		}

		Player caughtPlayer = gameState.caughtPlayer(this);

		// If this rovers caught a player
		if (caughtPlayer != null) {
			// Take all there oxygen
			caughtPlayer.depleteOxygen();
		}

		updateStrategy(gameState);

	}

	private void restartRover() {
		movementStrategy = new RoamMovement();
		currentLocation = startSpawn;
	}

	/**
	 * Changes the movement strategy in one of two cases
	 *
	 * 1. If the Rover is roaming and spots a target, changes to TrackMovement.
	 *
	 * 2. If the Rover is tracking and has not reached the player after a
	 * certain amount of ticks, revert to roaming.
	 *
	 * Otherwise does not update the movement strategy.
	 *
	 */
	private void updateStrategy(GameState gameState) {

		// If we are roaming, see if we can track anyone
		if (movementStrategy instanceof RoamMovement) {

			// Check if we can see any players
			Player target = ((RoamMovement) movementStrategy).viewTarget(this,
					gameState);

			// If we can see a target, chase them
			if (target != null) {
				// Change to tracking this target
				movementStrategy = new TrackMovement(this, gameState, target);
			}

		}
		// If we are in Track Mode
		else if (movementStrategy instanceof TrackMovement) {
			// If the tracker has been following for too long
			if (((TrackMovement) movementStrategy)
					.shouldGiveup(this, gameState)) {
				// Change back to Roaming
				movementStrategy = new RoamMovement();
			}
		}
	}

	/**
	 * Rotate the rover to the right of there current direction.
	 */
	public void rotate() {
		orientation = orientation.right();
	}

	public Location getLocation() {
		return currentLocation;
	}

	public void setOrientation(Direction orientation) {
		this.orientation = orientation;
	}

	@Override
	public Direction getOrientation() {
		return orientation;
	}

}
