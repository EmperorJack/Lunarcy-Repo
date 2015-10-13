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
public class Player implements Character, Serializable {
	private static final long serialVersionUID = 606752819269306395L;

	private static final int MAX_OXYGEN = 200;

	private final int MAX_INVENTORY_SIZE = 7;

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

	public void resetOxygen() {
		oxygen = getMaxOxygen();
	}

	public boolean giveItem(Item item) {
		if (item == null || inventory.size() >= MAX_INVENTORY_SIZE)
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
			if (i.getEntityID() == itemID) {
				item = i;
				break;
			}
		}
		if (item != null && inventory.contains(item)) {
			inventory.remove(item);
			return item;
		}
		return null;
	}

	public int getOxygen() {
		return oxygen;
	}

	public int getMaxOxygen() {
		return MAX_OXYGEN;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		if (location == null || location.getX() < 0 || location.getY() < 0) {
			return;
		}
		this.location = location;
	}

	public List<Item> getInventory() {
		return new ArrayList<Item>(inventory);
	}

	public List<ShipPart> getShipParts() {
		List<ShipPart> temp = new ArrayList<ShipPart>();
		for (Item i : inventory) {
			if (i instanceof ShipPart) {
				temp.add((ShipPart) i);
			}else if(i instanceof Container){
				for(Item item: ((Container)i).getItems()){
					if (item instanceof ShipPart) {
						temp.add((ShipPart) item);
					}
				}
			}
		}
		return temp;
	}

	public boolean hasKey(int keyCode) {
		for (Item i : inventory) {
			if (i instanceof Key) {
				if (((Key) i).keyCode == keyCode) {
					return true;
				}
			}else if(i instanceof Container){
				for(Item item: ((Container)i).getItems()){
					if (item instanceof Key) {
						if (((Key) item).keyCode == keyCode) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks the players inventory and any containers inside for an item with matching entityID
	 * @param itemID
	 * @return
	 */
	public boolean hasItem(int itemID){
		for(Item i: inventory){
			if(i.getEntityID() == itemID){
				return true;
			}else if(i instanceof Container){
				for(Item item: ((Container)i).getItems()){
					if(item.getEntityID() == itemID){
						return true;
					}
				}
			}
		}
		return false;
	}

	public Item getItem(int itemID){
		for(Item i: inventory){
			if(i.getEntityID() == itemID){
				return i;
			}
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

	public void depleteOxygen() {
		this.oxygen = 0;
	}

	/**
	 * Returns true if the player has armout
	 * in their inventory
	 * @return
	 */
	public boolean hasArmour() {
		for (Item i : inventory) {
			if (i instanceof Armour) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the player has a cloaking gadget
	 * in their inventory
	 * @return
	 */
	public boolean hasCloak() {
		for (Item i : inventory) {
			if (i instanceof CloakingGadget) {
				return true;
			}
		}
		return false;
	}

	/**
	 * FOR TESTING PURPOSES Adds some items to the players inventory
	 */
	public void testAddItems() {
		if (inventory == null)
			return;
		inventory.add(new ShipPart(id * 100, 0));
		inventory.add(new ShipPart(id * 100 + 1, 1));
		inventory.add(new ShipPart(id * 100 + 2, 2));
		inventory.add(new ShipPart(id * 100 + 3, 3));
		inventory.add(new CloakingGadget(id*100 + 4));
		Bag b = new Bag(id * 100 + 5);
		b.addItem(new Key(id*100+7, 1));
		b.addItem(new Key(id*100+8, 3));
		b.addItem(new Key(id*100+9, 3));
		inventory.add(b);
		inventory.add(new Armour(id * 100 + 6));
	}

}
