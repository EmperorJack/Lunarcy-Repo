package game;

/** 
 * This class controls the interaction between input and the GameState 
 * 
 * @author Robbie
 *
 */
public class GameLogic {
	private GameState state;
	
	public GameLogic(GameState state){
		this.state = state;
	}
	
	public GameLogic(int mapWidth, int mapHeight){
		this.state = new GameState(mapWidth,mapHeight);
	}
	
	/**
	 * Checks if a player can enter a square and if they can will move them into that square
	 * @param playerId The ID of the Player to be moved
	 * @param direction The direction to move the Player in
	 * @return True if the player was moved, False if they cannot
	 * @throws IllegalArgumentException if either direction is null
	 */
	public boolean movePlayer(int playerID, Direction direction){
		if (direction == null)
			throw new IllegalArgumentException("Parameter 'direction' may not be null");
		Player player = state.getPlayer(playerID);
		Square dest = state.getSquare(player.getLocation().getAdjacent(direction));
		
		if(dest != null && dest.canEnter(player, direction)){
			dest.addPlayer(player);
			player.move(direction);
			return true;
		}
		return false;
	}
	
	/**
	 * Turn a player to the left of their current orientation
	 * @param playerID the ID of the player to be turned
	 */
	public void turnPlayerLeft(int playerID){
		state.getPlayer(playerID).turnLeft();
	}
	
	/**
	 * Turn a player to the right of their current orientation
	 * @param playerID the ID of the player to be turned
	 */
	public void turnPlayerRight(int playerID){
		state.getPlayer(playerID).turnRight();
	}
}