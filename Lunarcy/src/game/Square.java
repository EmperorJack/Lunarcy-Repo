package game;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a square on the board, the base class has very little
 * functionality as blank squares cannot contain items/players or be entered.
 *
 * @author Robbie
 *
 */
public abstract class Square implements Serializable {
	protected Map<Direction, Wall> walls;

	public abstract boolean canEnter(Player player, Direction direction);
	public abstract boolean canExit(Player player, Direction direction);
	public abstract boolean addPlayer(Player player);
	public abstract void removePlayer(Player player);
	public abstract void tick();

	public Square(){
		walls = new HashMap<Direction, Wall>();
	}

	/**
	 * Toggles a wall on or off. Used for the map builder.
	 *
	 * @param dir
	 * @author Kelly
	 */
	public void addWall(Direction dir) {
		walls.put(dir, new SolidWall());
	}

	public void removeWall(Direction dir) {
		walls.put(dir, new EmptyWall());
	}
	public void addDoor(Direction dir) {
		walls.put(dir, new Door());
	}

	public void removeDoor(Direction dir) {
		walls.put(dir, new Door());
	}


	public Map<Direction, Wall> getWalls() {
		return walls;
	}
}
