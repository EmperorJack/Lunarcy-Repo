package bots;

import java.util.Iterator;
import game.Direction;
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

public class RoamMovement implements MoveStrategy {
	
	@Override
	public Location nextStep(Rover rover, Square[][] board) {		
		//Keep rotating the rover right if it can not move
		for(int attempt = 0; !validMove(rover, board, rover.getOrientation()); attempt++){
			
			rover.rotateRight();
			
			//Must be stuck, as we  have checked all directions
			if(attempt>4) {
				return null;
			}
		}
		
		return rover.getLocation().getAdjacent(rover.getOrientation());
	}
	
	public boolean validMove(Rover rover, Square[][] board, Direction dir) {

		Location source = rover.getLocation();
		Location destination = source.getAdjacent(dir);

		// If either of the locations are off the board, its not valid
		if (!validSquare(board, source) || !validSquare(board, destination)) {
			return false;
		}
		
		Square src = board[source.getY()][source.getX()];
		Square dest = board[destination.getY()][destination.getX()];

		// If the squares are valid, check if we can exit src and enter dest
		if (src != null && dest != null) {
			return src.canExit(rover, dir) && dest.canEnter(rover, dir.opposite());
		}

		return false;
	}

	private boolean validSquare(Square[][] board, Location loc) {
		if (loc.getX() < 0 || loc.getX() > board[0].length) {
			return false;
		}

		if (loc.getY() < 0 || loc.getY() > board.length) {
			return false;
		}

		return true;
	}

	/**
	 * If there is a player in the Rovers view, return the first player we come
	 * accross, else null.
	 *
	 * @return
	 */

	public Player viewTarget(Square[][] board, Rover rover) {
		
		
		int startX = 0;
		int startY = 0;
		int endX = board[0].length;
		int endY = board.length;
		
		//Narrow down the view based on rovers direction
		if(rover.getOrientation().equals(Direction.NORTH)){
			startX = rover.getLocation().getX();
			endX = rover.getLocation().getX();
			startY = 0;
			endY = rover.getLocation().getY();
		}
		
		if(rover.getOrientation().equals(Direction.SOUTH)){
			startX = rover.getLocation().getX();
			endX = rover.getLocation().getX();
			startY = rover.getLocation().getY();
			endY = board.length;
		}
		
		if(rover.getOrientation().equals(Direction.EAST)){
			startX = rover.getLocation().getX();
			endX = board[0].length;
			startY = rover.getLocation().getY();
			endY = rover.getLocation().getY();
		}
		
		if(rover.getOrientation().equals(Direction.WEST)){
			startX = 0;
			endX = rover.getLocation().getX();
			startY = rover.getLocation().getY();
			endY = rover.getLocation().getY();
		}

		// Check the board
		for (int y = startY; y < endY; y++) {
			for (int x = startX; x < endX; x++) {
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
