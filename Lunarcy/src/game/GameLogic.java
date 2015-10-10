package game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bots.*;

/**
 * This class controls the interaction between input and the GameState
 *
 * @author Robbie
 *
 */
public class GameLogic {
	private GameState state;
	private PlayerMove[] moves;

	private int tickCount;

	public GameLogic(GameState state) {
		tickCount = 0;
		this.state = state;
		moves = new PlayerMove[state.getPlayers().length];
	}

	/**
	 * Checks if a player can enter a square and if they can will move them into
	 * that square
	 *
	 * @param playerId
	 *            The ID of the Player to be moved
	 * @param direction
	 *            The direction to move the Player in
	 * @return True if the player was moved, False if they were not
	 */
	public boolean movePlayer(int playerID, Direction direction) {
		if (direction == null)
			return false;
		Player player = state.getPlayer(playerID);
		if (player == null)
			return false;

		if(validMove(player, direction)){
			Square src = state.getSquare(player.getLocation());
			Square dest = state.getSquare(player.getLocation().getAdjacent(direction));

			moves[playerID] = new PlayerMove(player,direction,src,dest);
			return true;
		}
		return false;
	}

	/**
	 * Checks if the Character may move in the direction specified
	 * @param character The Character to move
	 * @param direction The Direction the Character wants to move in
	 * @return True if the Character may move, False otherwise
	 */
	public boolean validMove(Character character, Direction direction){
		if(character == null && direction == null){
			return false;
		}

		Square src = state.getSquare(character.getLocation());
		Square dest = state.getSquare(character.getLocation().getAdjacent(direction));

		if(src!=null && dest!=null){
			return src.canExit(character,direction) && dest.canEnter(character, direction.opposite());
		}
		return false;
	}

	/**
	 * Turn a player to the left of their current orientation
	 *
	 * @param playerID
	 *            the ID of the player to be turned
	 */
	public void turnPlayerLeft(int playerID) {
		state.getPlayer(playerID).turnLeft();
	}

	/**
	 * Turn a player to the right of their current orientation
	 *
	 * @param playerID
	 *            the ID of the player to be turned
	 */
	public void turnPlayerRight(int playerID) {
		state.getPlayer(playerID).turnRight();
	}

	private void killPlayer(Player player){
		if(player==null)return;
		List<Item> inventory = player.getInventory();

		if(inventory.size() > 0){
			int i = (int)(inventory.size() * Math.random());
			dropItem(player.getId(), inventory.get(i).entityID);
		}

		List<Location> spawns = state.getSpawnPoints();
		Location loc = spawns.get((int)(spawns.size() * Math.random()));

		((WalkableSquare)state.getSquare(player.getLocation())).removePlayer(player);
		((WalkableSquare)state.getSquare(loc)).addPlayer(player);
		player.setLocation(loc);
	}

	/**
	 * Picks up the item corresponding to the itemID and gives it to the Player.
	 * Will only take the Item if it is in the same Square as Player and is on
	 * the side they are facing.
	 *
	 * @param player
	 *            The Player that is picking up the Item
	 * @param itemID
	 *            The entityID of the Item to pick up
	 * @return True if Item was picked up, false otherwise (Invalid playerID or
	 *         itemID)
	 */
	public boolean pickUpItem(int playerID, int itemID) {

		Player player = state.getPlayer(playerID);
		if (player == null || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			// Check all Items/Containers in the square to find the matching
			// item
			for (Entity e : wSquare.getEntities(player.getOrientation())) {
				if (e instanceof Item && e.entityID == itemID) {
					// Found the matching item
					Item item = wSquare.takeItem(player.getOrientation(),
							(Item) e);
					return player.giveItem(item);
				} else if (e instanceof Container) {
					// Have to check if the container has the item and can be
					// accessed
					Container container = (Container) e;
					if (container.hasItem(itemID)) {
						Item item = container.takeItem(player, itemID);
						if (item != null) {
							return player.giveItem(item);
						} else {
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Drops the specified Item from the Players inventory into the Square at
	 * the Players current location, in the Direction the Player is facing.
	 *
	 * @param player
	 *            The Player that is dropping the item
	 * @param itemID
	 *            The entityID of the Item being dropped
	 * @return True if the Item was dropped, false otherwise (Invalid playerID
	 *         or itemID)
	 */
	public boolean dropItem(int playerID, int itemID) {
		Player player = state.getPlayer(playerID);
		if (player == null || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			Item item = player.removeItem(itemID);
			if (item != null) {
				WalkableSquare wSquare = (WalkableSquare) square;
				wSquare.addEntity(player.getOrientation(), item);
			}
		}
		return false;
	}

	/**
	 * Puts the item corresponding to the itemID into a container. Will only put
	 * the Item if the container is in the same Square as Player and if it is on
	 * the side they are facing.
	 *
	 * @param player
	 *            The Player that is picking up the Item
	 * @param itemID
	 *            The entityID of the Item to pick up
	 * @return True if Item was picked up, false otherwise (Invalid playerID,
	 *         containerID or itemID)
	 */
	public boolean putItemIntoContainer(int playerID, int containerID,
			int itemID) {
		Player player = state.getPlayer(playerID);
		if (player == null || containerID < 0 || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			// Check all the containers to find a matching one
			for (Entity e : wSquare.getEntities(player.getOrientation())) {
				if (e instanceof Container && e.entityID == containerID) {
					Container container = (Container) e;
					if (!container.hasItem(itemID)) {
						Item item = player.removeItem(itemID);
						if (item != null) {
							return container.addItem(item);
						} else {
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Updates all the characters inside the GameState e.g Moves the Rovers and
	 * changes Player Oxygen
	 */
	public synchronized void tickGameState() {


		//Move all the rovers
		if(tickCount % 8 == 0){
			for(Rover r: state.getRovers()){
				r.tick();
			}
		}

		//Move all the players
		for(int i = 0; i < moves.length; i++){
			if(moves[i]!=null){
				moves[i].move();
				moves[i] = null;
			}
		}

		Set<Location> locations = new HashSet<Location>();
		// Update player oxygen
		for (Player player : state.getPlayers()) {
			if (player != null) {
				if(player.getOxygen()==0){
					killPlayer(player);
				}
				locations.add(player.getLocation());
			}
		}
		for (Location loc : locations) {
			Square square = state.getSquare(loc);
			square.tick();
		}

		Ship ship = state.getShip();
		if(ship.hasLaunched()){
			System.out.println(ship.getPilot().getName() + " has won the game!!");
		}

		tickCount++;
	}

	public GameState getGameState() {
		return state;
	}

	public Player getwinner(){
		if(state.getShip().hasLaunched()){
			return state.getShip().getPilot();
		}
		return null;
	}

	private class PlayerMove{
		private final Player player;
		private final Direction direction;
		private final Square source;
		private final Square destination;

		public PlayerMove(Player player, Direction direction, Square source, Square destination){
			this.player = player;
			this.direction = direction;
			this.source = source;
			this.destination = destination;
		}

		public void move(){
			if(player==null||direction==null||source==null||destination==null){
				return;
			}
			source.removePlayer(player);
			destination.addPlayer(player);
			player.move(direction);
		}
	}
}