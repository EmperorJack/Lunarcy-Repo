package game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Player character and contains all the information about the
 * Player e.g orientation, position, inventory, identification
 *
 * @author Robbie
 *
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 606752819269306395L;

	private final int id;
	private final String name;
	private Color colour;

	private Location location;
	private Direction orientation;
	private int oxygen;
	private List<Item> inventory;

	public Player(int uniqueID, String name, Color colour, Location location,
			Direction orientation) {
		this.id = uniqueID;
		this.name = name;
		this.location = location == null ? new Location(0, 0) : location;
		this.orientation = orientation == null ? Direction.NORTH : orientation;
		this.oxygen = 200;
		this.inventory = new ArrayList<Item>();
		this.colour = colour;
		testAddItems();
	}

	public void move(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException(
					"Parameter 'direction' may not be null");
		location = location.getAdjacent(direction);
	}

	public void modifyOxygen(int amount) {
		oxygen += amount;
		if (oxygen > 200) {
			oxygen = 200;
		} else if (oxygen < 0) {
			oxygen = 0;
		}
	}

	public int getOxygen() {
		return oxygen;
	}

	public Location getLocation() {
		return location;
	}

	public List<Item> getInventory() {
		return new ArrayList<Item>(inventory);
	}

	public boolean giveItem(Item item) {
		if (item == null)
			return false;
		return inventory.add(item);
	}

	/**
	 * Searches the players inventory to find a matching item and then removes
	 * it
	 *
	 * @param itemID
	 *            The enitiyID of the item being removed
	 * @return The Item that was removed, null if no match was found
	 */
	public Item removeItem(int itemID) {
		if (itemID < 0)
			return null;
		Item item = null;
		for (Item i : inventory) {
			if (i.entityID == itemID) {
				item = i;
			}
		}
		if (item != null && inventory.contains(item)) {
			inventory.remove(item);
			return item;
		}
		return null;
	}

	public Direction getOrientation() {
		return orientation;
	}

	public void turnLeft() {
		orientation = orientation.left();
	}

	public void turnRight() {
		orientation = orientation.right();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Color getColour() {
		return colour;
	}

	/**
	 * FOR TESTING PURPOSES Adds some items to the players inventory
	 */
	public void testAddItems() {
		if (inventory == null)
			return;
		inventory.add(new ShipPart(id * 100, 0));
		inventory.add(new Key(id * 100 + 1, 0));
		inventory.add(new Key(id * 100 + 2, 1));
		inventory.add(new ShipPart(id * 100 + 3, 1));
	}
}
