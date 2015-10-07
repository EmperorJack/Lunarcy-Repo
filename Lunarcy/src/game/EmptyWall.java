package game;

/**
 * Represents an in-game wall that is empty
 * and so, from a gameplay perspective, it doesn't exist
 * @author Robbie
 *
 */
public class EmptyWall implements Wall {
	private static final long serialVersionUID = -995852244184737539L;

	@Override
	public boolean pass(Player player) {
		return true;
	}
}
