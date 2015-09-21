package game_world;

import java.util.Set;

/**
 * Represents a Player character and contains all the information about the Player
 * 	e.g orientation, position, inventory, identification
 * 
 * @author Robbie
 *
 */
public class Player {
	private final int id;
	private final String name;
	// Unsure about type
	//private Color color;
	
	private Location location;
	private Direction orientation;
	private int oxygen;
	private Set<Item> inventory;
	
	public Player(int uniqueID, String name){
		this.id = uniqueID;
		this.name = name;
		this.oxygen = 200;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Set<Item> getInventory(){
		return inventory;
	}
	
	public Direction getOrientation(){
		return orientation;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}


