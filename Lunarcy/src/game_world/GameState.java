package game_world;

import game_world.map.Square;

import java.util.List;

/**
 * This class contains all the information about the game state,
 * it does not contain any of the logic for the game but should instead be
 * modified by another class containing the game logic.
 * 
 * @author Robbie
 *
 */
public class GameState {
	private Square[][] board;
	private List<Player> players;
	
	public GameState(int mapWidth, int mapHeight){
		board = new Square[mapWidth][mapHeight];
	}
	
	/**
	 * @param location - A location to retrieve the square from
	 * @return The Square at the (x,y) coordinate, from the location, on the board
	 * @throws IllegalArgumentException if argument is null
	 */
	public Square getSquare(Location location){
		if(location==null){
			throw new IllegalArgumentException("Parameter 'location' may not be null.");
		}
		int x = location.getX();
		int y = location.getY();
		if(x<0||x>=board.length||y<0||y>=board[0].length){
			return null;
		}else{
			return board[x][y];
		}
	}
	
	/**
	 * 
	 * @param location - The location of the Square to be replaced
	 * @param square - The Square that will take the location in the array
	 * @return The Square that was replaced, Null if the location does not exist on the board
	 * @throws IllegalArgumentException if argument is null
	 */
	public Square setSquare(Location location, Square square){
		if(location==null){
			throw new IllegalArgumentException("Parameter 'location' may not be null.");
		}if(square==null){
			throw new IllegalArgumentException("Parameter 'square' may not be null.");
		}
		int x = location.getX();
		int y = location.getY();
		if(x<0||x>=board.length||y<0||y>=board[0].length){
			return null;
		}else{
			Square old = board[x][y];
			board[x][y] = square;
			return old;
		}
	}
}
