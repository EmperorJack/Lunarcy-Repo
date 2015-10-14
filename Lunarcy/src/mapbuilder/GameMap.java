package mapbuilder;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import game.*;

public class GameMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Square[][] squares;
	private Map<Item, Integer> tierDictionary;
	private List<Location> playerSpawnPoints;
	private List<Location> roverSpawnPoints;
	private int entityCount = 0;

	public GameMap(){
		squares = new Square[30][30];
		tierDictionary = new HashMap<Item, Integer>();
		entityCount = 0;
	}

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

	public void initTierDictionary(){
		entityCount -= tierDictionary.size();
		setTierDictionary(new HashMap<Item, Integer>());
		//ADD SHIP PARTS
		addShipPart(0, 5, 0);
		addShipPart(1, 5, 1);
		addShipPart(2, 4, 1);
		addShipPart(3, 3, 2);
		addShipPart(4, 3, 3);
		//ADD Keys
		addKey(1,6,0);
		addKey(2,4,1);
		addKey(3,2,2);
		addBag(2,1);
		addBag(3,2);
		addArmor(2,3);
		addCloak(2,2);

	}

	public void initialiseShip(){
		for (int i =0; i < squares.length; i++){
			for (int j = 0; j < squares[0].length; j++){
				if (squares[i][j] instanceof Ship)
					squares[i][j] = new Ship(
							new ShipPart(2001, 0), new ShipPart(2002, 1), new ShipPart(
									2003, 2), new ShipPart(2004, 3), new ShipPart(2005,
									4));
			}
		}
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

	public void addBag(int count, int tier){
		for (int i = 0; i < count; i++){
			Bag newBag = new Bag(entityCount + 100);
			tierDictionary.put(newBag, tier);
			increaseEntityCount();
		}
	}

	public void addArmor(int count, int tier){
		for (int i = 0; i < count; i++){
			Armour newArmour = new Armour(entityCount + 100);
			tierDictionary.put(newArmour, tier);
			increaseEntityCount();
		}
	}

	public void addCloak(int count, int tier){
		for (int i = 0; i < count; i++){
			CloakingGadget newCloak = new CloakingGadget(entityCount + 100);
			tierDictionary.put(newCloak, tier);
			increaseEntityCount();
		}
	}


	public Map<Item, Integer> getTierDictionary() {
		return tierDictionary;
	}

	public void setTierDictionary(Map<Item, Integer> tierDictionary) {
		this.tierDictionary = tierDictionary;
	}

	public void clearTierDictionary(){
	}

	public List<Location> getPlayerSpawnPoints() {
		return playerSpawnPoints;
	}

	public void setPlayerSpawnPoints(List<Location> spawnPoints) {
		this.playerSpawnPoints = spawnPoints;
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

	public List<Location> getRoverSpawnPoints() {
		return roverSpawnPoints;
	}

	public void setRoverSpawnPoints(List<Location> roverSpawnPoints) {
		this.roverSpawnPoints = roverSpawnPoints;
	}

}
