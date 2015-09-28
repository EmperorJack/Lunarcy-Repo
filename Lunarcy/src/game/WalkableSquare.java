package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a subtype of Square that can be entered by a player, can also
 * contain items/furniture and has a wall on each direction.
 *
 * @author Robbie
 *
 */
public class WalkableSquare extends Square {

	private final String name;
	private final String description;

	private Map<Direction, Set<Item>> items;
	private Set<Player> players;
	private boolean inside;

	public WalkableSquare(String name, String description, boolean inside,
			Wall north, Wall east, Wall south, Wall west) {
		this.name = name;
		this.description = description;
		this.inside = inside;

		items = new HashMap<Direction, Set<Item>>();
		players = new HashSet<Player>();

		walls = new HashMap<Direction, Wall>();

		if (north == null) {
			north = new EmptyWall();
		}
		walls.put(Direction.North, north);

		if (east == null) {
			east = new EmptyWall();
		}
		walls.put(Direction.East, east);

		if (south == null) {
			south = new EmptyWall();
		}
		walls.put(Direction.South, south);

		if (west == null) {
			west = new EmptyWall();
		}
		walls.put(Direction.West, west);
	}

	/**
	 * Checks whether the specified player can enter the room *FROM* the
	 * direction parameter
	 * 
	 * @param player
	 *            The player that is attempting to enter
	 * @param direction
	 *            The direction the player is entering the room *FROM*
	 * @return True if the player may enter, False otherwise
	 * @throws IllegalArgumentException
	 *             if either argument is null
	 */
	public boolean canEnter(Player player, Direction direction) {
		if (player == null)
			throw new IllegalArgumentException(
					"Parameter 'player' may not be null");
		if (direction == null)
			throw new IllegalArgumentException(
					"Parameter 'direction' may not be null");
		return walls.get(direction).enter(player);
	}

	/**
	 * Adds the Player to the set of Players in the room.
	 * 
	 * @param player
	 *            The player that is attempting to enter
	 * @return True if player was added to the room, False otherwise
	 */
	public boolean addPlayer(Player player) {
		if (player != null){
			return players.add(player);
		}
		return false;
	}

	/**
	 * Adds the Player to the set of Players in the room.
	 * 
	 * @param player
	 *            The player that is attempting to enter
	 * @return True if player was added to the room, False otherwise
	 */
	public void removePlayer(Player player) {
		if (player != null) {
			if (players.contains(player)) {
				players.remove(player);
			}
		}
	}

	/**
	 * Get the set of items on a certain side of the room. Note: Modifying the
	 * returned set will not change the items in the room
	 * 
	 * @param side
	 *            the side of the Square the items are on
	 * @return Set<Item> of all the items on that side of the room
	 * @throws IllegalArgumentException
	 *             if argument is null
	 */
	public Set<Item> getItems(Direction side) {
		if (side == null)
			throw new IllegalArgumentException(
					"Parameter 'side' may not be null");
		return new HashSet<Item>(items.get(side));
	}

	/**
	 * Adds the item to the set of items on the specified side of the room.
	 *
	 * @param side
	 *            the side of the Square to add the item to
	 * @param item
	 *            the item to add
	 * @return True if item could be added, False otherwise
	 */
	public boolean addItem(Direction side, Item item) {
		if (side == null)
			throw new IllegalArgumentException(
					"Parameter 'side' may not be null");
		if (item == null)
			throw new IllegalArgumentException(
					"Parameter 'item' may not be null");
		return items.get(side).add(item);
	}

	/**
	 * Get the set of all the players in the room Note: Modifying the returned
	 * set will not change the players in the room
	 * 
	 * @return Set<Player> of all players inside the room
	 */
	public Set<Player> getPlayers() {
		return new HashSet<Player>(players);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean isInside() {
		return inside;
	}
}
