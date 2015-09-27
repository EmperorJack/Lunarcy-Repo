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
	 * @param player The Player to be moved
	 * @param direction The direction to move the Player in
	 * @return True if the player was moved, False if they cannot
	 * @throws IllegalArgumentException if either parameter is null
	 */
	public boolean movePlayer(Player player, Direction direction){
		if (player == null)
			throw new IllegalArgumentException("Parameter 'player' may not be null");
		if (direction == null)
			throw new IllegalArgumentException("Parameter 'direction' may not be null");
		
		Square dest = state.getSquare(player.getLocation().getAdjacent(direction));
		
		if(dest != null && dest.canEnter(player, direction)){
			dest.addPlayer(player);
			player.move(direction);
			return true;
		}
		return false;
	}
}