package game_world.map;

import game_world.Player;

/**
 * Represents a wall of a Square, may or may not be able to pass through the wall.
 * 
 * @author Robbie
 *
 */
public interface Wall {
	public boolean enter(Player player);
}
