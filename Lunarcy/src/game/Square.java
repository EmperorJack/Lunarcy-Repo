package game;

/**
 * Represents a square on the board, the base class has very little
 * functionality as blank squares cannot contain items/players or be entered.
 * 
 * @author Robbie
 *
 */
public interface Square {
	public boolean canEnter(Player player, Direction direction);
	public boolean addPlayer(Player player);
	public void removePlayer(Player player);
}
