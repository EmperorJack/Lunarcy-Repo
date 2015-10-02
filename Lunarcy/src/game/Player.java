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
	private final int id;
	private final String name;
	private Color colour;

	private Location location;
	private Direction orientation;
	private int oxygen;
	private List<Item> inventory;

	public Player(int uniqueID, String name, Location location, Direction orientation) {
		this.id = uniqueID;
		this.name = name;
		this.location = location == null ? new Location(1,1) : location;
		this.orientation = orientation == null ? Direction.NORTH : orientation;
		this.oxygen = 200;
		this.inventory = new ArrayList<Item>();
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

	public boolean giveItem(Item item){
		if(item==null)
			return false;
		return inventory.add(item);
	}

	public Item removeItem(Item item){
		if(item==null)
			return null;
		if(inventory.contains(item)){
			inventory.remove(item);
			return item;
		}
		return null;
	}

	public Direction getOrientation() {
		return orientation;
	}

	public void turnLeft(){
		orientation = Direction.left(orientation);
	}

	public void turnRight(){
		orientation = Direction.right(orientation);
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
}
