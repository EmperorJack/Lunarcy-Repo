package game;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mapbuilder.GameMap;
import storage.Storage;
import bots.*;

/**
 * This class contains all the information about the game state, it does not
 * contain any of the logic for the game but should instead be modified by
 * another class containing the game logic.
 *
 * @author Robbie
 *
 */

public class GameState implements Serializable {
	private static final long serialVersionUID = -6038094749199471954L;

	private static final int dayLength = 300;

	private Square[][] board;
	private List<Location> spawnPoints;
	private Ship ship;
	private Player[] players;
	private Set<Rover> rovers;

	private int tickCount;

	public GameState(int numPlayers, String map) {
		loadMap(map);
		rovers = new HashSet<Rover>();
		players = new Player[numPlayers];
		addRover(new Rover());
		((WalkableSquare) getSquare(new Location(1, 1))).setFurniture(
				Direction.NORTH, new Chest(69));
	}

	/**
	 * Distributes all the passed in Items amongst the containers on the board,
	 * matches the access level of each container (keyID) with the corresponding List in the Map parameter
	 * @param items A map from the access level an item should be stored in
	 * to a list of all the items in that access level
	 */
	public void distributeItems(Map<Item, Integer> itemsToAccess){
		Map<Integer, List<Item>> itemMap = sortItemsByAccess(itemsToAccess);
		//First have to change so we can find items by access level

		Map<Integer, Set<Container>> containers = new HashMap<Integer, Set<Container>>();
		//Then we need to map all containers to their access level
		for(int y = 0; y < board.length; y++){
			for(int x = 0; x < board[y].length; x++){
				//Find any WalkableSquare as only they have containers
				if(board[y][x] instanceof WalkableSquare){

					//Check all the possible directions
					for(Direction dir: Direction.values()){

						WalkableSquare square = (WalkableSquare)board[y][x];
						if(square.hasContainer(dir)){

							Container container = square.getContainer(dir);
							int access = container.getAccessLevel();
							//Check if there are already containers of this access level
							if(!containers.containsKey(access)){
								containers.put(access, new HashSet<Container>());
							}
							containers.get(access).add(container);
						}
					}
				}
			}
		}
		//Now we can begin to distribute the items between all the containers
		for(Integer access: containers.keySet()){
			List<Item> items = itemMap.get(access);
			//Go through and fill up all the containers
			while(!items.isEmpty()){
				for(Container container: containers.get(access)){
					if(items.isEmpty()){
						//Then we have no more items to distribute so quit
						break;
					}
					//Each time add a random item from the list;
					int index = (int)(items.size() * Math.random());
					container.addItem(items.remove(index));
				}
			}
		}

	}

	/**
	 * Reverses the Map so that for every item in the parameter map it will be placed into
	 * a set of items that is mapped to the Integer that corresponds to the item in the parameter map.
	 * @param items
	 * @return
	 */
	private Map<Integer, List<Item>> sortItemsByAccess(Map<Item, Integer> items){
		Map<Integer, List<Item>> itemMap = new HashMap<Integer, List<Item>>();

		for(Item item: items.keySet()){
			Integer access = items.get(item);
			if(!itemMap.containsKey(access)){
				itemMap.put(access, new ArrayList<Item>());
			}
			itemMap.get(access).add(item);
		}

		return itemMap;
	}

	/**
	 * @param location
	 *            A location to retrieve the square from
	 * @return The Square at the (x,y) coordinate, from the location, on the
	 *         board
	 * @throws IllegalArgumentException
	 *             if argument is null
	 */
	public Square getSquare(Location location) {
		if (location == null) {
			throw new IllegalArgumentException(
					"Parameter 'location' may not be null.");
		}
		int x = location.getX();
		int y = location.getY();
		if (x < 0 || x >= board[0].length || y < 0 || y >= board.length) {
			return null;
		} else {
			return board[y][x];
		}
	}

	/**
	 *
	 * @param location
	 *            The location of the Square to be replaced
	 * @param square
	 *            The Square that will take the location in the array
	 * @return The Square that was replaced, Null if the location does not exist
	 *         on the board
	 * @throws IllegalArgumentException
	 *             if argument is null
	 */
	public Square setSquare(Location location, Square square) {
		if (location == null) {
			throw new IllegalArgumentException(
					"Parameter 'location' may not be null.");
		}
		if (square == null) {
			throw new IllegalArgumentException(
					"Parameter 'square' may not be null.");
		}
		int x = location.getX();
		int y = location.getY();
		if (x < 0 || x >= board[0].length || y < 0 || y >= board.length) {
			return null;
		} else {
			Square old = board[y][x];
			board[y][x] = square;
			return old;
		}
	}

	public void loadMap(String map) {
		GameMap gameMap = Storage.loadGameMap(new File(map));
		board = gameMap.getSquares();
		spawnPoints = gameMap.getSpawnPoints();
			// Search the board to find the ship and save it
			// Probably need to do something if there is no ship
			// (InvalidMapException??)
			for (int y = 0; y < board.length; y++) {
				for (int x = 0; x < board[y].length; x++) {
					if (board[y][x] instanceof Ship) {
						ship = (Ship) board[y][x];
					}
				}
			}
		}

	/**
	 * Add a location to the Set of locations where players may spawn
	 *
	 * @param location
	 *            The Location to add
	 * @return True if the location was added as a spawn point, false otherwise
	 *         (null or invalid location)
	 */
	public boolean addSpawn(Location location) {
		if (location == null) {
			return false;
		}
		int x = location.getX();
		int y = location.getY();
		if (x < 0 || x >= board[0].length || y < 0 || y >= board.length) {
			return false;
		}
		return spawnPoints.add(location);
	}

	/**
	 * Adds the rover to the Set of Rovers
	 *
	 * @param rover
	 *            The Rover to be added
	 * @return True if the rover was added, False otherwise
	 */
	public boolean addRover(Rover rover) {
		if (rover == null) {
			return false;
		}
		return rovers.add(rover);
	}

	/**
	 * Creates a new player object and stores them in the game
	 *
	 * @param playerID
	 *            The ID of the player
	 * @param name
	 *            The name of the Player
	 * @param color
	 *            The Colour that the player selected
	 * @return
	 */
	public boolean addPlayer(int playerID, String name, Color colour) {
		Location spawn = spawnPoints.get((int) (Math.random() * spawnPoints
				.size()));
		Player player = new Player(playerID, name, colour, spawn,
				Direction.NORTH);
		if (playerID < 0 || playerID > players.length)
			return false;
		players[playerID] = player;
		return true;
	}

	/**
	 * Removes the Player with playerID of the parameter,
	 *
	 * @param playerID
	 * @return
	 */
	public boolean removePlayer(int playerID) {
		if (playerID < 0 || playerID > players.length) {
			return false;
		}
		players[playerID] = null;
		return true;
	}

	/**
	 * Get the Player whose ID matches the parameter
	 *
	 * @param playerID
	 *            The ID of the player wanted
	 * @return Player that has matching ID, null if invalid ID number
	 */
	public Player getPlayer(int playerID) {
		if (playerID < 0 || playerID > players.length)
			return null;
		return players[playerID];
	}

	/**
	 * Retrieves the ID number of the player whose name matches the parameter.
	 *
	 * @param playerName
	 *            The name of the Player whose name matches
	 * @return Player.getID() of the matching player, -1 if no player with that
	 *         name exists
	 */
	public int getPlayerID(String playerName) {
		for (Player p : players) {
			if (p != null && p.getName().equals(playerName)) {
				return p.getId();
			}
		}
		return -1;
	}

	/**
	 * If any of the players locations match the rovers, returns the first layer
	 * which matches.
	 *
	 * If none are found returns null
	 *
	 * @param rover
	 * @return
	 */
	public Player caughtPlayer(Rover rover) {
		if (rover == null) {
			return null;
		}
		for (Player player : players) {
			if (player != null
					&& player.getLocation().equals(rover.getLocation())) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Ticks the GameState and updates the time
	 */
	public void tick(){
		tickCount++;
	}
	/**
	 * Returns the time as a percentage of the day
	 * @return 0-100% of how much the day night cycle has gone through
	 */
	public int getTime(){
		return (int)((tickCount % dayLength) / (dayLength/100f));
	}

	public Square[][] getBoard() {
		return board;
	}

	public Ship getShip() {
		return ship;
	}

	public List<Location> getSpawnPoints() {
		return new ArrayList<Location>(spawnPoints);
	}

	public Player[] getPlayers() {
		return Arrays.copyOf(players, players.length);
	}

	public Set<Rover> getRovers() {
		return new HashSet<Rover>(rovers);
	}

	public boolean isOutside(Location location) {
		Square square = getSquare(location);
		if (!(square instanceof WalkableSquare)) {
			return false;
		}
		WalkableSquare walk = (WalkableSquare) square;
		return !walk.isInside();
	}

}
