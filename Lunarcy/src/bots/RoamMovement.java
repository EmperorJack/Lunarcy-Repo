package bots;

import java.util.List;

import game.Location;

/**
 * RoamMovement: Choose N random squares, from these squares choose the square
 * where the most Players have been seen.
 * 
 * @author b
 *
 */

public class RoamMovement extends ShortestPathMover {

	private final int MAXSQUARES = 3; // How many random squares to check
	private int[][] board; // TEMPORARY until Board class created

	/**
	 * Find a path from currentLocation to a randomly chosen square (prioritise
	 * popular squares)
	 * 
	 * @return path to the end location
	 */

	public List<Location> move(Location currentLocation) {

		int playersSeen = -1;
		Location mostVisited = null;

		// Iterate MAXSQUARES random squares, choosing the most visited one
		for (int i = 0; i < MAXSQUARES; i++) {

			// Choose a random location on the board
			Location rand = new Location((int) Math.random() * board.length, (int) Math.random() * board[0].length);

			// New most visited square has been found
			if (board[rand.getX()][rand.getY()] > playersSeen) {
				mostVisited = rand;
			}

		}

		return findPath(currentLocation, mostVisited);
	}

}
