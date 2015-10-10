package bots;

import java.util.ArrayList;
import java.util.List;

import game.GameState;
import game.Location;
import game.Player;

/**
 * Track Movement: Follow a Player around the map, computing the shortest path
 * to them and trying to reach them. Will give up after a set amount of time.
 *
 * @author b
 *
 */

@SuppressWarnings("serial")
public class TrackMovement extends ShortestPathMover {

	// The player we are chasing
	private Player target;
	
	//The path we are currently following
	private List<Location> path;
	
	public TrackMovement(Player target) {
		this.target = target;
		this.path = new ArrayList<Location>();
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
	 * Gives up if the player is inside
	 *
	 * @return
	 */
	public boolean shouldGiveup(GameState gamestate) {

		// If the players inside give up
		if (!gamestate.isOutside(target.getLocation())) {
			return true;
		}

		return false;
	}

	/**
	 * Finds the shortest path from currentLocation, to the target field.
	 * Returns this path if one was found, or null.
	 *
	 * @param currentLocation:
	 *            Where the rover is currently located
	 * @return the path
	 */
	public Location nextStep(Rover rover, GameState gamestate) {
		if (shouldGiveup(gamestate))
			return null;
		
		if(mustUpdate()){
			path = findPath(rover, gamestate.getBoard(), rover.getLocation(), target.getLocation());
		}

		return path.remove(0);
		
	}

}
