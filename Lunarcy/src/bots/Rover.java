package bots;

import java.util.List;

import game.GameState;
import game.Location;

import java.util.ArrayList;

/**
 * A rover is a 'bot' in the game. Each rover can be following one of three
 * movement strategies, at a given time.
 * 
 * 1. Roam: Follow a seemingly random path (prioritising popular squares),
 * change to #3 when a player is spotted
 * 
 * 2. Protect: Move around the map on a set path, this will not differ
 * throughout the game (ie will not change to Track or Roam)
 * 
 * 3. Track: Track and follow a specific Player, using shortest path to attempt
 * to capture them. Gives up after a set number of ticks, and reverts to
 * the Roam Strategy.
 * 
 * @author b
 *
 */
public class Rover {

	private ShortestPathMover movementStrategy;
	private List<Location> path; // Which squares the rover is going to follow
	private Location currentLocation; // Mantains the rovers position (x,y) at any given time
	private GameState gameState;

	Rover(GameState gameState, ShortestPathMover movementStrategy) {
		this.gameState = gameState;
		this.movementStrategy = movementStrategy;
		this.path = new ArrayList<Location>();
	}

	public void move() {
		// If we need a new path, update the current path
		if (movementStrategy.mustUpdate(path)){
			path = movementStrategy.path(gameState.getBoard(), currentLocation);
		}
		
		// Move along one step in the path
		// removing the location we visit
		currentLocation = path.remove(0);

	}

	public void updateMoveStrategy(ShortestPathMover movementStrategy) {
		this.movementStrategy = movementStrategy;
	}

}
