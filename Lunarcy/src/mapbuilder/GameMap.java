package mapbuilder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.*;

public class GameMap {
	private Square[][] squares;
	private Map<Item, Integer> tierDictionary;
	private List<Location> spawnPoints;
	private int entityCount = 0;

	public GameMap(){
		setSquares(new Square[20][20]);
		setTierDictionary(new HashMap<Item, Integer>());
		int entityCount = 0;
	}

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

	public void initTierDictionary(){
		setTierDictionary(new HashMap<Item, Integer>());
		//ADD SHIP PARTS
		addShipPart(0, 5, 0);
		addShipPart(1, 3, 1);
		addShipPart(2, 4, 1);
		addShipPart(3, 2, 2);
		addShipPart(4, 1, 3);
		//ADD Keys
		addKey(1,6,0);
		addKey(2,4,1);
		addKey(3,2,2);
	}

	public void addShipPart(int partID, int count, int tier){
		for (int i = 0; i < count; i++){
			ShipPart newPart = new ShipPart(entityCount + 10, partID);
			tierDictionary.put(newPart, tier);
			increaseEntityCount();
		}
	}

	public void addKey(int keyCode, int count, int tier){
		for (int i = 0; i < count; i++){
			Key newKey = new Key(entityCount + 100, keyCode);
			tierDictionary.put(newKey, tier);
			increaseEntityCount();
		}
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

	public int getEntityCount() {
		return entityCount;
	}

	public void setEntityCount(int entityCount) {
		this.entityCount = entityCount;
	}

	public void increaseEntityCount(){
		entityCount++;
	}

	public void decreaseEntityCount(){
		entityCount--;
	}

}
