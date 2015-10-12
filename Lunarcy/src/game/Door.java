package game;

/**
 * Represents a wall with an unlocked Door that may be entered by anyone
 * @author Robbie
 *
 */
public class Door implements Wall {
	private static final long serialVersionUID = 908914959512451832L;

	@Override
	public boolean canPass(Character character) {
		return character instanceof Player;
	}
}
