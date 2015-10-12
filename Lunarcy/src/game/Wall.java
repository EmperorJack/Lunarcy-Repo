package game;

import java.io.Serializable;

/**
 * Represents a wall of a Square, may or may not be able to pass through the wall.
 *
 * @author Robbie
 *
 */
public interface Wall extends Serializable {
	public boolean canPass(Character character);
}
