package game_world;

import java.util.List;

public interface MoveStrategy {
	public List<Location> move(Location currentLocation);
}

/**
 * RoamMovement: Choose N random squares,
 * from these squares choose the square where
 * the most Players have been seen. Use djikstras
 * to find a path to this square, and keep moving until
 * a Player is spotted.
 * 
 * @author b
 *
 */
class RoamMovement implements MoveStrategy{
	
	private final int MAXSQUARES = 3; //How many random squares to check
	private int[][] board; //TEMPORARY until Board class created
	
	/**
	 * Find a path from currentLocation to a randomly chosen
	 * square (prioritise popular squares)
	 * 
	 * @return path to the end location
	 */
	public List<Location> move(Location currentLocation) {
		
		int playersSeen = -1; //Choose the square where most players have been seen, start as -1 so we always find a square
		Location mostVisited = null; //The square where most have been seen
		
		for(int i=0; i < MAXSQUARES; i++){
			
			Location rand = new Location((int)Math.random()*board.length,  //Choose a random location on the board
					(int)Math.random()*board[0].length);
			
			if(board[rand.getX()][rand.getY()] > playersSeen){ //New most visited square has been found
				mostVisited = rand;
			}
					
		}
		
		return null;
		
	}

	

}

