package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import bots.*;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

/**
 * This class contains all the information about the game state, it does not
 * contain any of the logic for the game but should instead be modified by
 * another class containing the game logic.
 *
 * @author Robbie
 *
 */
public class GameState implements Serializable{
	private Square[][] board;
	private List<Player> players;
	private Set<Rover> rovers;

	public GameState(int mapWidth, int mapHeight, Player... players) {
		board = new Square[mapWidth][mapHeight];
		rovers = new HashSet<Rover>();
		this.players = new ArrayList<Player>();
		for(int curID = 0; curID < players.length; curID++){
			for(Player player: players){
				if(player.getId()==curID){
					this.players.add(curID, player);
				}
			}
		}
	}


	/**
	 * @param location
	 *            - A location to retrieve the square from
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
		if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
			return null;
		} else {
			return board[x][y];
		}
	}

	/**
	 *
	 * @param location
	 *            - The location of the Square to be replaced
	 * @param square
	 *            - The Square that will take the location in the array
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
		if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
			return null;
		} else {
			Square old = board[x][y];
			board[x][y] = square;
			return old;
		}
	}

	public void load() {
		try {
			FileInputStream file = new FileInputStream("map.xml");
			XStream xstream = new XStream();
			board = (Square[][]) xstream.fromXML(file);
		} catch (FileNotFoundException e) {

		}
	}

	public Player getPlayer(int playerID){
		return players.get(playerID);
	}

	public Square[][] getBoard() {
		return board;
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}
}
