package mapbuilder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.*;

public class GameMap {
	private Square[][] squares;
	private Map<Item, Integer> tierDictionary;
	private List<Location> spawnPoints;


	public GameMap(){
		setSquares(new Square[20][20]);
		setTierDictionary(new HashMap<Item, Integer>());
	}

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

	public Map<Item, Integer> getTierDictionary() {
		return tierDictionary;
	}

	public void setTierDictionary(Map<Item, Integer> tierDictionary) {
		this.tierDictionary = tierDictionary;
	}

	public List<Location> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(List<Location> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}
}
