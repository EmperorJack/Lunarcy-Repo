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
	 * Used to check a distance between a player and a rover
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	private int distance(Rover rover, Player player) {
		Location start = rover.getLocation();
		Location end = player.getLocation();

		return Math.abs(start.getX() - end.getX()) + // Horizontal difference +
				Math.abs(start.getY() - end.getY()); // Vertical Difference
	}

	/**
	 * Finds and returns the closest player (based on manhattan distance) to the
	 * rover.
	 *
	 * @return
	 */
	public Player viewTarget(Rover rover, GameState gameState) {

		// Start with largest possible distance
		int closestDistance = Integer.MAX_VALUE;

		Player closestPlayer = null;

		Square[][] board = gameState.getBoard();

		// Check the whole board
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[y].length; x++) {

				// Only check for players if its a walkable square
				if (board[y][x] instanceof WalkableSquare) {

					WalkableSquare square = (WalkableSquare) board[y][x];

					// Since it is a set of players, must make an iterator
					Iterator<Player> iter = square.getPlayers().iterator();

					// If there are players in the square, return the "first"
					// which is visible
					while (iter.hasNext()) {
						Player player = iter.next();

						// If a players outside, without a cloak, check the
						// distance
						if (gameState.isOutside(player.getLocation())
								&& !player.hasCloak()
								&& distance(rover, player) < closestDistance) {

							closestDistance = distance(rover, player);
							closestPlayer = player;
						}
					}
				}
			}
		}

		// Return the closest player we found, null if no player
		return closestPlayer;
	}

}
