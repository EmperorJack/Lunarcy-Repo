package bots;

import java.util.List;

import game.Location;
import game.Player;
import game.Square;

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
		Location rand = new Location((int) (Math.random() * board.length), (int) (Math.random() * board[0].length));

		return findPath(board, currentLocation, rand);
	}

	/**
	 * If there is a player in the Rovers view,
	 * return the first player we come accross, 
	 * else null.
	 * 
	 * @return
	 */
	
	public Player viewTarget(){
		//TODO: Iterate board, trying to find player
		
		return null;
	}
	
	
	@Override
	protected boolean mustUpdate(List<Location> path) {
		return path.isEmpty();
	}

}
