package game_world;

/**
 * Represents a square on the board, the base class has very little
 * functionality as blank squares cannot contain items/players or be entered.
 * 
 * @author Robbie
 *
 */
public interface Square {
	public String getName();
	public String getDescription();
}
