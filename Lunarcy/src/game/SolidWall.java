package game;
/**
 * Represents a wall in-game that cannot be walked through
 * @author Robbie
 *
 */
public class SolidWall implements Wall {
	@Override
	public boolean pass(Player player) {
		return false;
	}
}
