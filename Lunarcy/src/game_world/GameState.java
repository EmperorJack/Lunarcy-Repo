package game_world;

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
}
