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

	private Map<Direction, Set<Entity>> entities;
	private Set<Player> players;
	private boolean inside;

	public WalkableSquare(String name, String description, boolean inside,
			Wall north, Wall east, Wall south, Wall west) {
		this.name = name;
		this.description = description;
		this.inside = inside;

		entities = new HashMap<Direction, Set<Entity>>();

		//populate with empty sets to avoid Null Pointers
		entities.put(Direction.NORTH, new HashSet<Entity>());
		entities.put(Direction.EAST, new HashSet<Entity>());
		entities.put(Direction.SOUTH, new HashSet<Entity>());
		entities.put(Direction.WEST, new HashSet<Entity>());

		players = new HashSet<Player>();

		walls = new HashMap<Direction, Wall>();

		if (north == null) {
			north = new EmptyWall();
		}
		walls.put(Direction.NORTH, north);

		if (east == null) {
			east = new EmptyWall();
		}
		walls.put(Direction.EAST, east);

		if (south == null) {
			south = new EmptyWall();
		}
		walls.put(Direction.SOUTH, south);

		if (west == null) {
			west = new EmptyWall();
		}
		walls.put(Direction.WEST, west);
	}

	/**
	 * Tells the Square to Update itself for one game tick
	 * Only updates the players oxygen
	 */
	public void tick() {
		for(Player player: players){
			player.modifyOxygen(inside ? 4 : -2);
		}
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
		if (player == null||direction == null)
			return false;
		return walls.get(direction).enter(player);
	}

	/**
	 * Adds the Player to the set of Players in the room.
	 *
	 * @param player
	 *            The player that is being added
	 * @return True if player was added to the room, False otherwise
	 */
	public boolean addPlayer(Player player) {
		if (player != null){
			return players.add(player);
		}
		return false;
	}

	/**
	 * Removes the Player from the set of Players in the room.
	 *
	 * @param player
	 *            The player that is being removed
	 */
	public void removePlayer(Player player) {
		if (player != null) {
			if (players.contains(player)) {
				players.remove(player);
			}
		}
	}

	/**
	 * Get the set of entities on a certain side of the room. Note: Modifying the
	 * returned set will not change the entities in the room
	 *
	 * @param side
	 *            the side of the Square the entities are on
	 * @return Set<Entity> of all the entities on that side of the room
	 */
	public Set<Entity> getEntities(Direction side) {
		if (side == null)
			return null;
		return new HashSet<Entity>(entities.get(side));
	}

	/**
	 * Adds the entity to the set of entities on the specified side of the room.
	 *
	 * @param side
	 *            the side of the Square to add the entity to
	 * @param entity
	 *            the entity to add
	 * @return True if entity could be added, False otherwise
	 */
	public boolean addEntity(Direction side, Entity entity) {
		if (side == null||entity == null)
			return false;

		//Initialise the set if it hasnt been done yet
		if(entities.get(side) == null){
			entities.put(side, new HashSet<Entity>());
		}

		return entities.get(side).add(entity);
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

	public Item takeItem(Direction side, Item item){
		if (side == null||item == null)
			return null;
		if(entities.get(side).contains(item)){
			entities.get(side).remove(item);
			return item;
		}
		return null;
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
