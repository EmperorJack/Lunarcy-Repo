package game;

/**
 * Represents a wall with an unlocked Door that may be entered by anyone
 * @author Robbie
 *
 */
public class Door implements Wall {
	@Override
	public boolean pass(Player player) {
		return true;
	}
}
