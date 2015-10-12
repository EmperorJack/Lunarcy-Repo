package bots;

import java.util.List;

import game.GameState;
import game.Location;
import game.Player;

/**
 * Track Movement: Follow a Player around the map, computing the shortest path
 * to them and trying to reach them. Will give up if the player is further than
 * a set distance from the rover.
 *
 * @author Ben
 *
 */

@SuppressWarnings("serial")
public class TrackMovement extends ShortestPathMover {

	// The player we are chasing
	private Player target;

	// The path we are currently following
	private List<Location> path;

	private final int MAX_DISTANCE;

	public TrackMovement(Rover rover, GameState gamestate, Player target) {
		this.target = target;
		this.MAX_DISTANCE = gamestate.getBoard().length / 4;
		this.path = findPath(rover, gamestate, rover.getLocation(),
				target.getLocation());
	}

	/**
	 * Returns true if any of the following conditions hold
	 *
	 * 1. The paths end location does not equal the target players location 2.
	 * The path is null 3. The path is empty
	 *
	 * Otherwise returns false
	 */

	private boolean mustUpdate() {
		if (path == null || path.isEmpty())
			return true;

		// Return true if the end location does not equal the
		// target location, false otherwise
		return !path.get(path.size() - 1).equals(target.getLocation());
	}

	/**
	 * Gives up if the player is inside, or the player is too far away from you.
	 *
	 * @return
	 */
	public boolean shouldGiveup(Rover rover, GameState gamestate) {

		// If the players inside, or too far from you give up chasing them
		return !gamestate.isOutside(target.getLocation())
				|| estimate(rover.getLocation(), target.getLocation()) > MAX_DISTANCE;

	}

	/**
	 * Finds the shortest path from currentLocation, to the target field.
	 * Returns this path if one was found, or null.
	 *
	 * @param currentLocation
	 *            : Where the rover is currently located
	 * @return the path
	 */
	public Location nextStep(Rover rover, GameState gamestate) {

		// Makes sure the target is outside
		if (shouldGiveup(rover, gamestate)) {
			return null;
		}

		// Updates the path if the targets changed locations
		if (mustUpdate()) {
			path = findPath(rover, gamestate, rover.getLocation(),
					target.getLocation());
		}

		// If a path couldnt be found, return null
		if (path == null) {
			return null;
		}

		// if the path is empty, we are already at the player so do not need to
		// move
		if (path.isEmpty()) {
			return rover.getLocation();
		}

		return path.remove(0);

	}

}
