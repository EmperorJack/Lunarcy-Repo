package bots;

import java.util.Iterator;
import java.util.List;

import game.Location;
import game.Player;
import game.Square;
import game.WalkableSquare;

/**
 * RoamMovement: Choose N random squares, from these squares choose the square
 * where the most Players have been seen.
 *
 * @author b
 *
 */

public class RoamMovement extends ShortestPathMover {

	/**
	 * Find a path from currentLocation to a randomly chosen square.
	 *
	 * @return path to the end location
	 */

	public List<Location> path(Square[][] board, Location currentLocation) {

		// Choose a random location on the board
		Location rand = new Location((int) (Math.random() * board.length),
				(int) (Math.random() * board[0].length));

		return findPath(board, currentLocation, rand);
	}

	/**
	 * If there is a player in the Rovers view, return the first player we come
	 * accross, else null.
	 *
	 * @return
	 */

	public Player viewTarget(Square[][] board) {

		// TODO: Narrow viewing down to rovers perspective

		// Check the board
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				// Only check players if its a walkable square

				if (board[y][x] instanceof WalkableSquare) {
					WalkableSquare square = (WalkableSquare) board[y][x];

					Iterator<Player> iter = square.getPlayers().iterator();

					// If there are players in the square, return the "first"
					if (iter.hasNext()) {
						return iter.next();
					}
				}
			}
		}

		return null;
	}

	@Override
	protected boolean mustUpdate(List<Location> path) {
		return path.isEmpty();
	}

}
