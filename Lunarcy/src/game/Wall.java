package game;

/**
 * Represents a wall of a Square, may or may not be able to pass through the wall.
 * 
 * @author Robbie
 *
 */
public interface Wall {
	public boolean enter(Player player);
}
