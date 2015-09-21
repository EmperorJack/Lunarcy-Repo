package game_world;

import java.util.Set;

public class Player {
	private final int id;
	private final String name;
	// Unsure about type
	//private Color color;
	
	private Location location;
	private Direction facing;
	private int oxygen;
	// Unsure about type
	private Set<Item> inventory;
	
	public Player(int uniqueID, String name){
		this.id = uniqueID;
		this.name = name;
		this.oxygen = 200;
	}
	
	public Location getLocation() {
		return location;
	}
}


