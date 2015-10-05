package game;

/**
 * Represents an in-game wall that is empty
 * and so, from a gameplay perspective, it doesn't exist
 * @author Robbie
 *
 */
public class EmptyWall implements Wall {
	@Override
	public boolean enter(Player player) {
		return true;
	}
}
