package game;

import java.io.Serializable;
import java.util.List;
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
	private static final long serialVersionUID = 537178002390002546L;

	protected Map<Direction, Wall> walls;

	/**
	 * Checks whether the specified player can enter the room *FROM* the
	 * direction
	 *
	 * @param player
	 *            The player that is attempting to enter
	 * @param direction
	 *            The direction the player is entering the room *FROM*
	 * @return True if the player may enter, False otherwise
	 */
	public abstract boolean canEnter(Character character, Direction direction);

	/**
	 * Checks whether the specified player can exit the room *IN* the
	 * direction
	 *
	 * @param player
	 *            The player that is attempting to enter
	 * @param direction
	 *            The direction the player is exiting the room *IN*
	 * @return True if the player may exit, False otherwise
	 */
	public abstract boolean canExit(Character character, Direction direction);

	/**
	 * Adds the Player to the set of Players in the room.
	 *
	 * @param player
	 *            The player that is being added
	 * @return True if player was added to the room, False otherwise
	 */
	public abstract boolean addPlayer(Player player);

	/**
	 * Removes the Player from the set of Players in the room.
	 *
	 * @param player
	 *            The player that is being removed
	 */
	public abstract void removePlayer(Player player);
	public abstract void tick();

	/**
	 * Get the list of items on a certain side of the room.
	 *
	 * @param side
	 *            the side of the Square the items are on
	 * @return List<Item> of all the items on that side of the room
	 */
	public abstract List<Item> getItems(Direction side);

	/**
	 * Get the container on a certain side of the room, if there is one
	 * Will not return just plain furniture
	 *
	 * @param side
	 *            the side of the Square the entities are on
	 * @return The container on that side of the room, if there is one
	 */
	public abstract SolidContainer getContainer(Direction side);

	/**
	 * Get the furniture on a certain side of the Square, if there is one
	 * Will also return containers
	 *
	 * @param side
	 *            the side of the Square get the furniture from
	 * @return The furniture on that side of the room, if there is one
	 */
	public abstract Furniture getFurniture(Direction side);

	public Square(){
		walls = new HashMap<Direction, Wall>();
	}

	public Map<Direction, Wall> getWalls() {
		return walls;
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
		walls.put(dir, new EmptyWall());
	}

	public void addLockedDoor(Direction dir, int access) {
		walls.put(dir, new LockedDoor(access));

	}
}
