package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private static final long serialVersionUID = 7008891830061539518L;

	private final String name;
	private final String description;

	private Map<Direction, List<Item>> items;
	private Map<Direction, Container> containers;
	private Set<Player> players;
	private boolean inside;

	public WalkableSquare(String name, String description, boolean inside,
			Wall north, Wall east, Wall south, Wall west) {

		this.name = name;
		this.description = description;
		this.inside = inside;

		items = new HashMap<Direction, List<Item>>();
		containers = new HashMap<Direction, Container>();

		//populate with empty sets to avoid Null Pointers
		for(Direction dir: Direction.values()){
			items.put(dir, new ArrayList<Item>());
			containers.put(dir, null);
		}
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
			player.modifyOxygen(inside ? 4 : -1);
		}
	}

	public boolean canEnter(Character character, Direction direction) {
		if (character == null||direction == null)
			return false;
		return walls.get(direction).canPass(character);
	}

	public boolean canExit(Character character, Direction direction) {
		if (character == null||direction == null)
			return false;
		return walls.get(direction).canPass(character);
	}

	public boolean addPlayer(Player player) {
		if (player != null){
			return players.add(player);
		}
		return false;
	}

	public void removePlayer(Player player) {
		if (player != null) {
			if (players.contains(player)) {
				players.remove(player);
			}
		}
	}

	public List<Item> getItems(Direction side) {
		if (side == null)
			return null;
		return new ArrayList<Item>(items.get(side));
	}

	public Container getContainer(Direction side){
		if(side==null)
			return null;
		return containers.get(side);
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
		if (side == null||item == null)
			return false;

		//Initialise the set if it hasn't been done yet
		//This is required as the data loading does not call the constructor on any objects
		if(items.get(side) == null){
			items.put(side, new ArrayList<Item>());
		}

		return items.get(side).add(item);
	}

	/**
	 * Adds the container to the specified side of the room.
	 *
	 * @param side
	 *            the side of the Square to add the container to
	 * @param container
	 *            the container to add
	 * @return True if container could be added, False otherwise
	 */
	public boolean setContainer(Direction side, Container container) {
		if (side == null)
			return false;
		containers.put(side,container);
		return true;
	}

	/**
	 * Adds the container to the specified side of the room.
	 *
	 * @param side
	 *            the side of the Square to add the container to
	 * @param container
	 *            the container to add
	 * @return True if container could be added, False otherwise
	 */
	public boolean removeContainer(Direction side) {
		if (side == null)
			return false;
		containers.remove(side);
		return true;
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

	public Item takeItem(Direction side, int itemID){
		if (side == null||itemID < 0)
			return null;

		Item item = null;
		for(Item i: items.get(side)){
			if(i.entityID == itemID){
				item = i;
			}
		}
		items.get(side).remove(item);
		return item;
	}

	/**
	 * Checks if there is a container in the Entity set in the specified direction
	 * @param direction The direction to check
	 * @return True if there is a Container, False otherwise
	 */
	public boolean hasContainer(Direction direction){
		if(direction==null)
			return false;
		return containers.containsKey(direction) && containers.get(direction) != null;
	}

	public String getName() {
		return "Walkable Square";
	}

	public String getDescription() {

		String desc = "This square contains the following items: ";
		boolean hasItem = false;

		for(Direction dir: Direction.values()){

			for(Item i: items.get(dir)){
				desc += i.getName()+", ";
				hasItem = true;
			}

		}
		desc += " \n";

		//If there were no items
		if(!hasItem){
			return "This square has no items";
		}

		return desc;
	}

	public boolean isInside() {
		return inside;
	}
}
