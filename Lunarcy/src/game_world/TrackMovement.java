package game_world;

import java.util.List;

/**
 * Track Movement: Follow a Player around the map, computing the shortest path
 * to them and trying to reach them. Will give up after a set amount of time.
 * 
 * @author b
 *
 */
public class TrackMovement extends ShortestPathMover {

	Player target; // The player we are chasing
	Location lastSeen; // Where this player was last spotted

	public TrackMovement(Player target) {
		this.target = target;
		lastSeen = target.getLocation();
	}

	public List<Location> move(Location currentLocation) {
		// They have moved, so we must recompute

		if (target.getLocation() != lastSeen) {
			return findPath(currentLocation, target.getLocation());
		}

		// They have not moved, so we do not need a new path
		return null;

	}

}
