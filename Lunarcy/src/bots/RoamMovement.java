package bots;

import java.util.Iterator;
import game.Direction;
import game.GameState;
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

@SuppressWarnings("serial")
public class RoamMovement implements MoveStrategy {

	/**
	 * Find a path from currentLocation to a randomly chosen square.
	 *
	 * @return path to the end location
	 */

	public Location nextStep(Rover rover, GameState gamestate) {
		
		//Randomly rotate the rover
		if(Math.random() > 0.8){
			rover.setOrientation(Direction.randomDirection());
		}
		
		int rotated = 0;
		
		//Keep rotating until you are facing a valid direction
		while(!validMove(gamestate, rover, rover.getOrientation())){
			
			rover.rotate();
			
			rotated++;
			
			//Once you have rotated more than four times, you're stuck so return
			if(rotated > 4){
				return null;
			}
		}
		
		//At this point the rover must be facing a direction which they can move in
		return rover.getLocation().getAdjacent(rover.getOrientation());
		
	}
	
	private boolean validMove(GameState state, Rover rover, Direction direction){
		Square src = state.getSquare(rover.getLocation());
		Square dest = state.getSquare(rover.getLocation().getAdjacent(direction));

		if(src!=null && dest!=null){
			return src.canExit(rover,direction) && dest.canEnter(rover, direction.opposite());
		}
		return false;
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
			for (int x = 0; x < board[y].length; x++) {
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

}
