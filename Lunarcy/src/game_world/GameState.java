package game_world;

import java.util.List;

public class GameState {
	Square[][] board;
	List<Player> players;
	
	public GameState(int mapWidth, int mapHeight){
		board = new Square[mapWidth][mapHeight];
	}
}
