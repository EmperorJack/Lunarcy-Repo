package game_world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a subtype of Square that can be entered by a player,
 * can also contain items/furniture and has a wall on each direction.
 * 
 * @author Robbie
 *
 */
public abstract class WalkableSquare implements Square {
	private final String name;
	private final String description;
	
	private Map<Direction, Set<Item>> items;
	private Map<Direction, Wall> walls;
	private Set<Player> players;
	
	public WalkableSquare(String name, String description){
		this.name = name;
		this.description = description;
		items = new HashMap<Direction, Set<Item>>();
		walls = new HashMap<Direction, Wall>();
		players = new HashSet<Player>();
	}
	
	/**
	 * Get the set of items on a certain side of the room.
	 * Note: Modifying the returned set will not change the items in the room
	 * @param side - the side of the Square the items are on 
	 * @return Set<Item> of all the items on that side of the room
	 */
	public Set<Item> getItems(Direction side){
		if(side!=null){
			return new HashSet<Item>(items.get(side));
		}else{
			return new HashSet<Item>();
		}
	}

	/**
	 * Get the set of all the players in the room
	 * Note: Modifying the returned set will not change the players in the room
	 * @return Set<Player> of all players inside the room
	 */
	public Set<Player> getPlayers(){
		return new HashSet<Player>(players);
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}	
}
