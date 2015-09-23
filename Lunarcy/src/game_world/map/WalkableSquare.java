package game_world.map;

import game_world.Direction;
import game_world.Item;
import game_world.Player;

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
	 * Checks whether the specified player can enter the room *FROM* the direction parameter
	 * @param player - The player that is attempting to enter
	 * @param direction - The direction the player is entering the room *FROM*
	 * @return True if the player may enter or false otherwise
	 */
	protected abstract boolean canEnter(Player player, Direction direction);
	
	/**
	 * Checks whether the specified player can enter the room *FROM* the direction parameter
	 * and if they can then it adds the Player to the set of Players in the room.
	 * @param player - The player that is attempting to enter
	 * @param direction - The direction the player is entering the room *FROM*
	 * @return True if player can enter the room, False otherwise
	 * @throws IllegalArgumentException if either argument is null
	 */
	public boolean enter(Player player, Direction direction){
		if(player==null) throw new IllegalArgumentException("Parameter 'player' may not be null");
		if(direction==null) throw new IllegalArgumentException("Parameter 'direction' may not be null");
		if(canEnter(player, direction)){
			players.add(player);
			return true;
		}
		return false;
	}
	
	public void removePlayer(Player player){
		if(player!=null){
			if(players.contains(player)){
				players.remove(player);
			}
		}
	}
	
	/**
	 * Get the set of items on a certain side of the room.
	 * Note: Modifying the returned set will not change the items in the room
	 * @param side - the side of the Square the items are on 
	 * @return Set<Item> of all the items on that side of the room
	 * @throws IllegalArgumentException if argument is null
	 */
	public Set<Item> getItems(Direction side){
		if(side!=null){
			return new HashSet<Item>(items.get(side));
		}
		throw new IllegalArgumentException("Driection cannot be null");
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
