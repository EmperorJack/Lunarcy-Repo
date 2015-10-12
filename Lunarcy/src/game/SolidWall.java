package game;
/**
 * Represents a wall in-game that cannot be walked through
 * @author Robbie
 *
 */
public class SolidWall implements Wall {
	private static final long serialVersionUID = -4326090500543375072L;

	@Override
	public boolean canPass(Character character) {
		return false;
	}
}
