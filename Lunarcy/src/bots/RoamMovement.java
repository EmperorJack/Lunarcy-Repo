package bots;

import java.util.List;

import game.Location;
import game.Square;

/**
 * RoamMovement: Choose N random squares, from these squares choose the square
 * where the most Players have been seen.
 * 
 * @author b
 *
 */

public class RoamMovement extends ShortestPathMover {

	private final int MAXSQUARES = 3; // How many random squares to check

	/**
	 * Find a path from currentLocation to a randomly chosen square.
	 * 
	 * @return path to the end location
	 */

	public List<Location> path(Square[][] board, Location currentLocation) {

		int playersSeen = -1;
		Location mostVisited = null;

		// Choose a random location on the board
		Location rand = new Location((int) (Math.random() * board.length), (int) (Math.random() * board[0].length));

		return findPath(board, currentLocation, mostVisited);
	}

	@Override
	protected boolean mustUpdate(List<Location> path) {
		return path.isEmpty();
	}

}
