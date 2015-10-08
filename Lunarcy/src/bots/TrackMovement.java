package bots;

import java.util.List;

import game.Location;
import game.Player;
import game.Square;

/**
 * Track Movement: Follow a Player around the map, computing the shortest path
 * to them and trying to reach them. Will give up after a set amount of time.
 *
 * @author b
 *
 */
public class TrackMovement extends ShortestPathMover {

	// The player we are chasing
	private Player target;

	//Keep track of how many ticks we have been following player for
	private int tickCounter;

	//How many ticks to give up after
	private final int MAXTICKS = 50;

	private List<Location> path;
	
	public TrackMovement(Player target) {
		this.target = target;
		this.tickCounter = 0;
	}

	/**
	 * Returns true if any of the following conditions hold
	 *
	 * 1. The paths end location does not equal the target players location 2.
	 * The path is null 3. The path is empty
	 *
	 * Otherwise returns false
	 */
	@Override
	public boolean mustUpdate() {
		if (path == null || path.isEmpty())
			return true;

		// Return true if the end location does not equal the
		// target location, false otherwise
		return !path.get(path.size() - 1).equals(target.getLocation());
	}
	
	public Location nextStep(Rover rover, Square[][] board){
		if(mustUpdate()){
			path = findPath(board, rover, rover.getLocation(), target.getLocation());
		}
		return path.get(0);
	}

	/**
	 * Increments the tick counter,
	 * and returns whether the ticks are less
	 * than the maximum ticks.
	 *
	 * @return
	 */
	public boolean shouldGiveup(){
		return ++tickCounter > MAXTICKS;
	}

}
