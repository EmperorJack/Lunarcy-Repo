package game;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import bots.*;

import com.thoughtworks.xstream.XStream;

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

	private Square[][] board;
	private Set<Location> spawnPoints;
	private Player[] players;
	private Set<Rover> rovers;

	public GameState(int numPlayers,String map) {
		loadMap(map);
		rovers = new HashSet<Rover>();
		players = new Player[numPlayers];
		rovers.add(new Rover(this, new RoamMovement()));
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
		try {
			FileInputStream file = new FileInputStream(map);
			XStream xstream = new XStream();
			board = (Square[][]) xstream.fromXML(file);
			//To be read from map once File IO done with JSON
			spawnPoints = new HashSet<Location>();
			spawnPoints.add(new Location(1,1));
		} catch (FileNotFoundException e) {

		}
	}


	/**
	 * Creates a new player object and stores them in the game
	 * @param playerID The ID of the player
	 * @param name The name of the Player
	 * @param color The Colour that the player selected
	 * @return
	 */
	public boolean addPlayer(int playerID, String name, Color colour){
		Location spawn = spawnPoints.iterator().hasNext() ? spawnPoints.iterator().next() : null;
		Player player = new Player(playerID, name, colour, spawn, Direction.NORTH);
		if(playerID<0||playerID>players.length)return false;
		players[playerID] = player;
		return true;
	}

	/**
	 * Get the Player whose ID matches the parameter
	 * @param playerID The ID of the player wanted
	 * @return Player that has matching ID, null if invalid ID number
	 */
	public Player getPlayer(int playerID){
		if(playerID<0||playerID>players.length)return null;
		return players[playerID];
	}

	/**
	 * Retrieves the ID number of the player whose name matches the parameter.
	 * @param playerName The name of the Player whose name matches
	 * @return Player.getID() of the matching player, -1 if no player with that name exists
	 */
	public int getPlayerID(String playerName){
		for(Player p: players){
			if(p.getName().equals(playerName)){
				return p.getId();
			}
		}
		return -1;
	}

	public Square[][] getBoard() {
		return board;
	}

	public Player[] getPlayers() {
		return Arrays.copyOf(players, players.length);
	}

	public Set<Rover> getRovers() {
		return new HashSet<Rover>(rovers);
	}


	/**
	 * Returns the largest possible Location on the board.
	 * Assumes the board is Square.
	 */
	public Location getMaxLocation(){
		return new Location(board[0].length,board.length);
	}
}
