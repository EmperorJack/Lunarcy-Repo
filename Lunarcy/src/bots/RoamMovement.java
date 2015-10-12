package bots;

import java.util.Iterator;
import game.Direction;
import game.GameState;
import game.Location;
import game.Player;
import game.Square;
import game.WalkableSquare;

/**
 * RoamMovement: Keep moving in the direction you are facing, when you can no
 * longer move in that direction, turn right.
 *
 * Will randomly change directions when moving as to not follow the edges of the
 * map.
 *
 * @author evansben1
 *
 */

@SuppressWarnings("serial")
public class RoamMovement implements MoveStrategy {

	/**
	 * Moves the rover one step in either their current direction, or if that is
	 * not possible by one step in another direction.
	 *
	 * If the rover somehow gets stuck, null will be return.
	 *
	 * @param rover
	 * @param gamestate
	 * @return location of the next step
	 */
	public Location nextStep(Rover rover, GameState gamestate) {

		// Randomly rotate the rover
		if (Math.random() > 0.8) {
			rover.setOrientation(Direction.randomDirection());
		}

		int rotated = 0;

		// Keep rotating until you are facing a valid direction
		while (!validMove(gamestate, rover, rover.getOrientation())) {

			rover.rotate();

			rotated++;

			// If you have rotated more than four times, you're stuck so return
			if (rotated > 4) {
				return null;
			}
		}

		// At this point the rover must be facing a direction which they can
		// move in
		return rover.getLocation().getAdjacent(rover.getOrientation());

	}

	/**
	 * Returns whether the rover can move in the specified direction
	 *
	 * @param state
	 * @param rover
	 * @param direction
	 * @return true if the rover can move, false otherwise
	 */
	private boolean validMove(GameState state, Rover rover, Direction direction) {
		Square src = state.getSquare(rover.getLocation());
		Square dest = state.getSquare(rover.getLocation()
				.getAdjacent(direction));

		if (src != null && dest != null) {
			return src.canExit(rover, direction)
					&& dest.canEnter(rover, direction.opposite());
		}
		return false;
	}

	/**
	 * If there is a player on the board, return the first player we come
	 * across, else null.
	 *
	 * @return
	 */
	public Player viewTarget(Square[][] board) {

		// Check the board
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[y].length; x++) {

				// Only check players if its a walkable square
				if (board[y][x] instanceof WalkableSquare) {
					WalkableSquare square = (WalkableSquare) board[y][x];

					//Since it is a set of players, make an iterator
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

}
